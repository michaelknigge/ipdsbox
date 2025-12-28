package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Obtain Additional Exception Information order.
 */
public final class ObtainAdditionalExceptionInformationOrder extends XoaOrder {

    /**
     * Constructs the {@link ObtainAdditionalExceptionInformationOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    ObtainAdditionalExceptionInformationOrder(final IpdsByteArrayInputStream ipds) throws UnknownXoaOrderCode, IOException {
        super(ipds, XoaOrderCode.ObtainAdditionalExceptionInformation);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.ObtainAdditionalExceptionInformation.getValue());
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }
}
