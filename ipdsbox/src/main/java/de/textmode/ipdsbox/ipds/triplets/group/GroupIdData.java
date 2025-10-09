package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;
import de.textmode.ipdsbox.ipds.triplets.TripletId;

/**
 * Superclass of all classes representing the group data of the {@link GroupIdTriplet}.
 */
public abstract class GroupIdData {

    // TODO get rid of this thing!
    private final IpdsByteArrayInputStream stream;

    protected GroupIdData() {
        stream = null;
    }

    /**
     * Constructs the {@link GroupIdData}.
     * @param raw the raw IPDS data of the {@link GroupIdTriplet}.
     * @param expectedFormat the format of the {@link GroupIdData} this is expected in the raw data.
     * @throws IOException if the {@link GroupIdTriplet} is broken.
     */
    protected GroupIdData(final byte[] raw, final GroupIdFormat expectedFormat) throws IOException {
        this.stream = new IpdsByteArrayInputStream(raw);

        final int length = this.stream.readUnsignedByte();
        if (length < 3) {
            throw new IpdsboxRuntimeException("Passed Triplet is invalid");
        }

        final int id = this.stream.readByte();
        if (id != TripletId.GroupID.getId()) {
            throw new IpdsboxRuntimeException("Passed Triplet is no Group ID triplet");
        }

        final int format = this.stream.readByte();
        if (format != expectedFormat.getId()) {
            throw new IpdsboxRuntimeException(String.format("Expected format X'%1$s' but got X'%2$s'",
                Integer.toHexString(expectedFormat.getId()), Integer.toHexString(format)));
        }
    }

    /**
     * Returns the {@link IpdsByteArrayInputStream} that is used to parse the data.
     * @return the {@link IpdsByteArrayInputStream} that is used to parse the data.
     */
    protected final IpdsByteArrayInputStream getStream() {
        return this.stream;
    }

    /**
     * Returns the raw bytes of this {@link GroupIdData}.
     * @return the raw bytes of this {@link GroupIdData}.
     */
    public abstract byte[] toByteArray() throws IOException;
}
