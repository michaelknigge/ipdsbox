package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Select Medium Modifications order.
 */
public final class SelectMediumModificationsOrder extends XohOrder {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private final List<MediumModification> modifications = new ArrayList<>();

    /**
     * Constructs the {@link SelectMediumModificationsOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SelectMediumModificationsOrder(final byte[] data) throws UnknownXohOrderCode, IOException {
        super(data, XohOrderCode.SelectMediumModifications);

        final IpdsByteArrayInputStream stream = new IpdsByteArrayInputStream(data);
        stream.skip(2);

        stream.skip(8);

        while (stream.bytesAvailable() > 0) {
            this.modifications.add(new MediumModification(stream));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.SelectMediumModifications.getValue());

        out.writeUnsignedInteger32(0x00);
        out.writeUnsignedInteger32(0x00);

        for (final MediumModification mod : this.modifications) {
            mod.writeTo(out);
        }
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     * @param visitor the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }

    public final class MediumModification {

        private int type;
        private int modificationId;
        private byte[] modificationParameters;

        public MediumModification(final int type, final int modificationId) throws IOException {
            this.type = type;
            this.modificationId = modificationId;
            this.modificationParameters = EMPTY_BYTE_ARRAY;
        }

        public MediumModification(final IpdsByteArrayInputStream in) throws IOException {
            final int len = in.readUnsignedInteger16();
            this.type = in.readUnsignedByte();
            this.modificationId = in.readUnsignedInteger16();
            this.modificationParameters = in.readBytes(len - 5);
        }

        /**
         * Returns the type.
         * @return the type.
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
         * Returns the modification ID.
         * @return the modification ID.
         */
        public int getModificationId() {
            return this.modificationId;
        }

        /**
         * Sets the modification ID.
         */
        public void setModificationId(final int modificationId) {
            this.modificationId = modificationId;
        }

        /**
         * Returns the modification parameters.
         * @return the modification parameters.
         */
        public byte[] getModificationParameters() {
            return this.modificationParameters;
        }

        /**
         * Sets the modification parameters.
         */
        public void setModificationParameters(final byte[] modificationParameters) {
            this.modificationParameters = modificationParameters;
        }

        /**
         * Writes this {@link MediumModification} to the given {@link IpdsByteArrayOutputStream}.
         */

        public void writeTo(IpdsByteArrayOutputStream out) throws IOException {
            out.writeUnsignedInteger16(
                    5 + (this.modificationParameters == null ? 0 : this.modificationParameters.length) );

            out.writeUnsignedByte(this.type);
            out.writeUnsignedInteger16(this.modificationId);

            if (this.modificationParameters != null) {
                out.writeBytes(this.modificationParameters);
            }
        }
    }
}
