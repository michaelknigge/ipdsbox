package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * MVS and VSE COM-data format.
 */
public final class MvsAndVseComDataFormat extends GroupIdData {

    private final int fileFype;
    private final String jobClass;
    private final String jobName;
    private final String jobId;
    private final String forms;
    private final String programmerName;
    private final String roomNumber;
    private final String submissionDate;
    private final String submissionTime;

    /**
     * Constructs the {@link MvsAndVseComDataFormat}.
     */
    public MvsAndVseComDataFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        this.fileFype = ipds.readUnsignedByte();
        this.jobClass = ipds.readEbcdicString(1).trim();
        this.jobName = ipds.readEbcdicString(8).trim();
        this.jobId = ipds.readEbcdicString(8).trim();
        this.forms = ipds.readEbcdicString(8).trim();
        this.programmerName = ipds.readEbcdicString(60).trim();
        this.roomNumber = ipds.readEbcdicString(60).trim();
        this.submissionDate = ipds.readEbcdicString(11).trim();
        this.submissionTime = ipds.readEbcdicString(11).trim();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();

        os.writeUnsignedByte(this.fileFype);
        os.writeEbcdicString(this.jobClass, 1);
        os.writeEbcdicString(this.jobName, 8);
        os.writeEbcdicString(this.jobId, 8);
        os.writeEbcdicString(this.forms, 8);
        os.writeEbcdicString(this.programmerName, 60);
        os.writeEbcdicString(this.roomNumber, 60);
        os.writeEbcdicString(this.submissionDate, 11);
        os.writeEbcdicString(this.submissionTime, 11);

        return os.toByteArray();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FILE TYPE=");
        sb.append(Integer.toHexString(this.fileFype));
        sb.append(this.getFileTypeDescription());
        sb.append(", JOBCLASS=");
        sb.append(this.jobClass);
        sb.append(", JOBNAME=");
        sb.append(this.jobName);
        sb.append(", JOBID=");
        sb.append(this.jobId);
        sb.append(", FORMS=");
        sb.append(this.forms);
        sb.append(", PROGRAMMER=");
        sb.append(this.programmerName);
        sb.append(", ROOM=");
        sb.append(this.roomNumber);
        sb.append(", DATE=");
        sb.append(this.submissionDate);
        sb.append(", TIME=");
        sb.append(this.submissionTime);

        return sb.toString();
    }

    private String getFileTypeDescription() {
        if (this.fileFype == 0x80) {
            return " (Job header)";
        } else if (this.fileFype == 0x40) {
            return " (Data set header)";
        } else if (this.fileFype == 0x20) {
            return " (User data set)";
        } else if (this.fileFype == 0x10) {
            return " (Message data set)";
        } else if (this.fileFype == 0x04) {
            return " (Job trailer)";
        } else if (this.fileFype == 0x00) {
            return " (Not specified)";
        } else {
            return " (Unknown)";
        }
    }
}
