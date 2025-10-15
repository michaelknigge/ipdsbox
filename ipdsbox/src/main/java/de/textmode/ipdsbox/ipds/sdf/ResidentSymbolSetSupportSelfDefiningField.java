package de.textmode.ipdsbox.ipds.sdf;

import java.util.ArrayList;
import java.util.List;

/**
 * Resident Symbol-Set Support Self-Defining Field (page 221).
 *
 * <p>Parses the fields of the "Resident Symbol-Set Support self-defining field"
 * using an {@code IpdsByteArrayInputStream}. The field defines one or more
 * repeating group lists pairing a list of supported code pages with a list
 * of fonts (FGIDs) that support those code pages.
 *
 * <p>Reading methods by format/length:
 * <ul>
 *   <li>UBIN: 1→readUnsignedByte, 2→readUnsignedInteger16, 3→readUnsignedInteger24, 4→readUnsignedInteger32</li>
 *   <li>SBIN: 1→readByte, 2→readInteger16, 3→readInteger24, 4→readInteger32</li>
 *   <li>CODE: len 1→readUnsignedByte, len ≥2→readBytes(len)</li>
 *   <li>CHAR: readEbcdicString(len)</li>
 *   <li>BITS: len 1→readUnsignedByte, len ≥2→readBytes(len)</li>
 *   <li>UNDF: readBytes(len)</li>
 * </ul>
 *
 * <p>Writing uses the corresponding {@code IpdsByteArrayOutputStream} methods.
 */
public class ResidentSymbolSetSupportSelfDefiningField extends SelfDefiningField {

    // ---------------------------------------------------------------------
    // Header (table order)
    // ---------------------------------------------------------------------

    // 0–1: UBIN (2) — SDFlength
    private int SDFlength;

    // 2–3: CODE (2) — SDFID
    private byte[] SDFID;

    // 4..(SDFlength-1): one or more Resident Symbol-Set Repeating Group Lists
    private List<ResidentSymbolSetRepeatingGroupList> residentSymbolSetRepeatingGroupList = new ArrayList<>();

    /**
     * Creates and parses the Resident Symbol-Set Support self-defining field from the stream.
     *
     * @param ipds the {@code IpdsByteArrayInputStream} positioned at the start of this self-defining field
     */
    public ResidentSymbolSetSupportSelfDefiningField(final IpdsByteArrayInputStream ipds) {
        // Header
        this.SDFlength = ipds.readUnsignedInteger16(); // UBIN (2)
        this.SDFID    = ipds.readBytes(2);             // CODE (2)

        int consumed = 4;
        // Parse one or more Resident Symbol-Set Repeating Group Lists
        while (consumed < this.SDFlength) {
            // +0: UBIN (2) — Length (total length of this repeating-group list incl. this field)
            final int length = ipds.readUnsignedInteger16();
            consumed += 2;

            // Track how many bytes we will consume inside this group
            int groupConsumed = 2;

            // +1: CODE (1) — CodepageID (code page support ID format selector)
            final int codepageID = ipds.readUnsignedByte();
            groupConsumed += 1;

            // +2: UBIN (2) — Codepage list length (incl. this length field)
            final int codepageListLength = ipds.readUnsignedInteger16();
            groupConsumed += 2;

            // CodePageList payload: often starts with an entry-length (UBIN 2) followed by one or more 2-byte CODE CGIDs.
            int cpListConsumed = 0;
            final int cpEntryLength;

            if (codepageListLength >= 4) {
                // assume +0 UBIN list length (already read in parent), +1 UBIN entry length (2),
                // then repeating ++0–1 CODE CGID entries.
                cpEntryLength = ipds.readUnsignedInteger16(); // UBIN (2) — Entry length
                cpListConsumed += 2;
            } else {
                // Defensive: if too small to hold an entry-length, set default 2 and read nothing here.
                cpEntryLength = 2;
            }

            final List<byte[]> codePageList = new ArrayList<>();
            // Remaining bytes in the codepage list are entries
            int cpEntriesBytes = codepageListLength - (2 /*list length field not included here because it was read above at +2*/ + cpListConsumed);
            // Consume entries while full entries fit
            while (cpEntriesBytes >= cpEntryLength && cpEntryLength >= 2) {
                // For CODE entries with length >=2, we read exactly cpEntryLength bytes,
                // but the table indicates CGID is 2 bytes; we keep it general and store raw bytes.
                final byte[] cg = ipds.readBytes(cpEntryLength);
                codePageList.add(cg);
                cpEntriesBytes -= cpEntryLength;
            }
            groupConsumed += codepageListLength - 2; // subtract the 2 bytes we already counted for the list length itself at +2

            // Matching FontIDList:
            // +0: UBIN (2) — FontID list length
            final int fontIDListLength = ipds.readUnsignedInteger16();
            groupConsumed += 2;

            // +1: UBIN (2) — Entry length (value X'02' per spec)
            final int entryLength = ipds.readUnsignedInteger16();
            groupConsumed += 2;

            final List<byte[]> FGID = new ArrayList<>();
            int remainingFontBytes = fontIDListLength - 4; // subtract list length (2) + entry length (2)
            while (remainingFontBytes >= entryLength && entryLength >= 2) {
                final byte[] fg = ipds.readBytes(entryLength); // CODE len ≥2 → readBytes
                FGID.add(fg);
                remainingFontBytes -= entryLength;
            }

            // Build group object
            final ResidentSymbolSetRepeatingGroupList group = new ResidentSymbolSetRepeatingGroupList();
            group.setLength(length);
            group.setCodepageID(codepageID);
            group.setCodepageListLength(codepageListLength);
            group.setFontIDListLength(fontIDListLength);
            group.setEntryLength(entryLength);
            group.setCodePageList(codePageList);
            group.setFGID(FGID);

            this.residentSymbolSetRepeatingGroupList.add(group);

            // Account for any padding inside the group (if declared length > consumed)
            final int toSkip = length - groupConsumed;
            if (toSkip > 0) {
                // Skip any remaining bytes in this repeating group that are not parsed fields
                ipds.readBytes(toSkip);
            }
            consumed += (length - 2); // we've already counted the 2 bytes for 'length' when entered the group
        }
    }

