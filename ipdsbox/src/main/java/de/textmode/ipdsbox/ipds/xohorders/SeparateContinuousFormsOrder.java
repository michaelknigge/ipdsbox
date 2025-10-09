package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Separate Continuous Forms order.
 */
public final class SeparateContinuousFormsOrder extends XohOrder {

    /**
     * Constructs the {@link SeparateContinuousFormsOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SeparateContinuousFormsOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.SeparateContinuousForms);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.SeparateContinuousForms.getValue());
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
