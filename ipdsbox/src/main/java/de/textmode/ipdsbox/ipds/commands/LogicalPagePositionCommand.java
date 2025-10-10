package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

public final class LogicalPagePositionCommand extends IpdsCommand {

    private int xmPageOffset;
    private int ymPageOffset;
    private int placement;
    private int orientation;

    public LogicalPagePositionCommand() {

        super(IpdsCommandId.LPP);

        this.xmPageOffset = 0x00;
        this.ymPageOffset = 0x00;
        this.placement = 0x00;
        this.orientation = 0x00;
    }

    public LogicalPagePositionCommand(final IpdsByteArrayInputStream ipds) throws IOException, InvalidIpdsCommandException, UnknownTripletException {
        super(ipds, IpdsCommandId.LPP);

        ipds.skip(1);
        this.xmPageOffset = ipds.readUnsignedInteger24();
        this.placement = ipds.readUnsignedByte();
        this.ymPageOffset = ipds.readUnsignedInteger24();
        this.orientation = ipds.readUnsignedInteger16();
    }

    @Override
    void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedByte(0x00);
        ipds.writeUnsignedInteger24(this.xmPageOffset);
        ipds.writeUnsignedByte(this.placement);
        ipds.writeUnsignedInteger24(this.ymPageOffset);
        ipds.writeUnsignedInteger16(this.orientation);
    }

    /**
     * Returns the X offset.
     */
    public int getXmPageOffset() {
        return this.xmPageOffset;
    }

    /**
     * Sets the X offset.
     */
    public void setXmPageOffset(final int xmPageOffset) {
        this.xmPageOffset = xmPageOffset;
    }

    /**
     * Returns the Y offset.
     */
    public int getYmPageOffset() {
        return this.ymPageOffset;
    }

    /**
     * Sets the Y offset.
     */
    public void setYmPageOffset(final int ymPageOffset) {
        this.ymPageOffset = ymPageOffset;
    }

    /**
     * Returns the placement.
     */
    public int getPlacement() {
        return this.placement;
    }

    /**
     * Sets the placement.
     */
    public void setPlacement(final int placement) {
        this.placement = placement;
    }

    /**
     * Returns the orientation.
     */
    public int getOrientation() {
        return this.orientation;
    }

    /**
     * Sets the orientation.
     */
    public void setOrientation(final int orientation) {
        this.orientation = orientation;
    }
}
