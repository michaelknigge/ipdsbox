package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.xohorders.UnknownXohOrderCode;
import de.textmode.ipdsbox.ipds.xohorders.XohOrder;
import de.textmode.ipdsbox.ipds.xohorders.XohOrderFactory;

/**
 * The Execute Order Home State (XOH) command identifies a set of orders that may be received
 * only when the printer is in home state.
 */
public final class ExecuteOrderHomeStateCommand extends IpdsCommand {

    private final XohOrder order;

    public ExecuteOrderHomeStateCommand(final XohOrder order) {
        super(IpdsCommandId.XOH);
        this.order = order;
    }

    /**
     * Constructs the {@link ExecuteOrderHomeStateCommand}.
     * @param ipds the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     * @throws UnknownXohOrderCode if the given IPDS data describes an unknown {@link XohOrder}.
     * @throws IOException if the given IPDS data is broken.
     */
    public ExecuteOrderHomeStateCommand(final IpdsByteArrayInputStream ipds)
        throws InvalidIpdsCommandException, UnknownXohOrderCode, IOException {
        super(ipds, IpdsCommandId.XOH);

        this.order = XohOrderFactory.create(ipds);
    }

    /**
     * Returns the {@link XohOrder} that carries all parameters and
     * information of this {@link ExecuteOrderHomeStateCommand}.
     *
     * @return a concrete {@link XohOrder} object.
     */
    public XohOrder getOrder() {
        return this.order;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        this.order.writeTo(ipds);
    }
}
