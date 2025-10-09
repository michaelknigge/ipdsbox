package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class DeviceAppearanceTriplet extends Triplet {

    private int appearance;

    /**
     * Constructs a {@link DeviceAppearanceTriplet} from the given byte array.
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public DeviceAppearanceTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.DeviceAppearance);

        this.getStream().skip(1);
        this.appearance = this.getStream().readUnsignedInteger16();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(7);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(0);
        out.writeUnsignedInteger16(this.appearance);
        out.writeUnsignedInteger16(0x0000);
    }

    /**
     * Returns the appearance.
     * @return the appearance.
     */
    public int getAppearance() {
        return this.appearance;
    }

    /**
     * Sets the host assigned ID.
     */
    public void setAppearance(final int appearance) {
        this.appearance = appearance;
    }

    @Override
    public String toString() {
        return "DeviceAppearance{length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", appearance=" + Integer.toHexString(this.appearance) +
                "}";
    }
}
