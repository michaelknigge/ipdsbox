package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The UP3I Finishing Operation (X'8E') triplet specifies a specific finishing operation
 * to be applied to either a sheet or to a collection of sheets.
 */
public final class UP3IFinishingOperationTriplet extends Triplet {

    private final int sequenceNumber;
    private final byte[] finishingOperationData;

    /**
     * Constructs a {@link UP3IFinishingOperationTriplet} from the given byte array.
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public UP3IFinishingOperationTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.UP3IFinishingOperation);

        this.sequenceNumber = raw[2] & 0xFF;
        this.finishingOperationData = new byte[raw.length - 4];
        System.arraycopy(raw, 4, this.finishingOperationData, 0, this.finishingOperationData.length);
    }

    /**
     * Returns the sequence number of this triplet.
     * @return the sequence number of this triplet.
     */
    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    /**
     * Returns finishing operation data as defined in the UP3I Specification.
     * @return Finishing operation data as defined in the UP3I Specification.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "It is intended that the buffer may be modified")
    public byte[] getFinishingOperationData() {
        return this.finishingOperationData;
    }
}
