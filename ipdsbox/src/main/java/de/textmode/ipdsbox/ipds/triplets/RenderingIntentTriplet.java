package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class RenderingIntentTriplet extends Triplet {

    private int ioca;
    private int objectContainer;
    private int ptoca;
    private int goca;

    /**
     * Constructs a {@link RenderingIntentTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    RenderingIntentTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.RenderingIntent);

        ipds.skip(2);
        this.ioca = ipds.readUnsignedByte();
        this.objectContainer = ipds.readUnsignedByte();
        this.ptoca = ipds.readUnsignedByte();
        this.goca = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(0x0A);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedInteger16(0x0000);
        out.writeUnsignedByte(this.ioca);
        out.writeUnsignedByte(this.objectContainer);
        out.writeUnsignedByte(this.ptoca);
        out.writeUnsignedByte(this.goca);
        out.writeUnsignedInteger16(0x0000);
    }

    /**
     * Returns the IOCA.
     */
    public int getIOCA() {
        return this.ioca;
    }

    /**
     * Sets the IOCA.
     */
    public void setIOCA(final int ioca) {
        this.ioca = ioca;
    }

    /**
     * Returns the Object container.
     */
    public int getObjectContainer() {
        return this.objectContainer;
    }

    /**
     * Sets the Object container.
     */
    public void setObjectContainer(final int objectContainer) {
        this.objectContainer = objectContainer;
    }

    /**
     * Returns the PTOCA.
     */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public int getPTOCA() {
        return this.ptoca;
    }

    /**
     * Sets the PTOCA.
     */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public void setPTOCA(final int ptoca) {
        this.ptoca = ptoca;
    }

    /**
     * Returns the GOCA.
     */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public int getGOCA() {
        return this.goca;
    }

    /**
     * Sets the GOCA.
     */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public void setGOCA(final int goca) {
        this.goca = goca;
    }

    @Override
    public String toString() {
        return "RenderingIntent{"
                + "tid=0x" + Integer.toHexString(this.getTripletId())
                + ", ioca=0x" + Integer.toHexString(this.ioca)
                + ", objectContainer=0x" + Integer.toHexString(this.objectContainer)
                + ", ptoca=0x" + Integer.toHexString(this.ptoca)
                + ", goca=0x" + Integer.toHexString(this.goca)
                + '}';
    }
}
