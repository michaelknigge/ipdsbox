package mk.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Remove Saved Page Group order.
 */
public final class RemoveSavedPageGroupOrder extends XohOrder {

    /**
     * Constructs the {@link RemoveSavedPageGroupOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public RemoveSavedPageGroupOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.RemoveSavedGroup);
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
