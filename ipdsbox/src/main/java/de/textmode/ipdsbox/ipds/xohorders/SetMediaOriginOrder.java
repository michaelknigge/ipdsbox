package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Set Media Origin order.
 */
public final class SetMediaOriginOrder extends XohOrder {

    private int origin;

    public SetMediaOriginOrder() {
        super(XohOrderCode.SetMediaOrigin);
        this.origin = 0x00;
    }

    public SetMediaOriginOrder(final int origin) {
        super(XohOrderCode.SetMediaOrigin);
        this.origin = origin;
    }

    /**
     * Constructs the {@link SetMediaOriginOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    SetMediaOriginOrder(final IpdsByteArrayInputStream ipds) throws IOException {
        super(XohOrderCode.SetMediaOrigin);

        this.origin = ipds.readUnsignedByte();
    }

    /**
     * Returns the origin.
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
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public String toString() {
        return "SetMediaOriginOrder{" +
                "origin=0x" + Integer.toHexString(this.origin) +
                '}';
    }
}
