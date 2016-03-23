package mk.ipdsbox.ipds;

import mk.ipdsbox.core.InvalidIpdsCommandException;

/**
 * The No Operation (NOP) command has no effect on presentation. Zero or more data bytes
 * may be present but are ignored. This command is valid in any printer state.
 */
public class NoOperationCommand extends IpdsCommand {

    /**
     * Constructs the {@link NoOperationCommand}.
     * @param command the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     */
    protected NoOperationCommand(final byte[] command) throws InvalidIpdsCommandException {
        super(command, IpdsCommandId.NOP);
    }
}
