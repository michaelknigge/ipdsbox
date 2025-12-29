package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Control Edge Marks order.
 */
public final class ControlEdgeMarksOrder extends XoaOrder {

    private int edgeMark;

    /**
     * Constructs the {@link ControlEdgeMarksOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    ControlEdgeMarksOrder(final IpdsByteArrayInputStream ipds) throws IOException {
        super(XoaOrderCode.ControlEdgeMarks);

        this.edgeMark = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.ControlEdgeMarks.getValue());
        out.writeUnsignedByte(this.edgeMark);
    }

    /**
     * Returns the edge mark.
     */
    public int getEdgeMark() {
        return this.edgeMark;
    }

    /**
     * Sets the edge mark.
     */
    public void setEdgeMark(final int edgeMark) {
        this.edgeMark = edgeMark;
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public String toString() {
        return "ControlEdgeMarksOrder{" +
                "edgeMark=0x" + Integer.toHexString(this.edgeMark) +
                '}';
    }
}
