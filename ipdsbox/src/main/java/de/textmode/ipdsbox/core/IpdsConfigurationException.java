package de.textmode.ipdsbox.core;

/**
 * This {@link Exception} will be thrown if a faulty configuration is detected.
 */
public final class IpdsConfigurationException extends Exception {

    private static final long serialVersionUID = -6467497762243041478L;

    /**
     * Constructs a new {@link IpdsConfigurationException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public IpdsConfigurationException(final String message) {
        super(message);
    }
}
