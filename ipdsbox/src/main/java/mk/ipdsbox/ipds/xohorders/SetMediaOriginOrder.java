package mk.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Set Media Origin order.
 */
public final class SetMediaOriginOrder extends XohOrder {

    /**
     * Constructs the {@link SetMediaOriginOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SetMediaOriginOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.SetMediaOrigin);
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
