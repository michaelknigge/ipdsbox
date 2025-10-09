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
 * This class carries all parameters of the Deactivate Saved Page Group order.
 */
public final class DeactivateSavedPageGroupOrder extends XohOrder {

    private final List<Triplet> triplets = new ArrayList<>();

    /**
     * Constructs the {@link DeactivateSavedPageGroupOrder}.
     * @param ipds the raw IPDS data of the order.
     * @throws UnknownTripletException if the the IPDS data contains an unknown {@link Triplet}.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     * @throws IOException if the IPDS data is broken.
     * @throws InvalidIpdsCommandException if the IPDS data is invalid.
     */
    public DeactivateSavedPageGroupOrder(final IpdsByteArrayInputStream ipds)
        throws IOException, UnknownTripletException, UnknownXohOrderCode, InvalidIpdsCommandException {
        super(ipds, XohOrderCode.DeactivateSavedPageGroup);

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletBuilder.build(buffer));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.DeactivateSavedPageGroup.getValue());

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(out);
        }
    }

    /**
     * Returns a {@link List} of all {@link Triplet}s contained in the {@link DeactivateSavedPageGroupOrder}.
     * @return {@link List} of all {@link Triplet}s contained in the {@link DeactivateSavedPageGroupOrder}.
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
