package de.textmode.ipdsbox.ppd;

import java.io.IOException;
import java.io.OutputStream;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.commands.IpdsCommand;
import de.textmode.ipdsbox.ipds.commands.IpdsCommandId;
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
    public PagePrinterRequest(final IpdsCommand ipdsCommand) throws IOException, InvalidIpdsCommandException {
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
     */
    public PagePrinterRequest(final int request, final byte[] buffer) {
        this.request = request;
        this.buffer = buffer;
    }

    private static byte[] commandToByteArray(final IpdsCommand ipdsCommand)
            throws IOException, InvalidIpdsCommandException {

        // The format (example from a STM command):
        //
        // "00000001 00000005 0005D6E480"
        //     ^        ^        ^
        //     |        |        |
        //     |        |        +-- 0005 = length, D6E4 = IPDS command code, 80 = Flag (ACK requested)
        //     |        +-- Length of the following IPDS command. Maybe more than one can be sent at once?!?
        //     +-- Some kind of flags?
        final IpdsByteArrayOutputStream commandOnly = new IpdsByteArrayOutputStream();
        ipdsCommand.writeTo(commandOnly);

        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();
        final byte[] ipdsCommandBytes = commandOnly.toByteArray();

        // Traces showed that the first four bytes contain 0x00000001 if the command is sent *TO* a
        // printer and 0x00000000 if a ACK reply is sent *FROM* a printer.
        if (ipdsCommand.getCommandCodeId() == IpdsCommandId.ACK.getValue()) {
            out.writeUnsignedInteger32(0x00000000);
        } else {
            out.writeUnsignedInteger32(0x00000001);
        }

        out.writeUnsignedInteger32(ipdsCommandBytes.length);
        out.writeBytes(ipdsCommandBytes);

        return out.toByteArray();
    }

    /**
     * Returns the integer value of the request.
     */
    public int getRequest() {
        return this.request;
    }

    /**
     * Returns the data of the request (i. e. the IPDS data).
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "It is intended that the buffer may be modified")
    public byte[] getData() {
        return this.buffer;
    }

    /**
     * Writes the {@link PagePrinterRequest} to the given {@link OutputStream}.
     */
    public void writeTo(final OutputStream out) throws IOException {
        this.writeTo(out, false);
    }

    /**
     * Writes the {@link PagePrinterRequest} to the given {@link OutputStream}.
     */
    public void writeTo(final OutputStream out, final boolean isDebugMode) throws IOException {
        // The format (example from a STM command):
        //
        // "00000015 0000000E 00000001 00000005 0005D6E480"
        //     ^        ^        ^        ^         ^
        //     |        |        |        |         |
        //     |        |        |        |         +-- 0005 = length, D6E4 = IPDS command code, 80 = Flag (ACK requested)
        //     |        |        |        +-- Length of the following IPDS command. Maybe more than one can be sent at once?!?
        //     |        |        +-- Some kind of flags?
        //     |        +-- Maybe an "operation code", 0x0E is "IPDS data"?
        //     +-- Complete length (incl. itself)

        final byte[] overallLength = intToByteArray(this.getData().length + 8);
        final byte[] requestCode = intToByteArray(this.request);

        out.write(overallLength);
        out.write(requestCode);
        out.write(this.getData());

        if (isDebugMode) {
            System.err.println(
                    "SEND: " +
                            StringUtils.toHexString(overallLength) +
                            StringUtils.toHexString(requestCode) +
                            StringUtils.toHexString(this.getData()));
        }

        // Make sure to send it as fast as possible...
        out.flush();
    }

    /**
     * Converts the given integer value into a byte array.
     */
    private static byte[] intToByteArray(final int value) {
        final byte[] result = new byte[4];

        result[0] = (byte) ((value & 0xFF000000) >>> 24);
        result[1] = (byte) ((value & 0x00FF0000) >>> 16);
        result[2] = (byte) ((value & 0x0000FF00) >>> 8);
        result[3] = (byte) ((value & 0x000000FF) >>> 0);

        return result;
    }
}
