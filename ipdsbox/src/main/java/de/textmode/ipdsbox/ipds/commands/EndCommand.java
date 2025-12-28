package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The End (END) command is the ending control for a series of Write Image, Write Image 2, Write Graphics,
 * Write Bar Code, Write Object Container, Write Metadata, Load Code Page, or Load Font commands and
 * marks either the end of an image object, a graphics object, a bar code object, an object container object, or a
 * metadata object, or the end of a downloaded font sequence. For text objects, the End command is the ending
 * control for a series of Write Text commands; note that the End command is not used with text-major text in a
 * logical page. Zero or more data bytes can be transmitted but are ignored.
 */
public final class EndCommand extends IpdsCommand {

    private byte[] dataBytes;

    /**
     * Constructs the {@link EndCommand}.
     */
    public EndCommand() {
        this(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * Constructs the {@link EndCommand}.
     */
    public EndCommand(final byte[] dataBytes) {
        super(IpdsCommandId.END);

        this.dataBytes = dataBytes;
    }
    /**
     * Constructs the {@link EndCommand}.
     */
    EndCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.END);

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
