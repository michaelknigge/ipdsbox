package mk.ipdsbox.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import mk.ipdsbox.ipds.triplets.Triplet;

/**
 * An enhanced {@link ByteArrayInputStream} that can decode {@link String}s and read integer values
 * from the underlying byte array.
 */
public final class IpdsByteArrayInputStream {

    private static final String EMPTY_STRING = "";
    private static final Charset EBCDIC = Charset.forName("IBM500");
    private static final Charset ASCII = Charset.forName("IBM850");

    private final ByteArrayInputStream stream;

    /**
     * Constructs an {@link IpdsByteArrayInputStream}.
     * @param data the underlying byte array.
     */
    public IpdsByteArrayInputStream(final byte[] data) {
        this.stream = new ByteArrayInputStream(data);
    }

    /**
     * Skips count bytes of input from this input stream.
     * @param count the number of bytes to be skipped.
     * @throws IOException if there was an error in skipping (usually tried to skip more bytes than available)
     */
    public void skip(final long count) throws IOException {
        final long skipped = this.stream.skip(count);
        if (skipped != count) {
            throw new IOException(String.format(
                "Tried to skip %1$d bytes but skipped only %2$d bytes",
                count, skipped));
        }
    }

    /**
     * Reads a complete {@link Triplet} from the underlying byte array.
     * @return A byte array containg the raw data of the {@link Triplet} or <code>null</code> if there is
     * no further {@link Triplet} available.
     * @throws IOException if the {@link Triplet} to be read is broken.
     */
    public byte[] readTripletIfExists() throws IOException {
        final int available = this.stream.available();
        if (available == 0) {
            return null;
        }

        final int length = this.readByte();
        if (available < (length - 1)) {
            throw new IOException(String.format(
                "A triplet to be read seems to be %1$d bytes long but the IPDS data stream ends after %2$d bytes",
                length, available));
        }
        final byte[] buffer = new byte[length];
        buffer[0] = (byte) length;
        this.stream.read(buffer, 1, length - 1);

        return buffer;
    }

    /**
     * Reads one byte from the underlying byte array and returns the read value as an integer value.
     * @return the integer value of the read byte.
     * @throws IOException if there is less than one byte available in the underlying byte array.
     */
    public int readByte() throws IOException {
        if (this.stream.available() < 1) {
            throw new IOException("Less than 1 byte available.");
        }
        return this.stream.read();
    }

    /**
     * Reads two bytes (a word) from the underlying byte array and returns the integer value of word (big
     * endian byte order).
     * @return the integer value of the read word.
     * @throws IOException if there are less than two bytes available in the underlying byte array.
     */
    public int readWord() throws IOException {
        if (this.stream.available() < 2) {
            throw new IOException("Less than 2 bytes available.");
        }
        return (this.stream.read() << 8) | this.stream.read();
    }

    /**
     * Reads four bytes (a double word) from the underlying byte array and returns the integer value of double
     * word (big endian byte order).
     * @return the integer value of the read double word.
     * @throws IOException if there are less than four bytes available in the underlying byte array.
     */
    public long readDoubleWord() throws IOException {
        if (this.stream.available() < 4) {
            throw new IOException("Less than 4 bytes available.");
        }
        return (this.stream.read() << 24) | (this.stream.read() << 16) | (this.stream.read() << 8) | this.stream.read();
    }

    /**
     * Reads bytes from the underlying byte array and decodes the read bytes using the ASCII codepage 850.
     * @return the decoded ASCII string.
     * @throws IOException if there are less than the required bytes available in the underlying byte array.
     */
    public String readAsciiEncodedString() throws IOException {
        return this.stream.available() == 0
            ? EMPTY_STRING
            : new String(this.readFromStream(this.stream.available()), ASCII);
    }

    /**
     * Reads bytes from the underlying byte array and decodes the read bytes using the ASCII codepage 850.
     * @return the decoded ASCII string.
     * @throws IOException if there are less than the required bytes available in the underlying byte array.
     */
    public String readAsciiEncodedString(final int length) throws IOException {
        if (this.stream.available() < length) {
            throw new IOException("Less than " + length + " byte(s) available.");
        }
        return length == 0 ? EMPTY_STRING : new String(this.readFromStream(length), ASCII);
    }

    /**
     * Reads bytes from the underlying byte array and decodes the read bytes using the EBCDIC codepage 500.
     * @return the decoded EBCDIC string.
     * @throws IOException if there are less than the required bytes available in the underlying byte array.
     */
    public String readEbcdicEncodedString(final int length) throws IOException {
        if (this.stream.available() < length) {
            throw new IOException("Less than " + length + " byte(s) available.");
        }
        return length == 0 ? EMPTY_STRING : new String(this.readFromStream(length), EBCDIC);
    }

    /**
     * Reads some bytes from the underlying byte array in the given lengh.
     * @param length the required amount of bytes.
     * @return a byte array containing the read bytes.
     * @throws IOException if there have been less than <code>length</code> bytes read.
     */
    private byte[] readFromStream(final int length) throws IOException {
        final byte[] buffer = new byte[length];
        final int read = this.stream.read(buffer);
        if (read != length) {
            throw new IOException(
                String.format("Tried to read %1$d bytes but only %2$d bytes are available", length, read));
        }

        return buffer;
    }
}
