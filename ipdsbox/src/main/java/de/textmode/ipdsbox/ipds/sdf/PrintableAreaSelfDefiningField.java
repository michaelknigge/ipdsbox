package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Parses the fields of the "Printable-Area Self-Defining Field".
 */
public final class PrintableAreaSelfDefiningField extends SelfDefiningField {

    public class MediaIdEntry {
        private final int mediaIdType;
        private final byte[] mediaId;

        MediaIdEntry(final int mediaIdType, final byte[] mediaId) {
            this.mediaIdType = mediaIdType;
            this.mediaId = mediaId;
        }

        /**
         * Returns the media ID type.
         */
        public int getMediaIdType() {
            return this.mediaIdType;
        }

        /**
         * Returns the media ID.
         */
        public byte[] getMediaId() {
            return this.mediaId;
        }

        @Override
        public String toString() {
            return "MediaIdEntry{" +
                    "mediaIdType=" + this.mediaIdType +
                    ", mediaId=0x" + StringUtils.toHexString(this.mediaId) +
                    '}';
        }
    }

    private int mediaSourceId;
    private int unitBase;
    private int upub;
    private int actualMediumPresentationSpaceWidth;
    private int actualMediumPresentationSpaceLength;
    private int xMPPAOffset;
    private int yMPPAOffset;
    private int xMPPAExtent;
    private int yMPPAExtent;
    private int inputMediaSourceCharacteristicFlags;
    private final List<MediaIdEntry> mediaIdEntries = new ArrayList<>();

