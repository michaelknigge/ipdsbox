package de.textmode.ipdsbox.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.ipds.sdf.SelfDefiningField;
import de.textmode.ipdsbox.ipds.triplets.Triplet;

/**
 * The {@link IpdsByteArrayInputStream} is not really an DataInputStream, but it follows the
 * concept of the DataInputStream. It provides methods for reading native data types and
 * data types specific for IPDS.
 */
public final class IpdsByteArrayInputStream {

    private static final Charset EBCDIC = Charset.forName("ibm-500");
    private static final Charset ASCII = Charset.forName("ibm-850");

    private final byte[] data;
    private int offset;
    private int bytesLeft;


    /**
     * Constructs an {@link IpdsByteArrayInputStream}.
     *
     * @param data the underlying byte array
     */
    public IpdsByteArrayInputStream(final byte[] data) {
        this(data, 0, 0);
    }

    /**
     * Constructs an {@link IpdsByteArrayInputStream}.
     *
     * @param data the underlying byte array
     * @param startOffset the start offset (usually the first byte after the Structured field introducer)
     * @param paddingBytesToIgnore Padding bytes that shall be ignored
     */
    public IpdsByteArrayInputStream(final byte[] data, final int startOffset, final int paddingBytesToIgnore) {
        this.data = data;
        this.offset = startOffset;
        this.bytesLeft = data.length - startOffset - paddingBytesToIgnore;
    }

    /**
     * Checks if enough bytes are available to read. If not an {@link IOException} is thrown.
     *
     * @param length the required amount of bytes
     *
     * @throws IOException if there are less then <code>length</code> bytes left to read.
     */
    private void checkForAvailableBytes(final int length) throws IOException {
        if (length > this.bytesLeft) {
            throw new IOException(
                    "Tried to parse " + length
                    + " bytes at offset " + this.offset
                    + " but there are only " + this.bytesLeft
                    + " bytes left to parse.");
        }
    }

    /**
     * Returns the number of bytes available to read.
     *
     * @return the number of bytes available to read.
     */
    public int bytesAvailable() {
        return this.bytesLeft;
    }

    /**
     * Returns the current offset within the {@link IpdsByteArrayInputStream}.
     *
     * @return the current offset within the {@link IpdsByteArrayInputStream}.
     */
    public int tell() {
        return this.offset;
    }

    /**
     * Skips count bytes of input from this input stream.
     * @param count the number of bytes to be skipped.
     * @throws IOException if there was an error in skipping (usually tried to skip more bytes than available)
     */
    public void skip(final int count) throws IOException {
        this.checkForAvailableBytes(count);

        this.offset += count;
        this.bytesLeft -= count;
    }

    /**
     * Rewind count bytes of input from this input stream.
     * @param count the number of bytes to be walk back.
     * @throws IOException if there was an error in rewinding (usually tried to rewinding more bytes than available)
     */
    public void rewind(final int count) throws IOException {

        if (this.offset - count < 0) {
            throw new IOException("Tried to rewind " + count + " bytes at offset " + this.offset);
        }

        this.offset -= count;
        this.bytesLeft += count;
    }

    /**
     * Reads a complete {@link Triplet} from the underlying byte array.
     * @return A byte array containg the raw data of the {@link Triplet} or <code>null</code> if there is
     * no further {@link Triplet} available.
     * @throws IOException if the {@link Triplet} to be read is broken.
     */
    public byte[] readTripletIfExists() throws IOException {
        final int available = this.bytesAvailable();
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

        System.arraycopy(this.data, this.offset, buffer, 1, length - 1);

        this.offset += (length - 1);
        this.bytesLeft -= (length - 1);

        return buffer;
    }

    /**
     * Reads a complete {@link SelfDefiningField} from the underlying byte array.
     * @return A byte array containg the raw data of the {@link SelfDefiningField} or <code>null</code> if there is
     * no further {@link SelfDefiningField} available.
     * @throws IOException if the {@link SelfDefiningField} to be read is broken.
     */
    public byte[] readSelfDefiningFieldIfExists() throws IOException {
        final int available = this.bytesAvailable();
        if (available == 0) {
            return null;
        }

        final int length = this.readUnsignedInteger16();
        if (available < (length - 2)) {
            throw new IOException(String.format(
                    "A self-defining field to be read seems to be %1$d bytes long but the IPDS data stream ends after %2$d bytes",
                    length, available));
        }

        final byte[] buffer = new byte[length];
        buffer[0] = (byte) ((length >>> 8) & 0xFF);
        buffer[1] = (byte) (length & 0xFF);

        System.arraycopy(this.data, this.offset, buffer, 2, length - 2);

        this.offset += (length - 2);
        this.bytesLeft -= (length - 2);

        return buffer;
    }

    /**
     * Reads and returns some bytes.
     *
     * @param length how many bytes should be read and returned
     *
     * @return the requested amount of bytes read.
     *
     * @throws IOException if there are not enough bytes left to read.
     */
    public byte[] readBytes(final int length) throws IOException {
        this.checkForAvailableBytes(length);

        final byte[] result = new byte[length];
        System.arraycopy(this.data, this.offset, result, 0, length);

        this.offset += length;
        this.bytesLeft -= length;

        return result;
    }

