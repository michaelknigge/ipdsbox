package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ObjectOffsetTriplet extends Triplet {

    private int objectType;
    private long objectOffset; // UBIN(4) → long


    public ObjectOffsetTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.ObjectOffset);

        this.objectType = this.getStream().readUnsignedByte();
        this.getStream().skip(1);
        this.objectOffset = this.getStream().readUnsignedInteger32();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(8);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.objectType);
        out.writeUnsignedByte(0x00);
        out.writeUnsignedInteger32(this.objectOffset);

        return out.toByteArray();
    }

    /**
     * Returns the Object type.
     */
    public int getObjectType() {
        return this.objectType;
    }

    /**
     * Sets the Object type.
     */
    public void setObjectType(final int objectType) {
        this.objectType = objectType;
    }

    /**
     * Returns the Object offset.
     */
    public long getObjectOffset() {
        return this.objectOffset;
    }

    /**
     * Sets the Object offset.
     */
    public void setObjectOffset(final long objectOffset) {
        this.objectOffset = objectOffset;
    }

    @Override
    public String toString() {
        return "ObjectOffset{len=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", type=0x" + Integer.toHexString(this.objectType) +
                ", offset=" + this.objectOffset + "}";
    }
}
