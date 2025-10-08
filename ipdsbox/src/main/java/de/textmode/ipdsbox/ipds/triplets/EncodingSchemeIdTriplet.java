package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class EncodingSchemeIdTriplet extends Triplet {

    private int dataEsId;

    public EncodingSchemeIdTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.EncodingSchemeID);

        this.getStream().skip(2);
        this.dataEsId = this.getStream().readUnsignedInteger16();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(6);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedInteger16(0);
        out.writeUnsignedInteger16(this.dataEsId);

        return out.toByteArray();
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
        return "EncodingSchemeId{len=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", esid=" + Integer.toHexString(this.dataEsId) + "}";
    }
}
