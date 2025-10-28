package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class RawAcknowledgeData implements AcknowledgeData {

    private byte[] rawAcknowledgeData;

    RawAcknowledgeData(final IpdsByteArrayInputStream ipds) {
        this.rawAcknowledgeData = ipds.readRemainingBytes();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeBytes(this.rawAcknowledgeData);
    }

    /**
     * Returns the raw acknowledge data
     */
    public byte[] getRawAcknowledgeData() {
        return this.rawAcknowledgeData;
    }

    /**
     * Sets the raw acknowledge data
     */
    public void setRawAcknowledgeData(final byte[] rawAcknowledgeData) {
        this.rawAcknowledgeData = rawAcknowledgeData;
    }

    @Override
    public String toString() {
        return "RawAcknowledgeData{" +
                "rawAcknowledgeData=" + StringUtils.toHexString(this.rawAcknowledgeData) +
                '}';
    }
}
