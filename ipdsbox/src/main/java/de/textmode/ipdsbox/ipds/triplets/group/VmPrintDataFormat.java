package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * VM print-data format.
 */
public final class VmPrintDataFormat extends GroupIdData {

    private final String spoolClass;
    private final String fileName;
    private final String userId;
    private final String formName;
    private final String spoolId;

    /**
     * Constructs the {@link VmPrintDataFormat}.
     */
    public VmPrintDataFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.spoolClass = ipds.readEbcdicString(1).trim();
        this.fileName = ipds.readEbcdicString(8).trim();
        this.userId = ipds.readEbcdicString(8).trim();
        this.formName = ipds.readEbcdicString(8).trim();
        this.spoolId = ipds.readEbcdicString(4).trim();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();

        os.writeEbcdicString(this.spoolClass, 1);
        os.writeEbcdicString(this.fileName, 8);
        os.writeEbcdicString(this.userId, 8);
        os.writeEbcdicString(this.formName, 8);
        os.writeEbcdicString(this.spoolId, 4);

        return os.toByteArray();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SPOOL CLASS=");
        sb.append(this.spoolClass);
        sb.append(", FILE NAME=");
        sb.append(this.fileName);
        sb.append(", USER ID=");
        sb.append(this.userId);
        sb.append(", FORM NAME=");
        sb.append(this.formName);
        sb.append(", SPOOL ID=");
        sb.append(this.spoolId);

        return sb.toString();
    }
}
