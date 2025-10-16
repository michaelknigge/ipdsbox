package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field specifies the combinations of Resource Types and Resource ID Formats supported by
 * the printer, within the Activate Resource command. If this self-defining field is returned, the printer must also
 * return the AR-supported vector in the Sense Type and Model reply.
 */
public final class ActivateResourceSelfDefiningField extends SelfDefiningField {

    public class ActivateResourceEntry {
        private int resourceType;
        private int resourceIdFormat;

        /**
         * Returns the resource type.
         */
        public int getResourceType() {
            return this.resourceType;
        }

        /**
         * Sets the resource type.
         */
        public void setResourceType(final int resourceType) {
            this.resourceType = resourceType;
        }

        /**
         * Returns the resource ID format.
         */
        public int getResourceIdFormat() {
            return this.resourceIdFormat;
        }

        /**
         * Sets the resource ID format.
         */
        public void setResourceIdFormat(final int resourceIdFormat) {
            this.resourceIdFormat = resourceIdFormat;
        }

        @Override
        public String toString() {
            return "ActivateResourceEntry{" +
                    "resourceType=0x" + Integer.toHexString(this.resourceType) +
                    ", resourceIdFormat=0x" + Integer.toHexString(this.resourceIdFormat) +
                    '}';
        }
    }

    private List<ActivateResourceEntry> entries = new ArrayList<>();

    /**
     * Creates a new {@link ActivateResourceSelfDefiningField}.
     */
    public ActivateResourceSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.ActivateResource);
    }

    /**
     * Creates a {@link ActivateResourceSelfDefiningField} from the stream.
     */
    ActivateResourceSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.ActivateResource);

        while (ipds.bytesAvailable() > 0) {
            final ActivateResourceEntry entry = new ActivateResourceEntry();
            entry.setResourceType(ipds.readUnsignedByte());
            entry.setResourceIdFormat(ipds.readUnsignedByte());

            this.entries.add(entry);
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(4 + (this.entries.size() * 2));
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final ActivateResourceEntry entry : this.entries) {
            ipds.writeUnsignedByte(entry.getResourceType());
            ipds.writeUnsignedByte(entry.getResourceIdFormat());
        }
    }

    /**
     * Returns the entries.
     */
    public List<ActivateResourceEntry> getEntries() {
        return this.entries;
    }

    /**
     * Sets the entries.
     */
    public void setEntries(final List<ActivateResourceEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "ResourceSupportEntry{" +
                "entries=" + this.entries +
                '}';
    }
}
