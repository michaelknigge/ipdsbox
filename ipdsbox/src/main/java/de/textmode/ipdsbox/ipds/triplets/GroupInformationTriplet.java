package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.group.*;

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

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        final byte[] dataBytes = this.data == null ? null : this.data.toByteArray();

        final int len = 2 + (this.format == null ? 0 : 1) + (dataBytes == null ? 0 : dataBytes.length);

        out.writeUnsignedByte(len);
        out.writeUnsignedByte(this.getTripletId().getId());

        if (this.format != null) {
            out.writeUnsignedByte(this.format.getId());
        }

        if (dataBytes != null) {
            out.writeBytes(dataBytes);
        }
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
            throw new IpdsboxRuntimeException("No case for " + this.format); // TEST is RuntimeException too much?!
        }
    }

    @Override
    public String toString() {
        return "GroupInformationTriplet{" +
                "length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", format=" + this.format == null ? "no format" : this.format.getMeaning() +
                ", data=" + this.data == null ? "no data" : this.data.toString();
    }
}
