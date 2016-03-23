package mk.ipdsbox.ipds;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import mk.ipdsbox.core.InvalidIpdsCommandException;
import mk.ipdsbox.core.IpdsboxRuntimeException;

/**
 * An IPDS command sent to the printer. This is the abstract super class of all
 * IPDS commands.
 */
public abstract class IpdsCommand {

    private final byte[] command;
    private final int commandLength;
    private final IpdsCommandId commandCode;
    private final IpdsCommandFlags commandFlags;

    /**
     * Constructor.
     * @param command the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @param expectedCommandId the expected command id in bytes 2+3 of the IDPS command.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     * @throws IpdsboxRuntimeException if the passed data is invalid for the concrete
     * implementation of the {@link IpdsCommand}.
     */
    protected IpdsCommand(final byte[] command, final IpdsCommandId expectedCommandId)
        throws InvalidIpdsCommandException {
        this.commandLength = (command[0] & 0xFF) << 8 | (command[1] & 0xFF);
        if (command.length != this.commandLength) {
            throw new InvalidIpdsCommandException(String.format(
                "The length of the IPDS command (%1$d) does not match the length specified in the length field (%2$d).",
                command.length,
                this.commandLength));
        }

        final int intValue = (command[2] & 0xFF) << 8 | (command[3] & 0xFF);
        this.commandCode = IpdsCommandId.getForIfExists(intValue);
        if (this.commandCode == null) {
            throw new InvalidIpdsCommandException(String.format(
                "The IPDS command has the command id X'%1$s' which is unknown", Integer.toHexString(intValue)));
        }

        if (this.commandCode != expectedCommandId) {
            throw new IpdsboxRuntimeException(String.format(
                "An IpdsCommand with command id X'%1$s' was constructed but command id X'%2$s' was expected.",
                Integer.toHexString(this.commandCode.getValue()),
                Integer.toHexString(expectedCommandId.getValue())));
        }

        this.commandFlags = new IpdsCommandFlags(command[4]);
        this.command = command;
    }

    /**
     * Determines the length of the {@link IpdsCommand}.
     * @return the length of the {@link IpdsCommand}.
     */
    public final int getCommandLength() {
        return this.commandLength;
    }

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
     * Returns the data part of the IPDS command, which is the part after the flag byte or, if present,
     * the correlation Id.
     * @return the data part of the IPDS command.
     */
    public final byte[] getData() {
        final int offset = this.getCommandFlags().hasCorrelationID() ? 7 : 5;
        final int length = this.getCommandLength() - offset;

        final byte[] data = new byte[length];
        System.arraycopy(this.command, offset, data, 0, length);

        return data;
    }

    /**
     * Returns the data part of the IPDS command, which is the part after the flag byte or, if present,
     * the correlation Id. The data is returned as a String, decoded with a IBM-500 character set.
     * @return the data part of the IPDS command as a String object.
     * @throws UnsupportedEncodingException if the character set IBM-500 is unsupported.
     */
    public final String getDataAsString() throws UnsupportedEncodingException {
        return this.getDataAsString(Charset.forName("IBM-500"));
    }

    /**
     * Returns the data part of the IPDS command, which is the part after the flag byte or, if present,
     * the correlation Id.
     * @param charset the name of the character set that shall be used for decoding the raw data.
     * @return the data part of the IPDS command as a String object.
     * @throws UnsupportedEncodingException if the given character set is unsupported.
     */
    public final String getDataAsString(final String charset) throws UnsupportedEncodingException {
        return this.getDataAsString(Charset.forName(charset));
    }

    /**
     * Returns the data part of the IPDS command, which is the part after the flag byte or, if present,
     * the correlation Id.
     * @param charset the character set that shall be used for decoding the raw data.
     * @return the data part of the IPDS command as a String object.
     * @throws UnsupportedEncodingException if the given character set is unsupported.
     */
    public final String getDataAsString(final Charset charset) throws UnsupportedEncodingException {
        return new String(this.getData(), charset);
    }

    /**
     * Returns a string representation of the {@link IpdsCommand}.
     * @return a string starting with the acronym of the IPDS Command followed by a description.
     */
    @Override
    public final String toString() {
        return this.commandCode.toString() + " - " + this.commandCode.getDescription();

    }
}
