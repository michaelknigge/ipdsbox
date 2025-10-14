package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * Extended OS/400 print-data format.
 */
public final class ExtendedOs400PrintDataFormat extends GroupIdData {

    private final String libraryName;
    private final String queueName;
    private final String fileName;
    private final String fileNumber;
    private final String jobName;
    private final String userName;
    private final String jobNumber;
    private final String formsName;

    /**
     * Constructs the {@link ExtendedOs400PrintDataFormat}.
     */
    public ExtendedOs400PrintDataFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.libraryName = ipds.readEbcdicString(10).trim();
        this.queueName = ipds.readEbcdicString(10).trim();
        this.fileName = ipds.readEbcdicString(10).trim();
        this.fileNumber = ipds.readEbcdicString(6).trim();
        this.jobName = ipds.readEbcdicString(10).trim();
        this.userName = ipds.readEbcdicString(10).trim();
        this.jobNumber = ipds.readEbcdicString(6).trim();
        this.formsName = ipds.readEbcdicString(10).trim();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();

        os.writeEbcdicString(this.libraryName, 10);
        os.writeEbcdicString(this.queueName, 10);
        os.writeEbcdicString(this.fileName, 10);
        os.writeEbcdicString(this.fileNumber, 6);
        os.writeEbcdicString(this.jobName, 10);
        os.writeEbcdicString(this.userName, 10);
        os.writeEbcdicString(this.jobNumber, 6);
        os.writeEbcdicString(this.formsName, 10);

        return os.toByteArray();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LIBRARY NAME=");
        sb.append(this.libraryName);
        sb.append(", QUEUE NAME=");
        sb.append(this.queueName);
        sb.append(", FILE NAME=");
        sb.append(this.fileName);
        sb.append(", FILE NUMBER=");
        sb.append(this.fileNumber);
        sb.append(", JOB NAME=");
        sb.append(this.jobName);
        sb.append(", USER NAME=");
        sb.append(this.userName);
        sb.append(", JOB NUMBER=");
        sb.append(this.jobNumber);
        sb.append(", FORMS NAME=");
        sb.append(this.formsName);

        return sb.toString();
    }
}
