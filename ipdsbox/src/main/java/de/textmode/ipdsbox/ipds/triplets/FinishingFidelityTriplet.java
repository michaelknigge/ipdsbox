package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class FinishingFidelityTriplet extends Triplet {

    private int continuationRule;
    private int reportingRule;

    public FinishingFidelityTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.FinishingFidelity);

        this.continuationRule = this.getStream().readUnsignedByte();
        this.getStream().skip(1);
        this.reportingRule = this.getStream().readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(7);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.continuationRule);
        out.writeUnsignedByte(0);
        out.writeUnsignedByte(this.reportingRule);
        out.writeUnsignedInteger16(0);
    }

    /**
     * Returns the Continuation Rule.
     */
    public int getContinuationRule() {
        return this.continuationRule;
    }

    /**
     * Sets the Continuation Rule.
     */
    public void setContinuationRule(final int continuationRule) {
        this.continuationRule = continuationRule;
    }

    /**
     * Returns the Reporting Rule.
     */
    public int getReportingRule() {
        return this.reportingRule;
    }

    /**
     * Sets the Reporting Rule.
     */
    public void setReportingRule(final int reportingRule) {
        this.reportingRule = reportingRule;
    }

    @Override
    public String toString() {
        return "FinishingFidelity{" +
                "length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", continueRule=0x" + Integer.toHexString(this.continuationRule) +
                ", report=0x" + Integer.toHexString(this.reportingRule) + '}';
    }
}
