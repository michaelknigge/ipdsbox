package de.textmode.ipdsbox.core;

/**
 * This is the standard logger that logs to the standard output.
 */
public final class StandardLogger implements LoggerInterface {

    /**
     * The singleton instance of the {@link StandardLogger}.
     */
    public static final LoggerInterface INSTANCE = new StandardLogger();

    /**
     * Private constructor because {@link StandardLogger} is a singleton.
     */
    private StandardLogger() {
    }

    /**
     * Log the message to the standard output.
     * @param message the message {@link Object} to log.
     */
    private synchronized void logMessage(final Object message) {
        System.out.println(message);
    }

    /**
     * Log a message including the stack trace of the {@link Throwable} t passed as parameter.
     * @param message the message object to log.
     * @param t the {@link Throwable} to log, including its stack trace.
     */
    private synchronized void logMessage(final Object message, final Throwable t) {
        System.out.println(message);
        t.printStackTrace(System.out);
    }

    @Override
    public void debug(final Object message) {
        this.logMessage(message);
    }

    @Override
    public void debug(final Object message, final Throwable t) {
        this.logMessage(message, t);
    }

    @Override
    public void info(final Object message) {
        this.logMessage(message);
    }

    @Override
    public void info(final Object message, final Throwable t) {
        this.logMessage(message, t);
    }

    @Override
    public void warn(final Object message) {
        this.logMessage(message);
    }

    @Override
    public void warn(final Object message, final Throwable t) {
        this.logMessage(message, t);
    }

    @Override
    public void error(final Object message) {
        this.logMessage(message);
    }

    @Override
    public void error(final Object message, final Throwable t) {
        this.logMessage(message, t);
    }
}
