package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Page Counters Control order.
 */
public final class PageCountersControlOrder extends XohOrder {

    private int counterUpdate;

    /**
     * Constructs the {@link PageCountersControlOrder}.
     * @param ipds the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public PageCountersControlOrder(final IpdsByteArrayInputStream ipds) throws UnknownXohOrderCode, IOException {
        super(ipds, XohOrderCode.PageCountersControl);

        this.counterUpdate = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.PageCountersControl.getValue());
        out.writeUnsignedByte(this.counterUpdate);
    }

    /**
     * Returns the counter update.
     * @return the counter update.
     */
    public int getCounterUpdate() {
        return this.counterUpdate;
    }

    /**
     * Sets the counter update.
     */
    public void setCounterUpdate(final int counterUpdate) {
        this.counterUpdate = counterUpdate;
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
