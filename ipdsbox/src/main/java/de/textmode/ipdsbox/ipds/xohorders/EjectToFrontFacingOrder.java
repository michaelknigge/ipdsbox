package de.textmode.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Eject To Front Facing order.
 */
public final class EjectToFrontFacingOrder extends XohOrder {

    /**
     * Constructs the {@link EjectToFrontFacingOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public EjectToFrontFacingOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.EjectToFrontFacing);
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
