package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;

/**
 * This class carries all parameters of the Define Group Boundary order.
 */
public final class DefineGroupBoundaryOrder extends XohOrder {

    public static int INITIATE_GROUP = 0x00;
    public static int TERMINATE_GROUP = 0x01;

    private int orderType;
    private int groupLevel;
    private final List<Triplet> triplets = new ArrayList<>();

    public DefineGroupBoundaryOrder(final int orderType, final int groupLevel) {
        super(XohOrderCode.DefineGroupBoundary);

        this.orderType = orderType;
        this.groupLevel = groupLevel;
    }

    /**
     * Constructs the {@link DefineGroupBoundaryOrder}.
     *
     * @param ipds the raw IPDS data of the order.
     *
     * @throws UnknownTripletException if the the IPDS data contains an unknown {@link Triplet}.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     * @throws IOException if the IPDS data is broken.
     * @throws InvalidIpdsCommandException if the order contains invalid data.
     */
    public DefineGroupBoundaryOrder(final IpdsByteArrayInputStream ipds)
        throws UnknownXohOrderCode, IOException,InvalidIpdsCommandException {

        super(ipds, XohOrderCode.DefineGroupBoundary);

        this.orderType = ipds.readByte();
        this.groupLevel = ipds.readByte();

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletFactory.create(buffer));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.DefineGroupBoundary.getValue());
        out.writeUnsignedByte(this.orderType);
        out.writeUnsignedByte(this.groupLevel);

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(out);
        }
    }

    /**
     * Returns the order type.
     * @return the order type.
     */
    public int getOrderType() {
        return this.orderType;
    }

    /**
     * Sets the order type.
     */
    public void setOrderType(final int orderType) {
        this.orderType = orderType;
    }

    /**
     * Returns the group level.
     * @return the group level.
     */
    public int getGroupLevel() {
        return this.groupLevel;
    }

    /**
     * Sets the group level.
     */
    public void setGroupLevel(final int groupLevel) {
        this.groupLevel = groupLevel;
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
