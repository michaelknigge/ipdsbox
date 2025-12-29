package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This {@link UnknownXohOrder} is created for all unknown {@link XohOrder}.
 */
public final class UnknownXohOrder extends XohOrder {

    private final byte[] orderCodeData;

    /**
     * Constructs the {@link UnknownXohOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    UnknownXohOrder(final IpdsByteArrayInputStream ipds, final int orderCodeId) {
        super(orderCodeId);

        this.orderCodeData = ipds.readRemainingBytes();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(this.getOrderCodeId());
        out.writeBytes(this.orderCodeData);
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }
}
