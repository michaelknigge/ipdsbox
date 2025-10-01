package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * Variable-length Group ID format.
 */
public final class GroupIdDataFormatX08 extends GroupIdData {

    private final byte[] rawData;

    /**
     * Constructs the {@link GroupIdDataFormatX08}.
     * @param raw the raw IPDS data of the {@link GroupIdTriplet}.
     * @throws IOException if the {@link GroupIdTriplet} is broken.
     */
    public GroupIdDataFormatX08(final byte[] raw) throws IOException {
        super(raw, GroupIdFormat.VARIABLE_LENGTH_GROUP_ID);

        this.rawData = new byte[raw.length - 3];
        System.arraycopy(raw, 3, this.rawData, 0, raw.length - 3);
    }

    @Override
    public String toString() {
        return "DATA=" + StringUtils.toHexString(this.rawData);
    }
}
