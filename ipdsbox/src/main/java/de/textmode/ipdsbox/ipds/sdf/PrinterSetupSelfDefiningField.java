package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Printer Setup self-defining field lists all setup IDs that are currently active in the printer.
 */
public final class PrinterSetupSelfDefiningField extends SelfDefiningField {

    private List<Integer> setupIds = new ArrayList<>();

    /**
     * Constructs a new {@link PrinterSetupSelfDefiningField}.
     */
    public PrinterSetupSelfDefiningField() {
        super(SelfDefiningFieldId.PrinterSetup);
    }

    /**
     * Constructs the {@link PrinterSetupSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    PrinterSetupSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.PrinterSetup);

        while (ipds.bytesAvailable() > 0) {
            this.setupIds.add(ipds.readUnsignedInteger16());
        }
    }

    /**
     * Writes this {@link PrinterSetupSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + (this.setupIds.size() * 2));
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer operationType : this.setupIds) {
            out.writeUnsignedInteger16(operationType);
        }
    }

    /**
     * Returns a list containing all active Setup IDs.
     */
    public List<Integer> getSetupIds() {
        return this.setupIds;
    }

    /**
     * Sets a list containing all active Setup IDs.
     */
    public void setSetupIds(final List<Integer> setupIds) {
        this.setupIds = setupIds;
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
        return "PrinterSetupSelfDefiningField{"
                + "setupIds=" + this.setupIds
                + '}';
    }
}
