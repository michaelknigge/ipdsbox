package mk.ipdsbox.ppd;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A request from a Page Printer Requestor (the counterpart of the Page Printer Daemon, which is
 * usually a spooling system like IBM InfoPrint).
 */
public final class PagePrinterRequest {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private final int request;
    private final byte[] buffer;

    /**
     * Constructs a {@link PagePrinterRequest}.
     *
     * @param request The integer value of the request.
     */
    PagePrinterRequest(final int request) {
        this(request, EMPTY_BYTE_ARRAY);
    }

    /**
     * Constructs a {@link PagePrinterRequest}.
     *
     * @param request The integer value of the request.
     * @param buffer The raw data of the whole request.
     */
    PagePrinterRequest(final int request, final byte[] buffer) {
        this.request = request;
        this.buffer = buffer;
    }

    /**
     * Returns the integer value of the request.
     * @return Integer value of the request
     */
    public int getRequest() {
        return this.request;
    }

    /**
     * Returns the data of the request (i. e. the IPDS data).
     * @return Data of the request (i. e. the IPDS data)
     */
    public byte[] getData() {
        return this.buffer;
    }

    /**
     * Writes the {@link PagePrinterRequest} to the given {@link OutputStream}.
     * @param out The {@link OutputStream} to write to.
     * @throws IOException  if an I/O error occurs.
     */
    public void writeTo(final OutputStream out) throws IOException {
        if (this.getData().length == 0) {
            out.write(intToByteArray(8));
            out.write(intToByteArray(this.getRequest()));
        } else {
            out.write(intToByteArray(this.getData().length + 8));
            out.write(intToByteArray(this.getRequest()));
            out.write(this.getData());
        }
    }

    /**
     * Converts the given integer value into a byte array.
     * @param value The integer value to convert
     * @return A four byte long byte array containing the integer value.
     */
    private static byte[] intToByteArray(final int value) {
        final byte[] result = new byte[4];

        result[0] = (byte) ((value & 0xFF000000) >> 24);
        result[1] = (byte) ((value & 0x00FF0000) >> 16);
        result[2] = (byte) ((value & 0x0000FF00) >> 8);
        result[3] = (byte) ((value & 0x000000FF) >> 0);

        return result;
    }
}