    /**
     * Reads and returns some bytes, staring at the given offset.
     *
     * @param off     the offset where reading should start
     * @param length  how many bytes should be read and returned
     *
     * @return the requested amount of bytes read.
     *
     * @throws IOException if there are not enough bytes left to read.
     */
    public byte[] readBytes(final int off, final int length) throws IOException {
        final int diff = this.offset - off;
        this.offset -= diff;
        this.bytesLeft += diff;

        this.checkForAvailableBytes(length);

        final byte[] result = new byte[length];
        System.arraycopy(this.data, this.offset, result, 0, length);

        this.offset += length;
        this.bytesLeft -= length;

        return result;
    }

    /**
     * Reads and returns all remaining bytes.
     *
     * @return the remaining bytes. If no more bytes are available an empty byte array will be returned.
     */
    public byte[] readRemainingBytes() {

        final byte[] result = new byte[this.bytesLeft];
        System.arraycopy(this.data, this.offset, result, 0, this.bytesLeft);

        this.offset += this.bytesLeft;
        this.bytesLeft = 0;

        return result;
    }

    /**
     * Reads and returns some bytes and treads the bytes as an EBCDIC-International encoded String.
     *
     * @param length length of the string
     *
     * @return the String decoded from the read bytes.
     *
     * @throws IOException if there are not enough bytes left to read.
     */
    public String readEbcdicString(final int length) throws IOException {
        this.checkForAvailableBytes(length);

        final String result = EBCDIC.decode(ByteBuffer.wrap(this.data, this.offset, length)).toString();

        this.offset += length;
        this.bytesLeft -= length;

        return result;
    }

    /**
     * Reads and returns some bytes and treads the bytes as an ASCII encoded single byte character String.
     *
     * @param length length of the string
     *
     * @return the String decoded from the read bytes.
     *
     * @throws IOException if there are not enough bytes left to read.
     */
    public String readAsciiString(final int length) throws IOException {
        this.checkForAvailableBytes(length);

        final String result = ASCII.decode(ByteBuffer.wrap(this.data, this.offset, length)).toString();

        this.offset += length;
        this.bytesLeft -= length;

        return result;
    }

    /**
     * Reads and returns one input byte. The byte is treated as an unsigned value.
     *
     * @return the unsigned 8-bit value read.
     *
     * @throws IOException if there are not enough bytes left to parse.
     */
    public int readUnsignedByte() throws IOException {
        this.checkForAvailableBytes(1);

        final int result = ByteUtils.toUnsignedByte(this.data, this.offset);

        ++this.offset;
        --this.bytesLeft;

        return result;
    }

    /**
     * Reads and returns one input byte. The byte is treated as a signed value.
     *
     * @return the signed 8-bit value read.
     *
     * @throws IOException if there are not enough bytes left to parse.
     */
    public int readByte() throws IOException {
        this.checkForAvailableBytes(1);

        final int result = ByteUtils.toByte(this.data, this.offset);

        ++this.offset;
        --this.bytesLeft;

        return result;
    }

    /**
     * Reads two input bytes and returns a integer value. The value is treated as an unsigned value.
     *
     * @return the unsigned 16-bit value read.
     *
     * @throws IOException if there are not enough bytes left to parse.
     */
    public int readUnsignedInteger16() throws IOException {
        this.checkForAvailableBytes(2);

        final int result = ByteUtils.toUnsignedInteger16(this.data, this.offset);

        this.offset += 2;
        this.bytesLeft -= 2;

        return result;
    }

    /**
     * Reads two input bytes and returns a integer value. The value is treated as a signed value.
     *
     * @return the signed 16-bit value read.
     *
     * @throws IOException if there are not enough bytes left to parse.
     */
    public int readInteger16() throws IOException {
        this.checkForAvailableBytes(2);

        final int result = ByteUtils.toInteger16(this.data, this.offset);

        this.offset += 2;
        this.bytesLeft -= 2;

        return result;
    }

    /**
     * Reads three input bytes and returns a integer value. The value is treated as an unsigned value.
     *
     * @return the unsigned 24-bit value read.
     *
     * @throws IOException if there are not enough bytes left to parse.
     */
    public int readUnsignedInteger24() throws IOException {
        this.checkForAvailableBytes(3);

        final int result = ByteUtils.toUnsignedInteger24(this.data, this.offset);

        this.offset += 3;
        this.bytesLeft -= 3;

        return result;
    }

    /**
     * Reads three input bytes and returns a integer value. The value is treated as a signed value.
     *
     * @return the signed 24-bit value read.
     *
     * @throws IOException if there are not enough bytes left to parse.
     */
    public int readInteger24() throws IOException {
        this.checkForAvailableBytes(3);

        final int result = ByteUtils.toInteger24(this.data, this.offset);

        this.offset += 3;
        this.bytesLeft -= 3;

        return result;
    }

    /**
     * Reads four input bytes and returns a long value. The value is treated as an unsigned value.
     *
     * @return the unsigned 32-bit value read.
     *
     * @throws IOException if there are not enough bytes left to parse.
     */
    public long readUnsignedInteger32() throws IOException {
        this.checkForAvailableBytes(4);

        final long result = ByteUtils.toUnsignedInteger32(this.data, this.offset);

        this.offset += 4;
        this.bytesLeft -= 4;

        return result;
    }

    /**
     * Reads four input bytes and returns a integer value. The value is treated as a signed value.
     *
     * @return the signed 32-bit value read.
     *
     * @throws IOException if there are not enough bytes left to parse.
     */
    public int readInteger32() throws IOException {
        this.checkForAvailableBytes(4);

        final int result = ByteUtils.toInteger32(this.data, this.offset);

        this.offset += 4;
        this.bytesLeft -= 4;

        return result;
    }
}
