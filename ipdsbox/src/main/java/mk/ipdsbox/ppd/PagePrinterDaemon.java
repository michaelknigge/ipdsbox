package mk.ipdsbox.ppd;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import mk.ipdsbox.core.ExceptionHelper;
import mk.ipdsbox.core.LoggerInterface;

/**
 * The {@link PagePrinterDaemon} (PPD) receives page printer requests (PPR) from its counterpart.
 * This implementation supports TCP connections from a "real" counterpart as well as an
 * {@link InputStream}.
 */
public final class PagePrinterDaemon extends Observable implements Runnable {

    private final ServerSocket serverSocket;
    private final LoggerInterface logger;
    private final PagePrinterRequestHandler requestHandler;

    /**
     * The state of the {@link PagePrinterDaemon}.
     */
    public enum DaemonState {
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
    private volatile Socket clientSocket;

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
    }

    /**
     * Returns the current state of the {@link PagePrinterDaemon}.
     * @return the current state of the {@link PagePrinterDaemon}.
     */
    public DaemonState getDaemonState() {
        return this.state;
    }

    /**
     * Sets the new state of the {@link PagePrinterDaemon} and notifies all
     * {@link Observer}s of the state change.
     */
    private void setDaemonState(final DaemonState newState) {
        this.state = newState;
        this.setChanged();
        this.notifyObservers(this.state);
    }

    /**
     * Start up the {@link PagePrinterDaemon}. Note that the Page Printer Daemon will run
     * asynchronous in a thread, so this method will return pretty fast. In detail, this
     * method start a new thread that will execute the {@link #run()} method). So if a
     * synchronous server is needed, the #run method may be invoked directly.
     */
    public void startup() {
        this.logger.info("Starting page printer daemon....");
        new Thread(this).start();
    }

    /**
     * Waits for an incoming connection from a client, like IBM Info Print. The connection
     * is accepted and handled. If the connection gets lost, the next connection will be
     * accepted.
     */
    @Override
    public void run() {
        while (this.state != DaemonState.SHUTDOWN_REQUESTED) {
            try {
                this.acceptConnection();
                this.handleConnection();
            } catch (final IOException e) {
                if (this.state != DaemonState.SHUTDOWN_REQUESTED) {
                    this.logger.error(ExceptionHelper.stackTraceToString(e));
                }
            }
        }
        this.setDaemonState(DaemonState.SHUTDOWN_DONE);
    }

    /**
     * Listens on the {@link ServerSocket} and waits for an incoming connection.
     *
     * @return a ready to use {@link Socket}.
     * @throws IOException if no connection could be accepted due to an error.
     */
    private void acceptConnection() throws IOException {
        this.logger.info("Waiting for a connection...");
        this.setDaemonState(DaemonState.ACCEPTING);
        this.clientSocket = this.serverSocket.accept();
    }

    /**
     * Handles the connection. Reads {@link PagePrinterRequest}s from the given {@link Socket}
     * and passes all of them to the registered {@link PagePrinterRequestHandler}. The method
     * will return if the connection gets closed (gracefully or unexpectedly).
     */
    private void handleConnection() {
        this.setDaemonState(DaemonState.RUNNING);

        try {
            final InputStream in = this.clientSocket.getInputStream();

            PagePrinterRequest req;
            while ((req = PagePrinterRequestReader.read(in)) != null) {
                if (this.state != DaemonState.RUNNING) {
                    return;
                }
                this.requestHandler.handle(req);
            }
        } catch (final IOException e) {
            if (this.state != DaemonState.RUNNING) {
                return;
            }
            this.logger.error(ExceptionHelper.stackTraceToString(e));
        }
    }

    /**
     * Shut down the {@link PagePrinterDaemon}. This method will just initiate the shutdown
     * and return. This method will <b>not</b> wait until the shutdown is complete.
     */
    public void shutdown() {
        this.logger.info("Shutting down page printer daemon....");

        if (this.state != DaemonState.SHUTDOWN_REQUESTED && this.state != DaemonState.SHUTDOWN_DONE) {
            this.setDaemonState(DaemonState.SHUTDOWN_REQUESTED);
        }

        if (this.serverSocket != null && !this.serverSocket.isClosed()) {
            try {
                this.serverSocket.close();
            } catch (final IOException e) {
                this.logger.debug("An exception occurred on close of the server socket (ignored!)");
                this.logger.debug(ExceptionHelper.stackTraceToString(e));
            }
        }

        if (this.clientSocket != null && !this.clientSocket.isClosed()) {
            try {
                this.clientSocket.close();
            } catch (final IOException e) {
                this.logger.debug("An exception occurred on close of the client socket (ignored!)");
                this.logger.debug(ExceptionHelper.stackTraceToString(e));
            }
        }

        this.logger.info("Page printer shutdown request has been issued.");
    }

}
