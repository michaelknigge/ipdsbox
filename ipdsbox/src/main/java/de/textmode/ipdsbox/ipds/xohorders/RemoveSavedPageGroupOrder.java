package de.textmode.ipdsbox.ipds.xohorders;

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
 * This class carries all parameters of the Remove Saved Page Group order.
 */
public final class RemoveSavedPageGroupOrder extends XohOrder {

    private final List<Triplet> triplets = new ArrayList<>();

    /**
     * Constructs the {@link RemoveSavedPageGroupOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public RemoveSavedPageGroupOrder(final byte[] data)
            throws UnknownXohOrderCode, IOException, UnknownTripletException, InvalidIpdsCommandException {

        super(data, XohOrderCode.RemoveSavedGroup);

        final IpdsByteArrayInputStream stream = new IpdsByteArrayInputStream(data);
        stream.skip(2);

        byte[] buffer;
        while ((buffer = stream.readTripletIfExists()) != null) {
            this.triplets.add(TripletBuilder.build(buffer));
        }

    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.RemoveSavedGroup.getValue());

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(out);
        }
    }

    /**
     * Returns a {@link List} of all {@link Triplet}s contained in the {@link RemoveSavedPageGroupOrder}.
     * @return {@link List} of all {@link Triplet}s contained in the {@link RemoveSavedPageGroupOrder}.
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