    // ---------------------------------------------------------------------
    // Getters / Setters for header fields
    // ---------------------------------------------------------------------

    /** Returns the SDFlength. */
    public int getSDFlength() { return this.SDFlength; }

    /** Sets the SDFlength. */
    public void setSDFlength(final int SDFlength) { this.SDFlength = SDFlength; }

    /** Returns the SDFID. */
    public byte[] getSDFID() { return this.SDFID; }

    /** Sets the SDFID. */
    public void setSDFID(final byte[] SDFID) { this.SDFID = SDFID; }

    /** Returns the Resident Symbol-Set Repeating Group List. */
    public List<ResidentSymbolSetRepeatingGroupList> getResidentSymbolSetRepeatingGroupList() {
        return this.residentSymbolSetRepeatingGroupList;
    }

    /** Sets the Resident Symbol-Set Repeating Group List. */
    public void setResidentSymbolSetRepeatingGroupList(final List<ResidentSymbolSetRepeatingGroupList> list) {
        this.residentSymbolSetRepeatingGroupList = (list != null) ? list : new ArrayList<>();
    }

    // ---------------------------------------------------------------------
    // Writing (in exact table order)
    // ---------------------------------------------------------------------

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     *
     * <p>Note: {@code SDFlength} must reflect the total SDF length (including the 2-byte length field).
     * Recalculate when changing the repeating lists.
     *
     * @param ipds the {@code IpdsByteArrayOutputStream}
     */
    public void writeTo(final IpdsByteArrayOutputStream ipds) {
        // Header
        ipds.writeUnsignedInteger16(this.SDFlength); // UBIN(2) — SDFlength
        ipds.writeBytes(this.SDFID);                 // CODE(2) — SDFID

        // Repeating Group Lists
        for (final ResidentSymbolSetRepeatingGroupList g : this.residentSymbolSetRepeatingGroupList) {
            ipds.writeUnsignedInteger16(g.getLength());       // +0 UBIN(2) — Length
            ipds.writeUnsignedByte(g.getCodepageID());        // +1 CODE(1) — CodepageID

            // CodePageList
            ipds.writeUnsignedInteger16(g.getCodepageListLength()); // +2 UBIN(2) — Codepage list length
            // For CodePageList we mirror the reader's contract: emit an entry length (UBIN 2) if present,
            // then emit repeating CODE entries (len = entryLength, typically 2).
            // We use the same 'entryLength' field as used for the FontID list; if you maintain separate
            // entry-lengths for code pages vs fonts, adjust accordingly.
            ipds.writeUnsignedInteger16(g.getEntryLength());         // (assumed) UBIN(2) — Entry length for CGID entries
            if (g.getCodePageList() != null) {
                for (final byte[] cg : g.getCodePageList()) {
                    ipds.writeBytes(cg); // CODE len ≥2 → writeBytes
                }
            }

            // FontIDList
            ipds.writeUnsignedInteger16(g.getFontIDListLength());    // +0 UBIN(2) — FontID list length
            ipds.writeUnsignedInteger16(g.getEntryLength());         // +1 UBIN(2) — Entry length
            if (g.getFGID() != null) {
                for (final byte[] fg : g.getFGID()) {
                    ipds.writeBytes(fg); // CODE len ≥2 → writeBytes
                }
            }
        }
    }

