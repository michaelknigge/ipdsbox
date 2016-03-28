package mk.ipdsbox.ipds.commands;

import java.io.IOException;

import mk.ipdsbox.core.InvalidIpdsCommandException;
import mk.ipdsbox.ipds.triplets.Triplet;
import mk.ipdsbox.ipds.triplets.UnknownTripletException;
import mk.ipdsbox.ipds.xohorders.UnknownXohOrderCode;
import mk.ipdsbox.ipds.xohorders.XohOrder;
import mk.ipdsbox.ipds.xohorders.XohOrderBuilder;

/**
 * The Execute Order Home State (XOH) command identifies a set of orders that may be received
 * only when the printer is in home state.
 */
public final class ExecuteOrderHomeStateCommand extends IpdsCommand {

    private final XohOrder order;

    /**
     * Constructs the {@link ExecuteOrderHomeStateCommand}.
     * @param command the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     * @throws UnknownXohOrderCode if the given IPDS data describes an unknown {@link XohOrder}.
     * @throws UnknownTripletException if the given IPDS data describes an unknown {@link Triplet}.
     * @throws IOException if the given IPDS data is broken.
     */
    public ExecuteOrderHomeStateCommand(final byte[] command)
        throws InvalidIpdsCommandException, UnknownXohOrderCode, IOException, UnknownTripletException {
        super(command, IpdsCommandId.XOH);

        this.order = XohOrderBuilder.build(this.getData());
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
}
