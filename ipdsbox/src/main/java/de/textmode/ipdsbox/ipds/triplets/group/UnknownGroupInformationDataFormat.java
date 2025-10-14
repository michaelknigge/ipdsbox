package de.textmode.ipdsbox.ipds.triplets.group;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

import java.io.IOException;

/**
 * Unknown group information data format.
 */
public final class UnknownGroupInformationDataFormat extends GroupInformationData {

    private final byte[] rawData;

    /**
     * Constructs the {@link UnknownGroupInformationDataFormat}.
     */
    public UnknownGroupInformationDataFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.rawData = ipds.readRemainingBytes();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return this.rawData;
    }

    @Override
    public String toString() {
        return "Raw data = " + StringUtils.toHexString(this.rawData) + " (hex)";
    }
}
