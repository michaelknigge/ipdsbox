package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Erase Residual Print Data order.
 */
public final class EraseResidualPrintDataOrder extends XohOrder {

    /**
     * Constructs the {@link EraseResidualPrintDataOrder}.
     * @param ipds the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public EraseResidualPrintDataOrder(final IpdsByteArrayInputStream ipds) throws UnknownXohOrderCode, IOException {
        super(ipds, XohOrderCode.EraseResidualPrintData);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.EraseResidualPrintData.getValue());
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
