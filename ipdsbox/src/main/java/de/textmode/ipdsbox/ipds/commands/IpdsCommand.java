package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * An IPDS command sent to the printer. This is the abstract super class of all
 * IPDS commands.
 */
public abstract class IpdsCommand {

    private final IpdsCommandId commandCode;
    private final IpdsCommandFlags commandFlags;
    private int correlationId;

    protected IpdsCommand(final IpdsCommandId commandCode) {
        this.commandCode = commandCode;
        this.commandFlags = new IpdsCommandFlags();
        this.correlationId = 0;
    }

    /**
     * Constructor.
     * @param ipds the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @param expectedCommandId the expected command id in bytes 2+3 of the IDPS command.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     * @throws IpdsboxRuntimeException if the passed data is invalid for the concrete
     * implementation of the {@link IpdsCommand}.
     */
    protected IpdsCommand(
            final IpdsByteArrayInputStream ipds,
            final IpdsCommandId expectedCommandId) throws InvalidIpdsCommandException, IOException {

        final int availableLength = ipds.bytesAvailable();
        final int commandLength = ipds.readUnsignedInteger16();

        if (availableLength != commandLength) {
            throw new InvalidIpdsCommandException(String.format(
                "The length of the IPDS command (%1$d) does not match the length specified in the length field (%2$d).",
                availableLength,
                commandLength));
        }

        final int commandCodeValue = ipds.readUnsignedInteger16();
        this.commandCode = IpdsCommandId.getForIfExists(commandCodeValue);

        if (this.commandCode == null) {
            throw new InvalidIpdsCommandException(String.format(
                "The IPDS command has the command id X'%1$s' which is unknown", Integer.toHexString(commandCodeValue)));
        }

        if (this.commandCode != expectedCommandId) {
            throw new IpdsboxRuntimeException(String.format(
                "An IpdsCommand with command id X'%1$s' was constructed but command id X'%2$s' was expected.",
                Integer.toHexString(this.commandCode.getValue()),
                Integer.toHexString(expectedCommandId.getValue())));
        }

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
        ipds.writeUnsignedInteger16(this.commandCode.getValue());
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
     * Determines the {@link IpdsCommandId} of the {@link IpdsCommand}.
     * @return the {@link IpdsCommandId}.
     */
    public final IpdsCommandId getCommandCode() {
        return this.commandCode;
    }

    /**
     * Determines the {@link IpdsCommandFlags} of the {@link IpdsCommand}.
     * @return the {@link IpdsCommandFlags}.
     */
    public final IpdsCommandFlags getCommandFlags() {
        return this.commandFlags;
    }

    /**
     * Returns the correlation ID.
     * @return the correlation ID.
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
     * @return a string starting with the acronym of the IPDS Command followed by a description.
     */
    public final String getDescription() {
        return this.commandCode.toString() + " - " + this.commandCode.getDescription();
    }
}
