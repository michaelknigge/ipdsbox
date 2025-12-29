package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.SetupNameTriplet;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;
import de.textmode.ipdsbox.ipds.triplets.TripletId;

/**
 * This self-defining field reports the active setup name on the printer, if any. If there is an active setup name, it
 * is returned using a Setup Name (X'9E') triplet in the active-setup-name field. If there is no active setup name, the
 * SDF-length field is returned as X'0004' and the active-setup-name field is omitted.
 */
public final class ActiveSetupNameSelfDefiningField extends SelfDefiningField {

    private SetupNameTriplet activeSetupName = null;

    /**
     * Creates a new {@link ActiveSetupNameSelfDefiningField}.
     */
    public ActiveSetupNameSelfDefiningField() {
        super(SelfDefiningFieldId.ActiveSetupName);
    }

    /**
     * Creates a {@link ActiveSetupNameSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    ActiveSetupNameSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.ActiveSetupName);

        if (ipds.bytesAvailable() > 0) {
            final Triplet triplet = TripletFactory.create(ipds.readTripletIfExists());

            // Should be a Setup Name Triplet. If not, ignore...
            if (triplet.getTripletId() == TripletId.SetupName.getId()) {
                this.activeSetupName = (SetupNameTriplet) triplet;
            }
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {

        if (this.activeSetupName == null) {
            ipds.writeUnsignedInteger16(4);
            ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        } else {
            final IpdsByteArrayOutputStream tripletStream = new IpdsByteArrayOutputStream();
            this.activeSetupName.writeTo(tripletStream);

            final byte[] tripletBytes = tripletStream.toByteArray();

            ipds.writeUnsignedInteger16(4 + tripletBytes.length);
            ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
            ipds.writeBytes(tripletBytes);
        }
    }

    /**
     * Returns the active setup name.
     */
    public SetupNameTriplet getActiveSetupName() {
        return this.activeSetupName;
    }

    /**
     * Sets the active setup name.
     */
    public void setActiveSetupName(final SetupNameTriplet activeSetupName) {
        this.activeSetupName = activeSetupName;
    }

    @Override
    public String toString() {
        return "ActiveSetupNameSelfDefiningField{"
                + "activeSetupName=" + this.activeSetupName
                + '}';
    }
}
