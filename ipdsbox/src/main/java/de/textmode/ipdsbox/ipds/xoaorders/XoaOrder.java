package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Superclass of all Execute Anystate Orders.
 */
public abstract class XoaOrder {

    private final int orderCodeId;

    /**
     * Constructs the {@link XoaOrder}.
     */
    protected XoaOrder(final int orderCodeId) {
        this.orderCodeId = orderCodeId;
    }

    /**
     * Constructs the {@link XoaOrder}.
     */
    protected XoaOrder(final XoaOrderCode orderCode) {
        this.orderCodeId = orderCode.getValue();
    }

    /**
     * Returns the ID of the {@link XoaOrderCode} of this {@link XoaOrder}.
     */
    public final int getOrderCodeId() {
        return this.orderCodeId;
    }

    /**
     * Returns the {@link XoaOrderCode} of this {@link XoaOrder}. Returns <code>null</code> if the
     * {@link XoaOrderCode} is unknown.
     */
    public final XoaOrderCode getOrderCode() {
        return XoaOrderCode.getIfKnown(this.orderCodeId);
    }

    /**
     * Writes this {@link XoaOrder} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    public abstract void accept(final XoaOrderVisitor visitor);
}
