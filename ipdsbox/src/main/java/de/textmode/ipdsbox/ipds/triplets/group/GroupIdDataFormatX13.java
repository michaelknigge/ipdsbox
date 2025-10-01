package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * Extended OS/400 print-data format.
 */
public final class GroupIdDataFormatX13 extends GroupIdData {

    private final String libraryName;
    private final String queueName;
    private final String fileName;
    private final String fileNumber;
    private final String jobName;
    private final String userName;
    private final String jobNumber;
    private final String formsName;

    /**
     * Constructs the {@link GroupIdDataFormatX13}.
     * @param raw the raw IPDS data of the {@link GroupIdTriplet}.
     * @throws IOException if the {@link GroupIdTriplet} is broken.
     */
    public GroupIdDataFormatX13(final byte[] raw) throws IOException {
        super(raw, GroupIdFormat.EXTENDED_OS400);

        this.libraryName = this.getStream().readEbcdicEncodedString(10).trim();
        this.queueName = this.getStream().readEbcdicEncodedString(10).trim();
        this.fileName = this.getStream().readEbcdicEncodedString(10).trim();
        this.fileNumber = this.getStream().readEbcdicEncodedString(6).trim();
        this.jobName = this.getStream().readEbcdicEncodedString(10).trim();
        this.userName = this.getStream().readEbcdicEncodedString(10).trim();
        this.jobNumber = this.getStream().readEbcdicEncodedString(6).trim();
        this.formsName = this.getStream().readEbcdicEncodedString(10).trim();
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
