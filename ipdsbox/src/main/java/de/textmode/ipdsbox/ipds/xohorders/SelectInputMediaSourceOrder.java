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
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public SelectInputMediaSourceOrder(final byte[] data) throws UnknownXohOrderCode, IOException {
        super(data, XohOrderCode.SelectInputMediaSource);

        final IpdsByteArrayInputStream stream = new IpdsByteArrayInputStream(data);
        stream.skip(2);

        this.sourceId = stream.readUnsignedByte();
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
        return sourceId;
    }

    /**
     * Sets the source ID.
     */
    public void setSourceId(int sourceId) {
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
