package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;

public final class LogicalPageDescriptorCommand extends IpdsCommand {

    private int unitBase;
    private int xupub;
    private int yupub;
    private int xpExtent;
    private int ypExtent;
    private int orderedDataFlags;

    private int iAxisOrientation;
    private int bAxisOrientation;
    private int initialInline;
    private int initialBaseline;
    private int inlineMargin;
    private int intercharAdjustment;
    private int baselineIncrement;
    private int fontLocalId;
    private int color;

    private final List<Triplet> triplets = new ArrayList<Triplet>();


    public LogicalPageDescriptorCommand() {

        super(IpdsCommandId.LPD);

        this.unitBase = 0x00;
        this.xupub = 0x3840; // 14.400
        this.yupub = 0x3840; // 14.400
        this.xpExtent = 0x7FFF;
        this.ypExtent = 0x7FFF;
        this.orderedDataFlags = 0x00;

        this.iAxisOrientation = 0x0000;
        this.bAxisOrientation = 0x2D00;

        this.initialInline = 0x0000;
        this.initialBaseline = 0x0000;

        this.inlineMargin = 0xFFFF;
        this.intercharAdjustment = 0xFFFF;

        this.baselineIncrement = 0xFFFF;
        this.fontLocalId = 0xFF;
        this.color = 0xFFFF;
    }

    LogicalPageDescriptorCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.LPD);

        this.unitBase = ipds.readUnsignedByte();
        ipds.skip(1);

        this.xupub = ipds.readUnsignedInteger16();
        this.yupub = ipds.readUnsignedInteger16();
        ipds.skip(1);

        this.xpExtent = ipds.readUnsignedInteger24();
        ipds.skip(1);

        this.ypExtent = ipds.readUnsignedInteger24();
        ipds.skip(1);

        this.orderedDataFlags = ipds.readUnsignedByte();
        ipds.skip(8);

        this.iAxisOrientation = ipds.readUnsignedInteger16();
        this.bAxisOrientation = ipds.readUnsignedInteger16();

        this.initialInline = ipds.readInteger16();
        this.initialBaseline = ipds.readInteger16();

        this.inlineMargin = ipds.readUnsignedInteger16();
        this.intercharAdjustment = ipds.readUnsignedInteger16();

        ipds.skip(2);

        this.baselineIncrement = ipds.readUnsignedInteger16();
        this.fontLocalId = ipds.readUnsignedByte();
        this.color = ipds.readUnsignedInteger16();

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletFactory.create(buffer));
        }
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedByte(this.unitBase);
        ipds.writeUnsignedByte(0x00);

        ipds.writeUnsignedInteger16(this.xupub);
        ipds.writeUnsignedInteger16(this.yupub);
        ipds.writeUnsignedByte(0x00);

        ipds.writeUnsignedInteger24(this.xpExtent);
        ipds.writeUnsignedByte(0x00);

        ipds.writeUnsignedInteger24(this.ypExtent);
        ipds.writeUnsignedByte(0x00);

        ipds.writeUnsignedByte(this.orderedDataFlags);
        ipds.writeUnsignedInteger32(0); // 4 Bytes
        ipds.writeUnsignedInteger32(0); // 4 Bytes

        ipds.writeUnsignedInteger16(this.iAxisOrientation);
        ipds.writeUnsignedInteger16(this.bAxisOrientation);
        ipds.writeUnsignedInteger16(this.initialInline);
        ipds.writeUnsignedInteger16(this.initialBaseline);
        ipds.writeUnsignedInteger16(this.inlineMargin);
        ipds.writeUnsignedInteger16(this.intercharAdjustment);

        ipds.writeUnsignedInteger16(0x0000);

        ipds.writeUnsignedInteger16(this.baselineIncrement);
        ipds.writeUnsignedByte(this.fontLocalId);
        ipds.writeUnsignedInteger16(this.color);

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(ipds);
        }
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
     * Returns the xupub.
     */
    public int getXupub() {
        return this.xupub;
    }

    /**
     * Sets the xupub.
     */
    public void setXupub(final int xupub) {
        this.xupub = xupub;
    }

    /**
     * Returns the yupub.
     */
    public int getYupub() {
        return this.yupub;
    }

    /**
     * Sets the yupub.
     */
    public void setYupub(final int yupub) {
        this.yupub = yupub;
    }


    /**
     * Returns the Xp extent.
     */
    public int getXpExtent() {
        return this.xpExtent;
    }

    /**
     * Sets the Xp extent.
     */
    public void setXpExtent(final int xpExtent) {
        this.xpExtent = xpExtent;
    }

    /**
     * Returns the Yp extent.
     */
    public int getYpExtent() {
        return this.ypExtent;
    }

    /**
     * Sets the Yp extent.
     */
    public void setYpExtent(final int ypExtent) {
        this.ypExtent = ypExtent;
    }

    /**
     * Returns the Ordered data flags.
     */
    public int getOrderedDataFlags() {
        return this.orderedDataFlags;
    }

    /**
     * Sets the Ordered data flags.
     */
    public void setOrderedDataFlags(final int orderedDataFlags) {
        this.orderedDataFlags = orderedDataFlags;
    }

    /**
     * Returns the I-axis orientation.
     */
    public int getIAxisOrientation() {
        return this.iAxisOrientation;
    }

    /**
     * Sets the I-axis orientation.
     */
    public void setIAxisOrientation(final int iAxisOrientation) {
        this.iAxisOrientation = iAxisOrientation;
    }

    /**
     * Returns the B-axis orientation.
     */
    public int getBAxisOrientation() {
        return this.bAxisOrientation;
    }

    /**
     * Sets the B-axis orientation.
     */
    public void setBAxisOrientation(final int bAxisOrientation) {
        this.bAxisOrientation = bAxisOrientation;
    }

    /**
     * Returns the Initial Inline.
     */
    public int getInitialInline() {
        return this.initialInline;
    }

    /**
     * Sets the Initial Inline.
     */
    public void setInitialInline(final int initialInline) {
        this.initialInline = initialInline;
    }

    /**
     * Returns the Initial Baseline.
     */
    public int getInitialBaseline() {
        return this.initialBaseline;
    }

    /**
     * Sets the Initial Baseline.
     */
    public void setInitialBaseline(final int initialBaseline) {
        this.initialBaseline = initialBaseline;
    }

    /**
     * Returns the Inline margin.
     */
    public int getInlineMargin() {
        return this.inlineMargin;
    }

    /**
     * Sets the Inline margin.
     */
    public void setInlineMargin(final int inlineMargin) {
        this.inlineMargin = inlineMargin;
    }

    /**
     * Returns the Interchar. adjustment.
     */
    public int getIntercharAdjustment() {
        return this.intercharAdjustment;
    }

    /**
     * Sets the Interchar. adjustment.
     */
    public void setIntercharAdjustment(final int intercharAdjustment) {
        this.intercharAdjustment = intercharAdjustment;
    }

    /**
     * Returns the Baseline increment.
     */
    public int getBaselineIncrement() {
        return this.baselineIncrement;
    }

    /**
     * Sets the Baseline increment.
     */
    public void setBaselineIncrement(final int baselineIncrement) {
        this.baselineIncrement = baselineIncrement;
    }

    /**
     * Returns the font local ID.
     */
    public int getFontLocalId() {
        return this.fontLocalId;
    }

    /**
     * Sets the font local ID.
     */
    public void setFontLocalId(final int fontLocalId) {
        this.fontLocalId = fontLocalId;
    }

    /**
     * Returns the Color.
     */
    public int getColor() {
        return this.color;
    }

    /**
     * Sets the Color.
     */
    public void setColor(final int color) {
        this.color = color;
    }

    /**
     * Returns the Triplets.
     */
    public List<Triplet> getTriplets() {
        return this.triplets;
    }
}
