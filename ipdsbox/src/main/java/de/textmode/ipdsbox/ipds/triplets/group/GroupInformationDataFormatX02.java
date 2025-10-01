package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.ipds.triplets.GroupInformationTriplet;

/**
 * Copy set number format.
 */
public final class GroupInformationDataFormatX02 extends GroupInformationData {

    private final int copySetNumber;

    /**
     * Constructs the {@link GroupInformationDataFormatX02}.
     * @param raw the raw IPDS data of the {@link GroupInformationTriplet}.
     * @throws IOException if the {@link GroupInformationTriplet} is broken.
     */
    public GroupInformationDataFormatX02(final byte[] raw) throws IOException {
        super(raw, GroupInformationFormat.COPY_SET_NUMBER);

        this.copySetNumber = this.getStream().readWord();
    }

    /**
     * Returns <code>true</code> if a copy set number is provided/available.
     * @return <code>true</code> if a copy set number is provided/available.
     */
    public boolean isCopySetNumberProvided() {
        return this.copySetNumber != 0;
    }

    /**
     * Returns <code>true</code> if the copy set number is larger than 65,534.
     * @return <code>true</code> if the copy set number is larger than 65,534.
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
            return "Copy set number is larger than 65,534";
        }

        return "Copy set number " + this.copySetNumber;
    }
}
