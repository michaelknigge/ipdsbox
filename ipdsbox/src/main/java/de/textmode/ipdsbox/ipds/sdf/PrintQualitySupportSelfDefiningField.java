package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Print-Quality Support self-defining field specifies the minimum values for print quality supported by the
 * printer. This field need not be returned by printers that have only one print quality.
 */
public final class PrintQualitySupportSelfDefiningField extends SelfDefiningField {

    private List<Integer> boundaries = new ArrayList<>();

    /**
     * Constructs a new {@link PrintQualitySupportSelfDefiningField}.
     */
    public PrintQualitySupportSelfDefiningField() {
        super(SelfDefiningFieldId.PrintQualitySupport);
    }

    /**
     * Constructs the {@link PrintQualitySupportSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    public PrintQualitySupportSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.PrintQualitySupport);

        while (ipds.bytesAvailable() > 0) {
            this.boundaries.add(ipds.readUnsignedInteger16());
        }
    }

    /**
     * Writes this {@link PrintQualitySupportSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + (this.boundaries.size() * 2));
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer boundary : this.boundaries) {
            out.writeUnsignedInteger16(boundary);
        }
    }

    /**
     * Returns a list containing all active boundaries.
     */
    public List<Integer> getBoundaries() {
        return this.boundaries;
    }

    /**
     * Sets a list containing all active boundaries.
     */
    public void setBoundaries(final List<Integer> boundaries) {
        this.boundaries = boundaries;
    }

    @Override
    public String toString() {
        return "PrintQualitySupportSelfDefiningField{"
                + "boundaries=" + this.boundaries
                + '}';
    }
}
