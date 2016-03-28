package mk.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import mk.ipdsbox.ipds.triplets.GroupIdTriplet;

/**
 * VM print-data format.
 */
public final class GroupIdDataFormatX02 extends GroupIdData {

    private final String spoolClass;
    private final String fileName;
    private final String userId;
    private final String formName;
    private final String spoolId;

    /**
     * Constructs the {@link GroupIdDataFormatX02}.
     * @param raw the raw IPDS data of the {@link GroupIdTriplet}.
     * @throws IOException if the {@link GroupIdTriplet} is broken.
     */
    public GroupIdDataFormatX02(final byte[] raw) throws IOException {
        super(raw, GroupIdFormat.VM);

        this.spoolClass = this.getStream().readEbcdicEncodedString(1).trim();
        this.fileName = this.getStream().readEbcdicEncodedString(8).trim();
        this.userId = this.getStream().readEbcdicEncodedString(8).trim();
        this.formName = this.getStream().readEbcdicEncodedString(8).trim();
        this.spoolId = this.getStream().readEbcdicEncodedString(4).trim();
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
