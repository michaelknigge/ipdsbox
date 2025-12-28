package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ObjectOffsetTriplet extends Triplet {

    private int objectType;
    private long objectOffset;


    /**
     * Constructs a {@link ObjectOffsetTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    ObjectOffsetTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.ObjectOffset);

        this.objectType = ipds.readUnsignedByte();
        ipds.skip(1);
        this.objectOffset = ipds.readUnsignedInteger32();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(8);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedByte(this.objectType);
        out.writeUnsignedByte(0x00);
        out.writeUnsignedInteger32(this.objectOffset);
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
        return "ObjectOffset{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", type=0x" + Integer.toHexString(this.objectType) +
                ", offset=" + this.objectOffset +
                "}";
    }
}
