package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class TextFidelityTriplet extends Triplet {

    private int continuationRule;
    private int reportingRule;

    public TextFidelityTriplet(final IpdsByteArrayInputStream ipds) throws IOException, UnknownTripletException {
        super(ipds, TripletId.TextFidelity);

        this.continuationRule = ipds.readUnsignedByte();
        ipds.skip(1);
        this.reportingRule = ipds.readUnsignedByte();
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
        return "TextFidelity{" +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", continueRule=0x" + Integer.toHexString(this.continuationRule) +
                ", report=0x" + Integer.toHexString(this.reportingRule) +
                "}";
    }
}
