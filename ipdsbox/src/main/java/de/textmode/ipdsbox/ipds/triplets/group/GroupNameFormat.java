package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * Group name format.
 */
public final class GroupNameFormat extends GroupInformationData {

    private final byte[] rawData;

    /**
     * Constructs the {@link GroupNameFormat}.
     */
    public GroupNameFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        // A 1 to 250 byte long group name. The name is considered to be binary data,
        // unless there was a preceding CGCSGID triplet in the XOH-DGB command.
        this.rawData = ipds.readRemainingBytes();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return this.rawData;
    }

    @Override
    public String toString() {
        return "Group name = " + StringUtils.toHexString(this.rawData) + " (hex)";
    }
}
