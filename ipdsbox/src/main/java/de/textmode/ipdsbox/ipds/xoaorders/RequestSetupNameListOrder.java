package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletBuilder;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

/**
 * This class carries all parameters of the Activate Printer Alarm order.
 */
public final class RequestSetupNameListOrder extends XoaOrder {

    private int queryType;
    private int requestFlags;

    private final List<Triplet> triplets = new ArrayList<Triplet>();

    /**
     * Constructs the {@link RequestSetupNameListOrder}.
     *
     * @param ipds the raw IPDS data of the order.
     *
     * @throws UnknownXoaOrderCode if the the IPDS data contains an unknown {@link XoaOrderCode}.
     */
    public RequestSetupNameListOrder(final IpdsByteArrayInputStream ipds) throws UnknownXoaOrderCode, IOException, UnknownTripletException, InvalidIpdsCommandException {
        super(ipds, XoaOrderCode.RequestSetupNameList);

        this.queryType = ipds.readUnsignedByte();
        this.requestFlags = ipds.readUnsignedByte();

        ipds.skip(2);

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletBuilder.build(buffer));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.RequestSetupNameList.getValue());
        out.writeUnsignedByte(this.queryType);
        out.writeUnsignedByte(this.requestFlags);
        out.writeUnsignedInteger16(0);

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(out);
        }
    }

    /**
     * Returns the quety type.
     */
    public int getQueryType() {
        return this.queryType;
    }

    /**
     * Sets the quety type.
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
     * @return {@link List} of all {@link Triplet}s contained in the {@link RequestSetupNameListOrder}.
     */
    public List<Triplet> getTriplets() {
        return this.triplets;
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     * @param visitor the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }
}
