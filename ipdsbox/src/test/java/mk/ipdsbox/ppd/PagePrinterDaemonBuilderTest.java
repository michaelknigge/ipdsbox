package mk.ipdsbox.ppd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import junit.framework.TestCase;
import mk.ipdsbox.core.IpdsConfigurationException;
import mk.ipdsbox.core.LoggerInterface;

/**
 * JUnit tests of the {@link PagePrinterDaemonBuilder}.
 */
public final class PagePrinterDaemonBuilderTest extends TestCase {

    private TestcasePagePrinterRequestHandler handler;
    private LoggerInterface logger;

    @Override
    protected void setUp() {
        this.handler = new TestcasePagePrinterRequestHandler();
        this.logger = new TestcaseLogger();
    }

    /**
     * Build a {@link PagePrinterDaemon} that uses an {@link InputStream}.
     */
    public void testHappyFlowWithInputStream() throws Exception {
        new PagePrinterDaemonBuilder()
            .inputStream(new ByteArrayInputStream(new byte[0]))
            .logger(this.logger)
            .pagePrinterRequestHandler(this.handler)
            .build();
    }

    /**
     * Try to build a Build a {@link PagePrinterDaemon} without input (no {@link InputStream}
     * and no {@link Socket}).
     */
    public void testMissingInput() {
        try {
            new PagePrinterDaemonBuilder()
                .logger(this.logger)
                .pagePrinterRequestHandler(this.handler)
                .build();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("An input stream or a socket must be specified", e.getMessage());
        }
    }

    /**
     * Try to build a Build a {@link PagePrinterDaemon} with an {@link InputStream}
     * and a {@link Socket}).
     */
    public void testSocketAndInputStream() throws IOException {
        try {
            new PagePrinterDaemonBuilder()
                .inputStream(new ByteArrayInputStream(new byte[0]))
                .serverSocket(new ServerSocket(1234))
                .logger(this.logger)
                .pagePrinterRequestHandler(this.handler)
                .build();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("Only an input stream or a socket may be specified", e.getMessage());
        }
    }

    /**
     * Try to build a Build a {@link PagePrinterDaemon} without a logger.
     */
    public void testMissingLogger() {
        try {
            new PagePrinterDaemonBuilder()
                .inputStream(new ByteArrayInputStream(new byte[0]))
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
    public void testMissingRequestHandler() {
        try {
            new PagePrinterDaemonBuilder()
                .inputStream(new ByteArrayInputStream(new byte[0]))
                .logger(this.logger)
                .build();
            fail();
        } catch (final IpdsConfigurationException e) {
            assertEquals("No PagePrinterRequestHandler has been specified", e.getMessage());
        }
    }
}
