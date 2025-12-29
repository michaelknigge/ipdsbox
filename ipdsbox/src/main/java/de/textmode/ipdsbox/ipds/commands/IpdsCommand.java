package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * An IPDS command sent to the printer. This is the abstract super class of all
 * IPDS commands.
 */
public abstract class IpdsCommand {

    private final int commandCode;
    private final IpdsCommandFlags commandFlags;
    private int correlationId;

    /**
     * Constructor for building up new IPDS commands.
     */
    public IpdsCommand(final IpdsCommandId commandId) {
        this.commandCode = commandId.getValue();
        this.commandFlags = new IpdsCommandFlags();
        this.correlationId = 0;
    }

    /**
     * Constructor for known IPDS commands. The Constructor assumes that the total lengths of the IPDS command
     * as well as the IPDS command ID have already been read from the {@link IpdsByteArrayInputStream}.
     */
    protected IpdsCommand(
            final IpdsByteArrayInputStream ipds,
            final IpdsCommandId commandId) throws IOException {

        this(ipds, commandId.getValue());
    }

    /**
     * Constructor for unknown IPDS commands. The Constructor assumes that the total lengths of the IPDS command
     * as well as the IPDS command ID have already been read from the {@link IpdsByteArrayInputStream}.
     */
    protected IpdsCommand(final IpdsByteArrayInputStream ipds, final int commandCode) throws IOException {
        this.commandCode = commandCode;
        this.commandFlags = new IpdsCommandFlags((byte) ipds.readUnsignedByte());

        this.correlationId = this.commandFlags.hasCorrelationID()
                ? ipds.readUnsignedInteger16()
                : 0;

        // From here, the concrete IpdsCommand implementations can use the IpdsByteArrayInputStream
        // to read the specific data of the IpdsCommand. Reading will start at the "data" position
        // of the IPDS command.
    }

    /**
     * Writes this {@link IpdsCommandId} to the given {@link IpdsByteArrayOutputStream}.
     */
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException, InvalidIpdsCommandException {
        final IpdsByteArrayOutputStream dataStream = new IpdsByteArrayOutputStream();
        this.writeDataTo(dataStream);

        final byte[] data = dataStream.toByteArray();

        // Length = 2 Bytes    \
        // Command = 2 Bytes    --> 5 bytes
        // Flags = 1 Byte      /
        // Correlation ID = 2 Bytes (OPTIONAL FIELD!)
        // Data = 0-n Bytes
        final int len = 5 + data.length + (this.commandFlags.hasCorrelationID() ? 2 : 0);

        ipds.writeUnsignedInteger16(len);
        ipds.writeUnsignedInteger16(this.commandCode);
        ipds.writeUnsignedByte(this.commandFlags.getFlags());

        if (this.commandFlags.hasCorrelationID()) {
            ipds.writeUnsignedInteger16(this.correlationId);
        }

        ipds.writeBytes(data);
    }

    /**
     * Writes the binary data that is specific for the IPDS command to
     * the given {@link IpdsByteArrayOutputStream}.
     */
    protected abstract void writeDataTo(IpdsByteArrayOutputStream ipds) throws IOException, InvalidIpdsCommandException;

    /**
     * Returns the IPDS Command ID.
     */
    public final int getCommandCodeId() {
        return this.commandCode;
    }

    /**
     * Returns the IPDS Command. Might return <code>null</code> if the IPDS command is unknown.
     */
    public final IpdsCommandId getCommandCode() {
        return IpdsCommandId.getIfKnown(this.commandCode);
    }

    /**
     * Determines the {@link IpdsCommandFlags} of the {@link IpdsCommand}.
     */
    public final IpdsCommandFlags getCommandFlags() {
        return this.commandFlags;
    }

    /**
     * Returns the correlation ID.
     */
    public int getCorrelationId() {
        return this.correlationId;
    }

    /**
     * Sets the correlation ID.
     */
    public void setCorrelationId(final int correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * Returns a string representation of the {@link IpdsCommand}.
     */
    public final String getDescription() {
        final IpdsCommandId commandId = IpdsCommandId.getIfKnown(this.commandCode);

        return commandId != null
                ? commandId + " - " + commandId.getDescription()
                : "??? - Unknown IPDS command with ID=" + this.commandCode;
    }
}
