package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Product Identifier self-defining field is an optional field that specifies parameters that contain product
 * identification data. Each parameter is defined with a product-identifier parameter ID that specifies what the
 * subsequent product identifier describes.
 */
public final class ProductIdentifierSelfDefiningField extends SelfDefiningField {

    private List<ProductIdentifierEntry> entries = new ArrayList<>();

    /**
     * Creates a new {@link ProductIdentifierSelfDefiningField}.
     */
    public ProductIdentifierSelfDefiningField() {
        super(SelfDefiningFieldId.ProductIdentifier);
    }

    /**
     * Creates a {@link ProductIdentifierSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    ProductIdentifierSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.ProductIdentifier);

        while (ipds.bytesAvailable() > 0) {
            final int len = ipds.readUnsignedByte();
            final ProductIdentifierEntry entry = new ProductIdentifierEntry(
                    ipds.readUnsignedInteger16(),
                    ipds.readBytes(len - 3));

            this.entries.add(entry);
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        final IpdsByteArrayOutputStream temp = new IpdsByteArrayOutputStream();

        for (final ProductIdentifierEntry entry : this.entries) {
            temp.writeUnsignedByte(3 + entry.parameterValue.length);
            temp.writeUnsignedInteger16(entry.getParameterId());
            temp.writeBytes(entry.getParameterValue());
        }

        final byte[] params = temp.toByteArray();
        ipds.writeUnsignedInteger16(4 + params.length);
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        ipds.writeBytes(params);
    }

    /**
     * Returns the entries.
     */
    public List<ProductIdentifierEntry> getEntries() {
        return this.entries;
    }

    /**
     * Sets the entries.
     */
    public void setEntries(final List<ProductIdentifierEntry> entries) {
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
        return "ProductIdentifierSelfDefiningField{"
                + "entries=" + this.entries
                + '}';
    }

    public static final class ProductIdentifierEntry {
        private int parameterId;
        private byte[] parameterValue;

        public ProductIdentifierEntry(final int parameterId, final byte[] parameterValue) {
            this.parameterId = parameterId;
            this.parameterValue = parameterValue;
        }

        /**
         * Returns the Product-identifier parameter ID.
         */
        public int getParameterId() {
            return this.parameterId;
        }

        /**
         * Sets the Product-identifier parameter ID.
         */
        public void setParameterId(final int parameterId) {
            this.parameterId = parameterId;
        }

        /**
         * Returns the parameter value.
         */
        public byte[] getParameterValue() {
            return this.parameterValue;
        }

        /**
         * Sets the parameter value.
         */
        public void setParameterValue(final byte[] parameterValue) {
            this.parameterValue = parameterValue;
        }

        @Override
        public String toString() {
            return "ProductIdentifierEntry{"
                    + "parameterId=0x" + Integer.toHexString(this.parameterId)
                    + ", parameterValue=" + StringUtils.toHexString(this.parameterValue)
                    + '}';
        }
    }
}
