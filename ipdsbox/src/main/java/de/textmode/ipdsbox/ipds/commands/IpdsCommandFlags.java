package de.textmode.ipdsbox.ipds.commands;

import de.textmode.ipdsbox.core.BitUtils;

/**
 * This class encapsulates all valid IPDS command flags.
 */
public final class IpdsCommandFlags {

    private byte flags;

    /**
     * Constructs an {@link IpdsCommandFlags} object with not bits set.
     */
    public IpdsCommandFlags() {
        this.flags = 0x00;
    }

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
     * Sets or unsets the "Acknowledge Reply required" flag.
     */
    public void isAcknowledgmentRequired(final boolean trueOrFalse) {
        if (trueOrFalse) {
            this.flags = BitUtils.setBit(0, this.flags);
        } else {
            this.flags = BitUtils.unsetBit(0, this.flags);
        }
    }

    /**
     * Determines if the host is requesting continuation of the current Acknowledge Reply.
     * @return <code>true</code> if any only the host is requesting continuation of the current Acknowledge Reply.
     */
    public boolean isAcknowledgmentContinuationRequested() {
        return BitUtils.isBitSet(2, this.flags);
    }

    /**
     * Sets or unsets the "Acknowledge Continuation Requested" flag.
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
     * Sets or unsets the "Long Acknowledge Reply Accepted" flag.
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
     * Determines if the IPDS command contains a two byte correlation ID.
     * @return <code>true</code> if the IPDS command contains a two byte correlation ID.
     */
    public void hasCorrelationID(final boolean trueOrFalse) {
        if (trueOrFalse) {
            this.flags = BitUtils.setBit(1, this.flags);
        } else {
            this.flags = BitUtils.unsetBit(1, this.flags);
        }
    }

    /**
     * Returns the flag byte.
     */
    public byte getFlags() {
        return this.flags;
    }

    /**
     * Sets the flag byte.
     */
    public void setFlags(final byte flags) {
        this.flags = flags;
    }
}
