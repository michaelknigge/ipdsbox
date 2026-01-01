package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The DF Deactivation Types Supported self-defining field lists the optional deactivation types that are supported
 * by the printer.
 */
public final class DeactivateFontDeactivationTypesSupportedSelfDefiningField extends SelfDefiningField {

    private List<Integer> types = new ArrayList<>();

    /**
     * Constructs a new {@link DeactivateFontDeactivationTypesSupportedSelfDefiningField}.
     */
    public DeactivateFontDeactivationTypesSupportedSelfDefiningField() {
        super(SelfDefiningFieldId.DeactivateFontDeactivationTypesSupported);
    }

    /**
     * Constructs a new {@link DeactivateFontDeactivationTypesSupportedSelfDefiningField}
     * from the given {@link IpdsByteArrayInputStream}.
     */
    DeactivateFontDeactivationTypesSupportedSelfDefiningField(
            final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.DeactivateFontDeactivationTypesSupported);

        while (ipds.bytesAvailable() > 0) {
            this.types.add(ipds.readUnsignedByte());
        }
    }

    /**
     * Writes this {@link DeactivateFontDeactivationTypesSupportedSelfDefiningField}
     * to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + this.types.size());
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer type : this.types) {
            out.writeUnsignedByte(type);
        }
    }

    /**
     * Returns a list with all  types.
     */
    public List<Integer> getTypes() {
        return this.types;
    }

    /**
     * Sets a list with all  types. Can be used to add and remove types.
     */
    public void setTypes(final List<Integer> types) {
        this.types = types;
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
        return "DeactivateFontDeactivationTypesSupportedSelfDefiningField{"
                + "types=" + this.types
                + '}';
    }
}
