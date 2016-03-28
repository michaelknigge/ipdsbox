package mk.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Define Group Boundary order.
 */
public final class DefineGroupBoundaryOrder extends XohOrder {

    /**
     * Constructs the {@link DefineGroupBoundaryOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public DefineGroupBoundaryOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.DefineGroupBoundary);
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
