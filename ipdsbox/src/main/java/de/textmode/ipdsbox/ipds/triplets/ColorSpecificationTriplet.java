package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ColorSpecificationTriplet extends Triplet {

    private int colorSpace;
    private byte[] reserved4; // 4 bytes
    private int colSize1;
    private int colSize2;
    private int colSize3;
    private int colSize4;
    private byte[] colorValue;

    /**
     * Constructs a {@link ColorSpecificationTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    ColorSpecificationTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.ColorSpecification);

        ipds.skip(1);
        this.colorSpace = ipds.readUnsignedByte();
        ipds.skip(4);

        this.colSize1 = ipds.readUnsignedByte();
        this.colSize2 = ipds.readUnsignedByte();
        this.colSize3 = ipds.readUnsignedByte();
        this.colSize4 = ipds.readUnsignedByte();

        this.colorValue = ipds.readRemainingBytes();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(12 + this.colorValue.length);
        out.writeUnsignedByte(this.getTripletId());

        out.writeUnsignedByte(0x00);
        out.writeUnsignedByte(this.colorSpace);
        out.writeUnsignedInteger32(0L);

        out.writeUnsignedByte(this.colSize1);
        out.writeUnsignedByte(this.colSize2);
        out.writeUnsignedByte(this.colSize3);
        out.writeUnsignedByte(this.colSize4);

        out.writeBytes(this.colorValue);
    }

    /**
     * Returns the Color space.
     */
    public int getColorSpace() {
        return this.colorSpace;
    }

    /**
     * Sets the Color space.
     */
    public void setColorSpace(final int colorSpace) {
        this.colorSpace = colorSpace;
    }

    /**
     * Returns the ColSize1.
     */
    public int getColSize1() {
        return this.colSize1;
    }

    /**
     * Sets the ColSize1.
     */
    public void setColSize1(final int v) {
        this.colSize1 = v;
    }

    /**
     * Returns the ColSize2.
     */
    public int getColSize2() {
        return this.colSize2;
    }

    /**
     * Sets the ColSize2.
     */
    public void setColSize2(final int v) {
        this.colSize2 = v;
    }

    /**
     * Returns the ColSize3.
     */
    public int getColSize3() {
        return this.colSize3;
    }

    /**
     * Sets the ColSize3.
     */
    public void setColSize3(final int v) {
        this.colSize3 = v;
    }

    /**
     * Returns the ColSize4.
     */
    public int getColSize4() {
        return this.colSize4;
    }

    /**
     * Sets the ColSize4.
     */
    public void setColSize4(final int v) {
        this.colSize4 = v;
    }

    /**
     * Returns the Color value.
     */
    public byte[] getColorValue() {
        return this.colorValue;
    }

    /**
     * Sets the Color value.
     */
    public void setColorValue(final byte[] colorValue) {
        this.colorValue = colorValue;
    }

    @Override
    public String toString() {
        return "ColorSpec{"
                + "tid=0x" + Integer.toHexString(this.getTripletId())
                + ", cs=0x" + Integer.toHexString(this.colorSpace)
                + ", sizes=[" + this.colSize1 + "," + this.colSize2 + "," + this.colSize3 + "," + this.colSize4 + "]"
                + ", valueBytes=" + StringUtils.toHexString(this.getColorValue())
                + '}';
    }
}
