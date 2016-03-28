package mk.ipdsbox.core;

/**
 * This {@link Exception} will be thrown if ipdsbox detected that thre is something
 * wrong with the implementation or API use.
 */
public final class IpdsboxRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -7889729779157939903L;

    /**
     * Constructs a new {@link IpdsboxRuntimeException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public IpdsboxRuntimeException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@link IpdsboxRuntimeException} with the specified detail message and
     * nested {@link Throwable}.
     *
     * @param message the detail message.
     * @param e the nested {@link Throwable}.
     */
    public IpdsboxRuntimeException(final String message, final Throwable e) {
        super(message, e);
    }
}
