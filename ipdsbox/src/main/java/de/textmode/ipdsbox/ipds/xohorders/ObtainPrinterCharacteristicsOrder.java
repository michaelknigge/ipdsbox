package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Obtain Printer Characteristics order.
 */
public final class ObtainPrinterCharacteristicsOrder extends XohOrder {

    /**
     * Constructs the {@link ObtainPrinterCharacteristicsOrder}.
     */
    public ObtainPrinterCharacteristicsOrder() {
        super(XohOrderCode.ObtainPrinterCharacteristics);
    }

    /**
     * Constructs the {@link ObtainPrinterCharacteristicsOrder}.
     */
    ObtainPrinterCharacteristicsOrder(final IpdsByteArrayInputStream ipds) {
        super(XohOrderCode.ObtainPrinterCharacteristics);
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.ObtainPrinterCharacteristics.getValue());
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public String toString() {
        return "ObtainPrinterCharacteristicsOrder{}";
    }
}
