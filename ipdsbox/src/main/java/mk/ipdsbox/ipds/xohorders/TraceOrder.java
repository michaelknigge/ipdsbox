package mk.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Trace order.
 */
public final class TraceOrder extends XohOrder {

    /**
     * Constructs the {@link TraceOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public TraceOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.Trace);
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     * @param visitor the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }
}
