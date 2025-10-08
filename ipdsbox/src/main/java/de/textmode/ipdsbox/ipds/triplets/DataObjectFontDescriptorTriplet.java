package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class DataObjectFontDescriptorTriplet extends Triplet {

    private int fontFlags;
    private int fontTechnology;
    private int verticalFontSize;
    private int horizontalScaleFactor;
    private int characterRotation;
    private int encodingEnvironment;
    private int encodingID;

    public DataObjectFontDescriptorTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.DataObjectFontDescriptor);
        this.readFrom(this.getStream());
    }

    private void readFrom(final IpdsByteArrayInputStream in) throws IOException {
        this.fontFlags = in.readUnsignedByte();
        this.fontTechnology = in.readUnsignedByte();
        this.verticalFontSize = in.readUnsignedInteger16();
        this.horizontalScaleFactor = in.readUnsignedInteger16();
        this.characterRotation = in.readInteger16();
        this.encodingEnvironment = in.readUnsignedInteger16();
        this.encodingID = in.readUnsignedInteger16();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(0x10);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.fontFlags);
        out.writeUnsignedByte(this.fontTechnology);
        out.writeUnsignedInteger16(this.verticalFontSize);
        out.writeUnsignedInteger16(this.horizontalScaleFactor);
        out.writeInteger16(this.characterRotation);
        out.writeInteger16(this.encodingEnvironment);
        out.writeInteger16(this.encodingID);

        return out.toByteArray();
    }


    /**
     * Returns the Font flags.
     */
    public int getFontFlags() {
        return this.fontFlags;
    }

    /**
     * Sets the Font flags.
     */
    public void setFontFlags(final int fontFlags) {
        this.fontFlags = fontFlags;
    }

    /**
     * Returns the Font technology.
     */
    public int getFontTechnology() {
        return this.fontTechnology;
    }

    /**
     * Sets the Font technology.
     */
    public void setFontTechnology(final int fontTechnology) {
        this.fontTechnology = fontTechnology;
    }

    /**
     * Returns the vertical font size.
     */
    public int getVerticalFontSize() {
        return this.verticalFontSize;
    }

    /**
     * Sets the vertical font size.
     */
    public void setVerticalFontSize(final int verticalFontSize) {
        this.verticalFontSize = verticalFontSize;
    }

    /**
     * Returns the Horizontal scale factor.
     */
    public int getHorizontalScaleFactor() {
        return this.horizontalScaleFactor;
    }

    /**
     * Sets the Horizontal scale factor.
     */
    public void setHorizontalScaleFactor(final int horizontalScaleFactor) {
        this.horizontalScaleFactor = horizontalScaleFactor;
    }

    /**
     * Returns the Character rotation.
     */
    public int getCharacterRotation() {
        return this.characterRotation;
    }

    /**
     * Sets the Character rotation.
     */
    public void setCharacterRotation(final int characterRotation) {
        this.characterRotation = characterRotation;
    }

    /**
     * Returns the Encoding environment.
     */
    public int getEncodingEnvironment() {
        return this.encodingEnvironment;
    }

    /**
     * Sets the Encoding environment.
     */
    public void setEncodingEnvironment(final int encodingEnvironment) {
        this.encodingEnvironment = encodingEnvironment;
    }

    /**
     * Returns the EncodingID.
     */
    public int getEncodingID() {
        return this.encodingID;
    }

    /**
     * Sets the EncodingID.
     */
    public void setEncodingID(final int encodingID) {
        this.encodingID = encodingID;
    }


    @Override
    public String toString() {
        return "DataObjectFontDescriptor{" +
                "length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", fontFlags=0x" + Integer.toHexString(this.fontFlags) +
                ", fontTechnology=0x" + Integer.toHexString(this.fontTechnology) +
                ", specifiedVerticalFontSize=" + this.verticalFontSize +
                ", horizontalScaleFactor=" + this.horizontalScaleFactor +
                ", characterRotation=" + this.characterRotation +
                ", encodingEnvironment=" + this.encodingEnvironment +
                ", encodingID=" + this.encodingID +
                '}';
    }
}
