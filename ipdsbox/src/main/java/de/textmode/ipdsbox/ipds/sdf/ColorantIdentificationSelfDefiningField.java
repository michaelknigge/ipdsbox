package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field lists all colorants available in the printer. Colorants and combinations of colorants can be
 * selected using a highlight-color value in the range X'0100'–X'FFFF' along with an indexed CMR.
 */
public final class ColorantIdentificationSelfDefiningField extends SelfDefiningField {

    public class ColorantIdentificationEntry {
        private static final Charset UTF16BE = Charset.forName("utf-16be");

        private int entryType;
        private int colorantAvailabilityFlags;
        private String colorantName;

        public ColorantIdentificationEntry() {
            this.entryType = 0x01;
            this.colorantAvailabilityFlags = 0x00;
            this.colorantName = "AFPC_Device_K";
        }

        public ColorantIdentificationEntry(final IpdsByteArrayInputStream ipds) throws IOException {
            final int len = ipds.readUnsignedByte();

            this.entryType = ipds.readUnsignedByte();
            this.colorantAvailabilityFlags = ipds.readUnsignedByte();

            ipds.skip(2);

            this.colorantName = UTF16BE.decode(ByteBuffer.wrap(ipds.readBytes(len - 5))).toString();
        }

        public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {

            final byte[] utf16Name = UTF16BE.encode(this.colorantName).array();

            ipds.writeUnsignedByte(5 + utf16Name.length);
            ipds.writeUnsignedByte(this.entryType);
            ipds.writeUnsignedByte(this.colorantAvailabilityFlags);
            ipds.writeUnsignedInteger16(0x0000);
            ipds.writeBytes(utf16Name);
        }

        /**
         * Returns the entry type.
         */
        public int getEntryType() {
            return this.entryType;
        }

        /**
         * Sets the entry type.
         */
        public void setEntryType(final int entryType) {
            this.entryType = entryType;
        }

        /**
         * Returns the colorant availability flags.
         */
        public int getColorantAvailabilityFlags() {
            return this.colorantAvailabilityFlags;
        }

        /**
         * Sets the colorant availability flags.
         */
        public void setColorantAvailabilityFlags(final int colorantAvailabilityFlags) {
            this.colorantAvailabilityFlags = colorantAvailabilityFlags;
        }

        /**
         * Returns the colorant name.
         */
        public String getColorantName() {
            return this.colorantName;
        }

        /**
         * Sets the colorant name.
         */
        public void setColorantName(final String colorantName) {
            this.colorantName = colorantName;
        }

        @Override
        public String toString() {
            return "ColorantIdentificationEntry{"
                    + "entryType=0x" + Integer.toHexString(this.entryType)
                    + ", colorantAvailabilityFlags=0x" + Integer.toHexString(this.colorantAvailabilityFlags)
                    + ", colorantName='" + this.colorantName + '\''
                    + '}';
        }
    }

    private List<ColorantIdentificationEntry> entries = new ArrayList<>();


    /**
     * Constructs the {@link ColorantIdentificationSelfDefiningField}.
     */
    public ColorantIdentificationSelfDefiningField() {
        super(SelfDefiningFieldId.ColorantIdentification);
    }

    /**
     * Constructs the {@link ColorantIdentificationSelfDefiningField}.
     */
    ColorantIdentificationSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.ColorantIdentification);

        while (ipds.bytesAvailable() > 0) {
            this.entries.add(new ColorantIdentificationEntry(ipds));
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        final IpdsByteArrayOutputStream temp = new IpdsByteArrayOutputStream();

        for (final ColorantIdentificationEntry entry : this.entries) {
            entry.writeTo(temp);
        }

        final byte[] entriesArray = temp.toByteArray();

        ipds.writeUnsignedInteger16(4 + entriesArray.length);
        ipds.writeUnsignedInteger16(SelfDefiningFieldId.ColorantIdentification.getId());
        ipds.writeBytes(entriesArray);
    }

    /**
     * Returns a list of colorant identification entries.
     */
    public List<ColorantIdentificationEntry> getEntries() {
        return this.entries;
    }

    /**
     * Sets a list of colorant identification entries.
     */
    public void setEntries(final List<ColorantIdentificationEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "ColorantIdentificationSelfDefiningField{"
                + "entries=" + this.entries
                + '}';
    }
}
