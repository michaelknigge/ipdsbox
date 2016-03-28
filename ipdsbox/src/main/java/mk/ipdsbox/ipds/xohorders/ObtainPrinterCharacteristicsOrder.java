package mk.ipdsbox.ipds.xohorders;

/**
 * This class carries all parameters of the Obtain Printer Characteristics order.
 */
public final class ObtainPrinterCharacteristicsOrder extends XohOrder {

    /**
     * Constructs the {@link ObtainPrinterCharacteristicsOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public ObtainPrinterCharacteristicsOrder(final byte[] data) throws UnknownXohOrderCode {
        super(data, XohOrderCode.ObtainPrinterCharacteristics);
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     * @param visitor the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }
}
