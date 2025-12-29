package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class SetupNameTriplet extends Triplet {

    private static final Charset UTF16BE = Charset.forName("utf-16be");

    private String setupName;

    /**
     * Constructs a {@link SetupNameTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    SetupNameTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.SetupName);

        ipds.skip(2);
        this.setupName = UTF16BE.decode(ByteBuffer.wrap(ipds.readRemainingBytes())).toString();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        final byte[] encodedName = UTF16BE.encode(CharBuffer.wrap(this.setupName)).array();
        final int encodedFontNameLen = encodedName.length;

        out.writeUnsignedByte(4+ encodedFontNameLen);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedInteger16(0x0000);
        out.writeBytes(encodedName);
    }

    /**
     * Returns the setup name.
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
        return "SetupName{"
                + "tid=0x" + Integer.toHexString(this.getTripletId())
                + ", setupName=" + this.setupName
                + '}';
    }
}
