package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * Additional information format.
 */
public final class AdditionalInformationFormat extends GroupInformationData {

    private final byte[] rawData;

    /**
     * Constructs the {@link AdditionalInformationFormat}.
     */
    public AdditionalInformationFormat(final IpdsByteArrayInputStream ipds) throws IOException {

        // Additional information associated with this group. The information is
        // considered to be binary data, unless there was a preceding CGCSGID
        // triplet in the XOH-DGB command.
        this.rawData = ipds.readRemainingBytes();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return this.rawData;
    }

    @Override
    public String toString() {
        return "Group information = " + StringUtils.toHexString(this.rawData) + " (hex)";
    }
}
