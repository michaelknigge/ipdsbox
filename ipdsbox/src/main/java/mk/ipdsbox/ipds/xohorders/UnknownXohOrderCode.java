package mk.ipdsbox.ipds.xohorders;

/**
 * This {@link Exception} will be thrown if an unknown XOH order code is encountered.
 */
public final class UnknownXohOrderCode extends Exception {

    private static final long serialVersionUID = -3386478392298754670L;

    /**
     * Constructs a new exception with the specified detail message.
     * @param msg the detail message.
     */
    public UnknownXohOrderCode(final String msg) {
        super(msg);
    }
}
