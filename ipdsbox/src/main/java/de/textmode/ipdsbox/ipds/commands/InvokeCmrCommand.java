package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

/**
 * The Apply Finishing Operations (AFO) command is valid only in home state and directs the printer to apply
 * zero or more finishing operations to the current sheet and each copy of that sheet. The current sheet is the
 * sheet on which the first copy of the next received page is printed. The operations are not applied to sheets
 * after the copies of the current sheet.
 */
public final class InvokeCmrCommand extends IpdsCommand {

    private int invocationFlags;
    private List<Integer> hostAssignedIds = new ArrayList<>();

    /**
     * Constructs the {@link InvokeCmrCommand}.
     */
    public InvokeCmrCommand() {
        super(IpdsCommandId.ICMR);
    }

    /**
     * Constructs the {@link InvokeCmrCommand}.
     */
    public InvokeCmrCommand(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException, UnknownTripletException {
        super(ipds, IpdsCommandId.ICMR);

        this.invocationFlags = ipds.readUnsignedByte();
        ipds.skip(4);

        while (ipds.bytesAvailable() >= 2) {
            this.hostAssignedIds.add(ipds.readUnsignedInteger16());
        }
    }

    @Override
    void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedByte(this.invocationFlags);
        ipds.writeUnsignedInteger32(0x00000000);

        for (final Integer id : this.hostAssignedIds) {
            ipds.writeUnsignedInteger16(id);
        }
    }

    /**
     * Returns the invocation flags.
     */
    public int getInvocationFlags() {
        return this.invocationFlags;
    }

    /**
     * Sets the invocation flags.
     */
    public void setInvocationFlags(final int invocationFlags) {
        this.invocationFlags = invocationFlags;
    }

    /**
     * Returns the HAIDs of this IPDS command.
     */
    public List<Integer> getHostAssignedIds() {
        return this.hostAssignedIds;
    }

    /**
     * Sets the HAIDs of this IPDS command.
     */
    public void setHostAssignedIds(final List<Integer> hostAssignedIds) {
        this.hostAssignedIds = hostAssignedIds;
    }
}
