package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;
import de.textmode.ipdsbox.ipds.xoaorders.UnknownXoaOrderCode;
import de.textmode.ipdsbox.ipds.xoaorders.XoaOrder;
import de.textmode.ipdsbox.ipds.xoaorders.XoaOrderBuilder;
import de.textmode.ipdsbox.ipds.xohorders.UnknownXohOrderCode;
import de.textmode.ipdsbox.ipds.xohorders.XohOrder;

/**
 * The Execute Order Anystate (XOA) command identifies a set of orders that take effect immediately, regardless
 * of the current command state of the printer. This command is valid in any printer state.
 */
public final class ExecuteOrderAnystateCommand extends IpdsCommand {

    private final XoaOrder order;

    public ExecuteOrderAnystateCommand(final XoaOrder order) {
        super(IpdsCommandId.XOA);
        this.order = order;
    }

    /**
     * Constructs the {@link ExecuteOrderAnystateCommand}.
     * @param ipds the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     * @throws UnknownXohOrderCode if the given IPDS data describes an unknown {@link XohOrder}.
     * @throws UnknownTripletException if the given IPDS data describes an unknown {@link Triplet}.
     * @throws IOException if the given IPDS data is broken.
     */
    public ExecuteOrderAnystateCommand(final IpdsByteArrayInputStream ipds)
            throws InvalidIpdsCommandException, UnknownXohOrderCode, IOException, UnknownTripletException, UnknownXoaOrderCode {
        super(ipds, IpdsCommandId.XOA);

        this.order = XoaOrderBuilder.build(ipds);
    }

    /**
     * Returns the {@link XoaOrder} that carries all parameters and
     * information of this {@link ExecuteOrderAnystateCommand}.
     *
     * @return a concrete {@link XoaOrder} object.
     */
    public XoaOrder getOrder() {
        return this.order;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        this.order.writeTo(ipds);
    }
}
