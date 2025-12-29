package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Symbol-Set Support self-defining field specifies the limits of support for the Load Symbol Set command.
 */
public final class SymbolSetSupportSelfDefiningField extends SelfDefiningField {

    public class SymbolSetSupportEntry {
        private int valueEntryId; // 0x01 (fixed-box) or 0x02 (variable-box)

        // only for fixed-box sizes
        private int xBoxSize;
        private int yBoxSize;

        // only for fixed-box sizes
        private int unitBase;
        private int ppub;
        private int maximumSize;
        private int uniformSize;

        // for fixed-box sizes AND variable-box sizes
        private List<Integer> fgids = new ArrayList<>();

        /**
         * Returns the value entry ID.
         */
        public int getValueEntryId() {
            return this.valueEntryId;
        }

        /**
         * Sets the value entry ID.
         */
        public void setValueEntryId(final int valueEntryId) {
            this.valueEntryId = valueEntryId;
        }

        /**
         * Returns the X box size (only valid for fixed-box).
         */
        public int getXBoxSize() {
            return this.xBoxSize;
        }

        /**
         * Sets the X box size (only valid for fixed-box).
         */
        public void setXBoxSize(final int xBoxSize) {
            this.xBoxSize = xBoxSize;
        }

        /**
         * Returns the Y box size (only valid for fixed-box).
         */
        public int getYBoxSize() {
            return this.yBoxSize;
        }

        /**
         * Sets the Y box size (only valid for fixed-box).
         */
        public void setYBoxSize(final int yBoxSize) {
            this.yBoxSize = yBoxSize;
        }

        /**
         * Returns the unit base (only valid for variable-box).
         */
        public int getUnitBase() {
            return this.unitBase;
        }

        /**
         * Sets the unit base (only valid for variable-box).
         */
        public void setUnitBase(final int unitBase) {
            this.unitBase = unitBase;
        }

        /**
         * Returns the pels per unit base (only valid for variable-box).
         */
        public int getPpub() {
            return this.ppub;
        }

        /**
         * Sets the pels per unit base (only valid for variable-box).
         */
        public void setPpub(final int ppub) {
            this.ppub = ppub;
        }

        /**
         * Returns the maximum character-box X size (only valid for variable-box).
         */
        public int getMaximumSize() {
            return this.maximumSize;
        }

        /**
         * Sets the maximum character-box X size (only valid for variable-box).
         */
        public void setMaximumSize(final int maximumSize) {
            this.maximumSize = maximumSize;
        }

        /**
         * Returns the uniform character-box Y size (only valid for variable-box).
         */
        public int getUniformSize() {
            return this.uniformSize;
        }

        /**
         * Sets the uniform character-box Y size (only valid for variable-box).
         */
        public void setUniformSize(final int uniformSize) {
            this.uniformSize = uniformSize;
        }

        /**
         * Returns a list of all Font Typeface Global ID (FGID) supporting the box size.
         */
        public List<Integer> getFgids() {
            return this.fgids;
        }

        /**
         * Sets a list of all Font Typeface Global ID (FGID) supporting the box size.
         */
        public void setFgids(final List<Integer> fgids) {
            this.fgids = fgids;
        }

        @Override
        public String toString() {
            if (this.valueEntryId == 0x01) { // fixed-box
                return "SymbolSetSupportEntry{"	
                        + "valueEntryId=" + this.valueEntryId	
                        + ", xBoxSize=" + this.xBoxSize	
                        + ", yBoxSize=" + this.yBoxSize	
                        + ", fgids=" + this.fgids	
                        + '}';
            }

            if (this.valueEntryId == 0x02) { // variable-box
                return "SymbolSetSupportEntry{"	
                        + "valueEntryId=" + this.valueEntryId	
                        + ", unitBase=" + this.unitBase	
                        + ", ppub=" + this.ppub	
                        + ", maximumSize=" + this.maximumSize	
                        + ", uniformSize=" + this.uniformSize	
                        + ", fgids=" + this.fgids	
                        + '}';
            }

            // Should not happen... but who knows... toString() with all variables...
            return "SymbolSetSupportEntry{"	
                    + "valueEntryId=" + this.valueEntryId	
                    + ", xBoxSize=" + this.xBoxSize	
                    + ", yBoxSize=" + this.yBoxSize	
                    + ", unitBase=" + this.unitBase	
                    + ", ppub=" + this.ppub	
                    + ", maximumSize=" + this.maximumSize	
                    + ", uniformSize=" + this.uniformSize	
                    + ", fgids=" + this.fgids	
                    + '}';
        }
    }

