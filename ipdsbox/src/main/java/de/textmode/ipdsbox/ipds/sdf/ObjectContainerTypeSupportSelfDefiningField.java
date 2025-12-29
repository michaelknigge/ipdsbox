package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field lists the object containers supported by the printer and for each type of object indicates
 * whether the object is supported in home state, in page or overlay state, or in all three states.
 */
public final class ObjectContainerTypeSupportSelfDefiningField extends SelfDefiningField {

    public class TypeRecord {
        private int type;
        private List<byte[]> regIds = new ArrayList<>();

        /**
         * Returns the type.
         */
        public int getType() {
            return this.type;
        }

        /**
         * Sets the type.
         */
        public void setType(final int type) {
            this.type = type;
        }

        /**
         * Returns a list of all MO:DCA-registered object IDs supported.
         */
        public List<byte[]> getRegIds() {
            return this.regIds;
        }

        /**
         * Sets a list of all MO:DCA-registered object IDs supported.
         */
        public void setRegIds(final List<byte[]> fgids) {
            this.regIds = fgids;
        }

        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner(",", "[", "]");
            for (final byte[] regId : this.regIds) {
                sj.add(StringUtils.toHexString(regId));
            }

            return "TypeRecord{"
                    + "type=0x" + Integer.toHexString(this.type)
                    + ", regIds=" + sj.toString()
                    + '}';
        }
    }

    private List<TypeRecord> typeRecords = new ArrayList<>();

    /**
     * Creates a new {@link ObjectContainerTypeSupportSelfDefiningField}.
     */
    public ObjectContainerTypeSupportSelfDefiningField() {
        super(SelfDefiningFieldId.ObjectContainerTypeSupport);
    }

    /**
     * Creates a {@link ObjectContainerTypeSupportSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    ObjectContainerTypeSupportSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.ObjectContainerTypeSupport);

        while (ipds.bytesAvailable() > 0) {
            final int len = ipds.readUnsignedByte();
            final int count = len - 2;

            final TypeRecord typeRecord = new TypeRecord();
            typeRecord.setType(ipds.readUnsignedByte());

            for (int i = 0; i < count; i++) {
                typeRecord.getRegIds().add(ipds.readBytes(16));
            }

            this.typeRecords.add(typeRecord);
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        final IpdsByteArrayOutputStream typesStream = new IpdsByteArrayOutputStream();

        for (final TypeRecord typeRecord : this.typeRecords) {
            typesStream.writeUnsignedByte(2 + (typeRecord.getRegIds().size() * 16));
            typesStream.writeUnsignedByte(typeRecord.getType());

            for (final byte[] regId : typeRecord.getRegIds()) {
                typesStream.writeBytes(regId);
            }
        }

        final byte[] types = typesStream.toByteArray();
        ipds.writeUnsignedInteger16(4 + types.length);
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        ipds.writeBytes(types);
    }

    /**
     * Returns the type records.
     */
    public List<TypeRecord> getTypeRecords() {
        return this.typeRecords;
    }

    /**
     * Sets the type records.
     */
    public void setTypeRecords(final List<TypeRecord> typeRecords) {
        this.typeRecords = typeRecords;
    }

    @Override
    public String toString() {
        return "ObjectContainerTypeSupportSelfDefiningField{"
                + "typeRecords=" + this.typeRecords
                + '}';
    }
}
