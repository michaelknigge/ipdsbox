package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Discard Unstacked Pages order.
 */
public final class DiscardUnstackedPagesOrder extends XoaOrder {

    /**
     * Constructs the {@link DiscardUnstackedPagesOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    DiscardUnstackedPagesOrder(final IpdsByteArrayInputStream ipds) {
        super(XoaOrderCode.DiscardUnstackedPages);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(this.getOrderCodeId());
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public String toString() {
        return "DiscardUnstackedPagesOrder{}";
    }
}
