package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.ipds.triplets.GroupInformationTriplet;

/**
 * Microfilm save/restore format.
 */
public final class GroupInformationDataFormatX01 extends GroupInformationData {

    private final int indicatorByte;

    /**
     * Constructs the {@link GroupInformationDataFormatX01}.
     * @param raw the raw IPDS data of the {@link GroupInformationTriplet}.
     * @throws IOException if the {@link GroupInformationTriplet} is broken.
     */
    public GroupInformationDataFormatX01(final byte[] raw) throws IOException {
        super(raw, GroupInformationFormat.MICROFILM_SAVE_RESTORE);

        this.indicatorByte = this.getStream().readUnsignedByte();
    }

    /**
     * Indicates to save microfilm information associated with the group identified by a Group ID triplet.
     * @return true if microfilm information is to be saved.
     */
    public boolean isSaveMicrofilmInformation() {
        return this.indicatorByte == 0x80;
    }

    /**
     * Indicates to restore microfilm information associated with the group identified by a Group ID triplet.
     * @return true if microfilm information is to be restored.
     */
    public boolean isRestoreMicrofilmInformation() {
        return this.indicatorByte == 0x40;
    }

    @Override
    public String toString() {
        if (this.indicatorByte == 0x80) {
            return "Save microfilm information";
        }

        if (this.indicatorByte == 0x40) {
            return "Restore microfilm information";
        }

        return "Unknown value " + this.indicatorByte;
    }
}
