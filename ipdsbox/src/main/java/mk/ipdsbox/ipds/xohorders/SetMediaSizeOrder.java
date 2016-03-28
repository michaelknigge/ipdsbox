package mk.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Set Media Size order.
 */
public final class SetMediaSizeOrder extends XohOrder {

    /**
     * Constructs the {@link SetMediaSizeOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SetMediaSizeOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.SetMediaSize);
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
