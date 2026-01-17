package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;

/**
 * The Activate Resource (AR) command, previously known as Load Resource Equivalence (LRE), requests the
 * activation of resident resources in the printer or intermediate device.
 */
public final class ActivateResourceCommand extends IpdsCommand {

    private final List<ActivateResourceEntry> entries = new ArrayList<ActivateResourceEntry>();

    /**
     * Constructs the {@link ActivateResourceCommand}.
     */
    public ActivateResourceCommand() {
        super(IpdsCommandId.AR);
    }

    /**
     * Constructs the {@link ActivateResourceCommand}.
     */
    ActivateResourceCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.AR);

        while (ipds.bytesAvailable() > 0) {
            final int length = ipds.readUnsignedInteger16();

            if (length == 2) {
                this.entries.add(new NullEntry());
            } else {
                final byte[] entry = ipds.readBytes(length - 2);

                switch (entry[4] & 0xFF) {
                    case 0x00 -> this.entries.add(new ResetEntry(new IpdsByteArrayInputStream(entry)));
                    case 0x03 -> this.entries.add(new GridPartsFormatEntry(new IpdsByteArrayInputStream(entry)));
                    case 0x04 -> this.entries.add(new RemotePrintManagerMvsFormatEntry(new IpdsByteArrayInputStream(entry)));
                    case 0x05 -> this.entries.add(new ExtendedRemotePrintManagerMvsFormatEntry(new IpdsByteArrayInputStream(entry)));
                    case 0x06 -> this.entries.add(new MvsHostUnalterableRemoteFontEnvironmentFormatEntry(new IpdsByteArrayInputStream(entry)));
                    case 0x07 -> this.entries.add(new CodedFontFormatEntry(new IpdsByteArrayInputStream(entry)));
                    case 0x09 -> this.entries.add(new ObjectOidFormatEntry(new IpdsByteArrayInputStream(entry)));
                    case 0x0A -> this.entries.add(new DataObjectFontFormatEntry(new IpdsByteArrayInputStream(entry)));
                    default -> this.entries.add(new UnknownFormatEntry(new IpdsByteArrayInputStream(entry)));
                }
            }
        }
    }

    /**
     * Returns the entries of the AR command.
     */
    public List<ActivateResourceEntry> getEntries() {
        return this.entries;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        for (final ActivateResourceEntry entry : this.entries) {
            entry.writeTo(ipds);
        }
    }

    @Override
    public String toString() {
        return "ActivateResourceCommand{"
                + "entries=" + this.entries
                + '}';
    }

    /**
     * Interface for all Activate Resource entries.
     */
    public interface ActivateResourceEntry {
        /**
         * Writes the {@link ActivateResourceEntry} to the given {@link IpdsByteArrayOutputStream}.
         */
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException;
    }

    /**
     * Raw data of an unknown format entry.
     */
    public static final class UnknownFormatEntry implements ActivateResourceEntry {
        private final byte[] rawData;

        UnknownFormatEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
            os.writeUnsignedInteger16(ipds.bytesAvailable() + 2);
            os.writeBytes(ipds.readRemainingBytes());

            this.rawData = os.toByteArray();

        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            ipds.writeBytes(this.rawData);
        }

        @Override
        public String toString() {
            return "UnknownFormatEntry{"
                    + "rawData=" + StringUtils.toHexString(this.rawData)
                    + '}';

        }
    }

    /**
     * So called "Null Entry". Contains just the length.
     */
    public static final class NullEntry implements ActivateResourceEntry {
        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            ipds.writeUnsignedInteger16(2);
        }

        @Override
        public String toString() {
            return "NullEntry{}";
        }
    }

    /**
     * All Activate Resource entries that contain any data (length at least 0x0C bytes).
     */
    public abstract class NonNullEntry implements ActivateResourceEntry {
        private int resourceType;
        private int haid;
        private int sectionID;
        private int resourceIdFormat;
        private int fontInlineSequence;
        private int resourceClassFlags;
        private List<Triplet> resourceIdTriplets = new ArrayList();

        /**
         * Creates a {@link NonNullEntry} for the given resource Type and resource ID format.
         */
        NonNullEntry(final int resourceType, final int resourceIdFormat) throws IOException {
            this.resourceType = resourceType;
            this.resourceIdFormat = resourceIdFormat;
        }

        /**
         * Read the fixed part of the Acrtivate Resource entry.
         */
        NonNullEntry(final IpdsByteArrayInputStream ipds) throws IOException {

            this.resourceType = ipds.readUnsignedByte();
            this.haid = ipds.readUnsignedInteger16();
            this.sectionID = ipds.readUnsignedByte();
            this.resourceIdFormat = ipds.readUnsignedByte();
            this.fontInlineSequence = ipds.readUnsignedInteger16();
            ipds.skip(2); // two bytes "reserved"

            this.resourceClassFlags = ipds.readUnsignedByte();
        }

        /**
         * Reads the triplets from the given {@link IpdsByteArrayInputStream}.
         */
        protected void readTriplets(final IpdsByteArrayInputStream ipds) throws IOException {
            byte[] buffer;
            while ((buffer = ipds.readTripletIfExists()) != null) {
                this.resourceIdTriplets.add(TripletFactory.create(buffer));
            }
        }

        /**
         * Writes the fixed part of the Activate Resource entry, followed by the variable part (must not be null!),
         * followed by triplets if present.
         */
        protected void writeTo(final IpdsByteArrayOutputStream ipds, final byte[] resourceIdData) throws IOException {
            final IpdsByteArrayOutputStream triplatData = new IpdsByteArrayOutputStream();
            for (final Triplet triplet : this.resourceIdTriplets) {
                triplet.writeTo(triplatData);
            }

            final byte[] rawTripletData = triplatData.toByteArray();

            ipds.writeUnsignedInteger16(0x0C + rawTripletData.length + resourceIdData.length);

            ipds.writeUnsignedByte(this.resourceType);
            ipds.writeUnsignedInteger16(this.haid);
            ipds.writeUnsignedByte(this.sectionID);
            ipds.writeUnsignedByte(this.resourceIdFormat);
            ipds.writeUnsignedInteger16(this.fontInlineSequence);
            ipds.writeUnsignedInteger16(0x0000);
            ipds.writeUnsignedByte(this.resourceClassFlags);

            ipds.writeBytes(resourceIdData);
            ipds.writeBytes(rawTripletData);
        }

        /**
         * Returns the resource type.
         */
        public int getResourceType() {
            return this.resourceType;
        }

        /**
         * Sets the resource type.
         */
        public void setResourceType(final int resourceType) {
            this.resourceType = resourceType;
        }

        /**
         * Returns the HAID.
         */
        public int getHaid() {
            return this.haid;
        }

        /**
         * Sets the HAID.
         */
        public void setHaid(final int haid) {
            this.haid = haid;
        }

        /**
         * Returns the Section ID.
         */
        public int getSectionID() {
            return this.sectionID;
        }

        /**
         * Sets the Section ID.
         */
        public void setSectionID(final int sectionID) {
            this.sectionID = sectionID;
        }

        /**
         * Returns the RIDF.
         */
        public int getResourceIdFormat() {
            return this.resourceIdFormat;
        }

        /**
         * Sets the RIDF.
         */
        public void setResourceIdFormat(final int resourceIdFormat) {
            this.resourceIdFormat = resourceIdFormat;
        }

        /**
         * Returns the FIS.
         */
        public int getFontInlineSequence() {
            return this.fontInlineSequence;
        }

        /**
         * Sets the FIS.
         */
        public void setFontInlineSequence(final int fontInlineSequence) {
            this.fontInlineSequence = fontInlineSequence;
        }

        /**
         * Returns the Resource class flags.
         */
        public int getResourceClassFlags() {
            return this.resourceClassFlags;
        }

        /**
         * Sets the Resource class flags.
         */
        public void setResourceClassFlags(final int resourceClassFlags) {
            this.resourceClassFlags = resourceClassFlags;
        }

        /**
         * Returns the Resource ID.
         */
        public List<Triplet> getResourceIdTriplets() {
            return this.resourceIdTriplets;
        }

        /**
         * Sets the Resource ID.
         */
        public void setResourceIdTriplets(final List<Triplet> resourceIdTriplets) {
            this.resourceIdTriplets = resourceIdTriplets;
        }

        /**
         * Returns a String containing the values from the fixed part of the entry.
         */
        protected String getDescriptionForToString() {
            return "resourceType=0x" + Integer.toHexString(this.resourceType)
                    + ", haid=0x" + Integer.toHexString(this.haid)
                    + ", sectionID=0x" + Integer.toHexString(this.sectionID)
                    + ", resourceIdFormat=0x" + Integer.toHexString(this.resourceIdFormat)
                    + ", fontInlineSequence=0x" + Integer.toHexString(this.fontInlineSequence)
                    + ", resourceClassFlags=0x" + Integer.toHexString(this.resourceClassFlags)
                    + ", resourceIdTriplets=" + this.resourceIdTriplets;
        }
    }

    /**
     * So called "Reset". Contains just the fixed part with default values.
     */
    public final class ResetEntry extends NonNullEntry {

        /**
         * Creates a {@link ResetEntry} from the given {@link IpdsByteArrayInputStream}.
         */
        ResetEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            super(ipds);

            // The specification is not 100% clear about the fact if triplets may be present on
            // a "reset entry"... They make no sense, but the spec is not 100% clear about that..
            if (ipds.bytesAvailable() > 0) {
                super.readTriplets(ipds);
            }
        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            super.writeTo(ipds, ByteUtils.EMPTY_BYTE_ARRAY);
        }

        @Override
        public String toString() {
            return "ResetEntry{"
                    + super.getDescriptionForToString()
                    + '}';
        }
    }

    /**
     * GRID-parts format entry. This naming format is used for code pages, font character sets, double-byte LF1-type
     * coded-font sections, font indexes, and coded fonts.
     */
    public final class GridPartsFormatEntry extends NonNullEntry {

        /**
         * Creates a {@link GridPartsFormatEntry} from the given {@link IpdsByteArrayInputStream}.
         */
        GridPartsFormatEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            super(ipds);

            // TODO: Parse GRID-parts...

            // The specification is not 100% clear about the fact if triplets may be present on
            // a "reset entry"... They make no sense, but the spec is not 100% clear about that..
            if (ipds.bytesAvailable() > 0) {
                super.readTriplets(ipds);
            }
        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            // TODO: Write GRID-parts...
            super.writeTo(ipds, ByteUtils.EMPTY_BYTE_ARRAY);
        }

        @Override
        public String toString() {
            return "GridPartsFormatEntry{"
                    + super.getDescriptionForToString()
                    + '}';
        }
    }

    /**
     * Remote PrintManager MVS format (82 or 164 bytes/resource).
     */
    public final class RemotePrintManagerMvsFormatEntry extends NonNullEntry {

        /**
         * Creates a {@link RemotePrintManagerMvsFormatEntry} from the given {@link IpdsByteArrayInputStream}.
         */
        RemotePrintManagerMvsFormatEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            super(ipds);

            // TODO: Parse RemotePrintManagerMvsFormatEntry...

            // The specification is not 100% clear about the fact if triplets may be present on
            // a "reset entry"... They make no sense, but the spec is not 100% clear about that..
            if (ipds.bytesAvailable() > 0) {
                super.readTriplets(ipds);
            }
        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            // TODO: Write RemotePrintManagerMvsFormatEntry...
            super.writeTo(ipds, ByteUtils.EMPTY_BYTE_ARRAY);
        }

        @Override
        public String toString() {
            return "RemotePrintManagerMvsFormatEntry{"
                    + super.getDescriptionForToString()
                    + '}';
        }
    }

    /**
     * Extended Remote PrintManager MVS format (86 bytes/resource).
     */
    public final class ExtendedRemotePrintManagerMvsFormatEntry extends NonNullEntry {

        /**
         * Creates a {@link ExtendedRemotePrintManagerMvsFormatEntry} from the given {@link IpdsByteArrayInputStream}.
         */
        ExtendedRemotePrintManagerMvsFormatEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            super(ipds);

            // TODO: Parse ExtendedRemotePrintManagerMvsFormatEntry...

            // The specification is not 100% clear about the fact if triplets may be present on
            // a "reset entry"... They make no sense, but the spec is not 100% clear about that..
            if (ipds.bytesAvailable() > 0) {
                super.readTriplets(ipds);
            }
        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            // TODO: Write ExtendedRemotePrintManagerMvsFormatEntry...
            super.writeTo(ipds, ByteUtils.EMPTY_BYTE_ARRAY);
        }

        @Override
        public String toString() {
            return "ExtendedRemotePrintManagerMvsFormatEntry{"
                    + super.getDescriptionForToString()
                    + '}';
        }
    }


    /**
     * MVS Host Unalterable Remote Font environment (172 bytes/font).
     */
    public final class MvsHostUnalterableRemoteFontEnvironmentFormatEntry extends NonNullEntry {

        // TODO create getter and setter...
        private final int codePageCrc;
        private final String codePageMvsSystemId;
        private final String codePageVolumeSerial;
        private final String codePageDataSetName;
        private final String codePageDateStamp;
        private final String codePageTimeStamp;
        private final String codePageMember;
        private final int codePageGraphicCharacterSetGlobalId;
        private final int codePageGlobalId;

        private final int characterSetCrc;
        private final String characterSetMvsSystemId;
        private final String characterSetVolumeSerial;
        private final String characterSetDataSetName;
        private final String characterSetDateStamp;
        private final String characterSetTimeStamp;
        private final String characterSetMember;
        private final int characterSetFontTypefaceGlobalId;
        private final int characterSetFontWidth;

        /**
         * Creates a {@link MvsHostUnalterableRemoteFontEnvironmentFormatEntry} with default values.
         */
        MvsHostUnalterableRemoteFontEnvironmentFormatEntry() throws IOException {
            super(0x01, 0x06);

            this.codePageCrc = 0x0000;
            this.codePageMvsSystemId = ByteUtils.EMPTY_STRING;
            this.codePageVolumeSerial = ByteUtils.EMPTY_STRING;
            this.codePageDataSetName = ByteUtils.EMPTY_STRING;
            this.codePageDateStamp = ByteUtils.EMPTY_STRING;
            this.codePageTimeStamp = ByteUtils.EMPTY_STRING;
            this.codePageMember = ByteUtils.EMPTY_STRING;
            this.codePageGraphicCharacterSetGlobalId = 0x0000;
            this.codePageGlobalId = 0x0000;

            this.characterSetCrc = 0x0000;
            this.characterSetMvsSystemId = ByteUtils.EMPTY_STRING;
            this.characterSetVolumeSerial = ByteUtils.EMPTY_STRING;
            this.characterSetDataSetName = ByteUtils.EMPTY_STRING;
            this.characterSetDateStamp = ByteUtils.EMPTY_STRING;
            this.characterSetTimeStamp = ByteUtils.EMPTY_STRING;
            this.characterSetMember = ByteUtils.EMPTY_STRING;
            this.characterSetFontTypefaceGlobalId = 0x0000;
            this.characterSetFontWidth = 0x0000;
        }

        /**
         * Creates a {@link MvsHostUnalterableRemoteFontEnvironmentFormatEntry} from the given {@link IpdsByteArrayInputStream}.
         */
        MvsHostUnalterableRemoteFontEnvironmentFormatEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            super(ipds);

            this.codePageCrc = ipds.readUnsignedInteger16();
            this.codePageMvsSystemId = ipds.readEbcdicString(8).trim();
            this.codePageVolumeSerial = ipds.readEbcdicString(6).trim();
            this.codePageDataSetName = ipds.readEbcdicString(44).trim();
            this.codePageDateStamp = ipds.readEbcdicString(6).trim();
            this.codePageTimeStamp = ipds.readEbcdicString(8).trim();
            this.codePageMember = ipds.readEbcdicString(8).trim();
            this.codePageGraphicCharacterSetGlobalId = ipds.readUnsignedInteger16();
            this.codePageGlobalId = ipds.readUnsignedInteger16();

            this.characterSetCrc = ipds.readUnsignedInteger16();
            this.characterSetMvsSystemId = ipds.readEbcdicString(8).trim();
            this.characterSetVolumeSerial = ipds.readEbcdicString(6).trim();
            this.characterSetDataSetName = ipds.readEbcdicString(44).trim();
            this.characterSetDateStamp = ipds.readEbcdicString(6).trim();
            this.characterSetTimeStamp = ipds.readEbcdicString(8).trim();
            this.characterSetMember = ipds.readEbcdicString(8).trim();
            this.characterSetFontTypefaceGlobalId = ipds.readUnsignedInteger16();
            this.characterSetFontWidth = ipds.readUnsignedInteger16();

            if (ipds.bytesAvailable() > 0) {
                super.readTriplets(ipds);
            }
        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            final IpdsByteArrayOutputStream data = new IpdsByteArrayOutputStream();

            data.writeUnsignedInteger16(this.codePageCrc);
            data.writeEbcdicString(this.codePageMvsSystemId, 8);
            data.writeEbcdicString(this.codePageVolumeSerial, 6);
            data.writeEbcdicString(this.codePageDataSetName, 44);

            if (this.codePageDateStamp.isEmpty()) {
                data.writeBytes(new byte[6]);
            } else {
                data.writeEbcdicString(this.codePageDateStamp, 6);
            }

            if (this.codePageTimeStamp.isEmpty()) {
                data.writeBytes(new byte[8]);
            } else {
                data.writeEbcdicString(this.codePageTimeStamp, 8);
            }

            data.writeEbcdicString(this.codePageMember, 8);
            data.writeUnsignedInteger16(this.codePageGraphicCharacterSetGlobalId);
            data.writeUnsignedInteger16(this.codePageGlobalId);

            data.writeUnsignedInteger16(this.characterSetCrc);
            data.writeEbcdicString(this.characterSetMvsSystemId, 8);
            data.writeEbcdicString(this.characterSetVolumeSerial, 6);
            data.writeEbcdicString(this.characterSetDataSetName, 44);

            if (this.characterSetDateStamp.isEmpty()) {
                data.writeBytes(new byte[6]);
            } else {
                data.writeEbcdicString(this.characterSetDateStamp, 6);
            }

            if (this.characterSetTimeStamp.isEmpty()) {
                data.writeBytes(new byte[8]);
            } else {
                data.writeEbcdicString(this.characterSetTimeStamp, 8);
            }

            data.writeEbcdicString(this.characterSetMember, 8);
            data.writeUnsignedInteger16(this.characterSetFontTypefaceGlobalId);
            data.writeUnsignedInteger16(this.characterSetFontWidth);

            super.writeTo(ipds, data.toByteArray());
        }

        @Override
        public String toString() {
            return "MvsHostUnalterableRemoteFontEnvironmentFormatEntry{"
                    + super.getDescriptionForToString()
                    + "codePageCrc=0x" + Integer.toHexString(this.codePageCrc)
                    + ", codePageMvsSystemId=" + this.codePageMvsSystemId
                    + ", codePageVolumeSerial=" + this.codePageVolumeSerial
                    + ", codePageDataSetName=" + this.codePageDataSetName
                    + ", codePageDateStamp=" + this.codePageDateStamp
                    + ", codePageTimeStamp=" + this.codePageTimeStamp
                    + ", codePageMember=" + this.codePageMember
                    + ", codePageGraphicCharacterSetGlobalId=0x" + Integer.toHexString(
                            this.codePageGraphicCharacterSetGlobalId)
                    + ", codePageGlobalId=0x" + Integer.toHexString(this.codePageGlobalId)
                    + ", characterSetCrc=0x" + Integer.toHexString(this.characterSetCrc)
                    + ", characterSetMvsSystemId=" + this.characterSetMvsSystemId
                    + ", characterSetVolumeSerial=" + this.characterSetVolumeSerial
                    + ", characterSetDataSetName=" + this.characterSetDataSetName
                    + ", characterSetDateStamp=" + this.characterSetDateStamp
                    + ", characterSetTimeStamp=" + this.characterSetTimeStamp
                    + ", characterSetMember=" + this.characterSetMember
                    + ", characterSetFontTypefaceGlobalId=0x" + Integer.toHexString(this.characterSetFontTypefaceGlobalId)
                    + ", characterSetFontWidth=0x" + Integer.toHexString(this.characterSetFontWidth)
                    + '}';
        }
    }

    /**
     * Coded-font format.
     */
    public final class CodedFontFormatEntry extends NonNullEntry {

        /**
         * Creates a {@link CodedFontFormatEntry} from the given {@link IpdsByteArrayInputStream}.
         */
        CodedFontFormatEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            super(ipds);

            // TODO: Parse CodedFontFormatEntry...

            // The specification is not 100% clear about the fact if triplets may be present on
            // a "reset entry"... They make no sense, but the spec is not 100% clear about that..
            if (ipds.bytesAvailable() > 0) {
                super.readTriplets(ipds);
            }
        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            // TODO: Write CodedFontFormatEntry...
            super.writeTo(ipds, ByteUtils.EMPTY_BYTE_ARRAY);
        }

        @Override
        public String toString() {
            return "CodedFontFormatEntry{"
                    + super.getDescriptionForToString()
                    + '}';
        }
    }

    /**
     * Object-OID format
     */
    public final class ObjectOidFormatEntry extends NonNullEntry {

        /**
         * Creates a {@link ObjectOidFormatEntry} from the given {@link IpdsByteArrayInputStream}.
         */
        ObjectOidFormatEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            super(ipds);

            // TODO: Parse ObjectOidFormatEntry...

            // The specification is not 100% clear about the fact if triplets may be present on
            // a "reset entry"... They make no sense, but the spec is not 100% clear about that..
            if (ipds.bytesAvailable() > 0) {
                super.readTriplets(ipds);
            }
        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            // TODO: Write ObjectOidFormatEntry...
            super.writeTo(ipds, ByteUtils.EMPTY_BYTE_ARRAY);
        }

        @Override
        public String toString() {
            return "ObjectOidFormatEntry{"
                    + super.getDescriptionForToString()
                    + '}';
        }
    }

    /**
     * Data-object-font format.
     */
    public final class DataObjectFontFormatEntry extends NonNullEntry {

        /**
         * Creates a {@link DataObjectFontFormatEntry} from the given {@link IpdsByteArrayInputStream}.
         */
        DataObjectFontFormatEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            super(ipds);

            // TODO: Parse DataObjectFontFormatEntry...

            // The specification is not 100% clear about the fact if triplets may be present on
            // a "reset entry"... They make no sense, but the spec is not 100% clear about that..
            if (ipds.bytesAvailable() > 0) {
                super.readTriplets(ipds);
            }
        }

        @Override
        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            // TODO: Write DataObjectFontFormatEntry...
            super.writeTo(ipds, ByteUtils.EMPTY_BYTE_ARRAY);
        }

        @Override
        public String toString() {
            return "DataObjectFontFormatEntry{"
                    + super.getDescriptionForToString()
                    + '}';
        }
     }
}
