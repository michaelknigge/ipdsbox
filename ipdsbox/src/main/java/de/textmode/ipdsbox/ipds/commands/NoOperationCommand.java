package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The No Operation (NOP) command has no effect on presentation. Zero or more data bytes
 * may be present but are ignored. This command is valid in any printer state.
 */
public final class NoOperationCommand extends IpdsCommand {

    private byte[] dataBytes;

    /**
     * Constructs the {@link NoOperationCommand}.
     */
    public NoOperationCommand() {
        this(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * Constructs the {@link NoOperationCommand}.
     */
    public NoOperationCommand(final byte[] dataBytes) {
        super(IpdsCommandId.NOP);

        this.dataBytes = dataBytes;
    }

    /**
     * Constructs the {@link NoOperationCommand}.
     */
    NoOperationCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.NOP);

        this.dataBytes = ipds.readRemainingBytes();
    }

    /**
     * Returns the data bytes of the NOP.
     */
    public byte[] getDataBytes() {
        return this.dataBytes;
    }

    /**
     * Sets the data bytes of the NOP.
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

    @Override
    public String toString() {
        return "NoOperationCommand{" +
                "dataBytes=" + StringUtils.toHexString(this.dataBytes) +
                '}';
    }
}
