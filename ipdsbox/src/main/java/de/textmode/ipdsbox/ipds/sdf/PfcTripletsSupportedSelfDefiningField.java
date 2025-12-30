package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The PFC Triplets Supported self-defining field lists the optional triplets that are supported by the printer on the
 * Presentation Fidelity Control command. If the PFC command is supported by a printer, this self-defining field
 * must be returned in the XOH-OPC reply.
 */
public final class PfcTripletsSupportedSelfDefiningField extends SelfDefiningField {

    private List<Integer> tripletIds = new ArrayList<>();

    /**
     * Constructs the {@link PfcTripletsSupportedSelfDefiningField}.
     */
    public PfcTripletsSupportedSelfDefiningField() {
        super(SelfDefiningFieldId.PfcTripletsSupported);
    }

    /**
     * Constructs the {@link PfcTripletsSupportedSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    PfcTripletsSupportedSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.PfcTripletsSupported);

        while (ipds.bytesAvailable() > 0) {
            this.tripletIds.add(ipds.readUnsignedByte());
        }
    }

    /**
     * Writes this {@link PfcTripletsSupportedSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + this.tripletIds.size());
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer id : this.tripletIds) {
            out.writeUnsignedByte(id);
        }
    }

    /**
     * Returns a list with all triplet IDs.
     */
    public List<Integer> getTripletIds() {
        return this.tripletIds;
    }

    /**
     * Sets a list with all triplet IDs.
     */
    public void setTripletIds(final List<Integer> tripletIds) {
        this.tripletIds = tripletIds;
    }

    @Override
    public String toString() {
        return "PfcTripletsSupportedSelfDefiningField{"
                + "tripletIds=" + this.tripletIds
                + '}';
    }
}
