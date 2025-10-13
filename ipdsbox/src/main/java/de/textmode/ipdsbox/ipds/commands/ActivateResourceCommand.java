package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

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
    public ActivateResourceCommand(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException {
        super(ipds, IpdsCommandId.AR);

        while (ipds.bytesAvailable() > 0) {
            this.entries.add(new ActivateResourceEntry(ipds));
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

    public class ActivateResourceEntry {
        private int resourceType;
        private int haid;
        private int sectionID;
        private int resourceIdFormat;
        private int fontInlineSequence;
        private int resourceClassFlags;
        private byte[] resourceId;

        ActivateResourceEntry(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException {
            final int length = ipds.readUnsignedInteger16();

            this.resourceType = ipds.readUnsignedByte();
            this.haid = ipds.readUnsignedInteger16();
            this.sectionID = ipds.readUnsignedByte();
            this.resourceIdFormat = ipds.readUnsignedByte();
            this.fontInlineSequence = ipds.readUnsignedInteger16();

            // TODO parse.. attention... may contain triplets!
            this.resourceId = ipds.readBytes(length - 12);
        }

        void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            ipds.writeUnsignedInteger16(12 + this.resourceId.length);

            ipds.writeUnsignedByte(this.resourceType);
            ipds.writeUnsignedInteger16(this.haid);
            ipds.writeUnsignedByte(this.sectionID);
            ipds.writeUnsignedByte(this.resourceIdFormat);
            ipds.writeUnsignedInteger16(this.fontInlineSequence);

            ipds.writeBytes(this.resourceId);
        }

        /**
         * Returns the RT.
         */
        public int getResourceType() {
            return this.resourceType;
        }

        /**
         * Sets the RT.
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
        public byte[] getResourceId() {
            return this.resourceId;
        }

        /**
         * Sets the Resource ID.
         */
        public void setResourceId(final byte[] resourceId) {
            this.resourceId = resourceId;
        }

        @Override
        public String toString() {
            return "ActivateResourceEntry{" +
                    "length=" + 12 + this.resourceId.length +
                    ", resourceType=0x" + Integer.toHexString(this.resourceType) +
                    ", haid=" + Integer.toHexString(this.haid) +
                    ", sectionID=0x" + Integer.toHexString(this.sectionID) +
                    ", resourceIdFormat=0x" + Integer.toHexString(this.resourceIdFormat) +
                    ", fontInlineSequence=0x" + Integer.toHexString(this.fontInlineSequence) +
                    ", resourceClassFlags=0x" + Integer.toHexString(this.resourceClassFlags) +
                    ", resourceId=" + StringUtils.toHexString(this.resourceId) +
                    '}';
        }
    }
}
