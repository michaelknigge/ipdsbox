package mk.ipdsbox.ipds.triplets;

import java.io.IOException;

import mk.ipdsbox.core.IpdsboxRuntimeException;
import mk.ipdsbox.ipds.triplets.group.GroupIdData;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX01;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX02;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX03;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX04;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX05;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX06;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX08;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX13;
import mk.ipdsbox.ipds.triplets.group.GroupIdFormat;

/**
 * The Group ID triplet (X'00') is used to keep things together as a print unit.
 */
public final class GroupIdTriplet extends Triplet {

    private final GroupIdFormat format;
    private final GroupIdData data;

    /**
     * Constructs a {@link GroupIdTriplet} from the given byte array.
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public GroupIdTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.GroupID);

        if (raw.length > 2) {
            this.format = GroupIdFormat.getForIfExists(raw[2]);
            this.data = this.format == null ? null : this.parseFormatData(raw);
        } else {
            this.format = null;
            this.data = null;
        }
    }

    /**
     * Returns the {@link GroupIdFormat} of the {@link GroupIdTriplet} or <code>null</code> if the
     * {@link GroupIdTriplet} does not contain grouping information.
     * @return the {@link GroupIdFormat} of the {@link GroupIdTriplet} or <code>null</code> if the
     * {@link GroupIdTriplet} does not contain grouping information.
     */
    public GroupIdFormat getGroupIdFormatIfExist() {
        return this.format;
    }

    /**
     * Returns the {@link GroupIdData} of the {@link GroupIdTriplet} or <code>null</code> if the
     * {@link GroupIdTriplet} does not contain grouping information.
     * @return the {@link GroupIdData} of the {@link GroupIdTriplet} or <code>null</code> if the
     * {@link GroupIdTriplet} does not contain grouping information.
     */
    public GroupIdData getGroupIdDataIfExist() {
        return this.data;
    }

    private GroupIdData parseFormatData(final byte[] raw) throws IOException {
        switch (this.format) {
        case AIX_AND_OS2:
            return new GroupIdDataFormatX05(raw);
        case AIX_AND_WINDOWS:
            return new GroupIdDataFormatX06(raw);
        case EXTENDED_OS400:
            return new GroupIdDataFormatX13(raw);
        case MVS_AND_VSE:
            return new GroupIdDataFormatX01(raw);
        case MVS_AND_VSE_COM:
            return new GroupIdDataFormatX04(raw);
        case OS400:
            return new GroupIdDataFormatX03(raw);
        case VARIABLE_LENGTH_GROUP_ID:
            return new GroupIdDataFormatX08(raw);
        case VM:
            return new GroupIdDataFormatX02(raw);
        default:
            throw new IpdsboxRuntimeException("No case for " + this.format);
        }
    }
}
