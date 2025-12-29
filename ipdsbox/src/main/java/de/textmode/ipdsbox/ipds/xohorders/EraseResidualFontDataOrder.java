package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Erase Residual Font Data order.
 */
public final class EraseResidualFontDataOrder extends XohOrder {

    /**
     * Constructs the {@link EraseResidualFontDataOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    EraseResidualFontDataOrder(final IpdsByteArrayInputStream ipds) {
        super(XohOrderCode.EraseResidualFontData);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.EraseResidualFontData.getValue());
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public String toString() {
        return "EraseResidualFontDataOrder{}";
    }
}
