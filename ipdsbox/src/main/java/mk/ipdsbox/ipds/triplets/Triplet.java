package mk.ipdsbox.ipds.triplets;

/**
 * Triplets are variable-length substructures that can be used within one or more IPDS commands to provide
 * additional information for the command. A triplet is a three-part self-defining variable-length parameter
 * consisting of a length byte, an identifier byte, and parameter-value bytes.
 */
public abstract class Triplet {

    private final int length;
    private final TripletId tripletId;
    private final byte[] data;

    /**
     * Constructs the {@link Triplet}.
     * @param raw the raw data of the whole triplet.
     */
    public Triplet(final byte[] raw) {
        this.length = raw[0] & 0xFF;
        this.tripletId = TripletId.getForIfExists(raw[1] & 0xFF);
        this.data = new byte[raw.length - 2];

        System.arraycopy(raw, 2, this.data, 0, raw.length - 2);
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
    public final byte[] getData() {
        return this.data;
    }
}
