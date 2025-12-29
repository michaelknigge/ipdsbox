package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Common Bar Code Type/Modifier self-defining field lists those bar codes that are supported by the printer.
 */
public class CommonBarCodeTypeSelfDefiningField extends SelfDefiningField{

    private List<Integer> combinations = new ArrayList<>();

    /**
     * Constructs a new {@link CommonBarCodeTypeSelfDefiningField}.
     */
    public CommonBarCodeTypeSelfDefiningField() {
        super(SelfDefiningFieldId.CommonBarCodeType);
    }

    /**
     * Constructs a new {@link CommonBarCodeTypeSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    CommonBarCodeTypeSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.CommonBarCodeType);

        while (ipds.bytesAvailable() > 0) {
            this.combinations.add(ipds.readUnsignedByte());
        }
    }

    /**
     * Writes this {@link CommonBarCodeTypeSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + (this.combinations.size()));
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer combination : this.combinations) {
            out.writeUnsignedByte(combination);
        }
    }

    /**
     * Returns a list containing all active boundaries.
     */
    public List<Integer> getCombinations() {
        return this.combinations;
    }

    /**
     * Sets a list containing all active boundaries.
     */
    public void setCombinations(final List<Integer> combinations) {
        this.combinations = combinations;
    }

    @Override
    public String toString() {
        return "CommonBarCodeTypeSelfDefiningField{"
                + "combinations=" + this.combinations
                + '}';
    }
}
