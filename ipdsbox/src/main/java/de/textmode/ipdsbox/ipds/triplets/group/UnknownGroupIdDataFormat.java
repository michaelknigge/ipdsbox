package de.textmode.ipdsbox.ipds.triplets.group;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

import java.io.IOException;

/**
 * Unknown group ID data format.
 */
public final class UnknownGroupIdDataFormat extends GroupIdData {

    private final byte[] rawData;

    /**
     * Constructs the {@link UnknownGroupIdDataFormat}.
     */
    public UnknownGroupIdDataFormat(final IpdsByteArrayInputStream ipds) throws IOException {
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
