package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Set Media Size order.
 */
public final class SetMediaSizeOrder extends XohOrder {

    private int unitBase;
    private int upub;
    private int xmExtent;
    private int ymExtent;

    /**
     * Constructs the {@link SetMediaSizeOrder}.
     */
    public SetMediaSizeOrder() {
        super(XohOrderCode.SetMediaSize);

        this.unitBase = 0x00;
        this.upub = 0x3840;
        this.xmExtent = 0xFFFF;
        this.ymExtent = 0xFFFF;
    }

    /**
     * Constructs the {@link SetMediaSizeOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    SetMediaSizeOrder(final IpdsByteArrayInputStream ipds) throws IOException {
        super(XohOrderCode.SetMediaSize);

        this.unitBase = ipds.readUnsignedByte();
        this.upub = ipds.readUnsignedInteger16();
        this.xmExtent = ipds.readUnsignedInteger16();
        this.ymExtent = ipds.readUnsignedInteger16();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.SetMediaOrigin.getValue());
        out.writeUnsignedByte(this.unitBase);
        out.writeUnsignedInteger16(this.upub);
        out.writeUnsignedInteger16(this.xmExtent);
        out.writeUnsignedInteger16(this.ymExtent);
    }

    /**
     * Returns the unit base.
     */
    public int getUnitBase() {
        return this.unitBase;
    }

    /**
     * Sets the unit base.
     */
    public void setUnitBase(final int unitBase) {
        this.unitBase = unitBase;
    }

    /**
     * Returns the units per unit base.
     */
    public int getUpub() {
        return this.upub;
    }

    /**
     * Sets the units per unit base.
     */
    public void setUpub(final int upub) {
        this.upub = upub;
    }

    /**
     * Returns the X extent of the medium presentation space.
     */
    public int getXmExtent() {
        return this.xmExtent;
    }

    /**
     * Sets the X extent of the medium presentation space.
     */
    public void setXmExtent(final int xmExtent) {
        this.xmExtent = xmExtent;
    }

    /**
     * Returns the Y extent of the medium presentation space.
     */
    public int getYmExtent() {
        return this.ymExtent;
    }

    /**
     * Sets the Y extent of the medium presentation space.
     */
    public void setYmExtent(final int ymExtent) {
        this.ymExtent = ymExtent;
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
        return "SetMediaSizeOrder{"
                + "unitBase=0x" + Integer.toHexString(this.unitBase)
                + ", upub=" + this.upub
                + ", xmExtent=" + this.xmExtent
                + ", ymExtent=" + this.ymExtent
                + '}';
    }
}
