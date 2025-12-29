package de.textmode.ipdsbox.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import de.textmode.ipdsbox.core.StringUtils;

/**
 * The {@link IpdsByteArrayOutputStream} provides methods for writing native data types and
 * data types specific for IPDS.
 */
public final class IpdsByteArrayOutputStream {

    private static final Charset EBCDIC = Charset.forName("ibm-500");
    private static final Charset ASCII = Charset.forName("ibm-850");
    private static final Charset UTF16BE = StandardCharsets.UTF_16BE;

    private final ByteArrayOutputStream baos;

    /**
     * Constructs an {@link IpdsByteArrayInputStream}.
     */
    public IpdsByteArrayOutputStream() {
        this.baos = new ByteArrayOutputStream();
    }

    /**
     * Returns the current size (written bytes).
     */
    public int getSize() {
        return this.baos.size();
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
     * Writes an EBCDIC-International encoded String in the specified length (the String is filled
     * up with spaces if needed or cutted off).
     */
    public void writeEbcdicString(final String toWrite, final int len) throws IOException {
        if (toWrite.length() == len) {
            this.baos.write(EBCDIC.encode(CharBuffer.wrap(toWrite)).array());
        } else if (toWrite.length() >= len) {
            this.baos.write(EBCDIC.encode(CharBuffer.wrap(toWrite.substring(0, len))).array());
        } else {
            this.baos.write(EBCDIC.encode(CharBuffer.wrap(StringUtils.padRight(toWrite, len))).array());
        }
    }

    /**
     * Writes an ASCII encoded single byte character String.
     */
    public void writeAsciiString(final String toWrite) throws IOException {
        this.baos.write(ASCII.encode(CharBuffer.wrap(toWrite)).array());
    }

    /**
     * Writes an ASCII encoded single byte character String in the specified length (the String is filled
     * up with spaces if needed or cutted off).
     */
    public void writeAsciiString(final String toWrite, final int len) throws IOException {
        if (toWrite.length() == len) {
            this.baos.write(ASCII.encode(CharBuffer.wrap(toWrite)).array());
        } else if (toWrite.length() >= len) {
            this.baos.write(ASCII.encode(CharBuffer.wrap(toWrite.substring(0, len))).array());
        } else {
            this.baos.write(ASCII.encode(CharBuffer.wrap(StringUtils.padRight(toWrite, len))).array());
        }
    }

    /**
     * Writes an UTF-16BE encoded character String.
     */
    public void writeUtf16beString(final String toWrite) throws IOException {
        this.baos.write(UTF16BE.encode(CharBuffer.wrap(toWrite)).array());
    }

    /**
     * Writes an UTF-16BE encoded character String in the specified length (the String is filled
     * up with spaces if needed or cutted off).
     */
    public void writeUtf16beString(final String toWrite, final int len) throws IOException {
        if (toWrite.length() == len) {
            this.baos.write(UTF16BE.encode(CharBuffer.wrap(toWrite)).array());
        } else if (toWrite.length() >= len) {
            this.baos.write(UTF16BE.encode(CharBuffer.wrap(toWrite.substring(0, len))).array());
        } else {
            this.baos.write(UTF16BE.encode(CharBuffer.wrap(StringUtils.padRight(toWrite, len))).array());
        }
    }

    /**
     * Writes one byte. The byte is treated as an unsigned value.
     */
    public void writeUnsignedByte(final int toWrite) throws IOException {
        this.baos.write(toWrite & 0xFF);
    }

    /**
     * Writes one input byte. The byte is treated as a signed value.
     */
    public void writeByte(final int toWrite) throws IOException {
        this.baos.write(toWrite);
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
