package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Standard OCA Color Value Support self-defining field specifies the set of Standard OCA color values that
 * are supported by the printer. This self-defining field has no meaning for other color spaces.
 */
public class StandardOcaColorValueSupportSelfDefiningField extends SelfDefiningField {

    private List<Integer> colorValues = new ArrayList<>();

    /**
     * Constructs the {@link StandardOcaColorValueSupportSelfDefiningField}.
     */
    public StandardOcaColorValueSupportSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.StandardOcaColorValueSupport);
    }

    /**
     * Constructs the {@link StandardOcaColorValueSupportSelfDefiningField}.
     */
    public StandardOcaColorValueSupportSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.StandardOcaColorValueSupport);

        while (ipds.bytesAvailable() > 0) {
            this.colorValues.add(ipds.readUnsignedInteger16());
        }
    }

    /**
     * Writes this {@link StandardOcaColorValueSupportSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + (this.colorValues.size() * 2));
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer id : this.colorValues) {
            out.writeUnsignedInteger16(id);
        }
    }

    /**
     * Returns a list containing all supported OCA colors
     */
    public List<Integer> getColorValues() {
        return this.colorValues;
    }

    /**
     * Sets a list containing all supported OCA colors.
     */
    public void setColorValues(final List<Integer> colorValues) {
        this.colorValues = colorValues;
    }

    @Override
    public String toString() {
        return "StandardOcaColorValueSupportSelfDefiningField{" +
                "colorValues=" + this.colorValues +
                '}';
    }
}
