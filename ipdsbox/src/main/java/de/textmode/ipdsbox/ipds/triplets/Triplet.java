package de.textmode.ipdsbox.ipds.triplets;

import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * Triplets are variable-length substructures that can be used within one or more IPDS commands to provide
 * additional information for the command. A triplet is a three-part self-defining variable-length parameter
 * consisting of a length byte, an identifier byte, and parameter-value bytes.
 */
public abstract class Triplet {

    private final IpdsByteArrayInputStream stream;
    private final int length;
    private final TripletId tripletId;
    private final byte[] data;

    /**
     * Constructs the {@link Triplet}.
     * @param raw the raw data of the whole triplet.
     * @param tripletId the expected Triplet ID
     */
    public Triplet(final byte[] raw, final TripletId tripletId) {

        // TEST "raw" is no longer required... Except for the if here...
        if ((raw[1] & 0xFF) != tripletId.getId()) {
            throw new IpdsboxRuntimeException("Passed invalid data");
        }

        this.length = raw[0] & 0xFF;
        this.tripletId = tripletId;
        this.data = new byte[raw.length - 2];

        // For "high performance" we could use the given raw data instead of making a copy of the data...
        System.arraycopy(raw, 2, this.data, 0, raw.length - 2);

        this.stream = new IpdsByteArrayInputStream(this.data);
    }

    /**
     * Returns the {@link IpdsByteArrayInputStream} that is used to parse the data.
     * @return the {@link IpdsByteArrayInputStream} that is used to parse the data.
     */
    protected final IpdsByteArrayInputStream getStream() {
        return this.stream;
    }

    /**
     * Returns the length of the whole Triplet.
     * @return length of the whole Triplet (including the length field and the Triplet ID).
     */
    public final int getLength() {
        return this.length;
    }

    /**
     * Returns the {@link TripletId} of the Triplet.
     * @return the {@link TripletId} of the Triplet.
     */
    public final TripletId getTripletId() {
        return this.tripletId;
    }

    /**
     * Returns the data of the Triplet.
     * @return the data of the Triplet (not including the length field and the Triplet ID).
     */
    //@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "It is intended that the buffer may be modified")
    //public final byte[] getData() {
    //    return this.data;
    //}
}
