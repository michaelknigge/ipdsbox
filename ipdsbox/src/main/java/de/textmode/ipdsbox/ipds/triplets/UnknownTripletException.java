package de.textmode.ipdsbox.ipds.triplets;

/**
 * This {@link Exception} will be thrown if an unknown Triplet is encountered.
 */
public final class UnknownTripletException extends Exception {

    private static final long serialVersionUID = 8801606258645769894L;

    /**
     * Constructs a new exception with the specified detail message.
     * @param msg the detail message.
     */
    public UnknownTripletException(final String msg) {
        super(msg);
    }
}
