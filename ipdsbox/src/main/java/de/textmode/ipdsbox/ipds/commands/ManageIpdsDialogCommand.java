package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Manage IPDS Dialog (MID) command is valid only in home state and causes the printer to either start or
 * end an IPDS dialog.
 */
public final class ManageIpdsDialogCommand extends IpdsCommand {

    private int type;

    /**
     * Constructs the {@link ManageIpdsDialogCommand}.
     */
    public ManageIpdsDialogCommand() {
        this(0);
    }

    /**
     * Constructs the {@link ManageIpdsDialogCommand}.
     */
    public ManageIpdsDialogCommand(final int type) {
        super(IpdsCommandId.MID);

        this.type = type;
    }

    /**
     * Constructs the {@link ManageIpdsDialogCommand}.
     */
    ManageIpdsDialogCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.MID);

        this.type = ipds.readUnsignedByte();
    }

    /**
     * Returns the type.
     */
    public long getType() {
        return this.type;
    }

    /**
     * Sets the type.
     */
    public void setType(final int type) {
        this.type = type;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedByte(this.type);
    }

    @Override
    public String toString() {
        return "ManageIpdsDialogCommand{"
                + "type=0x" + Integer.toHexString(this.type)
                + '}';
    }
}
