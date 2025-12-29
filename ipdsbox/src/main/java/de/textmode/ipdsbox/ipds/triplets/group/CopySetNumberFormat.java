package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Copy set number format.
 */
public final class CopySetNumberFormat extends GroupInformationData {

    private final int copySetNumber;

    /**
     * Constructs the {@link CopySetNumberFormat}.
     */
    public CopySetNumberFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.copySetNumber = ipds.readUnsignedInteger16();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeUnsignedInteger16(this.copySetNumber);
        return os.toByteArray();
    }

    /**
     * Returns <code>true</code> if a copy set number is provided/available.
     */
    public boolean isCopySetNumberProvided() {
        return this.copySetNumber != 0;
    }

    /**
     * Returns <code>true</code> if the copy set number is larger than 65,534.
     */
    public boolean isHighCopySetNumber() {
        return this.copySetNumber == 0xFFFF;
    }

    @Override
    public String toString() {
        if (!this.isCopySetNumberProvided()) {
            return "No copy set number provided";
        }

        if (this.isHighCopySetNumber()) {
            return "Copy set number is larger than 65534";
        }

        return "Copy set number " + this.copySetNumber;
    }
}
