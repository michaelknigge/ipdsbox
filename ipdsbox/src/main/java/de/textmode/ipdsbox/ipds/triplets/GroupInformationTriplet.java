package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.group.AdditionalInformationFormat;
import de.textmode.ipdsbox.ipds.triplets.group.CopySetNumberFormat;
import de.textmode.ipdsbox.ipds.triplets.group.ExtendedCopySetNumberFormat;
import de.textmode.ipdsbox.ipds.triplets.group.GroupInformationData;
import de.textmode.ipdsbox.ipds.triplets.group.GroupNameFormat;
import de.textmode.ipdsbox.ipds.triplets.group.MicrofilmSaveRestoreFormat;
import de.textmode.ipdsbox.ipds.triplets.group.PageCountFormat;
import de.textmode.ipdsbox.ipds.triplets.group.UnknownGroupInformationDataFormat;

/**
 * The Group Information (X'6E') triplet is used to provide information about a group of pages.
 */
public final class GroupInformationTriplet extends Triplet {

    private int format;
    private GroupInformationData data;

    /**
     * Constructs a {@link GroupInformationTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    GroupInformationTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.GroupInformation);

        if (ipds.bytesAvailable() >= 1) {
            this.format = ipds.readUnsignedByte();
            this.data = ipds.bytesAvailable() >= 1 ? this.parseFormatData(ipds) : null;
        } else {
            this.format = -1;
            this.data = null;
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        final byte[] dataBytes = this.data == null ? null : this.data.toByteArray();

        final int len = 2 + (this.format == -1 ? 0 : 1) + (dataBytes == null ? 0 : dataBytes.length);

        out.writeUnsignedByte(len);
        out.writeUnsignedByte(this.getTripletId());

        if (this.format != -1) {
            out.writeUnsignedByte(this.format);
        }

        if (dataBytes != null) {
            out.writeBytes(dataBytes);
        }
    }

    /**
     * Returns the format or -1 if no format is set in this triplet.
     */
    public int getFormat() {
        return this.format;
    }

    /**
     * Sets the format or -1 if no format shoule be set in this triplet.
     */
    public void setFormat(final int format) {
        this.format = format;
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

    /**
     * Sets the {@link GroupInformationData} of the {@link GroupInformationTriplet} or <code>null</code> if the
     * {@link GroupInformationTriplet} does not contain grouping information.
     */
    public void setGroupInformationDataIfExist(final GroupInformationData data) {
        this.data = data;
    }

    private GroupInformationData parseFormatData(final IpdsByteArrayInputStream ipds) throws IOException {
        return switch (this.format) {
            case 0x01 -> new MicrofilmSaveRestoreFormat(ipds);
            case 0x02 -> new CopySetNumberFormat(ipds);
            case 0x03 -> new GroupNameFormat(ipds);
            case 0x04 -> new AdditionalInformationFormat(ipds);
            case 0x05 -> new PageCountFormat(ipds);
            case 0x82 -> new ExtendedCopySetNumberFormat(ipds);
            default -> new UnknownGroupInformationDataFormat(ipds);
        };
    }

    @Override
    public String toString() {
        return "GroupInformationTriplet{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", format=" + (this.format == -1 ? "no format" : this.format) +
                ", data=" + (this.data == null ? "no data" : this.data) +
                "}";
    }
}
