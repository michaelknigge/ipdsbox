package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This {@link UnknownXoaOrder} is created for all unknown {@link XoaOrder}.
 */
public final class UnknownXoaOrder extends XoaOrder {

    private final byte[] orderCodeData;

    /**
     * Constructs the {@link UnknownXoaOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    protected UnknownXoaOrder(final IpdsByteArrayInputStream ipds, final int orderCodeId) {
        super(orderCodeId);

        this.orderCodeData = ipds.readRemainingBytes();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(this.getOrderCodeId());
        out.writeBytes(this.orderCodeData);
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
        return "UnknownXoaOrder{" +
                "orderCode=0x" + Integer.toHexString(this.getOrderCodeId()) +
                ", orderCodeData=" + StringUtils.toHexString(this.orderCodeData) +
                '}';
    }
}
