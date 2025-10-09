package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Alternate Offset Stacker order.
 */
public final class AlternateOffsetStackerOrder extends XoaOrder {

    /**
     * Constructs the {@link AlternateOffsetStackerOrder}.
     *
     * @param ipds the raw IPDS data of the order.
     *
     * @throws UnknownXoaOrderCode if the the IPDS data contains an unknown {@link XoaOrderCode}.
     */
    public AlternateOffsetStackerOrder(final IpdsByteArrayInputStream ipds) throws UnknownXoaOrderCode, IOException {
        super(ipds, XoaOrderCode.AlternateOffsetStacker);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.AlternateOffsetStacker.getValue());
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
