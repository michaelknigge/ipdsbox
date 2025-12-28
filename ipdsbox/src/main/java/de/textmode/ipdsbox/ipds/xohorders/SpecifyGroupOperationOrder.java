package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Specify Group Operation order.
 */
public final class SpecifyGroupOperationOrder extends XohOrder {

    private int operation;
    private int groupLevel;

    /**
     * Constructs the {@link SpecifyGroupOperationOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    SpecifyGroupOperationOrder(final IpdsByteArrayInputStream ipds) throws UnknownXohOrderCode, IOException {
        super(ipds, XohOrderCode.SpecifyGroupOperation);

        this.operation = ipds.readUnsignedByte();
        this.groupLevel = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.SpecifyGroupOperation.getValue());
        out.writeUnsignedByte(this.operation);
        out.writeUnsignedByte(this.groupLevel);
    }

    /**
     * Returns the operation.
     * @return the operation.
     */
    public int getOperation() {
        return this.operation;
    }

    /**
     * Sets the operation.
     */
    public void setOperation(final int operation) {
        this.operation = operation;
    }

    /**
     * Returns the group level.
     */
    public int getGroupLevel() {
        return this.groupLevel;
    }

    /**
     * Sets the group level.
     */
    public void setGroupLevel(final int groupLevel) {
        this.groupLevel = groupLevel;
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }
}
