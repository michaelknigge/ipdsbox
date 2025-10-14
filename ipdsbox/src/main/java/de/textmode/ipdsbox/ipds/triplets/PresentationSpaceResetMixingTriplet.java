package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class PresentationSpaceResetMixingTriplet extends Triplet {

    private int mixingFlags;


    public PresentationSpaceResetMixingTriplet(final IpdsByteArrayInputStream ipds) throws IOException, UnknownTripletException {
        super(ipds, TripletId.PresentationSpaceResetMixing);

        this.mixingFlags = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(3);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.mixingFlags);
    }

    /**
     * Returns the Mixing flags.
     */
    public int getMixingFlags() {
        return this.mixingFlags;
    }

    /**
     * Sets the Mixing flags.
     */
    public void setMixingFlags(final int mixingFlags) {
        this.mixingFlags = mixingFlags;
    }

    @Override
    public String toString() {
        return "ResetMixing{" +
                "tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", flags=" + String
                    .format("B'%8s'", Integer.toBinaryString(this.mixingFlags))
                    .replace(' ', '0') +
                "}";
    }
}
