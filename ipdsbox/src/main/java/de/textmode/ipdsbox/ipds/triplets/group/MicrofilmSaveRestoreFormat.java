package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Microfilm save/restore format.
 */
public final class MicrofilmSaveRestoreFormat extends GroupInformationData {

    private final int indicatorByte;

    /**
     * Constructs the {@link MicrofilmSaveRestoreFormat}.
     */
    public MicrofilmSaveRestoreFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.indicatorByte = ipds.readUnsignedByte();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeUnsignedByte(this.indicatorByte);
        return os.toByteArray();
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
