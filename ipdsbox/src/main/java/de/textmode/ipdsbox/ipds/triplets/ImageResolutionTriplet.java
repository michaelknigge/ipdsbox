package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ImageResolutionTriplet extends Triplet {

    private int xUnitBase;
    private int yUnitBase;
    private int xupub;
    private int yupub;

    /**
     * Constructs a {@link ImageResolutionTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    ImageResolutionTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.ImageResolution);

        ipds.skip(2);
        this.xUnitBase = ipds.readUnsignedByte();
        this.yUnitBase = ipds.readUnsignedByte();
        this.xupub = ipds.readUnsignedInteger16();
        this.yupub = ipds.readUnsignedInteger16();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(0x0A);
        out.writeUnsignedByte(this.getTripletId());

        out.writeUnsignedInteger16(0x0000);
        out.writeUnsignedByte(this.xUnitBase);
        out.writeUnsignedByte(this.yUnitBase);
        out.writeUnsignedInteger16(this.xupub);
        out.writeUnsignedInteger16(this.yupub);
    }

    /**
     * Returns the X unit base.
     */
    public int getXUnitBase() {
        return this.xUnitBase;
    }

    /**
     * Sets the X unit base.
     */
    public void setXUnitBase(final int xUnitBase) {
        this.xUnitBase = xUnitBase;
    }

    /**
     * Returns the Y unit base.
     */
    public int getYUnitBase() {
        return this.yUnitBase;
    }

    /**
     * Sets the Y unit base.
     */
    public void setYUnitBase(final int yUnitBase) {
        this.yUnitBase = yUnitBase;
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
    public void setXupub(final int xupub) {
        this.xupub = xupub;
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
    public void setYupub(final int yupub) {
        this.yupub = yupub;
    }

    @Override
    public String toString() {
        return "ImageResolution{"
                + "tid=0x" + Integer.toHexString(this.getTripletId())
                + ", xUnitBase=0x" + Integer.toHexString(this.xUnitBase)
                + ", yUnitBase=0x" + Integer.toHexString(this.yUnitBase)
                + ", xupub=" + this.xupub
                + ", yupub=" + this.yupub
                + '}';
    }
}
