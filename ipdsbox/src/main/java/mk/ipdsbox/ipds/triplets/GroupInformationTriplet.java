package mk.ipdsbox.ipds.triplets;

import java.io.IOException;

import mk.ipdsbox.core.IpdsboxRuntimeException;
import mk.ipdsbox.ipds.triplets.group.GroupInformationData;
import mk.ipdsbox.ipds.triplets.group.GroupInformationDataFormatX01;
import mk.ipdsbox.ipds.triplets.group.GroupInformationDataFormatX02;
import mk.ipdsbox.ipds.triplets.group.GroupInformationDataFormatX03;
import mk.ipdsbox.ipds.triplets.group.GroupInformationDataFormatX04;
import mk.ipdsbox.ipds.triplets.group.GroupInformationDataFormatX05;
import mk.ipdsbox.ipds.triplets.group.GroupInformationDataFormatX82;
import mk.ipdsbox.ipds.triplets.group.GroupInformationFormat;

/**
 * The Group Information (X'6E') triplet is used to provide information about a group of pages.
 */
public final class GroupInformationTriplet extends Triplet {

    private final GroupInformationFormat format;
    private final GroupInformationData data;

    /**
     * Constructs a {@link GroupInformationTriplet} from the given byte array.
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public GroupInformationTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.GroupInformation);

        this.format = raw.length > 2 ? GroupInformationFormat.getForIfExists(raw[2] & 0xFF) : null;
        this.data = this.format != null && raw.length > 3 ? this.parseFormatData(raw) : null;
    }

    /**
     * Returns the {@link GroupInformationFormat} of the {@link GroupInformationTriplet} or <code>null</code> if the
     * {@link GroupInformationTriplet} does not contain grouping information.
     * @return the {@link GroupInformationFormat} of the {@link GroupInformationTriplet} or <code>null</code> if the
     * {@link GroupInformationTriplet} does not contain grouping information.
     */
    public GroupInformationFormat getGroupInformationFormatIfExist() {
        return this.format;
    }

    /**
     * Returns the {@link GroupInformationData} of the {@link GroupInformationTriplet} or <code>null</code> if the
     * {@link GroupInformationTriplet} does not contain grouping information.
     * @return the {@link GroupInformationData} of the {@link GroupInformationTriplet} or <code>null</code> if the
     * {@link GroupInformationTriplet} does not contain grouping information.
     */
    public GroupInformationData getGroupInformationDataIfExist() {
        return this.data;
    }

    private GroupInformationData parseFormatData(final byte[] raw) throws IOException {
        switch (this.format) {
        case MICROFILM_SAVE_RESTORE:
            return new GroupInformationDataFormatX01(raw);
        case COPY_SET_NUMBER:
            return new GroupInformationDataFormatX02(raw);
        case GROUP_NAME:
            return new GroupInformationDataFormatX03(raw);
        case ADDITIONAL_INFORMATION:
            return new GroupInformationDataFormatX04(raw);
        case PAGE_COUNT:
            return new GroupInformationDataFormatX05(raw);
        case EXTENDED_COPY_SET_NUMBER:
            return new GroupInformationDataFormatX82(raw);
        default:
            throw new IpdsboxRuntimeException("No case for " + this.format);
        }
    }
}
