package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.util.Arrays;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The UP3I Finishing Operation (X'8E') triplet specifies a specific finishing operation
 * to be applied to either a sheet or to a collection of sheets.
 */
public final class UP3IFinishingOperationTriplet extends Triplet {

    private final int sequenceNumber;
    private final byte[] data;

    /**
     * Constructs a {@link UP3IFinishingOperationTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    public UP3IFinishingOperationTriplet(final IpdsByteArrayInputStream ipds) throws IOException, UnknownTripletException {
        super(ipds, TripletId.UP3IFinishingOperation);

        this.sequenceNumber = ipds.readUnsignedByte();
        this.data = ipds.readRemainingBytes();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(3 + this.data.length);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.sequenceNumber);
        out.writeBytes(this.data);
    }

    /**
     * Returns the sequence number of this triplet.
     */
    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    /**
     * Returns finishing operation data as defined in the UP3I Specification.
     */
    public byte[] getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "UP3IFinishingOperationTriplet{" +
                "sequenceNumber=" + this.sequenceNumber +
                ", data=" + StringUtils.toHexString(this.data) +
                "}";
    }
}
