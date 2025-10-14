package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.sdf.SelfDefiningFieldId;

/**
 * Triplets are variable-length substructures that can be used within one or more IPDS commands to provide
 * additional information for the command. A triplet is a three-part self-defining variable-length parameter
 * consisting of a length byte, an identifier byte, and parameter-value bytes.
 */
public abstract class Triplet {

    private final TripletId tripletId;

    /**
     * Constructs a new {@link Triplet}.
     */
    public Triplet(final TripletId tripletId) {
        this.tripletId = tripletId;
    }

    /**
     * Constructs the {@link Triplet} from an {@link IpdsByteArrayInputStream}.
     */
    public Triplet(
            final IpdsByteArrayInputStream ipds,
            final TripletId expectedTripletId) throws IOException, UnknownTripletException {

        final int length = ipds.readUnsignedByte();
        final int available = ipds.bytesAvailable();
        if (length - 1 != available) {
            throw new IOException(String.format(
                    "A tripket to be read seems to be %1$d bytes long but the IPDS data stream ends after %2$d bytes",
                    length, available));
        }

        // TODO maybee better to create a "RawTriplet" instead of throwing an exception...
        this.tripletId = TripletId.getFor(ipds.readUnsignedByte());
        if (!tripletId.equals(expectedTripletId.getId())) {
            throw new IOException(String.format(
                    "Expected triplet 0x%1$s but got 0x%2$s",
                    Integer.toHexString(expectedTripletId.getId()),
                    Integer.toHexString(tripletId.getId())));
        }
    }

    /**
     * Returns the {@link TripletId} of the Triplet.
     */
    public final TripletId getTripletId() {
        return this.tripletId;
    }

    /**
     * Writes this {@link Triplet} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;
}
