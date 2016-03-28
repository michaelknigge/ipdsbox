package mk.ipdsbox.ipds.commands;

import mk.ipdsbox.core.BitUtils;

/**
 * This class encapsulates all valid IPDS command flags.
 */
public final class IpdsCommandFlags {

    private final byte flags;

    /**
     * Constructs an {@link IpdsCommandFlags} object.
     * @param flags raw flags byte from an IPDS command.
     */
    public IpdsCommandFlags(final byte flags) {
        this.flags = flags;
    }

    /**
     * Determines if the host requests the printer to send an Acknowledge Reply.
     * @return <code>true</code> if any only the host requests the printer to send an Acknowledge Reply.
     */
    public boolean isAcknowledgmentRequired() {
        return BitUtils.isBitSet(0, this.flags);

    }

    /**
     * Determines if the host is requesting continuation of the current Acknowledge Reply.
     * @return <code>true</code> if any only the host is requesting continuation of the current Acknowledge Reply.
     */
    public boolean isAcknowledgmentContinuationRequested() {
        return BitUtils.isBitSet(2, this.flags);
    }

    /**
     * Determines if the host can accept long acknowledge replies (up to 65,535 bytes long).
     * @return <code>true</code> if any only the host can accept long acknowledge replies (up
     * to 65,535 bytes long). <code>false</code> if the host can only accept short acknowledge
     * replies (up to 256 bytes long).
     */
    public boolean isLongAcknowledgeReplyAccepted() {
        return BitUtils.isBitSet(3, this.flags);
    }

    /**
     * Determines if the IPDS command contains a two byte correlation ID.
     * @return <code>true</code> if any only ihe IPDS command contains a two byte correlation ID.
     */
    public boolean hasCorrelationID() {
        return BitUtils.isBitSet(1, this.flags);
    }

}
