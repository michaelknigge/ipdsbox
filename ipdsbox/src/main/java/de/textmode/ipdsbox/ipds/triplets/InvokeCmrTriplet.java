package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class InvokeCmrTriplet extends Triplet {

    private int hostAssignedId;

    /**
     * Constructs a {@link InvokeCmrTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    InvokeCmrTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.LinkedFont);
        this.hostAssignedId = ipds.readUnsignedInteger16();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(4);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedInteger16(this.hostAssignedId);
    }

    /**
     * Returns the host assigned ID.
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
        return "InvokeCmr{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", haid=" + Integer.toHexString(this.hostAssignedId) +
                "}";
    }
}
