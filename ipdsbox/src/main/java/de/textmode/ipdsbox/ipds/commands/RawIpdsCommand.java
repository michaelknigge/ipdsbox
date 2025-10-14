package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The {@link RawIpdsCommand} is created for IPDS commands that are unknown or not parsed yet.
 *
 */
public final class RawIpdsCommand extends IpdsCommand {

    private final byte[] dataBytes;

    /**
     * Constructs the {@link RawIpdsCommand}.
     */
    public RawIpdsCommand(
            final IpdsByteArrayInputStream ipds,
            final IpdsCommandId commandId) throws InvalidIpdsCommandException, IOException {

        super(ipds, commandId);

        this.dataBytes = ipds.readRemainingBytes();
    }

    /**
     * Returns the raw data bytes of the IPDS command.
     */
    public byte[] getDataBytes() {
        return this.dataBytes;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        if (this.dataBytes != null) {
            ipds.writeBytes(this.dataBytes);
        }
    }
}
