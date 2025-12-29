package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Storage Pools self-defining field specifies storage pools within the printer. Each storage pool is defined
 * with an entry that specifies total storage and the objects that are stored within the pool.
 */
public final class StoragePoolsSelfDefiningField extends SelfDefiningField {

    public class StoragePoolEntry {
        private int entryId;
        private int storagePoolId;
        private long emptySize;
        private List<Integer> objectIds = new ArrayList<>();

        /**
         * Returns the entry ID.
         */
        public int getEntryId() {
            return this.entryId;
        }

        /**
         * Sets the entry ID.
         */
        public void setEntryId(final int entryId) {
            this.entryId = entryId;
        }

        /**
         * Returns the storage pool ID.
         */
        public int getStoragePoolId() {
            return this.storagePoolId;
        }

        /**
         * Sets the storage pool ID.
         */
        public void setStoragePoolId(final int storagePoolId) {
            this.storagePoolId = storagePoolId;
        }

        /**
         * Returns the size of the storage pool, in bytes, when empty.
         */
        public long getEmptySize() {
            return this.emptySize;
        }

        /**
         * Sets the size of the storage pool, in bytes, when empty.
         */
        public void setEmptySize(final long emptySize) {
            this.emptySize = emptySize;
        }

        /**
         * Returns a list of Object IDs.
         */
        public List<Integer> getObjectIds() {
            return this.objectIds;
        }

        /**
         * Sets a list of Object IDs.
         */
        public void setObjectIds(final List<Integer> objectIds) {
            this.objectIds = objectIds;
        }

        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner(",", "[", "]");
            for (final Integer id : this.objectIds) {
                sj.add("0x" + Integer.toHexString(id));
            }

            return "StoragePoolEntry{"
                    + "entryId=" + this.entryId
                    + ", storagePoolId=" + this.storagePoolId
                    + ", emptySize=" + this.emptySize
                    + ", objectIds=" + sj.toString()
                    + '}';
        }
    }

    private List<StoragePoolEntry> storagePoolEntries = new ArrayList<>();

    /**
     * Creates a new {@link StoragePoolsSelfDefiningField}.
     */
    public StoragePoolsSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.StoragePools);
    }

    /**
     * Creates a {@link StoragePoolsSelfDefiningField} from the stream.
     */
    StoragePoolsSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.StoragePools);

        while (ipds.bytesAvailable() > 0) {
            final int len = ipds.readUnsignedByte();
            final int count = (len - 11) / 2;

            final StoragePoolEntry entry = new StoragePoolEntry();
            entry.setEntryId(ipds.readUnsignedByte());
            entry.setStoragePoolId(ipds.readUnsignedByte());
            entry.setEmptySize(ipds.readUnsignedInteger32());
            ipds.skip(4);

            for (int i = 0; i < count; i++) {
                entry.getObjectIds().add(ipds.readUnsignedInteger16());
            }

            this.storagePoolEntries.add(entry);
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        final IpdsByteArrayOutputStream poolsStream = new IpdsByteArrayOutputStream();

        for (final StoragePoolEntry entry : this.storagePoolEntries) {
            poolsStream.writeUnsignedByte(11 + (entry.getObjectIds().size() * 2));
            poolsStream.writeUnsignedByte(entry.getEntryId());
            poolsStream.writeUnsignedByte(entry.getStoragePoolId());
            poolsStream.writeUnsignedInteger32(entry.getEmptySize());
            poolsStream.writeUnsignedInteger32(0x00);

            for (final Integer id : entry.getObjectIds()) {
                poolsStream.writeUnsignedInteger16(id);
            }
        }

        final byte[] pools = poolsStream.toByteArray();
        ipds.writeUnsignedInteger16(4 + pools.length);
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        ipds.writeBytes(pools);
    }

    /**
     * Returns the storage pool entries.
     */
    public List<StoragePoolEntry> getStoragePoolEntries() {
        return this.storagePoolEntries;
    }

    /**
     * Sets the storage pool entries.
     */
    public void setTypeRecords(final List<StoragePoolEntry> storagePoolEntries) {
        this.storagePoolEntries = storagePoolEntries;
    }

    @Override
    public String toString() {
        return "StoragePoolsSelfDefiningField{"
                + "storagePoolEntries=" + this.storagePoolEntries
                + '}';
    }
}
