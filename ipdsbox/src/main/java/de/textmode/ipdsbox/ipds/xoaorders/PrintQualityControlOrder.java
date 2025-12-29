package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Print-Quality Control order.
 */
public final class PrintQualityControlOrder extends XoaOrder {

    private int qualityLevel;

    /**
     * Constructs the {@link PrintQualityControlOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    PrintQualityControlOrder(final IpdsByteArrayInputStream ipds) throws IOException {
        super(XoaOrderCode.PrintQualityControl);

        this.qualityLevel = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.PrintQualityControl.getValue());
        out.writeUnsignedByte(this.qualityLevel);
    }

    /**
     * Returns the quality level.
     */
    public int getQualityLevel() {
        return this.qualityLevel;
    }

    /**
     * Sets the quality level.
     */
    public void setQualityLevel(final int qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }
}
