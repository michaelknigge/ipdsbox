package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            final byte[] entry = ipds.readBytes(length - 2);

            this.entries.add(new ActivateResourceEntry(new IpdsByteArrayInputStream(entry)));
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

    public static final class ActivateResourceEntry {
        private int resourceType;
        private int haid;
        private int sectionID;
        private int resourceIdFormat;
        private int fontInlineSequence;
        private int resourceClassFlags;
        private List<Triplet> resourceIdTriplets = new ArrayList();

        ActivateResourceEntry(final IpdsByteArrayInputStream ipds) throws IOException {

            if (ipds.bytesAvailable() >= 1) {
                this.resourceType = ipds.readUnsignedByte();
            }

            if (ipds.bytesAvailable() >= 2) {
                this.haid = ipds.readUnsignedInteger16();
            }

            if (ipds.bytesAvailable() >= 1) {
                this.sectionID = ipds.readUnsignedByte();
            }

            if (ipds.bytesAvailable() >= 1) {
                this.resourceIdFormat = ipds.readUnsignedByte();
            }

            if (ipds.bytesAvailable() >= 2) {
                this.fontInlineSequence = ipds.readUnsignedInteger16();
            }

            if (ipds.bytesAvailable() >= 2) {
                ipds.skip(2); // two bytes "reserved"
            }

            if (ipds.bytesAvailable() >= 1) {
                this.resourceClassFlags = ipds.readUnsignedByte();
            }

            // TODO... There is a fixed part in front of the triplets :-(
            byte[] rawTriplet = ipds.readTripletIfExists();
            while (rawTriplet != null) {
                this.resourceIdTriplets.add(TripletFactory.create(rawTriplet));
                rawTriplet = ipds.readTripletIfExists();

            }

        }

        void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            throw new IOException("Not Yet Implemented");


            /*
            ipds.writeUnsignedInteger16(12 + this.resourceId.length);

            ipds.writeUnsignedByte(this.resourceType);
            ipds.writeUnsignedInteger16(this.haid);
            ipds.writeUnsignedByte(this.sectionID);
            ipds.writeUnsignedByte(this.resourceIdFormat);
            ipds.writeUnsignedInteger16(this.fontInlineSequence);

            ipds.writeBytes(this.resourceId);
             */
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
        public List<Triplet> getResourceIdTriplets() {
            return this.resourceIdTriplets;
        }

        /**
         * Sets the Resource ID.
         */
        public void setResourceIdTriplets(final List<Triplet> resourceIdTriplets) {
            this.resourceIdTriplets = resourceIdTriplets;
        }

        @Override
        public String toString() {
            return "ActivateResourceEntry{"
                    + "resourceType=0x" + Integer.toHexString(this.resourceType)
                    + ", haid=" + Integer.toHexString(this.haid)
                    + ", sectionID=0x" + Integer.toHexString(this.sectionID)
                    + ", resourceIdFormat=0x" + Integer.toHexString(this.resourceIdFormat)
                    + ", fontInlineSequence=0x" + Integer.toHexString(this.fontInlineSequence)
                    + ", resourceClassFlags=0x" + Integer.toHexString(this.resourceClassFlags)
                    + ", resourceIdTriplets=" + this.resourceIdTriplets
                    + '}';
        }
    }
}
