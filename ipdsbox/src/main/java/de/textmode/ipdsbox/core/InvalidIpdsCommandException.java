package de.textmode.ipdsbox.core;

import de.textmode.ipdsbox.ipds.commands.IpdsCommand;

/**
 * This {@link Exception} will be thrown if a faulty {@link IpdsCommand} is detected.
 */
public final class InvalidIpdsCommandException extends Exception {

    private static final long serialVersionUID = -2325962965186085213L;

    /**
     * Constructs a new exception with the specified detail message.
     */
    public InvalidIpdsCommandException(final String msg) {
        super(msg);
    }
}
