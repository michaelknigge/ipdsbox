package de.textmode.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Select Input Media Source order.
 */
public final class SelectInputMediaSourceOrder extends XohOrder {

    /**
     * Constructs the {@link SelectInputMediaSourceOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SelectInputMediaSourceOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.SelectInputMediaSource);
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
