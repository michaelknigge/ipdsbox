package de.textmode.ipdsbox.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * The {@link IpdsByteArrayOutputStream} provides methods for writing native data types and
 * data types specific for IPDS.
 */
public final class IpdsByteArrayOutputStream {

    private static final Charset EBCDIC = Charset.forName("ibm-500");
    private static final Charset ASCII = Charset.forName("ibm-850");

    private final ByteArrayOutputStream baos;

    /**
     * Constructs an {@link IpdsByteArrayInputStream}.
     */
    public IpdsByteArrayOutputStream() {
        this.baos = new ByteArrayOutputStream();
    }

    /**
     * Returns the underlying byte array.
     */
    public byte[] toByteArray() {
        return this.baos.toByteArray();
    }

    /**
     * Writes some bytes.
     */
    public void writeBytes(final byte[] toWrite) throws IOException {
        this.baos.write(toWrite);
    }

    /**
     * Writes an EBCDIC-International encoded String.
     */
    public void writeEbcdicString(final String toWrite) throws IOException {
        this.baos.write(EBCDIC.encode(CharBuffer.wrap(toWrite)).array());
    }

    /**
     * Writes an ASCII encoded single byte character String.
     */
    public void writeAsciiString(final String toWrite) throws IOException {
        this.baos.write(ASCII.encode(CharBuffer.wrap(toWrite)).array());
    }

    /**
     * Writes one byte. The byte is treated as an unsigned value.
     */
    public void writeUnsignedByte(final int toWrite) throws IOException {
        this.baos.write(toWrite & 0xFF); // TEST
    }

    /**
     * Writes one input byte. The byte is treated as a signed value.
     */
    public void writeByte(final int toWrite) throws IOException {
        this.baos.write(toWrite); // TEST
    }

    /**
     * Writes a 16 Bit value. The value is treated as an unsigned value.
     */
    public void writeUnsignedInteger16(final int toWrite) throws IOException {
        this.baos.write((toWrite >>> 8) & 0xFF);
        this.baos.write(toWrite & 0xFF);
    }

    /**
     * Writes a 16 Bit value. The value is treated as a signed value.
     */
    public void writeInteger16(final int toWrite) throws IOException {
        this.baos.write((toWrite >>> 8) & 0xFF);
        this.baos.write(toWrite & 0xFF);
    }

    /**
     * Writes a 24 Bit value. The value is treated as an unsigned value.
     */
    public void writeUnsignedInteger24(final int toWrite) throws IOException {
        this.baos.write((toWrite >>> 16) & 0xFF);
        this.baos.write((toWrite >>> 8) & 0xFF);
        this.baos.write(toWrite & 0xFF);
    }

    /**
     * Writes a 24 Bit value. The value is treated as a signed value.
     */
    public void writeInteger24(final int toWrite) throws IOException {
        this.baos.write((toWrite >>> 16) & 0xFF);
        this.baos.write((toWrite >>> 8) & 0xFF);
        this.baos.write(toWrite & 0xFF);
    }

    /**
     * Writes a 32 Bit value. The value is treated as an unsigned value.
     */
    public void writeUnsignedInteger32(final long toWrite) throws IOException {
        this.baos.write((int) (toWrite >>> 24) & 0xFF);
        this.baos.write((int) (toWrite >>> 16) & 0xFF);
        this.baos.write((int) (toWrite >>> 8) & 0xFF);
        this.baos.write((int) toWrite & 0xFF);
    }

    /**
     * Writes a 32 Bit value. The value is treated as a signed value.
     */
    public void writeInteger32(final int toWrite) throws IOException {
        this.baos.write((toWrite >>> 24) & 0xFF);
        this.baos.write((toWrite >>> 16) & 0xFF);
        this.baos.write((toWrite >>> 8) & 0xFF);
        this.baos.write(toWrite & 0xFF);
    }
}
