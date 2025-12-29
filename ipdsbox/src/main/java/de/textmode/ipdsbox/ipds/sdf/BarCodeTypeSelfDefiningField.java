package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Bar Code Type/Modifier self-defining field lists the optional bar codes that are supported by the printer in
 * addition to those required by the listed BCOCA subset (either BCD1 or BCD2). Type/Modifier combinations
 * that are required by the listed subset (either BCD1 or BCD2) should not appear in this self-defining field.
 */
public final class BarCodeTypeSelfDefiningField extends SelfDefiningField {

    public class BarCodeEntry {
        private int type;
        private int modifiers;

        /**
         * Returns the bar code type.
         */
        public int getBarCodeType() {
            return this.type;
        }

        /**
         * Sets the bar code type.
         */
        public void setBarCodeType(final int type) {
            this.type = type;
        }

        /**
         * Returns the code value for modifiers.
         */
        public int getModifiers() {
            return this.modifiers;
        }

        /**
         * Sets the value for modifiers.
         */
        public void setModifiers(final int modifiers) {
            this.modifiers = modifiers;
        }

        @Override
        public String toString() {
            return "BarCodeEntry{"
                    + "type=0x" + Integer.toHexString(this.type)
                    + ", modifiers=0x" + Integer.toHexString(this.modifiers)
                    + '}';
        }
    }

    private int bcocaSubset;
    private List<BarCodeEntry> entries = new ArrayList<>();

    /**
     * Creates a new {@link BarCodeTypeSelfDefiningField}.
     */
    public BarCodeTypeSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.BarCodeType);
    }

    /**
     * Creates a {@link BarCodeTypeSelfDefiningField} from the stream.
     */
    BarCodeTypeSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.BarCodeType);

        this.bcocaSubset = ipds.readUnsignedInteger16();


        while (ipds.bytesAvailable() > 0) {
            final BarCodeEntry entry = new BarCodeEntry();
            entry.setBarCodeType(ipds.readUnsignedByte());
            entry.setModifiers(ipds.readUnsignedByte());

            this.entries.add(entry);
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(6 + (this.entries.size() * 2));
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        ipds.writeUnsignedInteger16(this.getBcocaSubset());

        for (final BarCodeEntry entry : this.entries) {
            ipds.writeUnsignedByte(entry.getBarCodeType());
            ipds.writeUnsignedByte(entry.getModifiers());
        }
    }

    /**
     * Returns the BCOCA subset.
     */
    public int getBcocaSubset() {
        return this.bcocaSubset;
    }

    /**
     * Sets the BCOCA subset.
     */
    public void setBcocaSubset(final int bcocaSubset) {
        this.bcocaSubset = bcocaSubset;
    }

    /**
     * Returns the entries.
     */
    public List<BarCodeEntry> getEntries() {
        return this.entries;
    }

    /**
     * Sets the entries.
     */
    public void setEntries(final List<BarCodeEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "BarCodeTypeSelfDefiningField{"
                + "bcocaSubset=0x" + Integer.toHexString(this.bcocaSubset)
                + ", entries=" + this.entries
                + '}';
    }
}
