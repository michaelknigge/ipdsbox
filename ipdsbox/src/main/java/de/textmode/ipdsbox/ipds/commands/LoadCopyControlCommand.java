package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class LoadCopyControlCommand extends IpdsCommand {

    private final List<CopySubgroup> subgroups = new ArrayList<>();

    public LoadCopyControlCommand() {
        super(IpdsCommandId.LCC);
    }

    LoadCopyControlCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.LCC);

        while (ipds.bytesAvailable() > 0) {
            this.subgroups.add(new CopySubgroup(ipds));
        }
    }

    /**
     * Returns a list of copy subgroups.
     */
    public List<CopySubgroup> getCopySubgroups() {
        return this.subgroups;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        for (final CopySubgroup subgroup : this.subgroups) {
            subgroup.writeTo(ipds);
        }
    }

    public static class CopySubgroup {
        private final int copies;
        private final List<Keyword> keywords = new ArrayList<>();

        public CopySubgroup() {
            this.copies = 1;
        }

        CopySubgroup(final int copies) {
            this.copies = copies;
        }

        CopySubgroup(final IpdsByteArrayInputStream ipds) throws IOException {
            int count = ipds.readUnsignedByte() - 2;
            this.copies = ipds.readUnsignedByte();

            while (count > 0) {
                --count;

                this.keywords.add(new Keyword(ipds.readUnsignedByte(), ipds.readUnsignedByte()));
            }
        }

        void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
            ipds.writeUnsignedByte(2 + (this.keywords.size() * 2));
            ipds.writeUnsignedByte(this.copies);

            for (final Keyword keyword : this.keywords) {
                ipds.writeUnsignedByte(keyword.getKeywordId());
                ipds.writeUnsignedByte(keyword.getKeywordParameter());
            }
        }

        public List<Keyword> getKeywords() {
            return this.keywords;
        }

        @Override
        public String toString() {
            return "CopySubgroup{"	
                    + "copies=" + this.copies	
                    + ", keywords=" + this.keywords	
                    + '}';
        }
    }

    public static class Keyword {
        private int keywordId;
        private int keywordParameter;

        public Keyword(final int keywordId, final int keywordParameter) {
            this.keywordId = keywordId;
            this.keywordParameter = keywordParameter;
        }

        /**
         * Returns the keyword ID.
         */
        public int getKeywordId() {
            return this.keywordId;
        }

        /**
         * Sets the keyword ID.
         */
        public void setKeywordId(final int keywordId) {
            this.keywordId = keywordId;
        }

        /**
         * Returns the keyword parameter.
         */
        public int getKeywordParameter() {
            return this.keywordParameter;
        }

        /**
         * Sets the keyword parameter.
         */
        public void setKeywordParameter(final int keywordParameter) {
            this.keywordParameter = keywordParameter;
        }

        @Override
        public String toString() {
            return "Keyword{"	
                    + "keywordId=0x" + Integer.toHexString(this.keywordId)	
                    + ", keywordParameter=0x" + Integer.toHexString(this.keywordParameter)	
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "LoadCopyControlCommand{"	
                + "subgroups=" + this.subgroups	
                + '}';
    }
}
