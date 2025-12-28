package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.group.AixAndOs2ComDataFormat;
import de.textmode.ipdsbox.ipds.triplets.group.AixAndWindowsPrintDataFormat;
import de.textmode.ipdsbox.ipds.triplets.group.ExtendedOs400PrintDataFormat;
import de.textmode.ipdsbox.ipds.triplets.group.GroupIdData;
import de.textmode.ipdsbox.ipds.triplets.group.MvsAndVseComDataFormat;
import de.textmode.ipdsbox.ipds.triplets.group.MvsAndVsePrintDataFormat;
import de.textmode.ipdsbox.ipds.triplets.group.Os400PrintDataFormat;
import de.textmode.ipdsbox.ipds.triplets.group.UnknownGroupIdDataFormat;
import de.textmode.ipdsbox.ipds.triplets.group.VariableLengthGroupIdFormat;
import de.textmode.ipdsbox.ipds.triplets.group.VmPrintDataFormat;

/**
 * The Group ID triplet (X'00') is used to keep things together as a print unit.
 */
public final class GroupIdTriplet extends Triplet {

    private int format;
    private GroupIdData data;

    /**
     * Constructs a {@link GroupIdTriplet} with default Data.
     */
    public GroupIdTriplet() {
        super(TripletId.GroupID);

        this.format = -1;
        this.data = null;
    }

    /**
     * Constructs a {@link GroupIdTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    GroupIdTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.GroupID);

        if (ipds.bytesAvailable() >= 1) {
            this.format = ipds.readUnsignedByte();
            this.data = ipds.bytesAvailable() >= 1 ? this.parseFormatData(ipds) : null;
        } else {
            this.format = -1;
            this.data = null;
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
     * Returns the {@link GroupIdData} of the {@link GroupIdTriplet} or <code>null</code> if the
     * {@link GroupIdTriplet} does not contain grouping information.
     * @return the {@link GroupIdData} of the {@link GroupIdTriplet} or <code>null</code> if the
     * {@link GroupIdTriplet} does not contain grouping information.
     */
    public GroupIdData getGroupIdData() {
        return this.data;
    }

    /**
     * Sets the {@link GroupIdData} of the {@link GroupIdTriplet} or <code>null</code> if the
     * {@link GroupIdTriplet} does not contain grouping information.
     */
    public void setGroupInformationDataIfExist(final GroupIdData data) {
        this.data = data;
    }

    private GroupIdData parseFormatData(final IpdsByteArrayInputStream ipds) throws IOException {
        return switch (this.format) {
            case 0x01 -> new MvsAndVsePrintDataFormat(ipds);
            case 0x02 -> new VmPrintDataFormat(ipds);
            case 0x03 -> new Os400PrintDataFormat(ipds);
            case 0x04 -> new MvsAndVseComDataFormat(ipds);
            case 0x05 -> new AixAndOs2ComDataFormat(ipds);
            case 0x06 -> new AixAndWindowsPrintDataFormat(ipds);
            case 0x08 -> new VariableLengthGroupIdFormat(ipds);
            case 0x13 -> new ExtendedOs400PrintDataFormat(ipds);
            default -> new UnknownGroupIdDataFormat(ipds);
        };
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

    @Override
    public String toString() {
        return "GroupIDTriplet{" +
                "tid=0x" + String.format("%02X", this.getTripletId()) +
                ", format=" + (this.format == -1 ? "no format" : this.format) +
                ", data=" + (this.data == null ? "no data" : this.data) +
                "}";
    }
}
