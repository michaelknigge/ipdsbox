package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class DeviceAppearanceTriplet extends Triplet {

    private int appearance;

    /**
     * Constructs a {@link DeviceAppearanceTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    DeviceAppearanceTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.DeviceAppearance);

        ipds.skip(1);
        this.appearance = ipds.readUnsignedInteger16();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(7);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedByte(0);
        out.writeUnsignedInteger16(this.appearance);
        out.writeUnsignedInteger16(0x0000);
    }

    /**
     * Returns the appearance.
     */
    public int getAppearance() {
        return this.appearance;
    }

    /**
     * Sets the appearance.
     */
    public void setAppearance(final int appearance) {
        this.appearance = appearance;
    }

    @Override
    public String toString() {
        return "DeviceAppearance{"
                + "tid=0x" + Integer.toHexString(this.getTripletId())
                + ", appearance=" + Integer.toHexString(this.appearance)
                + '}';
    }
}
