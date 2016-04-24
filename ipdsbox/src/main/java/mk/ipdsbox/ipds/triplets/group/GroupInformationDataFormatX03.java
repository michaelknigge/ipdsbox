package mk.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import mk.ipdsbox.core.StringUtils;
import mk.ipdsbox.ipds.triplets.GroupInformationTriplet;

/**
 * Group name format.
 */
public final class GroupInformationDataFormatX03 extends GroupInformationData {

    private final byte[] rawData;

    /**
     * Constructs the {@link GroupInformationDataFormatX03}.
     * @param raw the raw IPDS data of the {@link GroupInformationTriplet}.
     * @throws IOException if the {@link GroupInformationTriplet} is broken.
     */
    public GroupInformationDataFormatX03(final byte[] raw) throws IOException {
        super(raw, GroupInformationFormat.GROUP_NAME);

        // A 1 to 250 byte long group name. The name is considered to be binary data,
        // unless there was a preceding CGCSGID triplet in the XOH-DGB command.
        this.rawData = new byte[raw.length - 3];
        System.arraycopy(raw, 3, this.rawData, 0, raw.length - 3);
    }

    @Override
    public String toString() {
        return "Group name = " + StringUtils.toHexString(this.rawData) + " (hex)";
    }
}
