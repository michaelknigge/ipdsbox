package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Mapping a font local identifier to a HAID is called establishing a font equivalence. The LFE command is used to
 * establish font equivalences for both coded fonts and for data-object fonts. The pool of font local IDs for a page
 * or data object is shared among these two kinds of fonts. Likewise, the complete-font HAID pool is shared
 * among the two kinds of fonts.
 */
public final class LoadFontEquivalenceCommand extends IpdsCommand {

    private final List<FontEquivalenceEntry> entries = new ArrayList<FontEquivalenceEntry>();

    /**
     * Constructs the {@link LoadFontEquivalenceCommand}.
     */
    public LoadFontEquivalenceCommand() {
        super(IpdsCommandId.LFE);
    }

    /**
     * Constructs the {@link LoadFontEquivalenceCommand}.
     */
    LoadFontEquivalenceCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.LFE);

        while (ipds.bytesAvailable() > 0) {
            this.entries.add(new FontEquivalenceEntry(ipds));
        }
    }

    /**
     * Returns the entries of the LFE command.
     */
    public List<FontEquivalenceEntry> getEntries() {
        return this.entries;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        for (final FontEquivalenceEntry entry : this.entries) {
            entry.writeTo(ipds);
        }
    }

    public final class FontEquivalenceEntry {

        private int fontLocalId;
        private int haid;
        private int fontInlineSequence;
        private int graphicCharacterSetGlobalIdentifier;
        private int codePageGlobalIdentifier;
        private int fontTypefaceGlobalIdentifier;
        private int fontWidth;
        private int flags;


        FontEquivalenceEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            this.fontLocalId = ipds.readUnsignedByte();
            this.haid = ipds.readUnsignedInteger16();
            this.fontInlineSequence = ipds.readUnsignedInteger16();
            this.graphicCharacterSetGlobalIdentifier = ipds.readUnsignedInteger16();
            this.codePageGlobalIdentifier = ipds.readUnsignedInteger16();
            this.fontTypefaceGlobalIdentifier = ipds.readUnsignedInteger16();
            this.fontWidth = ipds.readUnsignedInteger16();
            ipds.skip(1);
            this.flags = ipds.readUnsignedByte();
            ipds.skip(1);
        }

        void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            ipds.writeUnsignedByte(this.fontLocalId);
            ipds.writeUnsignedInteger16(this.haid);
            ipds.writeUnsignedInteger16(this.fontInlineSequence);
            ipds.writeUnsignedInteger16(this.graphicCharacterSetGlobalIdentifier);
            ipds.writeUnsignedInteger16(this.codePageGlobalIdentifier);
            ipds.writeUnsignedInteger16(this.fontTypefaceGlobalIdentifier);
            ipds.writeUnsignedInteger16(this.fontWidth);

            ipds.writeUnsignedByte(0);
            ipds.writeUnsignedByte(this.flags);
            ipds.writeUnsignedByte(0);
        }

        /**
         * Returns the font local ID.
         */
        public int getFontLocalId() {
            return this.fontLocalId;
        }

        /**
         * Sets the font local ID.
         */
        public void setFontLocalId(final int fontLocalId) {
            this.fontLocalId = fontLocalId;
        }

        /**
         * Returns the host assigned ID.
         */
        public int getHaid() {
            return this.haid;
        }

        /**
         * Sets the host assigned ID.
         */
        public void setHaid(final int haid) {
            this.haid = haid;
        }

        /**
         * Returns the font inline sequence.
         */
        public int getFontInlineSequence() {
            return this.fontInlineSequence;
        }

        /**
         * Sets the font inline sequence.
         */
        public void setFontInlineSequence(final int fontInlineSequence) {
            this.fontInlineSequence = fontInlineSequence;
        }

        /**
         * Returns the GCSGID.
         */
        public int getGraphicCharacterSetGlobalIdentifier() {
            return this.graphicCharacterSetGlobalIdentifier;
        }

        /**
         * Sets the GCSGID.
         */
        public void setGraphicCharacterSetGlobalIdentifier(final int graphicCharacterSetGlobalIdentifier) {
            this.graphicCharacterSetGlobalIdentifier = graphicCharacterSetGlobalIdentifier;
        }

        /**
         * Returns the CPGID.
         */
        public int getCodePageGlobalIdentifier() {
            return this.codePageGlobalIdentifier;
        }

        /**
         * Sets the CPGID.
         */
        public void setCodePageGlobalIdentifier(final int codePageGlobalIdentifier) {
            this.codePageGlobalIdentifier = codePageGlobalIdentifier;
        }

        /**
         * Returns the FGID.
         */
        public int getFontTypefaceGlobalIdentifier() {
            return this.fontTypefaceGlobalIdentifier;
        }

        /**
         * Sets the FGID.
         */
        public void setFontTypefaceGlobalIdentifier(final int fontTypefaceGlobalIdentifier) {
            this.fontTypefaceGlobalIdentifier = fontTypefaceGlobalIdentifier;
        }

        /**
         * Returns the font width.
         */
        public int getFontWidth() {
            return this.fontWidth;
        }

        /**
         * Sets the font width.
         */
        public void setFontWidth(final int fontWidth) {
            this.fontWidth = fontWidth;
        }

        /**
         * Returns the LFE flags.
         */
        public int getFlags() {
            return this.flags;
        }

        /**
         * Sets the LFE flags.
         */
        public void setFlags(final int flags) {
            this.flags = flags;
        }

        @Override
        public String toString() {
            return "LoadFontEquivalenceEntry{"
                    + "fontLocalId=0x" + Integer.toHexString(this.fontLocalId)
                    + ", haid=0x" + Integer.toHexString(this.haid)
                    + ", fontInlineSequence=0x" + Integer.toHexString(this.fontInlineSequence)
                    + ", GCSGID=0x" + Integer.toHexString(this.graphicCharacterSetGlobalIdentifier)
                    + ", CPGID=0x" + Integer.toHexString(this.codePageGlobalIdentifier)
                    + ", FGID=0x" + Integer.toHexString(this.fontTypefaceGlobalIdentifier)
                    + ", fontWidth=0x" + Integer.toHexString(this.fontWidth)
                    + ", flags=0x" + Integer.toHexString(this.flags)
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "LoadFontEquivalenceCommand{"
                + "entries=" + this.entries
                + '}';
    }
}
