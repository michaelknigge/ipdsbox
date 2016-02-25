package mk.ipdsbox.ppd;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import mk.ipdsbox.core.LoggerInterface;

/**
 * The {@link PagePrinterDaemon} (PPD) receives page printer requests (PPR) from its counterpart.
 * This implementation supports TCP connections from a "real" counterpart as well as an
 * {@link InputStream}.
 */
public final class PagePrinterDaemon {

    private final ServerSocket serverSocket;
    private final InputStream inputStream;
    private final LoggerInterface logger;
    private final PagePrinterRequestHandler requestHandler;

    /**
     * The state of the {@link PagePrinterDaemon}.
     */
    private enum DaemonState {
        /**
         * The daemon is initialized but not running yet.
         */
        INITIALIZED,

        /**
         * The daemon is waiting (accepting) incoming connections.
         */
        ACCEPTING,

        /**
         * The daemon is currently running (reading from the PPD/PPR stream).
         */
        RUNNING,

        /**
         * Shutdown of the daemon was requested.
         */
        SHUTDOWN_REQUESTED,

        /**
         * The daemon has been shut down.
         */
        SHUTDOWN_DONE
    }

    private volatile DaemonState state = DaemonState.INITIALIZED;

    /**
     * Constructs a {@link PagePrinterDaemon} that will read the requests and IPDS commands
     * from the given {@link InputStream}. If the {@link InputStream} reaches its end the
     * {@link PagePrinterDaemon} will be shut down.
     *
     * @param inputStream the {@link InputStream} to read from
     * @param logger a logger used for output of messages to the user
     * @param requestHandler the {@link PagePrinterRequestHandler} that will be invoked for
     * every {@link PagePrinterRequest}
     */
    PagePrinterDaemon(final InputStream inputStream, final LoggerInterface logger,
        final PagePrinterRequestHandler requestHandler) {
        this.logger = logger;
        this.requestHandler = requestHandler;
        this.inputStream = inputStream;
        this.serverSocket = null;
    }

    /**
     * Constructs a {@link PagePrinterDaemon} that will read the requests and IPDS commands
     * from the a {@link Socket}. If an established connection terminates the {@link PagePrinterDaemon}
     * will listen and wait for the next incoming connection.
     *
     * @param serverSocket the {@link ServerSocket} that will be used for accepting connections
     * @param logger a logger used for output of messages to the user
     * @param requestHandler the {@link PagePrinterRequestHandler} that will be invoked for
     * every {@link PagePrinterRequest}
     */
    PagePrinterDaemon(final ServerSocket serverSocket, final LoggerInterface logger,
        final PagePrinterRequestHandler requestHandler) {
        this.logger = logger;
        this.requestHandler = requestHandler;
        this.serverSocket = serverSocket;
        this.inputStream = null;
    }

    /**
     * Start up the {@link PagePrinterDaemon}. The {@link PagePrinterDaemon} will start reading
     * page printer requests and IPDS commands.
     *
     * @throws IOException  If an error occurred while reading from the {@link InputStream}
     *  or when the {@link InputStream} ends unexpectedly.
     */
    public void startup() throws IOException {

        if (this.inputStream != null) {
            this.logger.info("Starting page printer daemon in stream mode....");
            this.processInputStream(this.inputStream);
        } else {
            this.logger.info("Starting page printer daemon in daemon mode....");
            Socket s;
            // TODO we need to handle SoekcteException better (gets thrown if we close the socket).
            // TODO we should put this in a own method....
            while ((s = this.serverSocket.accept()) != null) {
                this.logger.info("Accepted connection from " + s.getRemoteSocketAddress());
                this.processInputStream(s.getInputStream());

                if (this.state == DaemonState.RUNNING) {
                    this.logger.info("Connection closed");
                } else {
                    return;
                }
            }
        }
    }

    /**
     * Reads {@link PagePrinterRequest}s from the given {@link InputStream} and passes all
     * to the registered {@link PagePrinterRequestHandler}.
     *
     * @param in The {@link InputStream} from which the {@link PagePrinterRequest}s will be read.
     *
     * @throws IOException  If an error occurred while reading from the {@link InputStream}
     *  or when the {@link InputStream} ends unexpectedly.
     */
    private void processInputStream(final InputStream in) throws IOException {

        this.state = DaemonState.RUNNING;

        PagePrinterRequest req;
        while ((req = PagePrinterRequestReader.read(in)) != null) {

            if (this.state != DaemonState.RUNNING) {
                return;
            }

            this.requestHandler.handle(req);
        }
    }

    /**
     * Shut down the {@link PagePrinterDaemon}.
     */
    public void shutdown() throws IOException {
        this.logger.info("Shutting down page printer daemon....");

        if (this.state == DaemonState.RUNNING) {
            this.state = DaemonState.SHUTDOWN_REQUESTED;
        }

        if (this.serverSocket != null) {
            // TODO we need to handle IOException better....
            this.serverSocket.close();
        }

        // TODO we need to wait for the daemon to switch to state SHUTDOWN_DONE
        this.logger.info("Page printer daemon has been shut down.");
    }
}
