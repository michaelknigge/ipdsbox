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
    public ObtainPrinterCharacteristicsOrder() throws UnknownXohOrderCode, IOException {
        super(XohOrderCode.ObtainPrinterCharacteristics);
    }

    /**
     * Constructs the {@link ObtainPrinterCharacteristicsOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    ObtainPrinterCharacteristicsOrder(final IpdsByteArrayInputStream ipds) throws UnknownXohOrderCode, IOException {
        super(ipds, XohOrderCode.ObtainPrinterCharacteristics);
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
}
