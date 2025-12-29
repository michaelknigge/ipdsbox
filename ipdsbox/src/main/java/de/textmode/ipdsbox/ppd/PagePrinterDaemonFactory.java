package de.textmode.ipdsbox.ppd;

import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Observer;

import de.textmode.ipdsbox.core.IpdsConfigurationException;
import de.textmode.ipdsbox.core.LoggerInterface;

/**
 * Factory for building and configuring a {@link PagePrinterDaemon}.
 */
public final class PagePrinterDaemonFactory {

    private static final String ERROR_NO_HANDLER = "No PagePrinterRequestHandler has been specified";
    private static final String ERROR_NO_LOGGER = "No Logger has been specified";
    private static final String ERROR_NO_SOCKET = "No server socket has been specified";

    private LoggerInterface logger;
    private PagePrinterRequestHandler requestHandler;
    private ServerSocket serverSocket;
    private Observer observer;

    /**
     * Creates a {@link PagePrinterDaemon}. At least an {@link InputStream} or {@link ServerSocket}
     * must have been set as well as a {@link PagePrinterRequestHandler}.
     *
     * @throws IpdsConfigurationException if a faulty configuration is detected.
     */
    PagePrinterDaemon create() throws IpdsConfigurationException {
        if (this.requestHandler == null) {
            throw new IpdsConfigurationException(ERROR_NO_HANDLER);
        } else if (this.logger == null) {
            throw new IpdsConfigurationException(ERROR_NO_LOGGER);
        } else if (this.serverSocket == null) {
            throw new IpdsConfigurationException(ERROR_NO_SOCKET);
        }

        final PagePrinterDaemon daemon = new PagePrinterDaemon(this.serverSocket, this.logger, this.requestHandler);

        if (this.observer != null) {
            daemon.addObserver(this.observer);
            daemon.notifyObservers(daemon.getDaemonState());
        }

        return daemon;
    }

    /**
     * Sets the (optional) observer that gets notified about state changes of the
     * {@link PagePrinterDaemon}.
     *
     * @param o the {@link Observer} of the {@link PagePrinterDaemon}.
     */
    PagePrinterDaemonFactory observer(final Observer o) {
        this.observer = o;
        return this;
    }

    /**
     * Sets the logger to be used using the {@link LoggerInterface}. If no logger is set
     * the built {@link PagePrinterDaemon} will print log messages to the standard output.
     *
     * @param l the {@link LoggerInterface} implementation used for logging.
     */
    PagePrinterDaemonFactory logger(final LoggerInterface l) {
        this.logger = l;
        return this;
    }

    /**
     * Sets the {@link ServerSocket} the {@link PagePrinterDaemon} will use to listen for incoming connections.
     * If an established connection terminates the {@link PagePrinterDaemon} will listen and wait for the
     * next incoming connection.
     *
     * @param s the {@link ServerSocket} used for reading the PPR requests.
     */
    PagePrinterDaemonFactory serverSocket(final ServerSocket s) {
        this.serverSocket = s;
        return this;
    }

    /**
     * Sets the {@link PagePrinterRequestHandler} that will passed every {@link PagePrinterRequest} to.
     *
     * @param h the {@link PagePrinterRequestHandler} used for processing {@link PagePrinterRequest}.
     */
    PagePrinterDaemonFactory pagePrinterRequestHandler(final PagePrinterRequestHandler h) {
        this.requestHandler = h;
        return this;
    }
}