    private List<SymbolSetSupportEntry> entries = new ArrayList<>();

    /**
     * Creates a new {@link SymbolSetSupportSelfDefiningField}.
     */
    public SymbolSetSupportSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.SymbolSetSupport);
    }

    /**
     * Creates a {@link SymbolSetSupportSelfDefiningField} from the stream.
     */
    SymbolSetSupportSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.SymbolSetSupport);

        while (ipds.bytesAvailable() > 0) {
            final int entryLength = ipds.readUnsignedByte();

            final SymbolSetSupportEntry entry = new SymbolSetSupportEntry();
            entry.setValueEntryId(ipds.readUnsignedByte());

            final int count;

            if (entry.getValueEntryId() == 0x01) {
                // parse fixed-box size entry
                entry.setXBoxSize(ipds.readUnsignedByte());
                entry.setYBoxSize(ipds.readUnsignedByte());
                ipds.skip(2);
                count = entryLength - 6;
            } else {
                // pase variable-box size entry
                entry.setUnitBase(ipds.readUnsignedByte());
                ipds.skip(1);
                entry.setPpub(ipds.readUnsignedInteger16());
                entry.setMaximumSize(ipds.readUnsignedByte());
                entry.setUniformSize(ipds.readUnsignedByte());
                ipds.skip(2);
                count = entryLength - 10;
            }

            for (int ix = 0; ix < count; ++ix) {
                entry.getFgids().add(ipds.readUnsignedInteger16());
            }

            this.entries.add(entry);
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        final IpdsByteArrayOutputStream entryStream = new IpdsByteArrayOutputStream();

        for (final SymbolSetSupportEntry entry : this.entries) {

            if (entry.getValueEntryId() == 0x01) {
                entryStream.writeUnsignedByte(6 + entry.getFgids().size() * 2);
                entryStream.writeUnsignedByte(entry.getValueEntryId()); // Should be 0x01
                entryStream.writeUnsignedByte(entry.getXBoxSize());
                entryStream.writeUnsignedByte(entry.getYBoxSize());
                entryStream.writeUnsignedByte(0x00);
                entryStream.writeUnsignedByte(0x02);
            } else {
                entryStream.writeUnsignedByte(10 + entry.getFgids().size() * 2);
                entryStream.writeUnsignedByte(entry.getValueEntryId()); // Should be 0x02
                entryStream.writeUnsignedByte(entry.getUnitBase());
                entryStream.writeUnsignedByte(0x00);
                entryStream.writeUnsignedInteger16(entry.getPpub());
                entryStream.writeUnsignedByte(entry.getMaximumSize());
                entryStream.writeUnsignedByte(entry.getUniformSize());
                entryStream.writeUnsignedByte(0x00);
                entryStream.writeUnsignedByte(0x02);
            }

            for (final Integer fgid : entry.getFgids()) {
                entryStream.writeUnsignedInteger16(fgid);
            }
        }

        final byte[] entries = entryStream.toByteArray();

        ipds.writeUnsignedInteger16(4 + entries.length);
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        ipds.writeBytes(entries);
    }

    /**
     * Returns the entries (fixed-box size and variable-box size value entries).
     */
    public List<SymbolSetSupportEntry> getEntries() {
        return this.entries;
    }

    /**
     * Sets the entries (fixed-box size and variable-box size value entries).
     */
    public void setEntries(final List<SymbolSetSupportEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "SymbolSetSupportSelfDefiningField{"	
                + "entries=" + this.entries	
                + '}';
    }
}
