package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * A self-defining field that is not parsed.
 */
public class RawSelfDefiningField extends SelfDefiningField{

    private byte[] rawData;

    /**
     * Constructs the {@link RawSelfDefiningField}.
     */
    public RawSelfDefiningField(final SelfDefiningFieldId id) throws IOException {
        super (id);
    }

    /**
     * Constructs the {@link RawSelfDefiningField}.
     */
    public RawSelfDefiningField(
            final IpdsByteArrayInputStream ipds,
            final SelfDefiningFieldId expectedId) throws IOException, UnknownSelfDefinedFieldException {

        super (ipds, expectedId);

        this.rawData = ipds.readRemainingBytes();
    }

    /**
     * Writes this {@link RawSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + this.rawData.length);
        out.writeUnsignedInteger16(super.getSelfDefiningFieldId().getId());
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
}
