package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.commands.IpdsCommand;
import de.textmode.ipdsbox.ipds.commands.IpdsCommandId;

/**
 * The printer uses the Acknowledge Reply to return information such as page counters, copy counters, sense
 * data, and any requested information to the presentation services program. The presentation services program
 * uses the acknowledge data to maintain control over the printing process and to initiate exception-recovery
 * procedures when necessary.
 */
public final class AcknowledgeReply extends IpdsCommand {

    private int acktype;

    private int stackedPageCounter;
    private int stackedCopyCounter;

    // the following counters are only valid/available in Eighteen-Byte Counter Format
    private int receivedPageCounter;
    private int committedPageCounter;
    private int committedCopyCounter;
    private int operatorViewingPageCounter;
    private int operatorViewingCopyCounter;
    private int jamRecoveryPageCounter;
    private int jamRecoveryCopyCounter;

    private AcknowledgeData acknowledgeData;


    /**
     * Constructs the {@link AcknowledgeReply}.
     */
    public AcknowledgeReply() {
        this(0xFF);
    }

    /**
     * Constructs the {@link AcknowledgeReply}.
     */
    public AcknowledgeReply(final int acktype) {
        super(IpdsCommandId.ACK);

        this.acktype = acktype;
        this.initCounters();
    }

    /**
     * Constructs the {@link AcknowledgeReply}.
     */
    public AcknowledgeReply(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException {
        super(ipds, IpdsCommandId.ACK);

        this.acktype = ipds.readUnsignedByte();

        if (this.acktype == 0xFF) {
            this.initCounters();
        } else if (getCounterLen(this.acktype) == 4) {
            assert ipds.bytesAvailable() >= 4;
            this.stackedPageCounter = ipds.readUnsignedInteger16();
            this.stackedCopyCounter = ipds.readUnsignedInteger16();
        } else {
            assert ipds.bytesAvailable() >= 18;
            this.receivedPageCounter = ipds.readUnsignedInteger16();
            this.committedPageCounter = ipds.readUnsignedInteger16();
            this.committedCopyCounter = ipds.readUnsignedInteger16();
            this.operatorViewingPageCounter = ipds.readUnsignedInteger16();
            this.operatorViewingCopyCounter = ipds.readUnsignedInteger16();
            this.jamRecoveryPageCounter = ipds.readUnsignedInteger16();
            this.jamRecoveryCopyCounter = ipds.readUnsignedInteger16();
            this.stackedPageCounter = ipds.readUnsignedInteger16();
            this.stackedCopyCounter = ipds.readUnsignedInteger16();
        }

        this.acknowledgeData = AcknowledgeDataFactory.create(this.acktype, ipds);
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException, InvalidIpdsCommandException {

        ipds.writeUnsignedByte(this.acktype);

        if (this.acktype == 0xFF) {
            // no counters..
        } else if (getCounterLen(this.acktype) == 4) {
            ipds.writeUnsignedInteger16(this.stackedPageCounter);
            ipds.writeUnsignedInteger16(this.stackedCopyCounter);
        } else {
            ipds.writeUnsignedInteger16(this.receivedPageCounter);
            ipds.writeUnsignedInteger16(this.committedPageCounter);
            ipds.writeUnsignedInteger16(this.committedCopyCounter);
            ipds.writeUnsignedInteger16(this.operatorViewingPageCounter);
            ipds.writeUnsignedInteger16(this.operatorViewingCopyCounter);
            ipds.writeUnsignedInteger16(this.jamRecoveryPageCounter);
            ipds.writeUnsignedInteger16(this.jamRecoveryCopyCounter);
            ipds.writeUnsignedInteger16(this.stackedPageCounter);
            ipds.writeUnsignedInteger16(this.stackedCopyCounter);
        }

        if (this.acktype != 0xFF) {
            this.acknowledgeData.writeTo(ipds);
        }
    }

    private static int getCounterLen(final int ackType) throws InvalidIpdsCommandException {
        if (ackType >= 0 && ackType <= 8) {
            return 4;
        } if (ackType == 0x80) {
            return 4;
        } else if (ackType == 0xFF) {
            return 0;
        } else if (ackType >= 0x40 && ackType <= 0x48) {
            return 18;
        } else if (ackType >= 0xC0) {
            return 18;
        }

        throw new InvalidIpdsCommandException("Unknown acknowledge type " + Integer.toHexString(ackType));
    }

    private void initCounters() {
        this.stackedPageCounter = 0;
        this.stackedCopyCounter = 0;

        // the following counters are only valid/available in Eighteen-Byte Counter Format
        this.receivedPageCounter = 0;
        this.committedPageCounter = 0;
        this.committedCopyCounter = 0;
        this.operatorViewingPageCounter = 0;
        this.operatorViewingCopyCounter = 0;
        this.jamRecoveryPageCounter = 0;
        this.jamRecoveryCopyCounter = 0;
    }

    /**
     * Returns the acknowledge type.
     */
    public int getAcknowledgeType() {
        return this.acktype;
    }

    /**
     * Sets the acknowledge type.
     */
    public void setAcknowledgeType(final int pageId) {
        this.acktype = pageId;
    }

    /**
     * Returns the stacked page counter.
     */
    public int getStackedPageCounter() {
        return this.stackedPageCounter;
    }

    /**
     * Sets the stacked page counter.
     */
    public void setStackedPageCounter(final int stackedPageCounter) {
        this.stackedPageCounter = stackedPageCounter;
    }

    /**
     * Returns the stacked copy counter.
     */
    public int getStackedCopyCounter() {
        return this.stackedCopyCounter;
    }

    /**
     * Sets the stacked copy counter.
     */
    public void setStackedCopyCounter(final int stackedCopyCounter) {
        this.stackedCopyCounter = stackedCopyCounter;
    }

    /**
     * Returns the received page counter.
     */
    public int getReceivedPageCounter() {
        return this.receivedPageCounter;
    }

    /**
     * Sets the received page counter.
     */
    public void setReceivedPageCounter(final int receivedPageCounter) {
        this.receivedPageCounter = receivedPageCounter;
    }

    /**
     * Returns the Committed page counter.
     */
    public int getCommittedPageCounter() {
        return this.committedPageCounter;
    }

    /**
     * Sets the Committed page counter.
     */
    public void setCommittedPageCounter(final int committedPageCounter) {
        this.committedPageCounter = committedPageCounter;
    }

    /**
     * Returns the Committed copy counter.
     */
    public int getCommittedCopyCounter() {
        return this.committedCopyCounter;
    }

    /**
     * Sets the Committed copy counter.
     */
    public void setCommittedCopyCounter(final int committedCopyCounter) {
        this.committedCopyCounter = committedCopyCounter;
    }

    /**
     * Returns the operator viewing page counter.
     */
    public int getOperatorViewingPageCounter() {
        return this.operatorViewingPageCounter;
    }

    /**
     * Sets the operator viewing page counter.
     */
    public void setOperatorViewingPageCounter(final int operatorViewingPageCounter) {
        this.operatorViewingPageCounter = operatorViewingPageCounter;
    }

    /**
     * Returns the operator viewing copy counter.
     */
    public int getOperatorViewingCopyCounter() {
        return this.operatorViewingCopyCounter;
    }

    /**
     * Sets the operator viewing copy counter.
     */
    public void setOperatorViewingCopyCounter(final int operatorViewingCopyCounter) {
        this.operatorViewingCopyCounter = operatorViewingCopyCounter;
    }

    /**
     * Returns the jam recovery page counter.
     */
    public int getJamRecoveryPageCounter() {
        return this.jamRecoveryPageCounter;
    }

    /**
     * Sets the jam recovery page counter.
     */
    public void setJamRecoveryPageCounter(final int jamRecoveryPageCounter) {
        this.jamRecoveryPageCounter = jamRecoveryPageCounter;
    }

    /**
     * Returns the jam recovery copy counter.
     */
    public int getJamRecoveryCopyCounter() {
        return this.jamRecoveryCopyCounter;
    }

    /**
     * Sets the jam recovery copy counter.
     */
    public void setJamRecoveryCopyCounter(final int jamRecoveryCopyCounter) {
        this.jamRecoveryCopyCounter = jamRecoveryCopyCounter;
    }

    /**
     * Returns the special data area of the Ack.
     */
    public AcknowledgeData getAcknowledgeData() {
        return this.acknowledgeData;
    }

    /**
     * Sets the special data area of the Ack.
     */
    public void setAcknowledgeData(final AcknowledgeData acknowledgeData) {
        this.acknowledgeData = acknowledgeData;
    }
}
