package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class SetupNameTriplet extends Triplet {

    private static final Charset UTF16BE = Charset.forName("utf-16be");

    private String setupName;

    /**
     * Constructs a {@link SetupNameTriplet} from the given byte array.
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public SetupNameTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.SetupName);

        this.getStream().skip(2);
        this.setupName = UTF16BE.decode(ByteBuffer.wrap(this.getStream().readRemainingBytes())).toString();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        final byte[] encodedName = UTF16BE.encode(CharBuffer.wrap(this.setupName)).array();
        final int encodedFontNameLen = encodedName.length;

        out.writeUnsignedByte(4+ encodedFontNameLen);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedInteger16(0x0000);
        out.writeBytes(encodedName);
    }

    /**
     * Returns the setup name.
     * @return the setup name.
     */
    public String getSetupName() {
        return this.setupName;
    }

    /**
     * Sets the setup name.
     */
    public void setSetupName(final String setupName) {
        this.setupName = setupName;
    }

    @Override
    public String toString() {
        return "SetupName{length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", setupName=" + this.setupName +"}";
    }
}
