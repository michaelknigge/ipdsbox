package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Superclass of all Execute Anystate Orders.
 */
public abstract class XoaOrder {

    private final XoaOrderCode orderCode;

    /**
     * Constructs the {@link XoaOrder}.
     */
    public XoaOrder(final XoaOrderCode orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * Constructs the {@link XoaOrder} object from IPDS data read with an {@link IpdsByteArrayInputStream}.
     *
     * @param ipds the raw IPDS data of the {@link XoaOrder}.
     * @param code the expected {@link XoaOrderCode}.
     *
     * @throws IOException if the the IPDS data is invalid / truncated.
     * @throws UnknownXoaOrderCode if the the IPDS data contains an unknown XOH order code.
     */
    public XoaOrder(final IpdsByteArrayInputStream ipds, final XoaOrderCode code) throws UnknownXoaOrderCode, IOException {
        if (ipds.readUnsignedInteger16() != code.getValue()) {
            throw new UnknownXoaOrderCode("Passed invalid data");
        }

        this.orderCode = code;
    }

    /**
     * Returns the {@link XoaOrderCode} of this {@link XoaOrder}.
     * @return the {@link XoaOrderCode} of this {@link XoaOrder}.
     */
    public final XoaOrderCode getOrderCode() {
        return this.orderCode;
    }

    /**
     * Writes this {@link XoaOrder} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     * @param visitor the {@link XoaOrderVisitor}.
     */
    public abstract void accept(final XoaOrderVisitor visitor);
}
