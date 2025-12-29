package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Superclass of all Execute Home State Orders.
 */
public abstract class XohOrder {

    private final int orderCodeId;

    /**
     * Constructs the {@link XohOrder}.
     */
    public XohOrder(final int orderCodeId) {
        this.orderCodeId = orderCodeId;
    }

    /**
     * Constructs the {@link XohOrder} object from IPDS data read with an {@link IpdsByteArrayInputStream}.
     */
    protected XohOrder(final XohOrderCode code) {
        this.orderCodeId = code.getValue();
    }

    /**
     * Returns the ID of the {@link XohOrderCode} of this {@link XohOrder}.
     */
    public final int getOrderCodeId() {
        return this.orderCodeId;
    }

    /**
     * Returns the {@link XohOrderCode} of this {@link XohOrder}. Returns <code>null</code> if the
     * {@link XohOrderCode} is unknown.
     */
    public final XohOrderCode getOrderCode() {
        return XohOrderCode.getIfKnown(this.orderCodeId);
    }

    /**
     * Writes this {@link XohOrder} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;

    /**
     * Accept method for the {@link XohOrderVisitor}.
     */
    public abstract void accept(final XohOrderVisitor visitor);
}
