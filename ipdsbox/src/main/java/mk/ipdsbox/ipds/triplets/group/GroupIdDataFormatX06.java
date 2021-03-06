package mk.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import mk.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * AIX and Windows print-data format.
 */
public final class GroupIdDataFormatX06 extends GroupIdData {

    private final String fileName;

    /**
     * Constructs the {@link GroupIdDataFormatX06}.
     * @param raw the raw IPDS data of the {@link GroupIdTriplet}.
     * @throws IOException if the {@link GroupIdTriplet} is broken.
     */
    public GroupIdDataFormatX06(final byte[] raw) throws IOException {
        super(raw, GroupIdFormat.AIX_AND_WINDOWS);

        this.fileName = this.getStream().readAsciiEncodedString().trim();
    }

    @Override
    public String toString() {
        return "FILE NAME=" + this.fileName;
    }
}
