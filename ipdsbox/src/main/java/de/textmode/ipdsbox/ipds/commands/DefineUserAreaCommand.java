package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

public final class DefineUserAreaCommand extends IpdsCommand {

    private final int reset;
    private int unitBase;
    private int upub;
    private int xmOffset;
    private int ymOffset;
    private int xmExtent;
    private int ymExtent;


    public DefineUserAreaCommand() {

        super(IpdsCommandId.DUA);

        this.reset = 0x01;
        this.unitBase = 0x00;
        this.upub = 0x3840; // 14.400
        this.xmOffset = 0x7FFF;
        this.ymOffset = 0x7FFF;
        this.xmExtent = 0x7FFF;
        this.ymExtent = 0x7FFF;
    }

    public DefineUserAreaCommand(final IpdsByteArrayInputStream ipds) throws IOException, InvalidIpdsCommandException, UnknownTripletException {
        super(ipds, IpdsCommandId.DUA);

        this.reset = ipds.readUnsignedByte();
        this.unitBase = ipds.readUnsignedByte();
        this.upub = ipds.readUnsignedInteger16();
        this.xmOffset = ipds.readUnsignedInteger24();
        this.ymOffset = ipds.readUnsignedInteger24();
        this.xmExtent = ipds.readUnsignedInteger24();
        this.ymExtent = ipds.readUnsignedInteger24();
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedByte(this.reset);
        ipds.writeUnsignedByte(this.unitBase);
        ipds.writeUnsignedInteger16(this.upub);
        ipds.writeUnsignedInteger24(this.xmOffset);
        ipds.writeUnsignedInteger24(this.ymOffset);
        ipds.writeUnsignedInteger24(this.xmExtent);
        ipds.writeUnsignedInteger24(this.ymExtent);
    }

    /**
     * Returns the Unit base.
     */
    public int getUnitBase() {
        return this.unitBase;
    }

    /**
     * Sets the Unit base.
     */
    public void setUnitBase(final int unitBase) {
        this.unitBase = unitBase;
    }

    /**
     * Returns the upub.
     */
    public int getUpub() {
        return this.upub;
    }

    /**
     * Sets the upub.
     */
    public void setUpub(final int upub) {
        this.upub = upub;
    }

    /**
     * Returns the Xm Offset.
     */
    public int getXmOffset() {
        return this.xmOffset;
    }

    /**
     * Sets the Xm Offset.
     */
    public void setXmOffset(final int xmOffset) {
        this.xmOffset = xmOffset;
    }

    /**
     * Returns the Ym Offset.
     */
    public int getYmOffset() {
        return this.ymOffset;
    }

    /**
     * Sets the Ym Offset.
     */
    public void setYmOffset(final int ymOffset) {
        this.ymOffset = ymOffset;
    }

    /**
     * Returns the Xm extent.
     */
    public int getXmExtent() {
        return this.xmExtent;
    }

    /**
     * Sets the Xm extent.
     */
    public void setXmExtent(final int xmExtent) {
        this.xmExtent = xmExtent;
    }

    /**
     * Returns the Ym extent.
     */
    public int getYmExtent() {
        return this.ymExtent;
    }

    /**
     * Sets the Ym extent.
     */
    public void setYmExtent(final int ymExtent) {
        this.ymExtent = ymExtent;
    }
}
