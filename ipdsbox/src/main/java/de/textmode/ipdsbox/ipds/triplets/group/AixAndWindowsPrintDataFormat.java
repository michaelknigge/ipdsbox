package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * AIX and Windows print-data format.
 */
public final class AixAndWindowsPrintDataFormat extends GroupIdData {

    private final String fileName;

    /**
     * Constructs the {@link AixAndWindowsPrintDataFormat}.
     */
    public AixAndWindowsPrintDataFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.fileName = ipds
                .readAsciiString(ipds.bytesAvailable())
                .trim();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();

        os.writeAsciiString(this.fileName);

        return os.toByteArray();
    }

    @Override
    public String toString() {
        return "FILE NAME=" + this.fileName;
    }
}
