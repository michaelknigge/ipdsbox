package de.textmode.ipdsbox.ipds.commands;

import de.textmode.ipdsbox.core.BitUtils;

/**
 * This class encapsulates all valid IPDS command (and acknowledge  reply) flags.
 */
public final class IpdsCommandFlags {

    private byte flags;

    /**
     * Constructs an {@link IpdsCommandFlags} object with no bits set.
     */
    public IpdsCommandFlags() {
        this.flags = 0x00;
    }

    /**
     * Constructs an {@link IpdsCommandFlags} object.
     * @param flags raw flags byte from an IPDS command.
     */
    public IpdsCommandFlags(final byte flags) {
        this.flags = (byte) (flags & 0xFF);
    }

    /**
     * Determines if the host requests the printer to send an Acknowledge Reply.
     * @return <code>true</code> if any only the host requests the printer to send an Acknowledge Reply.
     */
    public boolean isAcknowledgmentRequired() {
        return BitUtils.isBitSet(0, this.flags);
    }

    /**
     * Sets or unsets the "Acknowledge Reply required" flag (bit 0).
     */
    public void isAcknowledgmentRequired(final boolean trueOrFalse) {
        if (trueOrFalse) {
            this.flags = BitUtils.setBit(0, this.flags);
        } else {
            this.flags = BitUtils.unsetBit(0, this.flags);
        }
    }

    /**
     * Determines if the host is requesting continuation of the current Acknowledge Reply <b>OR</b>
     * the printer has more ACKs to send.
     * @return <code>true</code> if any only the host is requesting continuation of the current Acknowledge Reply
     * <b>OR</b> the printer can continue the acknowledge reply.
     */
    public boolean isAcknowledgmentContinuationRequested() {
        return BitUtils.isBitSet(2, this.flags);
    }

    /**
     * Sets or unsets the "Acknowledge Continuation Requested" flag (bit 2).
     */
    public void isAcknowledgmentContinuationRequested(final boolean trueOrFalse) {
        if (trueOrFalse) {
            this.flags = BitUtils.setBit(2, this.flags);
        } else {
            this.flags = BitUtils.unsetBit(2, this.flags);
        }
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
     * Sets or unsets the "Long Acknowledge Reply Accepted" flag (bit 3).
     */
    public void isLongAcknowledgeReplyAccepted(final boolean trueOrFalse) {
        if (trueOrFalse) {
            this.flags = BitUtils.setBit(3, this.flags);
        } else {
            this.flags = BitUtils.unsetBit(3, this.flags);
        }
    }

    /**
     * Determines if the IPDS command contains a two byte correlation ID.
     * @return <code>true</code> if the IPDS command contains a two byte correlation ID.
     */
    public boolean hasCorrelationID() {
        return BitUtils.isBitSet(1, this.flags);
    }

    /**
     * Sets or unsets the "Long Acknowledge Reply Accepted" flag (bit 1).
     */
    public void hasCorrelationID(final boolean trueOrFalse) {
        if (trueOrFalse) {
            this.flags = BitUtils.setBit(1, this.flags);
        } else {
            this.flags = BitUtils.unsetBit(1, this.flags);
        }
    }

    /**
     * Determines if the IPDS acknowledge reply indicates that additional information is available and can
     * be retrieved using "XOA Obtain Additional Exception Information (OAEI)". Note that this flag is only
     * valid for NACKs and is ignored for AKCs.
     *
     * @return <code>true</code> if additional information is available.
     */
    public boolean isAdditionalInformationAvailable() {
        return BitUtils.isBitSet(6, this.flags);
    }

    /**
     * Sets the flag if additional information is available. Note that this flag is only
     * valid for NACKs and is ignored for AKCs.
     */
    public void isAdditionalInformationAvailable(final boolean trueOrFalse) {
        if (trueOrFalse) {
            this.flags = BitUtils.setBit(6, this.flags);
        } else {
            this.flags = BitUtils.unsetBit(6, this.flags);
        }
    }

    /**
     * Determines if the IPDS acknowledge reply indicates that the "Persistent NACK flag" is set.
     *
     * @return <code>true</code> if the IPDS acknowledge reply indicates that the "Persistent NACK flag" is set.
     */
    public boolean isPersistentNack() {
        return BitUtils.isBitSet(7, this.flags);
    }

    /**
     * Sets the "Persistent NACK flag".
     */
    public void isPersistentNack(final boolean trueOrFalse) {
        if (trueOrFalse) {
            this.flags = BitUtils.setBit(7, this.flags);
        } else {
            this.flags = BitUtils.unsetBit(7, this.flags);
        }
    }

    /**
     * Returns the flag byte.
     */
    public byte getFlags() {
        return (byte) (this.flags & 0xFF);
    }

    /**
     * Sets the flag byte.
     */
    public void setFlags(final byte flags) {
        this.flags = (byte) (flags & 0xFF);
    }

    @Override
    public String toString() {
        return "IpdsCommandFlags{"
                + "flags=" + Integer.toHexString(this.flags)
                + '}';
    }
}
