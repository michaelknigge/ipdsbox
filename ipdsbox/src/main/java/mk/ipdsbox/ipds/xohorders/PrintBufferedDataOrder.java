package mk.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Print Buffered Data order.
 */
public final class PrintBufferedDataOrder extends XohOrder {

    /**
     * Constructs the {@link PrintBufferedDataOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public PrintBufferedDataOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.PrintBufferedData);
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
