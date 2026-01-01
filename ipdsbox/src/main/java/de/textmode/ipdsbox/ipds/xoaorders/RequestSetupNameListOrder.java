package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;

/**
 * This class carries all parameters of the Activate Printer Alarm order.
 */
public final class RequestSetupNameListOrder extends XoaOrder {

    private int queryType;
    private int requestFlags;

    private final List<Triplet> triplets = new ArrayList<Triplet>();

    /**
     * Constructs the {@link RequestSetupNameListOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    RequestSetupNameListOrder(final IpdsByteArrayInputStream ipds) throws IOException {
        super(XoaOrderCode.RequestSetupNameList);

        this.queryType = ipds.readUnsignedByte();
        this.requestFlags = ipds.readUnsignedByte();

        ipds.skip(2);

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletFactory.create(buffer));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(this.getOrderCodeId());
        out.writeUnsignedByte(this.queryType);
        out.writeUnsignedByte(this.requestFlags);
        out.writeUnsignedInteger16(0);

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(out);
        }
    }

    /**
     * Returns the query type.
     */
    public int getQueryType() {
        return this.queryType;
    }

    /**
     * Sets the query type.
     */
    public void setQueryType(final int queryType) {
        this.queryType = queryType;
    }

    /**
     * Returns the request flags.
     */
    public int getRequestFlags() {
        return this.requestFlags;
    }

    /**
     * Sets the request flags.
     */
    public void setRequestFlags(final int requestFlags) {
        this.requestFlags = requestFlags;
    }

    /**
     * Returns a {@link List} of all {@link Triplet}s contained in the {@link RequestSetupNameListOrder}.
     */
    public List<Triplet> getTriplets() {
        return this.triplets;
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
        return "RequestSetupNameListOrder{"
                + "queryType=0x" + Integer.toHexString(this.queryType)
                + ", requestFlags=0x" + Integer.toHexString(this.requestFlags)
                + ", triplets=" + this.triplets
                + '}';
    }
}
