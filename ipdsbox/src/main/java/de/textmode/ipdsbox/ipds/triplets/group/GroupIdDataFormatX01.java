package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * MVS and VSE print-data format.
 */
public final class GroupIdDataFormatX01 extends GroupIdData {

    private final String jobClass;
    private final String jobName;
    private final String jobId;
    private final String forms;

    /**
     * Constructs the {@link GroupIdDataFormatX01}.
     * @param raw the raw IPDS data of the {@link GroupIdTriplet}.
     * @throws IOException if the {@link GroupIdTriplet} is broken.
     */
    public GroupIdDataFormatX01(final byte[] raw) throws IOException {
        super(raw, GroupIdFormat.MVS_AND_VSE);

        this.jobClass = this.getStream().readEbcdicString(1).trim();
        this.jobName = this.getStream().readEbcdicString(8).trim();
        this.jobId = this.getStream().readEbcdicString(8).trim();
        this.forms = this.getStream().readEbcdicString(8).trim();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();

        os.writeEbcdicString(this.jobClass, 1);
        os.writeEbcdicString(this.jobName, 8);
        os.writeEbcdicString(this.jobId, 8);
        os.writeEbcdicString(this.forms, 8);

        return os.toByteArray();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("JOBCLASS=");
        sb.append(this.jobClass);
        sb.append(", JOBNAME=");
        sb.append(this.jobName);
        sb.append(", JOBID=");
        sb.append(this.jobId);
        sb.append(", FORMS=");
        sb.append(this.forms);

        return sb.toString();
    }
}
