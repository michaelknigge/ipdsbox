package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class LinkedFontTriplet extends Triplet {

    private static final Charset UTF16BE = Charset.forName("utf-16be");

    private int hostAssignedId;
    private int fontIdType;
    private int fontIndex;
    private String fullFontName;

    /**
     * Constructs a {@link LinkedFontTriplet} from the given byte array.
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public LinkedFontTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.LinkedFont);
        this.readFrom(this.getStream());
    }

    private void readFrom(final IpdsByteArrayInputStream in) throws IOException {
        this.hostAssignedId = in.readUnsignedInteger16();
        this.fontIdType = in.readUnsignedByte();

        if (this.fontIdType == 0x00) {
            this.fontIndex = -1;
            this.fullFontName = "";
        } else if (this.fontIdType == 0x01) {
            this.fontIndex = in.readUnsignedInteger16();
            this.fullFontName = "";
        } else if (this.fontIdType == 0x02) {
            this.fontIndex = -1;
            this.fullFontName = UTF16BE.decode(ByteBuffer.wrap(in.readRemainingBytes())).toString();
        }

        // TODO: Shall we throw an exception if font ID type is unsupported?!?
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(0);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedInteger16(this.hostAssignedId);

        if (this.fontIdType == 0x01) {
            out.writeUnsignedInteger16(this.fontIndex);
        } else if (this.fontIdType == 0x02) {
            out.writeUtf16beString(this.fullFontName);
        }

        // TODO: Shall we throw an exception if font ID type is unsupported?!?

        final byte[] result = out.toByteArray();
        result[0] = (byte) (result.length & 0xFF);

        return result;
    }

    /**
     * Returns the host assigned ID.
     * @return the host assigned ID.
     */
    public int getHostAssignedId() {
        return this.hostAssignedId;
    }

    /**
     * Sets the host assigned ID.
     */
    public void setHostAssignedId(final int hostAssignedId) {
        this.hostAssignedId = hostAssignedId;
    }

    /**
     * Returns the font ID type.
     * @return the font ID type.
     */
    public int getFontIdType() {
        return this.fontIdType;
    }

    /**
     * Sets the font ID type.
     */
    public void setFontIdType(final int fontIdType) {
        this.fontIdType = fontIdType;
    }

    /**
     * Returns the font index (if font ID type is 0x01).
     * @return the font index.
     */
    public int getFontIndex() {
        return this.fontIndex;
    }

    /**
     * Sets the font index.
     */
    public void setFontIndex(final int fontIndex) {
        this.fontIndex = fontIndex;
    }

    /**
     * Returns the full font name (if font ID type is 0x02).
     * @return the full font name.
     */
    public String getFullFontName() {
        return this.fullFontName;
    }

    /**
     * Sets the full font name.
     */
    public void setFullFontName(final String fullFontName) {
        this.fullFontName = fullFontName;
    }

    @Override
    public String toString() {
        return "LinkedFont{length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", haid=" + Integer.toHexString(this.hostAssignedId) +
                ", fontIdType=" + Integer.toHexString(this.fontIdType) +
                ", fontIndex=" + this.fontIndex +
                ", fullFontName=" + this.fullFontName +"}";
    }
}
