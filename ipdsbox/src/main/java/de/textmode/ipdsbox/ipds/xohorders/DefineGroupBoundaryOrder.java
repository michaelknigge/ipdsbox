package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletBuilder;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

/**
 * This class carries all parameters of the Define Group Boundary order.
 */
public final class DefineGroupBoundaryOrder extends XohOrder {

    private final List<Triplet> triplets = new ArrayList<>();
    private final boolean initiateGroup;
    private final int groupLevel;

    /**
     * Constructs the {@link DefineGroupBoundaryOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownTripletException if the the IPDS data contains an unknown {@link Triplet}.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     * @throws IOException if the IPDS data is broken.
     * @throws InvalidIpdsCommandException if the order contains invalid data.
     */
    public DefineGroupBoundaryOrder(final byte[] data)
        throws UnknownXohOrderCode, IOException, UnknownTripletException, InvalidIpdsCommandException {

        super(data, XohOrderCode.DefineGroupBoundary);
        final IpdsByteArrayInputStream stream = new IpdsByteArrayInputStream(data);
        stream.skip(2);

        final int orderType = stream.readByte();
        if (orderType == 0x00) {
            this.initiateGroup = true;
        } else if (orderType == 0x01) {
            this.initiateGroup = false;
        } else {
            throw new InvalidIpdsCommandException(
                String.format("The byte at offset 2 of the XOH order DGB should be X'00' or X'01' but it is X'%1$s'",
                    Integer.toHexString(orderType)));
        }

        this.groupLevel = stream.readByte();

        byte[] buffer;
        while ((buffer = stream.readTripletIfExists()) != null) {
            this.triplets.add(TripletBuilder.build(buffer));
        }
    }

    /**
     * Returns <code>true</code> if and only this order initiates a group.
     * @return <code>true</code> if and only this order initiates a group.
     */
    public boolean isInitiateGroup() {
        return this.initiateGroup;
    }

    /**
     * Returns <code>true</code> if and only this order terminates a group.
     * @return <code>true</code> if and only this order terminates a group.
     */
    public boolean isTerminateGroup() {
        return !this.initiateGroup;
    }

    /**
     * Returns the hierarchical level of the group.
     * @return Level of the group, which is a value between X'00' and x'FF'.
     */
    public int getGroupLevel() {
        return this.groupLevel;
    }

    /**
     * Returns a {@link List} of all {@link Triplet}s contained in the {@link DefineGroupBoundaryOrder}.
     * @return {@link List} of all {@link Triplet}s contained in the {@link DefineGroupBoundaryOrder}.
     */
    public List<Triplet> getTriplets() {
        return this.triplets;
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
