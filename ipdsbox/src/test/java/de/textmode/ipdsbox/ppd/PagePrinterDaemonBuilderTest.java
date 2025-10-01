package de.textmode.ipdsbox.ppd;

import java.net.ServerSocket;

import de.textmode.ipdsbox.core.IpdsConfigurationException;
import de.textmode.ipdsbox.core.LoggerInterface;
import junit.framework.TestCase;

/**
 * JUnit tests of the {@link PagePrinterDaemonBuilder}.
 */
public final class PagePrinterDaemonBuilderTest extends TestCase {

    private static final int PORT_NUMBER = 7001;

    private TestcasePagePrinterRequestHandler handler;
    private LoggerInterface logger;
    private ServerSocket serverSocket;

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

    /**
     * Try to build a Build a {@link PagePrinterDaemon} without a {@link ServerSocket}.
     */
    public void testMissingInput() {
        try {
            new PagePrinterDaemonBuilder()
                .logger(this.logger)
                .pagePrinterRequestHandler(this.handler)
                .build();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("No server socket has been specified", e.getMessage());
        }
    }

    /**
     * Try to build a Build a {@link PagePrinterDaemon} without a logger.
     */
    public void testMissingLogger() throws Exception {
        try {
            new PagePrinterDaemonBuilder()
                .serverSocket(this.serverSocket)
                .pagePrinterRequestHandler(this.handler)
                .build();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("No Logger has been specified", e.getMessage());
        }
    }

    /**
     * Try to build a Build a {@link PagePrinterDaemon} without a {@link PagePrinterRequestHandler}.
     */
    public void testMissingRequestHandler() throws Exception {
        try {
            new PagePrinterDaemonBuilder()
                .serverSocket(this.serverSocket)
                .logger(this.logger)
                .build();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("No PagePrinterRequestHandler has been specified", e.getMessage());
        }
    }
}
