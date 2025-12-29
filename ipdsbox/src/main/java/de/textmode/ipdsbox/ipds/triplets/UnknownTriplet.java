package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Represents an unknown triplet.
 */
public final class UnknownTriplet extends Triplet {

    private final byte[] rawData;

    /**
     * Constructs a {@link UnknownTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    UnknownTriplet(final IpdsByteArrayInputStream ipds, final int tripletId) throws IOException {
        super(tripletId);
        this.rawData = ipds.readRemainingBytes();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(3 + this.rawData.length);
        out.writeUnsignedByte(this.getTripletId());
        out.writeBytes(this.rawData);
    }

    /**
     * Returns the raw data of the triplet.
     */
    public byte[] getRawData() {
        return this.rawData;
    }

    @Override
    public String toString() {
        return "UnknownTriplet{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", rawData=" + StringUtils.toHexString(this.rawData) +
                "}";
    }
}
