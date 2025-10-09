package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Obtain Additional Exception Information order.
 */
public final class ObtainAdditionalExceptionInformationOrder extends XoaOrder {

    /**
     * Constructs the {@link ObtainAdditionalExceptionInformationOrder}.
     *
     * @param ipds the raw IPDS data of the order.
     *
     * @throws UnknownXoaOrderCode if the the IPDS data contains an unknown {@link XoaOrderCode}.
     */
    public ObtainAdditionalExceptionInformationOrder(final IpdsByteArrayInputStream ipds) throws UnknownXoaOrderCode, IOException {
        super(ipds, XoaOrderCode.ObtainAdditionalExceptionInformation);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.ObtainAdditionalExceptionInformation.getValue());
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     * @param visitor the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }
}
