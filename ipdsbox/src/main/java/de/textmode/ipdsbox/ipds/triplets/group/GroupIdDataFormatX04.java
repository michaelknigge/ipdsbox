package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * MVS and VSE COM-data format.
 */
public final class GroupIdDataFormatX04 extends GroupIdData {

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
     * Constructs the {@link GroupIdDataFormatX04}.
     * @param raw the raw IPDS data of the {@link GroupIdTriplet}.
     * @throws IOException if the {@link GroupIdTriplet} is broken.
     */
    public GroupIdDataFormatX04(final byte[] raw) throws IOException {
        super(raw, GroupIdFormat.MVS_AND_VSE_COM);

        this.fileFype = this.getStream().readUnsignedByte();
        this.jobClass = this.getStream().readEbcdicString(1).trim();
        this.jobName = this.getStream().readEbcdicString(8).trim();
        this.jobId = this.getStream().readEbcdicString(8).trim();
        this.forms = this.getStream().readEbcdicString(8).trim();
        this.programmerName = this.getStream().readEbcdicString(60).trim();
        this.roomNumber = this.getStream().readEbcdicString(60).trim();
        this.submissionDate = this.getStream().readEbcdicString(11).trim();
        this.submissionTime = this.getStream().readEbcdicString(11).trim();
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
