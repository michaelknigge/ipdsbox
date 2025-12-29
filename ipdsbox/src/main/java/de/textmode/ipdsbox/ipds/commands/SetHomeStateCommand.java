package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

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
public final class SetHomeStateCommand extends IpdsCommand {

    /**
     * Constructs the {@link SetHomeStateCommand}.
     */
    public SetHomeStateCommand() throws InvalidIpdsCommandException, IOException {
        super(IpdsCommandId.SHS);
    }

    /**
     * Constructs the {@link SetHomeStateCommand}.
     */
    SetHomeStateCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.SHS);
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) {
        // No data is transmitted with this command.
    }

    @Override
    public String toString() {
        return "SetHomeStateCommand{}";
    }
}
