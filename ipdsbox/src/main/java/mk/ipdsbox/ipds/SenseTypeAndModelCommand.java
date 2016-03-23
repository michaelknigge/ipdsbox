package mk.ipdsbox.ipds;

import mk.ipdsbox.core.InvalidIpdsCommandException;

/**
 * The Sense Type and Model (STM) command requests the printer to respond with device-dependent
 * information that identifies the printer and its capabilities. The printer returns this
 * information to the host in the special data area of one or more Acknowledge Replies to the
 * STM command. No data is transmitted with this command.
 */
public final class SenseTypeAndModelCommand extends IpdsCommand {

    /**
     * Constructs the {@link SenseTypeAndModelCommand}.
     * @param command the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     */
    protected SenseTypeAndModelCommand(final byte[] command) throws InvalidIpdsCommandException {
        super(command, IpdsCommandId.STM);
    }
}
