package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Extended copy set number format.
 */
public final class ExtendedCopySetNumberFormat extends GroupInformationData {

    private final long copySetNumber;
    private final long totalCopies;

    /**
     * Constructs the {@link ExtendedCopySetNumberFormat}.
     */
    public ExtendedCopySetNumberFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.copySetNumber = ipds.readUnsignedInteger32();
        this.totalCopies = ipds.readUnsignedInteger32();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();

        os.writeUnsignedInteger32(this.copySetNumber);
        os.writeUnsignedInteger32(this.totalCopies);

        return os.toByteArray();
    }

    /**
     * Returns <code>true</code> if a copy set number is provided/available.
     * @return <code>true</code> if a copy set number is provided/available.
     */
    public boolean isCopySetNumberProvided() {
        return this.copySetNumber != 0;
    }

    /**
     * Returns <code>true</code> if the total number of copies is provided/available.
     * @return <code>true</code> if the total number of copies is provided/available.
     */
    public boolean isTotalNumberOfCopiesProvided() {
        return this.totalCopies != 0;
    }

    /**
     * Returns <code>true</code> if the copy set number is larger than 4,294,967,294.
     * @return <code>true</code> if the copy set number is larger than 4,294,967,294.
     */
    public boolean isHighCopySetNumber() {
        return this.copySetNumber == 0xFFFFFFFF;
    }

    /**
     * Returns <code>true</code> if the total number of copies is larger than 4,294,967,294.
     * @return <code>true</code> if the total number of copies is larger than 4,294,967,294.
     */
    public boolean isHighNumberOfCopies() {
        return this.totalCopies == 0xFFFFFFFF;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        if (!this.isCopySetNumberProvided()) {
            sb.append("No copy set number provided");
        } else if (this.isHighCopySetNumber()) {
            sb.append("Copy set number is larger than 4,294,967,294");
        } else {
            sb.append("Copy set number " + this.copySetNumber);
        }

        if (!this.isTotalNumberOfCopiesProvided()) {
            sb.append(", no number of copies provided");
        } else if (this.isHighCopySetNumber()) {
            sb.append(", number of copies is larger than 4,294,967,294");
        } else {
            sb.append(", number of copies " + this.totalCopies);
        }

        return sb.toString();
    }
}
