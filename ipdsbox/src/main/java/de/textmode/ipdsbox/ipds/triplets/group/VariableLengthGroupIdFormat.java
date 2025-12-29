package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * Variable-length Group ID format.
 */
public final class VariableLengthGroupIdFormat extends GroupIdData {

    private final byte[] rawData;

    /**
     * Constructs the {@link VariableLengthGroupIdFormat}.
     */
    public VariableLengthGroupIdFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.rawData = ipds.readRemainingBytes();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return rawData;
    }

    @Override
    public String toString() {
        return "DATA=" + StringUtils.toHexString(this.rawData);
    }
}
