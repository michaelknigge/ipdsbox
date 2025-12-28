package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Stack Received Pages order.
 */
public final class StackReceivedPagesOrder extends XohOrder {

    /**
     * Constructs the {@link StackReceivedPagesOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    StackReceivedPagesOrder(final IpdsByteArrayInputStream ipds) throws UnknownXohOrderCode, IOException {
        super(ipds, XohOrderCode.StackReceivedPages);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.StackReceivedPages.getValue());
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }
}
