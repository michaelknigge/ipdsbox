package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Execute Order Anystate RRL RT & RIDF Support self-defining field specifies the combinations of resource
 * types and resource ID formats that the printer supports in an XOA-RRL command.
 */
public final class ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField extends SelfDefiningField {

    private List<ResourceSupportEntry> entries = new ArrayList<>();

    /**
     * Creates a new {@link ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField}.
     */
    public ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField() {
        super(SelfDefiningFieldId.ExecuteOrderAnystateRequestResidentResourceListSupport);
    }

    /**
     * Creates a {@link ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField}
     * from the given {@link IpdsByteArrayInputStream}.
     */
    ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField(
            final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.ExecuteOrderAnystateRequestResidentResourceListSupport);

        while (ipds.bytesAvailable() > 0) {
            final ResourceSupportEntry entry = new ResourceSupportEntry();
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

        for (final ResourceSupportEntry entry : this.entries) {
            ipds.writeUnsignedByte(entry.getResourceType());
            ipds.writeUnsignedByte(entry.getResourceIdFormat());
        }
    }

    /**
     * Returns the entries.
     */
    public List<ResourceSupportEntry> getEntries() {
        return this.entries;
    }

    /**
     * Sets the entries.
     */
    public void setEntries(final List<ResourceSupportEntry> entries) {
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
        return "ResourceSupportEntry{"
                + "entries=" + this.entries
                + '}';
    }

    public static final class ResourceSupportEntry {
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
            return "ResourceSupportEntry{"
                    + "resourceType=0x" + Integer.toHexString(this.resourceType)
                    + ", resourceIdFormat=0x" + Integer.toHexString(this.resourceIdFormat)
                    + '}';
        }
    }
}
