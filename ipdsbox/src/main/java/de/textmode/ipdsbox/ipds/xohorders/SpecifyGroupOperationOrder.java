package de.textmode.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Specify Group Operation order.
 */
public final class SpecifyGroupOperationOrder extends XohOrder {

    /**
     * Constructs the {@link SpecifyGroupOperationOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SpecifyGroupOperationOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.SpecifyGroupOperation);
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
