package mk.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Select Medium Modifications order.
 */
public final class SelectMediumModificationsOrder extends XohOrder {

    /**
     * Constructs the {@link SelectMediumModificationsOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SelectMediumModificationsOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.SelectMediumModifications);
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
