package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * IM-Image and Coded-Font Resolution Self-Defining Field.
 */
public final class ImImageAndCodedFontResolutionSelfDefiningField extends SelfDefiningField {

    private int unitBase;
    private int fontResolutions;
    private int xPels;
    private int yPels;

    /**
     * Constructs the {@link ImImageAndCodedFontResolutionSelfDefiningField}.
     */
    public ImImageAndCodedFontResolutionSelfDefiningField() {
        super(SelfDefiningFieldId.ImImageAndCodedFontResolution);

        this.unitBase = 0x00;
        this.fontResolutions = 0x00;
        this.xPels = 600;
        this.yPels = 600;
    }
    /**
     * Creates and parses the IM-Image and Coded-Font Resolution self-defining field from the stream.
     */
    ImImageAndCodedFontResolutionSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.ImImageAndCodedFontResolution);

        this.unitBase = ipds.readUnsignedByte();
        this.fontResolutions = ipds.readUnsignedByte();
        this.xPels = ipds.readUnsignedInteger16();
        this.yPels = ipds.readUnsignedInteger16();
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(0x0A);
        ipds.writeUnsignedInteger16(SelfDefiningFieldId.ImImageAndCodedFontResolution.getId());

        ipds.writeUnsignedByte(this.unitBase);
        ipds.writeUnsignedByte(this.fontResolutions);
        ipds.writeUnsignedInteger16(this.xPels);
        ipds.writeUnsignedInteger16(this.yPels);
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
     * Returns the Font resolutions.
     */
    public int getFontResolutions() {
        return this.fontResolutions;
    }

    /**
     * Sets the Font resolutions.
     */
    public void setFontResolutions(final int fontResolutions) {
        this.fontResolutions = fontResolutions;
    }

    /**
     * Returns the X pels.
     */
    public int getXPels() {
        return this.xPels;
    }

    /**
     * Sets the X pels.
     */
    public void setXPels(final int xPels) {
        this.xPels = xPels;
    }

    /**
     * Returns the Y pels.
     */
    public int getYPels() {
        return this.yPels;
    }

    /**
     * Sets the Y pels.
     */
    public void setYPels(final int yPels) {
        this.yPels = yPels;
    }

    @Override
    public String toString() {
        return "ImImageAndCodedFontResolutionSelfDefiningField{"
                + "unitBase=" + this.unitBase
                + ", fontResolutions=" + this.fontResolutions
                + ", xPels=" + this.xPels
                + ", yPels=" + this.yPels
                + '}';
    }
}
