package de.textmode.ipdsbox.ppd;

import java.io.IOException;
import java.io.OutputStream;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.commands.IpdsCommand;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A request from a Page Printer Requester (the counterpart of the Page Printer Daemon, which is
 * usually a spooling system like IBM InfoPrint).
 */
public final class PagePrinterRequest {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private final int request;
    private final byte[] buffer;

    /**
     * Constructs a {@link PagePrinterRequest}.
     */
    public PagePrinterRequest(final IpdsCommand ipdsCommand) throws IOException {
        this(0x0E, commandToByteArray(ipdsCommand));
    }

    /**
     * Constructs a {@link PagePrinterRequest}.
     *
     * @param request The integer value of the request.
     */
    public PagePrinterRequest(final int request) {
        this(request, EMPTY_BYTE_ARRAY);
    }

    /**
     * Constructs a {@link PagePrinterRequest}.
     *
     * @param request The integer value of the request.
     * @param buffer The raw data of the whole request.
     */
    public PagePrinterRequest(final int request, final byte[] buffer) {
        this.request = request;
        this.buffer = buffer;
    }

    private static byte[] commandToByteArray(final IpdsCommand ipdsCommand) throws IOException {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        ipdsCommand.writeTo(os);

        return os.toByteArray();
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
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "It is intended that the buffer may be modified")
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

        // Make sure to send it as fast as possible...
        out.flush();
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
