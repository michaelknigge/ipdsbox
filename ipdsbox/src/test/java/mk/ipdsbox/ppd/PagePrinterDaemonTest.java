package mk.ipdsbox.ppd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;
import mk.ipdsbox.core.IpdsConfigurationException;
import mk.ipdsbox.ppd.PagePrinterDaemon.DaemonState;

/**
 * JUnit tests of the {@link PagePrinterDaemon}.
 */
public final class PagePrinterDaemonTest extends TestCase implements Observer {

    private static final String LOCALHOST = "localhost";
    private static final int PORT_NUMBER = 7001;

    private TestcasePagePrinterRequestHandler handler;
    private TestcaseLogger logger;
    private ServerSocket serverSocket;
    private DaemonState state;

    @Override
    protected void setUp() throws Exception {
        this.handler = new TestcasePagePrinterRequestHandler();
        this.logger = new TestcaseLogger();

        this.serverSocket = new ServerSocket(PORT_NUMBER);
        this.serverSocket.setReuseAddress(true);
    }

    @Override
    protected void tearDown() throws Exception {
        if (!this.serverSocket.isClosed()) {
            this.serverSocket.close();
        }
    }

    @Override
    public void update(final Observable pagePrinterDaemon, final Object daemonState) {
        this.state = (DaemonState) daemonState;
    }

    /**
     * Builds a {@link PagePrinterDaemon} for the JUnit test.
     */
    private PagePrinterDaemon buildPagePrinterDaemon()
        throws IpdsConfigurationException, IOException {
        final PagePrinterDaemon daemon = new PagePrinterDaemonBuilder()
            .serverSocket(this.serverSocket)
            .logger(this.logger)
            .observer(this)
            .pagePrinterRequestHandler(this.handler)
            .build();

        this.state = daemon.getDaemonState();

        return daemon;
    }

    /**
     * Waits for a specific state.
     */
    private void waitForState(final DaemonState s) throws InterruptedException {
        for (int i = 0; i < 100; ++i) {
            if (this.state == s) {
                return;
            } else {
                Thread.sleep(50);
            }
        }
        throw new InterruptedException("State not changed to " + this.state + " witin 5 seconds");
    }

    /**
     * Waits until the list of handled commands has an expected (or greater) size.
     */
    private void waitForHandledCommand(final int size) throws InterruptedException {
        for (int i = 0; i < 100; ++i) {
            if (this.handler.getHandledRequests().size() >= size) {
                return;
            } else {
                Thread.sleep(50);
            }
        }
        throw new InterruptedException("Command has not been handled witin 5 seconds");
    }

    /**
     * Fires up the daemon and shuts it down - no connection is made.
     */
    public void testWithNoConnectionFromClient() throws Exception {
        final PagePrinterDaemon daemon = this.buildPagePrinterDaemon();
        daemon.startup();
        this.waitForState(DaemonState.ACCEPTING);

        daemon.shutdown();
        this.waitForState(DaemonState.SHUTDOWN_DONE);
    }

    /**
     * Fires up the daemon, connect to the daemon and close the connection without any data sent.
     */
    public void testWithNoDataFlow() throws Exception {
        final PagePrinterDaemon daemon = this.buildPagePrinterDaemon();
        daemon.startup();
        this.waitForState(DaemonState.ACCEPTING);

        final Socket socket = new Socket(LOCALHOST, PORT_NUMBER);
        this.waitForState(DaemonState.RUNNING);
        socket.close();

        daemon.shutdown();
        this.waitForState(DaemonState.SHUTDOWN_DONE);
    }

    /**
     * Fires up the daemon and connect it multiple times.
     */
    public void testMultipleConnectsWithNoDataFlow() throws Exception {
        final PagePrinterDaemon daemon = this.buildPagePrinterDaemon();
        daemon.startup();
        this.waitForState(DaemonState.ACCEPTING);

        for (int i = 0; i < 3; ++i) {
            final Socket socket = new Socket(LOCALHOST, PORT_NUMBER);
            this.waitForState(DaemonState.RUNNING);
            socket.close();
            this.waitForState(DaemonState.ACCEPTING);
        }

        daemon.shutdown();
        this.waitForState(DaemonState.SHUTDOWN_DONE);
    }

    /**
     * Fires up the daemon, connect it multiple times and send some data.
     */
    public void testMultipleConnectsWithDataFlow() throws Exception {
        final PagePrinterDaemon daemon = this.buildPagePrinterDaemon();
        daemon.startup();
        this.waitForState(DaemonState.ACCEPTING);

        // This is usually the first PPD/PPR command sent to the IPDS printer
        // after the connection has been established...
        final byte[] data = DatatypeConverter.parseHexBinary("00000010000000010000000100000002");

        for (int i = 0; i < 3; ++i) {
            final Socket socket = new Socket(LOCALHOST, PORT_NUMBER);
            this.waitForState(DaemonState.RUNNING);

            assertEquals(i, this.handler.getHandledRequests().size());
            socket.getOutputStream().write(data);
            this.waitForHandledCommand(i + 1);

            socket.close();
            this.waitForState(DaemonState.ACCEPTING);
        }

        daemon.shutdown();
        this.waitForState(DaemonState.SHUTDOWN_DONE);
    }
}
