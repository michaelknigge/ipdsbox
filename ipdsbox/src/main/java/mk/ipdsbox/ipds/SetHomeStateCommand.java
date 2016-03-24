package mk.ipdsbox.ipds;

import mk.ipdsbox.core.InvalidIpdsCommandException;

/**
 * The Set Home State (SHS) command is valid in any printer state. When the printer receives the SHS
 * command in page state or any derivative of page state, the current page ends, the complete or partially
 * complete page continues through the print process, and the printer returns to home state. If this command is
 * syntactically correct, no exceptions can result from its execution.
 * <p>
 * If the printer is in any resource state, such as page-segment state, overlay state, font state, or any derivative of
 * these states, the partial resource is deleted before the printer returns to home state. In home state, an SHS
 * command is treated as a No Operation (NOP) command.
 */
public class SetHomeStateCommand extends IpdsCommand {

    /**
     * Constructs the {@link SetHomeStateCommand}.
     * @param command the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     */
    protected SetHomeStateCommand(final byte[] command) throws InvalidIpdsCommandException {
        super(command, IpdsCommandId.SHS);
    }
}
