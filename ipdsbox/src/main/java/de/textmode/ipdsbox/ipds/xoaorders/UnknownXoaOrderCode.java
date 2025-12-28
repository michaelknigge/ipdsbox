package de.textmode.ipdsbox.ipds.xoaorders;

/**
 * This {@link Exception} will be thrown if an unknown XOA order code is encountered.
 */
public final class UnknownXoaOrderCode extends Exception {

    private static final long serialVersionUID = -56577349614831498L;

    /**
     * Constructs a new exception with the specified detail message.
     */
    public UnknownXoaOrderCode(final String msg) {
        super(msg);
    }
}
