package de.textmode.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Separate Continuous Forms order.
 */
public final class SeparateContinuousFormsOrder extends XohOrder {

    /**
     * Constructs the {@link SeparateContinuousFormsOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SeparateContinuousFormsOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.SeparateContinuousForms);
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
