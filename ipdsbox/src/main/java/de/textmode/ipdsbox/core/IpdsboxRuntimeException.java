package de.textmode.ipdsbox.core;

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
}
