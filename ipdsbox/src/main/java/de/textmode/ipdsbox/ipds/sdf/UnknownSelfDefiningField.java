package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * A self-defining field that is not parsed.
 */
public class UnknownSelfDefiningField extends SelfDefiningField{

    private byte[] rawData;

    /**
     * Constructs the {@link UnknownSelfDefiningField}.
     */
    public UnknownSelfDefiningField(final IpdsByteArrayInputStream ipds, final int sdfId) throws IOException {
        super (sdfId);
        this.rawData = ipds.readRemainingBytes();
    }

    /**
     * Writes this {@link UnknownSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + this.rawData.length);
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        out.writeBytes(this.rawData);
    }

    /**
     * Returns the raw data.
     */
    public byte[] getRawData() {
        return this.rawData;
    }

    /**
     * Sets the raw data.
     */
    public void setRawData(final byte[] rawData) {
        this.rawData = rawData;
    }

    @Override
    public String toString() {
        return "UnknownSelfDefiningField{"
                + "sdfid=" + Integer.toHexString(this.getSelfDefiningFieldId())
                + ", rawData=" + StringUtils.toHexString(this.rawData)
                + '}';
    }
}
