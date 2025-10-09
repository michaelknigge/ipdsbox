package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class MetricAdjustmentTriplet extends Triplet {

    private int unitBase;
    private int xupub;
    private int yupub;
    private int hUniformIncrement;
    private int vUniformIncrement;
    private int hBaselineAdjustment;
    private int vBaselineAdjustment;

    public MetricAdjustmentTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.MetricAdjustment);
        this.readFrom(this.getStream());
    }

    /**
     * Reads all fields from the given stream in triplet order.
     */
    private void readFrom(final IpdsByteArrayInputStream in) throws IOException {
        this.unitBase = in.readUnsignedByte();
        this.xupub = in.readUnsignedInteger16();
        this.yupub = in.readUnsignedInteger16();
        this.hUniformIncrement = in.readInteger16();
        this.vUniformIncrement = in.readInteger16();
        this.hBaselineAdjustment = in.readInteger16();
        this.vBaselineAdjustment = in.readInteger16();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(0x0F);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.unitBase);
        out.writeUnsignedInteger16(this.xupub);
        out.writeUnsignedInteger16(this.yupub);
        out.writeInteger16(this.hUniformIncrement);
        out.writeInteger16(this.vUniformIncrement);
        out.writeInteger16(this.hBaselineAdjustment);
        out.writeInteger16(this.vBaselineAdjustment);
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
    public void setUnitBase(final int v) {
        this.unitBase = v;
    }

    /**
     * Returns the XUPUB.
     */
    public int getXupub() {
        return this.xupub;
    }

    /**
     * Sets the XUPUB.
     */
    public void setXupub(final int v) {
        this.xupub = v;
    }

    /**
     * Returns the YUPUB.
     */
    public int getYupub() {
        return this.yupub;
    }

    /**
     * Sets the YUPUB.
     */
    public void setYupub(final int v) {
        this.yupub = v;
    }

    /**
     * Returns the H uniform increment.
     */
    public int getHUniformIncrement() {
        return this.hUniformIncrement;
    }

    /**
     * Sets the H uniform increment.
     */
    public void setHUniformIncrement(final int v) {
        this.hUniformIncrement = v;
    }

    /**
     * Returns the V uniform increment.
     */
    public int getVUniformIncrement() {
        return this.vUniformIncrement;
    }

    /**
     * Sets the V uniform increment.
     */
    public void setVUniformIncrement(final int v) {
        this.vUniformIncrement = v;
    }

    /**
     * Returns the H baseline adjustment.
     */
    public int getHBaselineAdjustment() {
        return this.hBaselineAdjustment;
    }

    /**
     * Sets the H baseline adjustment.
     */
    public void setHBaselineAdjustment(final int v) {
        this.hBaselineAdjustment = v;
    }

    /**
     * Returns the V baseline adjustment.
     */
    public int getVBaselineAdjustment() {
        return this.vBaselineAdjustment;
    }

    /**
     * Sets the V baseline adjustment.
     */
    public void setVBaselineAdjustment(final int v) {
        this.vBaselineAdjustment = v;
    }

    @Override
    public String toString() {
        return "MetricAdjustment{" +
                "length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", unitBase=0x" + Integer.toHexString(this.unitBase) +
                ", xupub=" + this.xupub +
                ", yupub=" + this.yupub +
                ", hUniformIncrement=" + this.hUniformIncrement +
                ", vUniformIncrement=" + this.vUniformIncrement +
                ", hBaselineAdjustment=" + this.hBaselineAdjustment +
                ", vBaselineAdjustment=" + this.vBaselineAdjustment +
                '}';
    }
}
