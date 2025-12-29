package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;

/**
 * This class carries all parameters of the Remove Saved Page Group order.
 */
public final class RemoveSavedPageGroupOrder extends XohOrder {

    private final List<Triplet> triplets = new ArrayList<>();

    /**
     * Constructs the {@link RemoveSavedPageGroupOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    RemoveSavedPageGroupOrder(final IpdsByteArrayInputStream ipds) throws IOException {

        super(XohOrderCode.RemoveSavedGroup);

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletFactory.create(buffer));
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
        return "RemoveSavedPageGroupOrder{" +
                "triplets=" + this.triplets +
                '}';
    }
}
