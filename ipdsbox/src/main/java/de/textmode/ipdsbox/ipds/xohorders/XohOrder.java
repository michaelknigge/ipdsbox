package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Superclass of all Execute Home State Orders. Note that all implementations have to implement
 * a constructor (and just this one constructor) that accepts a byte[] as the one any only parameter.
 */
public abstract class XohOrder {

    private final XohOrderCode orderCode;

    public XohOrder(final XohOrderCode orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * Constructs the {@link XohOrder} object.
     * @param data the raw IPDS data of the {@link XohOrder}.
     * @param code the expected {@link XohOrderCode}.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown XOH order code.
     */
    public XohOrder(final byte[] data, final XohOrderCode code) throws UnknownXohOrderCode {
        // TODO Use IpdsByteArrayInputStream...
        final int c = ((data[0] & 0xFF) << 8) | (data[1] & 0xFF);
        if (c != code.getValue()) {
            throw new UnknownXohOrderCode("Passed invalid data");
        }
        this.orderCode = XohOrderCode.getFor(c);
    }

    /**
     * Returns the {@link XohOrderCode} of this {@link XohOrder}.
     * @return the {@link XohOrderCode} of this {@link XohOrder}.
     */
    public final XohOrderCode getOrderCode() {
        return this.orderCode;
    }

    /**
     * Writes this {@link XohOrder} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;

    /**
     * Accept method for the {@link XohOrderVisitor}.
     * @param visitor the {@link XohOrderVisitor}.
     */
    public abstract void accept(final XohOrderVisitor visitor);
}
