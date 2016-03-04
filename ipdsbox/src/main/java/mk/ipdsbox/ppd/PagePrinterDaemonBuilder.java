package mk.ipdsbox.ppd;

import java.io.InputStream;
import java.net.ServerSocket;

import mk.ipdsbox.core.IpdsConfigurationException;
import mk.ipdsbox.core.LoggerInterface;

/**
 * Builder for building and configuring a {@link PagePrinterDaemon}.
 */
public final class PagePrinterDaemonBuilder {

    private static final String ERROR_NO_HANDLER = "No PagePrinterRequestHandler has been specified";
    private static final String ERROR_NO_LOGGER = "No Logger has been specified";
    private static final String ERROR_STREAM_AND_SOCKET = "Only an input stream or a socket may be specified";
    private static final String ERROR_NO_STREAM_OR_SOCKET = "An input stream or a socket must be specified";

    private LoggerInterface logger;
    private PagePrinterRequestHandler requestHandler;
    private InputStream inputStream;
    private ServerSocket serverSocket;

    /**
     * Builds a {@link PagePrinterDaemon}. At least an {@link InputStream} or {@link ServerSocket}
     * must have been set as well as a {@link PagePrinterRequestHandler}.
     *
     * @throws IpdsConfigurationException if a faulty configuration is detected.
     */
    PagePrinterDaemon build() throws IpdsConfigurationException {
        if (this.requestHandler == null) {
            throw new IpdsConfigurationException(ERROR_NO_HANDLER);
        } else if (this.logger == null) {
            throw new IpdsConfigurationException(ERROR_NO_LOGGER);
        } else if (this.inputStream != null && this.serverSocket != null) {
            throw new IpdsConfigurationException(ERROR_STREAM_AND_SOCKET);
        } else if (this.inputStream == null && this.serverSocket == null) {
            throw new IpdsConfigurationException(ERROR_NO_STREAM_OR_SOCKET);
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
     * reaches the end, the {@link PagePrinterDaemon} will terminate.
     *
     * @param i the {@link InputStream} used for reading the PPR requests.
     */
    PagePrinterDaemonBuilder inputStream(final InputStream i) {
        this.inputStream = i;
        return this;
    }

    /**
     * Sets the {@link ServerSocket} the {@link PagePrinterDaemon} will use to listen for incoming connections.
     * If an established connection terminates the {@link PagePrinterDaemon} will listen and wait for the
     * next incoming connection.
     *
     * @param s the {@link ServerSocket} used for reading the PPR requests.
     */
    PagePrinterDaemonBuilder serverSocket(final ServerSocket s) {
        this.serverSocket = s;
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
