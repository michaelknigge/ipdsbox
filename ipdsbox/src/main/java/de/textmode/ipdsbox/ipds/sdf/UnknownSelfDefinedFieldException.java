package de.textmode.ipdsbox.ipds.sdf;

/**
 * This {@link Exception} will be thrown if an unknown self-defined field is encountered.
 */
public final class UnknownSelfDefinedFieldException extends Exception {

    private static final long serialVersionUID = -7919838806340174499L;

    /**
     * Constructs a new exception with the specified detail message.
     * @param msg the detail message.
     */
    public UnknownSelfDefinedFieldException(final String msg) {
        super(msg);
    }
}
