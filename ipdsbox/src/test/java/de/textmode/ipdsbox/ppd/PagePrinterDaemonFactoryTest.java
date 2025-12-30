package de.textmode.ipdsbox.ppd;

import java.net.ServerSocket;

import de.textmode.ipdsbox.core.IpdsConfigurationException;
import de.textmode.ipdsbox.core.LoggerInterface;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import junit.framework.TestCase;

/**
 * JUnit tests of the {@link PagePrinterDaemonFactory}.
 */
@SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public final class PagePrinterDaemonFactoryTest extends TestCase {

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
     * Try to create a {@link PagePrinterDaemon} without a {@link ServerSocket}.
     */
    public void testMissingInput() {
        try {
            new PagePrinterDaemonFactory()
                .logger(this.logger)
                .pagePrinterRequestHandler(this.handler)
                .create();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("No server socket has been specified", e.getMessage());
        }
    }

    /**
     * Try to create a {@link PagePrinterDaemon} without a logger.
     */
    public void testMissingLogger() throws Exception {
        try {
            new PagePrinterDaemonFactory()
                .serverSocket(this.serverSocket)
                .pagePrinterRequestHandler(this.handler)
                .create();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("No Logger has been specified", e.getMessage());
        }
    }

    /**
     * Try to create a {@link PagePrinterDaemon} without a {@link PagePrinterRequestHandler}.
     */
    public void testMissingRequestHandler() throws Exception {
        try {
            new PagePrinterDaemonFactory()
                .serverSocket(this.serverSocket)
                .logger(this.logger)
                .create();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("No PagePrinterRequestHandler has been specified", e.getMessage());
        }
    }
}
