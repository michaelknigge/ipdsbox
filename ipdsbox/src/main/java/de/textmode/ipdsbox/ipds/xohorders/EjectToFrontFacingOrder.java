package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Eject To Front Facing order.
 */
public final class EjectToFrontFacingOrder extends XohOrder {

    /**
     * Constructs the {@link EjectToFrontFacingOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public EjectToFrontFacingOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.EjectToFrontFacing);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.EjectToFrontFacing.getValue());
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     * @param visitor the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }

}
