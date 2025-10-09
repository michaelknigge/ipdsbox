package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Set Media Origin order.
 */
public final class SetMediaOriginOrder extends XohOrder {

    private int origin;

    /**
     * Constructs the {@link SetMediaOriginOrder}.
     * @param ipds the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SetMediaOriginOrder(final IpdsByteArrayInputStream ipds) throws UnknownXohOrderCode, IOException {
        super(ipds, XohOrderCode.SetMediaOrigin);

        this.origin = ipds.readUnsignedByte();
    }

    /**
     * Returns the origin.
     * @return the origin.
     */
    public int getOrigin() {
        return this.origin;
    }

    /**
     * Sets the origin.
     */
    public void setOrigin(final int origin) {
        this.origin = origin;
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.SetMediaOrigin.getValue());
        out.writeUnsignedByte(this.origin);
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
