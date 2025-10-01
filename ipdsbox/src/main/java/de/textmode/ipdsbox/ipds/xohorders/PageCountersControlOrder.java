package de.textmode.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Page Counters Control order.
 */
public final class PageCountersControlOrder extends XohOrder {

    /**
     * Constructs the {@link PageCountersControlOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public PageCountersControlOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.PageCountersControl);
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
