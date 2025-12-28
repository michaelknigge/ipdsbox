package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Superclass of all Execute Home State Orders.
 */
public abstract class XohOrder {

    private final XohOrderCode orderCode;

    /**
     * Constructs the {@link XohOrder}.
     */
    public XohOrder(final XohOrderCode orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * Constructs the {@link XohOrder} object from IPDS data read with an {@link IpdsByteArrayInputStream}.
     */
    public XohOrder(final IpdsByteArrayInputStream ipds, final XohOrderCode code) throws UnknownXohOrderCode, IOException {
        if (ipds.readUnsignedInteger16() != code.getValue()) {
            throw new UnknownXohOrderCode("Passed invalid data");
        }

        this.orderCode = code;
    }

    /**
     * Returns the {@link XohOrderCode} of this {@link XohOrder}.
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
     */
    public abstract void accept(final XohOrderVisitor visitor);
}
