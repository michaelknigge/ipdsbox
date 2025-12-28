package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class EncodingSchemeIdTriplet extends Triplet {

    private int dataEsId;

    /**
     * Constructs a {@link EncodingSchemeIdTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    EncodingSchemeIdTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.EncodingSchemeID);

        ipds.skip(2);
        this.dataEsId = ipds.readUnsignedInteger16();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(6);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedInteger16(0);
        out.writeUnsignedInteger16(this.dataEsId);
    }

    /**
     * Returns the Data ESID.
     */
    public int getDataEsId() {
        return this.dataEsId;
    }

    /**
     * Sets the Data ESID.
     */
    public void setDataEsId(final int dataEsId) {
        this.dataEsId = dataEsId;
    }

    @Override
    public String toString() {
        return "EncodingSchemeId{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", esid=" + Integer.toHexString(this.dataEsId) +
                "}";
    }
}
