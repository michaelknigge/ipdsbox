package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Mark Form order.
 */
public final class MarkFormOrder extends XoaOrder {

    /**
     * Constructs the {@link MarkFormOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    public MarkFormOrder(final IpdsByteArrayInputStream ipds) {
        super(XoaOrderCode.MarkForm);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.MarkForm.getValue());
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }
}
