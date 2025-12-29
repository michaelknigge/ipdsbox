package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field specifies the group operations supported by a printer, pre-processor, or post-processor
 * in the XOH Specify Group Operation command. If this self-defining field is returned, the printer must also return
 * the XOH-DGB-supported property pair (X'9004') and the XOH-SGO-supported property pair (X'9003') in the
 * Device-Control command-set vector of an STM reply.
 */
public class SupportedGroupOperationsSelfDefiningField extends SelfDefiningField{

    private List<Integer> operationTypes = new ArrayList<>();

    /**
     * Constructs the {@link SupportedGroupOperationsSelfDefiningField}.
     */
    public SupportedGroupOperationsSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.SupportedGroupOperations);
    }

    /**
     * Constructs the {@link SupportedGroupOperationsSelfDefiningField}.
     */
    public SupportedGroupOperationsSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.SupportedGroupOperations);

        while (ipds.bytesAvailable() > 0) {
            this.operationTypes.add(ipds.readUnsignedByte());
        }
    }

    /**
     * Writes this {@link SupportedGroupOperationsSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
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
        return "SupportedGroupOperationsSelfDefiningField{"
                + "operationTypes=" + this.operationTypes
                + '}';
    }
}