    // ---------------------------------------------------------------------
    // toString
    // ---------------------------------------------------------------------

    /**
     * Returns a string representation containing the parsed values.
     *
     * @return string representation of this self-defining field
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResidentSymbolSetSupportSelfDefiningField{");
        sb.append("SDFlength=").append(this.SDFlength);
        sb.append(", SDFID=").append(bytesToHex(this.SDFID));
        sb.append(", groups=").append(this.residentSymbolSetRepeatingGroupList.size()).append('[');
        for (int i = 0; i < this.residentSymbolSetRepeatingGroupList.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.residentSymbolSetRepeatingGroupList.get(i));
        }
        sb.append("]}");
        return sb.toString();
    }

    private static String bytesToHex(final byte[] data) {
        if (data == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        for (final byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    // ---------------------------------------------------------------------
    // Nested type for the repeating group list
    // ---------------------------------------------------------------------

    /**
     * Resident Symbol-Set Repeating Group List entry.
     *
     * <p>Contains a CodePage list and a matching Font ID list.</p>
     */
    public static class ResidentSymbolSetRepeatingGroupList {
        // +0 UBIN (2) — Length
        private int length;

        // +1 CODE (1) — CodepageID
        private int codepageID;

        // +2 UBIN (2) — Codepage list length
        private int codepageListLength;

        // (assumed) UBIN (2) — Entry length (applied for both CGID and FGID entries; typical value X'02')
        private int entryLength;

        // CodePageList entries: ++0–1 CODE — CGID
        private List<byte[]> codePageList = new ArrayList<>();

        // FontIDList:
        // +0 UBIN (2) — FontID list length
        private int fontIDListLength;

        // ++0–1 CODE — FGID entries
        private List<byte[]> FGID = new ArrayList<>();

        /** Returns the Length. */
        public int getLength() { return this.length; }

        /** Sets the Length. */
        public void setLength(final int length) { this.length = length; }

        /** Returns the CodepageID. */
        public int getCodepageID() { return this.codepageID; }

        /** Sets the CodepageID. */
        public void setCodepageID(final int codepageID) { this.codepageID = codepageID; }

        /** Returns the Codepage list length. */
        public int getCodepageListLength() { return this.codepageListLength; }

        /** Sets the Codepage list length. */
        public void setCodepageListLength(final int codepageListLength) { this.codepageListLength = codepageListLength; }

        /** Returns the Entry length. */
        public int getEntryLength() { return this.entryLength; }

        /** Sets the Entry length. */
        public void setEntryLength(final int entryLength) { this.entryLength = entryLength; }

        /** Returns the CodePageList. */
        public List<byte[]> getCodePageList() { return this.codePageList; }

        /** Sets the CodePageList. */
        public void setCodePageList(final List<byte[]> codePageList) {
            this.codePageList = (codePageList != null) ? codePageList : new ArrayList<>();
        }

        /** Returns the Font ID list length. */
        public int getFontIDListLength() { return this.fontIDListLength; }

        /** Sets the Font ID list length. */
        public void setFontIDListLength(final int fontIDListLength) { this.fontIDListLength = fontIDListLength; }

        /** Returns the FGID. */
        public List<byte[]> getFGID() { return this.FGID; }

        /** Sets the FGID. */
        public void setFGID(final List<byte[]> FGID) {
            this.FGID = (FGID != null) ? FGID : new ArrayList<>();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{length=");
            sb.append(this.length)
                    .append(", codepageID=").append(this.codepageID)
                    .append(", codepageListLength=").append(this.codepageListLength)
                    .append(", entryLength=").append(this.entryLength)
                    .append(", codePageList=[");
            for (int i = 0; i < this.codePageList.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(bytesToHex(this.codePageList.get(i)));
            }
            sb.append("], fontIDListLength=").append(this.fontIDListLength)
                    .append(", FGID=[");
            for (int i = 0; i < this.FGID.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(bytesToHex(this.FGID.get(i)));
            }
            sb.append("]}");
            return sb.toString();
        }
    }
}
