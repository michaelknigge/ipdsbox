package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ColorManagementResourceDescriptorTriplet extends Triplet {

    private int mode;

    public ColorManagementResourceDescriptorTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.ColorManagementResourceDescriptor);

        this.getStream().skip(1);
        this.mode = this.getStream().readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(0x05);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(0x00);
        out.writeUnsignedByte(this.mode);
        out.writeUnsignedByte(0x00);
    }

    /**
     * Returns the mode.
     */
    public int getMode() {
        return this.mode;
    }

    /**
     * Sets the mode.
     */
    public void setMode(final int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "ColorManagementResourceDescriptor{" +
                "length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", mode=0x" + Integer.toHexString(this.mode) +
                '}';
    }
}
