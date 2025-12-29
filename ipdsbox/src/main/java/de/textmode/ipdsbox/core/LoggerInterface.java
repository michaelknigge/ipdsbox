package de.textmode.ipdsbox.core;

/**
 * Interface for logging. ipdsbox uses this interface to output log messages of any kind.
 * Because ipdsbox uses this simple interface, any logging framework (log4j, SLF4J, tinylog)
 * can be used for logging purposes and ipdsbox has no dependency to a particular logging
 * framework.
 */
public interface LoggerInterface {

    /**
     * Log a message {@link Object} with the DEBUG level.
     * @param message the message {@link Object} to log.
     */
    void debug(final Object message);

    /**
     * Log a message {@link Object} with the DEBUG level including the stack trace of the
     * {@link Throwable} t passed as parameter.
     * @param message the message {@link Object} to log.
     * @param t the {@link Throwable} to log, including its stack trace.
     */
    void debug(final Object message, final Throwable t);

    /**
     * Log a message {@link Object} with the INFO level.
     * @param message the message {@link Object} to log.
     */
    void info(final Object message);

    /**
     * Log a message {@link Object} with the INFO level including the stack trace of the
     * {@link Throwable} t passed as parameter.
     * @param message the message object to log.
     * @param t the {@link Throwable} to log, including its stack trace.
     */
    void info(final Object message, final Throwable t);

    /**
     * Log a message {@link Object} with the WARNING level.
     * @param message the message object to log.
     */
    void warn(final Object message);

    /**
     * Log a message object with the WARNING level including the stack trace of the
     * {@link Throwable} t passed as parameter.
     * @param message the message object to log.
     * @param t the {@link Throwable} to log, including its stack trace.
     */
    void warn(final Object message, final Throwable t);

    /**
     * Log a message {@link Object} with the ERROR level.
     * @param message the message object to log.
     */
    void error(final Object message);

    /**
     * Log a message object with the ERROR level including the stack trace of the
     * {@link Throwable} t passed as parameter.
     * @param message the message object to log.
     * @param t the {@link Throwable} to log, including its stack trace.
     */
    void error(final Object message, final Throwable t);
}
