package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Separate Continuous Forms order.
 */
public final class SeparateContinuousFormsOrder extends XohOrder {

    /**
     * Constructs the {@link SeparateContinuousFormsOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    SeparateContinuousFormsOrder(final IpdsByteArrayInputStream ipds) {
        super(XohOrderCode.SeparateContinuousForms);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.SeparateContinuousForms.getValue());
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
        return "SeparateContinuousFormsOrder{}";
    }
}
