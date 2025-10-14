package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * A self-defining field describes characteristics of an IPDS  printer.
 */
public abstract class SelfDefiningField {

    private SelfDefiningFieldId selfDefiningFieldId;

    /**
     * Constructs the {@link SelfDefiningField}.
     */
    public SelfDefiningField(final SelfDefiningFieldId selfDefiningFieldId) {
        this.selfDefiningFieldId = selfDefiningFieldId;
    }

    /**
     * Constructs the {@link SelfDefiningField}.
     */
    public SelfDefiningField(
            final IpdsByteArrayInputStream ipds,
            final SelfDefiningFieldId expectedId) throws IOException, UnknownSelfDefinedFieldException {

        final int length = ipds.readUnsignedInteger16();
        final int available = ipds.bytesAvailable();

        if (length - 2 != available) {
            throw new IOException(String.format(
                    "A self-defining field to be read seems to be %1$d bytes long but the IPDS data stream ends after %2$d bytes",
                    length, available));
        }

        this.selfDefiningFieldId = SelfDefiningFieldId.getFor(ipds.readUnsignedInteger16());
        if (!this.selfDefiningFieldId.equals(expectedId)) {
            throw new IOException(String.format(
                    "Expected self-defined field 0x%1$s but got 0x%2$s",
                    Integer.toHexString(expectedId.getId()),
                    Integer.toHexString(this.selfDefiningFieldId.getId())));
        }
    }

    /**
     * Writes this {@link SelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;

    /**
     * Returns the self-defining field ID.
     */
    public SelfDefiningFieldId getSelfDefiningFieldId() {
        return this.selfDefiningFieldId;
    }

    /**
     * Sets the self-defining field ID.
     */
    public void setSelfDefiningFieldId(final SelfDefiningFieldId selfDefiningFieldId) {
        this.selfDefiningFieldId = selfDefiningFieldId;
    }
}
