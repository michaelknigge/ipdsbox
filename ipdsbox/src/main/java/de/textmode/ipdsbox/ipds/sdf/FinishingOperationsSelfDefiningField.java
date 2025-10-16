package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Finishing Operations self-defining field lists all the different types of finishing operations that the printer
 * supports with the Finishing Operation (X'85') triplet.
 */
public class FinishingOperationsSelfDefiningField extends SelfDefiningField{

    private List<Integer> operationTypes = new ArrayList<>();

    /**
     * Constructs the {@link FinishingOperationsSelfDefiningField}.
     */
    public FinishingOperationsSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.FinishingOperations);
    }

    /**
     * Constructs the {@link FinishingOperationsSelfDefiningField}.
     */
    public FinishingOperationsSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.FinishingOperations);

        while (ipds.bytesAvailable() > 0) {
            this.operationTypes.add(ipds.readUnsignedByte());
        }
    }

    /**
     * Writes this {@link FinishingOperationsSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + this.operationTypes.size());
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer operationType : this.operationTypes) {
            out.writeUnsignedByte(operationType);
        }
    }

    /**
     * Returns a list with all operation types.
     */
    public List<Integer> getOperationTypes() {
        return this.operationTypes;
    }

    /**
     * Sets a list with all operation types. Can be used to add and remove operation types.
     */
    public void setOperationTypes(final List<Integer> operationTypes) {
        this.operationTypes = operationTypes;
    }

    @Override
    public String toString() {
        return "FinishingOperationsSelfDefiningField{" +
                "operationTypes=" + this.operationTypes +
                '}';
    }
}
