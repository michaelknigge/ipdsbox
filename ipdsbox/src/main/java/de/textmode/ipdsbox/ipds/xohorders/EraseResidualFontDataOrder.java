package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Erase Residual Font Data order.
 */
public final class EraseResidualFontDataOrder extends XohOrder {

    /**
     * Constructs the {@link EraseResidualFontDataOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public EraseResidualFontDataOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.EraseResidualFontData);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.EraseResidualFontData.getValue());
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
