package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ObjectContainerPresentationSpaceSizeTriplet extends Triplet {

    private int pdfPresentationSpaceSize;
    private int xUnitBase;
    private int yUnitBase;
    private int xupub;
    private int yupub;
    private int xocExtent;
    private int yocExtent;

    /**
     * Constructs a {@link ObjectContainerPresentationSpaceSizeTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    ObjectContainerPresentationSpaceSizeTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.ObjectContainerPresentationSpaceSize);

        ipds.skip(2);
        this.pdfPresentationSpaceSize = ipds.readUnsignedByte();

        if (ipds.bytesAvailable() >= 12) {
            this.xUnitBase = ipds.readUnsignedByte();
            this.yUnitBase = ipds.readUnsignedByte();
            this.xupub = ipds.readUnsignedInteger16();
            this.yupub = ipds.readUnsignedInteger16();
            this.xocExtent = ipds.readUnsignedInteger24();
            this.yocExtent = ipds.readUnsignedInteger24();
        } else {
            this.xUnitBase = 0x00;
            this.yUnitBase = 0x00;
            this.xupub = 0;
            this.yupub = 0;
            this.xocExtent = 0;
            this.yocExtent = 0;
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        if (this.xupub != 0 && this.yupub != 0 && this.xocExtent != 0 && this.yocExtent != 0) {
            out.writeUnsignedByte(0x11);
            out.writeUnsignedByte(this.getTripletId());
            out.writeUnsignedInteger16(0x0000);
            out.writeUnsignedByte(this.pdfPresentationSpaceSize);
            out.writeUnsignedByte(this.xUnitBase);
            out.writeUnsignedByte(this.yUnitBase);
            out.writeUnsignedInteger16(this.xupub);
            out.writeUnsignedInteger16(this.yupub);
            out.writeUnsignedInteger24(this.xocExtent);
            out.writeUnsignedInteger24(this.yocExtent);
        } else {
            out.writeUnsignedByte(0x05);
            out.writeUnsignedByte(this.getTripletId());
            out.writeUnsignedInteger16(0x0000);
            out.writeUnsignedByte(this.pdfPresentationSpaceSize);
        }
    }

    /**
     * Returns the PDF Presentation space size.
     */
    public int getPdfPresentationSpaceSize() {
        return this.pdfPresentationSpaceSize;
    }

    /**
     * Sets the PDF Presentation space size.
     */
    public void setPdfPresentationSpaceSize(final int pdfPresentationSpaceSize) {
        this.pdfPresentationSpaceSize = pdfPresentationSpaceSize;
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
    public void setYupub(final int yupub) {
        this.yupub = yupub;
    }

    /**
     * Returns the Xoc extent.
     */
    public int getXocExtent() {
        return this.xocExtent;
    }

    /**
     * Sets the Xoc extent.
     */
    public void setXocExtent(final int xocExtent) {
        this.xocExtent = xocExtent;
    }

    /**
     * Returns the Yoc extent.
     */
    public int getYocExtent() {
        return this.yocExtent;
    }

    /**
     * Sets the Yoc extent.
     */
    public void setYocExtent(final int yocExtent) {
        this.yocExtent = yocExtent;
    }

    @Override
    public String toString() {
        if (this.xupub != 0 && this.yupub != 0 && this.xocExtent != 0 && this.yocExtent != 0) {
            return "ObjectContainerPresentationSpaceSize{" +
                    "tid=0x" + Integer.toHexString(this.getTripletId()) +
                    ", pdfPresentationSpaceSize=0x" + Integer.toHexString(this.pdfPresentationSpaceSize) +
                    ", xUnitBase=" + Integer.toHexString(this.xUnitBase) +
                    ", yUnitBase=" + Integer.toHexString(this.yUnitBase) +
                    ", xupub=" + this.xupub +
                    ", yupub=" + this.yupub +
                    ", xocExtent=" + this.xocExtent +
                    ", yocExtent=" + this.yocExtent +
                    '}';
        }

        return "ObjectContainerPresentationSpaceSize{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", pdfPresentationSpaceSize=0x" + Integer.toHexString(this.pdfPresentationSpaceSize) +
                '}';
    }
}
