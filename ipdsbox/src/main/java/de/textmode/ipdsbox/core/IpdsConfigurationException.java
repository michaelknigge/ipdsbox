package de.textmode.ipdsbox.core;

import de.textmode.ipdsbox.printer.IpdsPrinter;

/**
 * This {@link Exception} will be thrown if a faulty configuration of the {@link IpdsPrinter}
 * or {@link .textmode.ipdsbox.ppd.PagePrinterDaemon} is detected.
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
