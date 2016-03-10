package mk.ipdsbox.ipds;

import mk.ipdsbox.core.InvalidIpdsCommandException;

/**
 * An IPDS command sent to the printer. This is the abstract super class of all
 * IPDS commands.
 */
public abstract class IpdsCommand {

    private final int commandLength;
    private final IpdsCommandId commandCode;
    private final IpdsCommandFlags commandFlags;

    /**
     * Constructor.
     * @param data the raw IPDS data stream, not including the part of the PPD/PPR protocol.
     * @throws InvalidIpdsCommandException if there is something wrong with the supplied IPDS data stream.
     */
    protected IpdsCommand(final byte[] data) throws InvalidIpdsCommandException {
        this.commandLength = (data[0] & 0xFF) << 8 | (data[1] & 0xFF);
        if (data.length != this.commandLength) {
            throw new InvalidIpdsCommandException(String.format(
                "The length of the IPDS command (%1$d) does not match the length specified in the length field (%2$d).",
                data.length,
                this.commandLength));
        }

        final int intValue = (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
        this.commandCode = IpdsCommandId.getForIfExists(intValue);
        if (this.commandCode == null) {
            throw new InvalidIpdsCommandException(String.format(
                "The IPDS command has the command id X'%1$s' which is unknown", Integer.toHexString(intValue)));
        }

        this.commandFlags = new IpdsCommandFlags(data[4]);
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
}
