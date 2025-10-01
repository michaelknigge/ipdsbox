package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.ipds.triplets.GroupInformationTriplet;

/**
 * Additional information format.
 */
public final class GroupInformationDataFormatX04 extends GroupInformationData {

    private final byte[] rawData;

    /**
     * Constructs the {@link GroupInformationDataFormatX04}.
     * @param raw the raw IPDS data of the {@link GroupInformationTriplet}.
     * @throws IOException if the {@link GroupInformationTriplet} is broken.
     */
    public GroupInformationDataFormatX04(final byte[] raw) throws IOException {
        super(raw, GroupInformationFormat.ADDITIONAL_INFORMATION);

        // Additional information associated with this group. The information is
        // considered to be binary data, unless there was a preceding CGCSGID
        // triplet in the XOH-DGB command.
        this.rawData = new byte[raw.length - 3];
        System.arraycopy(raw, 3, this.rawData, 0, raw.length - 3);
    }

    @Override
    public String toString() {
        return "Group information = " + StringUtils.toHexString(this.rawData) + " (hex)";
    }
}
