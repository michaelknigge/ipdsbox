package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.Arrays;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The {@link UnknownIpdsCommand} is created for IPDS commands that are unknown or not parsed yet.
 *
 */
public final class UnknownIpdsCommand extends IpdsCommand {

    private final byte[] rawData;

    /**
     * Constructs the {@link UnknownIpdsCommand} from the given {@link IpdsByteArrayInputStream}.
     */
    UnknownIpdsCommand(
            final IpdsByteArrayInputStream ipds,
            final int commandIdValue) throws IOException {

        super(ipds, commandIdValue);

        this.rawData = ipds.readRemainingBytes();
    }

    /**
     * Returns the raw data bytes of the IPDS command.
     */
    public byte[] getDataBytes() {
        return this.rawData;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        if (this.rawData != null) {
            ipds.writeBytes(this.rawData);
        }
    }

    @Override
    public String toString() {
        return "UnknownIpdsCommand{"
                + "commandId=" + Integer.toHexString(this.getCommandCodeId())
                + ", rawData=" + Arrays.toString(this.rawData)
                + '}';
    }
}
