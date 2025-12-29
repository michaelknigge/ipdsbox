package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * Constructs the {@link DefineGroupBoundaryOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    DefineGroupBoundaryOrder(final IpdsByteArrayInputStream ipds) throws IOException {

        super(XohOrderCode.DefineGroupBoundary);

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
     */
    public List<Triplet> getTriplets() {
        return this.triplets;
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public String toString() {
        return "DefineGroupBoundaryOrder{" +
                "orderType=0x" + Integer.toHexString(this.orderType) +
                ", groupLevel=" + this.groupLevel +
                ", triplets=" + this.triplets +
                '}';
    }
}
