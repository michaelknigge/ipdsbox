package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class TonerSaverTriplet extends Triplet {

    private int control;

    public TonerSaverTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.TonerSaver);

        this.getStream().skip(1);
        this.control = this.getStream().readUnsignedByte();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(6);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(0);
        out.writeUnsignedByte(this.control);
        out.writeUnsignedInteger16(0);

        return out.toByteArray();
    }

    /**
     * Returns the Control.
     */
    public int getControl() {
        return this.control;
    }

    /**
     * Sets the Control.
     */
    public void setControl(final int control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return "TonerSaver{" +
                "length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", control=0x" + Integer.toHexString(this.control) + '}';
    }
}
