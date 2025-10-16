package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field lists the medium modification IDs that are currently supported by the XOH-SMM
 * command. If this self-defining field is returned, the printer must also return the Select-Medium-Modificationssupport
 * property ID (X'900E') in the Sense Type and Model reply.
 */
public class ResidentSymbolSetSupportSelfDefiningField extends SelfDefiningField {

    public final class ResidentSymbolSet {
        private int codePageId;
        private List<Integer> cpgids;
        private List<Integer> fgids;

        /**
         * Returns the Code Page Support ID.
         */
        public int getCodePageId() {
            return this.codePageId;
        }

        /**
         * Sets the Code Page Support ID.
         */
        public void setCodePageId(final int codePageId) {
            this.codePageId = codePageId;
        }

        /**
         * Returns a list of Code Page Global IDs.
         */
        public List<Integer> getCpgids() {
            return this.cpgids;
        }

        /**
         * Sets a list of Code Page Global IDs.
         */
        public void setCpgids(final List<Integer> cpgids) {
            this.cpgids = cpgids;
        }

        /**
         * Returns a list of Font Typeface Global IDs
         */
        public List<Integer> getFgids() {
            return this.fgids;
        }

        /**
         * Sets a list of Font Typeface Global IDs.
         */
        public void setFgids(final List<Integer> fgids) {
            this.fgids = fgids;
        }

        @Override
        public String toString() {
            final StringJoiner cpgidsJoiner = new StringJoiner(",", "[", "]");
            for (final Integer cpgid : this.cpgids) {
                cpgidsJoiner.add("0x" + Integer.toHexString(cpgid));
            }

            final StringJoiner fgidsJoiner = new StringJoiner(",", "[", "]");
            for (final Integer fgid : this.fgids) {
                fgidsJoiner.add("0x" + Integer.toHexString(fgid));
            }

            return "ResidentSymbolSet{" +
                    "codePageId=" + this.codePageId +
                    ", cpgids=" + cpgidsJoiner.toString() +
                    ", fgids=" + fgidsJoiner.toString() +
                    '}';
        }
    }

    private List<ResidentSymbolSet> residentSymbolSets = new ArrayList<>();

    /**
     * Constructs the {@link ResidentSymbolSetSupportSelfDefiningField}.
     */
    public ResidentSymbolSetSupportSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.ResidentSymbolSetSupport);
    }

    /**
     * Constructs the {@link ResidentSymbolSetSupportSelfDefiningField}.
     */
    public ResidentSymbolSetSupportSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.ResidentSymbolSetSupport);

        while (ipds.bytesAvailable() > 0) {
            ipds.skip(1);

            final ResidentSymbolSet residentSymbolSet = new ResidentSymbolSet();
            residentSymbolSet.setCodePageId(ipds.readUnsignedByte());

            final int codePageCount = (ipds.readUnsignedByte() - 2) / 2;
            ipds.skip(1);

            for (int ix = 0; ix < codePageCount; ix++) {
                residentSymbolSet.cpgids.add(ipds.readUnsignedInteger16());
            }

            final int fontTypefaceCount = (ipds.readUnsignedByte() - 2) / 2;
            ipds.skip(1);

            for (int ix = 0; ix < fontTypefaceCount; ix++) {
                residentSymbolSet.fgids.add(ipds.readUnsignedInteger16());
            }

            this.residentSymbolSets.add(residentSymbolSet);
        }
    }

    /**
     * Writes this {@link ResidentSymbolSetSupportSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {

        final IpdsByteArrayOutputStream repeatingGroups = new IpdsByteArrayOutputStream();

        for (final ResidentSymbolSet residentSymbolSet : this.residentSymbolSets) {
            final int codePageListLen = 4 + residentSymbolSet.cpgids.size() * 2;
            final int fontListLen = 2 + residentSymbolSet.fgids.size() * 2;

            repeatingGroups.writeUnsignedByte(codePageListLen + fontListLen);
            repeatingGroups.writeUnsignedByte(residentSymbolSet.getCodePageId());

            repeatingGroups.writeUnsignedByte(2 + (residentSymbolSet.getCpgids().size() * 2));
            for (final Integer cpgid : residentSymbolSet.getCpgids()) {
                repeatingGroups.writeUnsignedInteger16(cpgid);
            }

            repeatingGroups.writeUnsignedByte(2 + (residentSymbolSet.getFgids().size() * 2));
            for (final Integer fgid : residentSymbolSet.getFgids()) {
                repeatingGroups.writeUnsignedInteger16(fgid);
            }
        }

        final byte[] rg = repeatingGroups.toByteArray();

        out.writeUnsignedInteger16(4 + rg.length);
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        out.writeBytes(rg);
    }

    /**
     * Returns a list containing all supported Resident Symbol-Sets.
     */
    public List<ResidentSymbolSet> getMediumModificationIds() {
        return this.residentSymbolSets;
    }

    /**
     * Sets a list containing all supported Resident Symbol-Sets.
     */
    public void setMediumModificationIds(final List<ResidentSymbolSet> residentSymbolSets) {
        this.residentSymbolSets = residentSymbolSets;
    }

    @Override
    public String toString() {
        return "ResidentSymbolSetSupportSelfDefiningField{" +
                "residentSymbolSets=" + this.residentSymbolSets +
                '}';
    }
}
