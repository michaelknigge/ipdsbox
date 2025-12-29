package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Finishing Options self-defining field lists all the finishing options that the printer
 * supports with the Finishing Operation (X'85') triplet.
 */
public class FinishingOptionsSelfDefiningField extends SelfDefiningField {

    private List<Integer> optionTypes = new ArrayList<>();

    /**
     * Constructs a new {@link FinishingOptionsSelfDefiningField}.
     */
    public FinishingOptionsSelfDefiningField() {
        super(SelfDefiningFieldId.FinishingOptions);
    }

    /**
     * Constructs a new {@link FinishingOptionsSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    public FinishingOptionsSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.FinishingOptions);

        while (ipds.bytesAvailable() > 0) {
            this.optionTypes.add(ipds.readUnsignedByte());
        }
    }

    /**
     * Writes this {@link FinishingOptionsSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + this.optionTypes.size());
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer optionType : this.optionTypes) {
            out.writeUnsignedByte(optionType);
        }
    }

    /**
     * Returns a list with all option types.
     */
    public List<Integer> getOptionTypes() {
        return this.optionTypes;
    }

    /**
     * Sets a list with all option types. Can be used to add and remove option types.
     */
    public void setOptionTypes(final List<Integer> optionTypes) {
        this.optionTypes = optionTypes;
    }

    @Override
    public String toString() {
        return "FinishingOptionsSelfDefiningField{"
                + "optionTypes=" + this.optionTypes
                + '}';
    }
}
