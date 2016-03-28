package mk.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import mk.ipdsbox.ipds.triplets.GroupIdTriplet;

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

        this.jobClass = this.getStream().readEbcdicEncodedString(1).trim();
        this.jobName = this.getStream().readEbcdicEncodedString(8).trim();
        this.jobId = this.getStream().readEbcdicEncodedString(8).trim();
        this.forms = this.getStream().readEbcdicEncodedString(8).trim();
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
