package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class SenseDataAcknowledgeData implements AcknowledgeData {

    // TODO: decode it correctly so we have concrete fields for the ack data...
    private byte[] rawAcknowledgeData;

    SenseDataAcknowledgeData(final IpdsByteArrayInputStream ipds) {
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

    public int getExceptionCode() {
        if (this.rawAcknowledgeData.length == 3) {
            return
                    ((this.rawAcknowledgeData[0] & 0xFF) << 16) |
                    ((this.rawAcknowledgeData[1] & 0xFF) << 8) |
                     (this.rawAcknowledgeData[2] & 0xFF);
        } else if (this.rawAcknowledgeData.length == 24) {
            return
                    ((this.rawAcknowledgeData[0] & 0xFF) << 16) |
                    ((this.rawAcknowledgeData[1] & 0xFF) << 8) |
                     (this.rawAcknowledgeData[19] & 0xFF);

        } else {
            return 0xFFFFFF;
        }
    }
}
