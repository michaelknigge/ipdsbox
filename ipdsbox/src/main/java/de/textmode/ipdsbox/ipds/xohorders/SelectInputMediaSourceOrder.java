package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Select Input Media Source order.
 */
public final class SelectInputMediaSourceOrder extends XohOrder {

    private int sourceId;

    /**
     * Constructs the {@link SelectInputMediaSourceOrder}.
     * @param ipds the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SelectInputMediaSourceOrder(final IpdsByteArrayInputStream ipds) throws UnknownXohOrderCode, IOException {
        super(ipds, XohOrderCode.SelectInputMediaSource);

        this.sourceId = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.SelectInputMediaSource.getValue());
        out.writeUnsignedByte(this.sourceId);
    }

    /**
     * Returns the source ID.
     * @return the source ID.
     */
    public int getSourceId() {
        return this.sourceId;
    }

    /**
     * Sets the source ID.
     */
    public void setSourceId(final int sourceId) {
        this.sourceId = sourceId;
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
