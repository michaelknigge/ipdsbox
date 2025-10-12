package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Sense Type and Model (STM) command requests the printer to respond with device-dependent
 * information that identifies the printer and its capabilities. The printer returns this
 * information to the host in the special data area of one or more Acknowledge Replies to the
 * STM command. No data is transmitted with this command.
 */
public final class SenseTypeAndModelCommand extends IpdsCommand {

    /**
     * Constructs the {@link SenseTypeAndModelCommand}.
     */
    public SenseTypeAndModelCommand() throws InvalidIpdsCommandException, IOException {
        super(IpdsCommandId.STM);

        // We should not send STM's without a ARQ (which will be a NOP)...
        this.getCommandFlags().isAcknowledgmentRequired(true);
    }

    /**
     * Constructs the {@link SenseTypeAndModelCommand}.
     * @param ipds the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     */
    public SenseTypeAndModelCommand(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException {
        super(ipds, IpdsCommandId.STM);
    }

    @Override
    void writeDataTo(final IpdsByteArrayOutputStream ipds) {
        // No data is transmitted with this command.
    }
}
