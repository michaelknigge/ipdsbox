package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ColorManagementResourceDescriptorTriplet extends Triplet {

    private int mode;

    public ColorManagementResourceDescriptorTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.ColorManagementResourceDescriptor);

        ipds.skip(1);
        this.mode = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(0x05);
        out.writeUnsignedByte(this.getTripletId());
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
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", mode=0x" + Integer.toHexString(this.mode) +
                "}";
    }
}
