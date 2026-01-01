package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.xoaorders.XoaOrder;
import de.textmode.ipdsbox.ipds.xoaorders.XoaOrderFactory;

/**
 * The Execute Order Anystate (XOA) command identifies a set of orders that take effect immediately, regardless
 * of the current command state of the printer. This command is valid in any printer state.
 */
public final class ExecuteOrderAnyStateCommand extends IpdsCommand {

    private final XoaOrder order;

    /**
     * Constructs a {@link ExecuteOrderAnyStateCommand} with the given {@link XoaOrder}.
     */
    public ExecuteOrderAnyStateCommand(final XoaOrder order) {
        super(IpdsCommandId.XOA);
        this.order = order;
    }

    /**
     * Constructs the {@link ExecuteOrderAnyStateCommand} from the given {@link IpdsByteArrayInputStream}.
     */
    ExecuteOrderAnyStateCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.XOA);

        this.order = XoaOrderFactory.create(ipds);
    }

    /**
     * Returns the {@link XoaOrder} that carries all parameters and
     * information of this {@link ExecuteOrderAnyStateCommand}.
     */
    public XoaOrder getOrder() {
        return this.order;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        this.order.writeTo(ipds);
    }

    @Override
    public String toString() {
        return "ExecuteOrderAnyStateCommand{"
                + "order=" + this.order
                + '}';
    }
}
