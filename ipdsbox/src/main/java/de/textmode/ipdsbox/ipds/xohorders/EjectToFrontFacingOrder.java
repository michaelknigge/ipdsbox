package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Eject To Front Facing order.
 */
public final class EjectToFrontFacingOrder extends XohOrder {

    /**
     * Constructs the {@link EjectToFrontFacingOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    EjectToFrontFacingOrder(final IpdsByteArrayInputStream ipds) {
        super(XohOrderCode.EjectToFrontFacing);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.EjectToFrontFacing.getValue());
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
        return "EjectToFrontFacingOrder{}";
    }
}
