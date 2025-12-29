package de.textmode.ipdsbox.ppd;

import java.io.IOException;
import java.io.InputStream;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Reads data from an {@link InputStream} and constructs {@link PagePrinterRequest}
 * objects from the read data.
 *
 * <p>The specification of the PPD/PPR protocol is available in the IBM manual
 * <i>PSF-AFP TCP/IP Attachment Functional Specification</i>, but this manual
 * not publicly available. Everything which is coded here is based on packet
 * sniffing and some assumptions.
 */
public final class PagePrinterRequestReader {

    /**
     * Exception error message if the data stream ends unexpectedly.
     */
    static final String ERROR_UNEXPECTED_EOF = "Unexpeced end of data stream";

    /**
     * Exception error message if the page printer request has an invalid length.
     */
    static final String ERROR_INVALID_LENGTH = "The received request has an invalid length of ";

    private PagePrinterRequestReader() {
    }

    /**
     * Reads bytes from an {@link InputStream} and construct a new {@link PagePrinterRequest} from the read bytes.
     *
     * @param in {@link InputStream} to be read from
     *
     * @return If EOF occurred on reading the first byte <code>null</code> will be returned, otherwise
     *  a newly constructed {@link PagePrinterRequest}.
     *
     * @throws IOException If an error occurred while reading from the {@link InputStream} or when
     *  the {@link InputStream} ends unexpectedly.
     */
    public static PagePrinterRequest read(final InputStream in) throws IOException {
        return read(in, false);
    }

    /**
     * Reads bytes from an {@link InputStream} and construct a new {@link PagePrinterRequest} from the read bytes.
     *
     * @param in {@link InputStream} to be read from
     *
     * @return If EOF occurred on reading the first byte <code>null</code> will be returned, otherwise
     *  a newly constructed {@link PagePrinterRequest}.
     *
     * @throws IOException If an error occurred while reading from the {@link InputStream} or when
     *  the {@link InputStream} ends unexpectedly.
     */
    public static PagePrinterRequest read(final InputStream in, final boolean printReadBytes) throws IOException {

        // The first four byte contain the overall length of the request, including
        // the length of the four bytes itself.
        final int length = readFourBytes(in, true);
        if (length == -1) {
            return null;
        }

        // The second four bytes (it is assumed they do) contain the "request". Because the
        // specification is not available we do not know which meaning a particular value has.
        final int request = readFourBytes(in, false);

        // The remaining bytes contain the data of the command (i. e. the IPDS data).
        // Note that not every command carries additional data.
        int dataLength = length - 8;
        if (dataLength < 0) {
            throw new IOException(ERROR_INVALID_LENGTH + length);
        }

        if (dataLength == 0) {
            return new PagePrinterRequest(request);
        }

        // Now read the remaining bytes of the request.
        final byte[] buffer = new byte[dataLength];

        int offset = 0;
        while (dataLength > 0) {
            final int filled = in.read(buffer, offset, dataLength);
            if (filled == -1) {
                throw new IOException(ERROR_UNEXPECTED_EOF);
            }
            offset += filled;
            dataLength -= filled;
        }

        if (printReadBytes) {
            final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
            os.writeUnsignedInteger32(length);
            os.writeUnsignedInteger32(request);

            System.err.println("RECV: " + StringUtils.toHexString(os.toByteArray()) + StringUtils.toHexString(buffer));
        }

        return new PagePrinterRequest(request, buffer);
    }

    /**
     * Reads the next four bytes from the {@link InputStream} and returns the integer value
     * of the bytes. Optionally an exception is thrown if the {@link InputStream} reaches
     * its end while reading a byte.
     *
     * @param in {@link InputStream} to read from.
     *
     * @param endOfStreamIsOkOnFirstByte <code>true</code> if and only if it is ok to reach the
     *  end of the stream when reading the first byte
     *
     * @return The positive integer value of the read 4 bytes. If the end of the stream has been
     *  reached -1 is returned if <i>endOfStreamIsOkOnFirstByte</i> is <code>true</code>, otherwise
     *  an {@link IOException} is thrown.
     */
    private static int readFourBytes(final InputStream in, final boolean endOfStreamIsOkOnFirstByte)
        throws IOException {

        final int byte1;
        byte1 = in.read();
        if (byte1 == -1) {
            if (endOfStreamIsOkOnFirstByte) {
                return -1;
            }
            throw new IOException(ERROR_UNEXPECTED_EOF);
        }

        final int byte2 = in.read();
        final int byte3 = in.read();
        final int byte4 = in.read();
        if (byte2 == -1 || byte3 == -1 || byte4 == -1) {
            throw new IOException(ERROR_UNEXPECTED_EOF);
        }

        return (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4;
    }
}
