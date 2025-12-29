package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class TonerSaverTriplet extends Triplet {

    private int control;

    /**
     * Constructs a {@link TonerSaverTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    TonerSaverTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.TonerSaver);

        ipds.skip(1);
        this.control = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(6);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedByte(0);
        out.writeUnsignedByte(this.control);
        out.writeUnsignedInteger16(0);
    }

    /**
     * Returns the Control.
     */
    public int getControl() {
        return this.control;
    }

    /**
     * Sets the Control.
     */
    public void setControl(final int control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return "TonerSaver{"
                + "tid=0x" + Integer.toHexString(this.getTripletId())
                + ", control=0x" + Integer.toHexString(this.control)
                + '}';
    }
}
