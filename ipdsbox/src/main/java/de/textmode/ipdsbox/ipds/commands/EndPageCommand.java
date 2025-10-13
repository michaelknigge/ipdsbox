package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The End Page (EP) command causes the printer to return to home state from page state, page segment state,
 * or overlay state and thus marks the end of a page, a page segment, or an overlay. The EP command is an
 * implicit command to schedule that page for printing if the command is being used to exit page state; all data for
 * that page is available to the printer. Zero or more data bytes can be transmitted but are ignored.
 */
public final class EndPageCommand extends IpdsCommand {

    private byte[] dataBytes;

    /**
     * Constructs the {@link EndPageCommand}.
     */
    public EndPageCommand() {
        this(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * Constructs the {@link EndPageCommand}.
     */
    public EndPageCommand(final byte[] dataBytes) {
        super(IpdsCommandId.EP);

        this.dataBytes = dataBytes;
    }
    /**
     * Constructs the {@link EndPageCommand}.
     */
    public EndPageCommand(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException {
        super(ipds, IpdsCommandId.EP);

        this.dataBytes = ipds.readRemainingBytes();
    }

    /**
     * Returns the data bytes of the IPDS Command.
     */
    public byte[] getDataBytes() {
        return this.dataBytes;
    }

    /**
     * Sets the data bytes of the IPDS Command.
     */
    public void setDataBytes(final byte[] dataBytes) {
        this.dataBytes = dataBytes;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        if (this.dataBytes != null) {
            ipds.writeBytes(this.dataBytes);
        }
    }
}
