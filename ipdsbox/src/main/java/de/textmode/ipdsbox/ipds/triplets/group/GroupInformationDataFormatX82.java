package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.ipds.triplets.GroupInformationTriplet;

/**
 * Extended copy set number format.
 */
public final class GroupInformationDataFormatX82 extends GroupInformationData {

    private final long copySetNumber;
    private final long totalCopies;

    /**
     * Constructs the {@link GroupInformationDataFormatX02}.
     * @param raw the raw IPDS data of the {@link GroupInformationTriplet}.
     * @throws IOException if the {@link GroupInformationTriplet} is broken.
     */
    public GroupInformationDataFormatX82(final byte[] raw) throws IOException {
        super(raw, GroupInformationFormat.EXTENDED_COPY_SET_NUMBER);

        this.copySetNumber = this.getStream().readDoubleWord();
        this.totalCopies = this.getStream().readDoubleWord();
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
