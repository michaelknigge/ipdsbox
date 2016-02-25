package mk.ipdsbox.ppd;

import java.io.InputStream;
import java.net.ServerSocket;

import mk.ipdsbox.core.LoggerInterface;
import mk.ipdsbox.core.StandardLogger;

/**
 * Builder for building and configuring a {@link PagePrinterDaemon}.
 */
public final class PagePrinterDaemonBuilder {

    private LoggerInterface logger = StandardLogger.INSTANCE;
    private PagePrinterRequestHandler requestHandler;
    private InputStream inputStream;
    private ServerSocket serverSocket;

    /**
     * Builds a {@link PagePrinterDaemon}. At least an {@link InputStream} or {@link ServerSocket}
     * must have been set as well as a PagePrinterRequestHandler.
     */
    PagePrinterDaemon build() {
        if (this.requestHandler == null) {
            return null;
        }

        if (this.inputStream != null) {
            return new PagePrinterDaemon(this.inputStream, this.logger, this.requestHandler);
        } else {
            return new PagePrinterDaemon(this.serverSocket, this.logger, this.requestHandler);
        }
    }

    /**
     * Sets the logger to be used using the {@link LoggerInterface}. If no logger is set
     * the built {@link PagePrinterDaemon} will print log messages to the standard output.
     *
     * @param l the {@link LoggerInterface} implementation used for logging.
     */
    PagePrinterDaemonBuilder logger(final LoggerInterface l) {
        this.logger = l;
        return this;
    }

    /**
     * Sets the {@link InputStream} the {@link PagePrinterDaemon} will read from. If the {@link InputStream}
     * reaches the end, the {@link PagePrinterDaemon} will terminate. If a {@link InputStream} is set
     * any previously set {@link ServerSocket} is ignored.
     *
     * @param i the {@link InputStream} used for reading the PPR requests.
     */
    PagePrinterDaemonBuilder inputStream(final InputStream i) {
        this.inputStream = i;
        this.serverSocket = null;
        return this;
    }

    /**
     * Sets the {@link ServerSocket} the {@link PagePrinterDaemon} will use to listen for incoming connections.
     * If an established connection terminates the {@link PagePrinterDaemon} will listen and wait for the
     * next incoming connection. If a {@link ServerSocket} is set any previously set {@link InputStream}
     * is ignored.
     *
     * @param s the {@link ServerSocket} used for reading the PPR requests.
     */
    PagePrinterDaemonBuilder serverSocket(final ServerSocket s) {
        this.serverSocket = s;
        this.inputStream = null;
        return this;
    }

    /**
     * Sets the {@link PagePrinterRequestHandler} that will passed every {@link PagePrinterRequest} to.
     *
     * @param h the {@link PagePrinterRequestHandler} used for processing {@link PagePrinterRequest}.
     */
    PagePrinterDaemonBuilder pagePrinterRequestHandler(final PagePrinterRequestHandler h) {
        this.requestHandler = h;
        return this;
    }
}
