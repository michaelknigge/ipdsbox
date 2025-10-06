package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * AIX and OS/2 COM-data format.
 */
public final class GroupIdDataFormatX05 extends GroupIdData {

    private final int fileFype;
    private final String fileName;

    /**
     * Constructs the {@link GroupIdDataFormatX05}.
     * @param raw the raw IPDS data of the {@link GroupIdTriplet}.
     * @throws IOException if the {@link GroupIdTriplet} is broken.
     */
    public GroupIdDataFormatX05(final byte[] raw) throws IOException {
        super(raw, GroupIdFormat.AIX_AND_OS2);

        this.fileFype = this.getStream().readUnsignedByte();
        this.fileName = this.getStream()
                .readAsciiString(this.getStream().bytesAvailable())
                .trim();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();

        os.writeUnsignedByte(this.fileFype);
        os.writeAsciiString(this.fileName);

        return os.toByteArray();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FILE TYPE=");
        sb.append(Integer.toHexString(this.fileFype));
        sb.append(this.getFileTypeDescription());
        sb.append(", FILE NAME=");
        sb.append(this.fileName);

        return sb.toString();
    }

    private String getFileTypeDescription() {
        if (this.fileFype == 0x80) {
            return " (Job header)";
        } else if (this.fileFype == 0x40) {
            return " (Copy separator)";
        } else if (this.fileFype == 0x20) {
            return " (User print file)";
        } else if (this.fileFype == 0x10) {
            return " (Message file)";
        } else if (this.fileFype == 0x08) {
            return " (User exit page)";
        } else if (this.fileFype == 0x04) {
            return " (Job trailer)";
        } else if (this.fileFype == 0x00) {
            return " (Not specified)";
        } else {
            return " (Unknown)";
        }
    }
}
