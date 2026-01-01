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
public final class MediaDestinationsSelfDefiningField extends SelfDefiningField {

    private int defaultId;
    private List<MediaDestinationsEntry> entries = new ArrayList<>();

    /**
     * Creates a new {@link MediaDestinationsSelfDefiningField}.
     */
    public MediaDestinationsSelfDefiningField() {
        super(SelfDefiningFieldId.MediaDestinations);
    }

    /**
     * Creates a {@link MediaDestinationsSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    MediaDestinationsSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.MediaDestinations);

        this.defaultId = ipds.readUnsignedInteger16();

        while (ipds.bytesAvailable() > 0) {
            final MediaDestinationsEntry entry = new MediaDestinationsEntry();
            entry.setFirst(ipds.readUnsignedInteger16());
            entry.setLast(ipds.readUnsignedInteger16());

            this.entries.add(entry);
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(6 + (this.entries.size() * 4));
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        ipds.writeUnsignedInteger16(this.getDefaultId());

        for (final MediaDestinationsEntry entry : this.entries) {
            ipds.writeUnsignedInteger16(entry.getFirst());
            ipds.writeUnsignedInteger16(entry.getLast());
        }
    }

    /**
     * Returns the Default media-destination ID.
     */
    public int getDefaultId() {
        return this.defaultId;
    }

    /**
     * Sets the Default media-destination ID.
     */
    public void setDefaultId(final int defaultId) {
        this.defaultId = defaultId;
    }

    /**
     * Returns the entries.
     */
    public List<MediaDestinationsEntry> getEntries() {
        return this.entries;
    }

    /**
     * Sets the entries.
     */
    public void setEntries(final List<MediaDestinationsEntry> entries) {
        this.entries = entries;
    }

    /**
     * Accept method for the {@link SelfDefiningFieldVisitor}.
     */
    @Override
    public void accept(final SelfDefiningFieldVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public String toString() {
        return "MediaDestinationsSelfDefiningField{"
                + "defaultId=" + this.defaultId
                + ", entries=" + this.entries
                + '}';
    }

    public static final class MediaDestinationsEntry {
        private int first;
        private int last;

        /**
         * Returns the First number in a range of available, contiguous media-destination IDs.
         */
        public int getFirst() {
            return this.first;
        }

        /**
         * Sets the First number in a range of available, contiguous media-destination IDs.
         */
        public void setFirst(final int first) {
            this.first = first;
        }

        /**
         * Returns the Last number in a range of available, contiguous media-destination IDs.
         */
        public int getLast() {
            return this.last;
        }

        /**
         * Sets the Last number in a range of available, contiguous media-destination IDs.
         */
        public void setLast(final int last) {
            this.last = last;
        }

        @Override
        public String toString() {
            return "MediaDestinationsEntry{"
                    + "first=" + this.first
                    + ", last=" + this.last
                    + '}';
        }
    }
}