    /**
     * Creates a new {@link PrintableAreaSelfDefiningField}.
     */
    public PrintableAreaSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.PrintableArea);
    }

    /**
     * Creates a {@link PrintableAreaSelfDefiningField} from the given stream.
     */
    PrintableAreaSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.PrintableArea);

        this.mediaSourceId = ipds.readUnsignedByte();
        ipds.skip(1);

        this.unitBase = ipds.readUnsignedByte();
        ipds.skip(1);

        this.upub = ipds.readUnsignedInteger16();
        this.actualMediumPresentationSpaceWidth = ipds.readUnsignedInteger16();
        this.actualMediumPresentationSpaceLength = ipds.readUnsignedInteger16();
        this.xMPPAOffset = ipds.readUnsignedInteger16();
        this.yMPPAOffset = ipds.readUnsignedInteger16();
        this.xMPPAExtent = ipds.readUnsignedInteger16();
        this.yMPPAExtent = ipds.readUnsignedInteger16();
        this.inputMediaSourceCharacteristicFlags = ipds.readUnsignedInteger16();

        while (ipds.bytesAvailable() > 0) {
            final int len = ipds.readUnsignedInteger16();
            assert len >= 4;

            this.mediaIdEntries.add(new MediaIdEntry(
                    ipds.readUnsignedByte(),
                    ipds.readBytes(len - 3)));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {

        int entriesLen = 0;
        for (final MediaIdEntry mediaIdEntry : this.mediaIdEntries) {
            entriesLen += (3 + mediaIdEntry.getMediaId().length);
        }

        ipds.writeUnsignedInteger16(24 + entriesLen);
        ipds.writeUnsignedInteger16(SelfDefiningFieldId.PrintableArea.getId());

        ipds.writeUnsignedByte(this.mediaSourceId);
        ipds.writeUnsignedByte(0x00);
        ipds.writeUnsignedByte(this.unitBase);
        ipds.writeUnsignedByte(0x00);

        ipds.writeUnsignedInteger16(this.upub);
        ipds.writeUnsignedInteger16(this.actualMediumPresentationSpaceWidth);
        ipds.writeUnsignedInteger16(this.actualMediumPresentationSpaceLength);
        ipds.writeUnsignedInteger16(this.xMPPAOffset);
        ipds.writeUnsignedInteger16(this.yMPPAOffset);
        ipds.writeUnsignedInteger16(this.xMPPAExtent);
        ipds.writeUnsignedInteger16(this.yMPPAExtent);
        ipds.writeUnsignedInteger16(this.inputMediaSourceCharacteristicFlags);

        for (final MediaIdEntry mediaIdEntry : this.mediaIdEntries) {
            ipds.writeUnsignedInteger16(3 + mediaIdEntry.mediaId.length);
            ipds.writeUnsignedByte(mediaIdEntry.mediaIdType);
            ipds.writeBytes(mediaIdEntry.mediaId);
        }
    }

    /**
     * Returns the Media source ID.
     */
    public int getMediaSourceId() {
        return this.mediaSourceId;
    }

    /**
     * Sets the Media source ID.
     */
    public void setMediaSourceId(final int mediaSourceId) {
        this.mediaSourceId = mediaSourceId;
    }

    /**
     * Returns the Unit base.
     */
    public int getUnitBase() {
        return this.unitBase;
    }

    /**
     * Sets the Unit base.
     */
    public void setUnitBase(final int unitBase) {
        this.unitBase = unitBase;
    }

    /**
     * Returns the UPUB.
     */
    public int getUpub() {
        return this.upub;
    }

    /**
     * Sets the UPUB.
     */
    public void setUpub(final int upub) {
        this.upub = upub;
    }

    /**
     * Returns the Actual medium presentation space width.
     */
    public int getActualMediumPresentationSpaceWidth() {
        return this.actualMediumPresentationSpaceWidth;
    }

    /**
     * Sets the Actual medium presentation space width.
     */
    public void setActualMediumPresentationSpaceWidth(final int value) {
        this.actualMediumPresentationSpaceWidth = value;
    }

    /**
     * Returns the Actual medium presentation space length.
     */
    public int getActualMediumPresentationSpaceLength() {
        return this.actualMediumPresentationSpaceLength;
    }

    /**
     * Sets the Actual medium presentation space length.
     */
    public void setActualMediumPresentationSpaceLength(final int value) {
        this.actualMediumPresentationSpaceLength = value;
    }

    /**
     * Returns the X m PPA offset.
     */
    public int getXMPPAOffset() {
        return this.xMPPAOffset;
    }

    /**
     * Sets the X m PPA offset.
     */
    public void setXMPPAOffset(final int value) {
        this.xMPPAOffset = value;
    }

    /**
     * Returns the Y m PPA offset.
     */
    public int getYMPPAOffset() {
        return this.yMPPAOffset;
    }

    /**
     * Sets the Y m PPA offset.
     */
    public void setYMPPAOffset(final int value) {
        this.yMPPAOffset = value;
    }

    /**
     * Returns the X m PPA extent.
     */
    public int getXMPPAExtent() {
        return this.xMPPAExtent;
    }

    /**
     * Sets the X m PPA extent.
     */
    public void setXMPPAExtent(final int value) {
        this.xMPPAExtent = value;
    }

    /**
     * Returns the Y m PPA extent.
     */
    public int getYMPPAExtent() {
        return this.yMPPAExtent;
    }

    /**
     * Sets the Y m PPA extent.
     */
    public void setYMPPAExtent(final int value) {
        this.yMPPAExtent = value;
    }

    /**
     * Returns the Input media source characteristic flags.
     */
    public int getInputMediaSourceCharacteristicFlags() {
        return this.inputMediaSourceCharacteristicFlags;
    }

    /**
     * Sets the Input media source characteristic flags.
     */
    public void setInputMediaSourceCharacteristicFlags(final int value) {
        this.inputMediaSourceCharacteristicFlags = value;
    }

    @Override
    public String toString() {
        return "PrintableAreaSelfDefiningField{" +
                "mediaSourceID=" + this.mediaSourceId +
                ", unitBase=" + this.unitBase +
                ", upub=" + this.upub +
                ", actualMediumPresentationSpaceWidth=" + this.actualMediumPresentationSpaceWidth +
                ", actualMediumPresentationSpaceLength=" + this.actualMediumPresentationSpaceLength +
                ", xMPPAOffset=" + this.xMPPAOffset +
                ", yMPPAOffset=" + this.yMPPAOffset +
                ", xMPPAExtent=" + this.xMPPAExtent +
                ", yMPPAExtent=" + this.yMPPAExtent +
                ", inputMediaSourceCharacteristicFlags=" + this.inputMediaSourceCharacteristicFlags +
                ", mediaIdEntries=" + this.mediaIdEntries +
                '}';
    }
}
