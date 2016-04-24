package mk.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import mk.ipdsbox.core.IpdsboxRuntimeException;
import mk.ipdsbox.io.IpdsByteArrayInputStream;
import mk.ipdsbox.ipds.triplets.GroupInformationTriplet;
import mk.ipdsbox.ipds.triplets.TripletId;

/**
 * Superclass of all classes representing the group data of the {@link GroupInformationTriplet}.
 */
public abstract class GroupInformationData {

    private final IpdsByteArrayInputStream stream;

    /**
     * Constructs the {@link GroupInformationData}.
     * @param raw the raw IPDS data of the {@link GroupInformationTriplet}.
     * @param expectedFormat the format of the {@link GroupInformationData} this is expected in the raw data.
     * @throws IOException if the {@link GroupInformationTriplet} is broken.
     */
    protected GroupInformationData(final byte[] raw, final GroupInformationFormat expectedFormat) throws IOException {
        this.stream = new IpdsByteArrayInputStream(raw);

        final int length = this.stream.readByte();
        if (length < 3) {
            throw new IpdsboxRuntimeException("Passed Triplet is invalid");
        }

        final int id = this.stream.readByte();
        if (id != TripletId.GroupInformation.getId()) {
            throw new IpdsboxRuntimeException("Passed Triplet is no Group Information triplet");
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
}
