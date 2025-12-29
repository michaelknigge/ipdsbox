package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Print Buffered Data order.
 */
public final class PrintBufferedDataOrder extends XohOrder {

    /**
     * Constructs the {@link PrintBufferedDataOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    PrintBufferedDataOrder(final IpdsByteArrayInputStream ipds) {
        super(XohOrderCode.PrintBufferedData);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.PrintBufferedData.getValue());
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
        return "PrintBufferedDataOrder{}";
    }
}
