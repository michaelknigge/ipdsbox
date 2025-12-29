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
public class DeactiveteFontDeactivationTypesSupportedSelfDefiningField extends SelfDefiningField{

    private List<Integer> types = new ArrayList<>();

    /**
     * Constructs a new {@link DeactiveteFontDeactivationTypesSupportedSelfDefiningField}.
     */
    public DeactiveteFontDeactivationTypesSupportedSelfDefiningField() {
        super(SelfDefiningFieldId.DeactiveteFontDeactivationTypesSupported);
    }

    /**
     * Constructs a new {@link DeactiveteFontDeactivationTypesSupportedSelfDefiningField}
     * from the given {@link IpdsByteArrayInputStream}.
     */
    DeactiveteFontDeactivationTypesSupportedSelfDefiningField(
            final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.DeactiveteFontDeactivationTypesSupported);

        while (ipds.bytesAvailable() > 0) {
            this.types.add(ipds.readUnsignedByte());
        }
    }

    /**
     * Writes this {@link DeactiveteFontDeactivationTypesSupportedSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
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

    @Override
    public String toString() {
        return "DeactiveteFontDeactivationTypesSupportedSelfDefiningField{"
                + "types=" + this.types
                + '}';
    }
}
