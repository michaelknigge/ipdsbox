package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class InvokeCmrTriplet extends Triplet {

    private int hostAssignedId;

    /**
     * Constructs a {@link InvokeCmrTriplet} from the given byte array.
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public InvokeCmrTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.LinkedFont);
        this.hostAssignedId = this.getStream().readUnsignedInteger16();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(4);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedInteger16(this.hostAssignedId);

        return out.toByteArray();
    }

    /**
     * Returns the host assigned ID.
     * @return the host assigned ID.
     */
    public int getHostAssignedId() {
        return this.hostAssignedId;
    }

    /**
     * Sets the host assigned ID.
     */
    public void setHostAssignedId(final int hostAssignedId) {
        this.hostAssignedId = hostAssignedId;
    }

    @Override
    public String toString() {
        return "InvokeCmr{length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", haid=" + Integer.toHexString(this.hostAssignedId) +
                "}";
    }
}
