package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ColorFidelityTriplet extends Triplet {

    private int continuationRule;
    private int reportingRule;
    private int substitutionRule;

    public ColorFidelityTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.ColorFidelity);

        this.continuationRule = this.getStream().readUnsignedByte();
        this.getStream().skip(1);
        this.reportingRule = this.getStream().readUnsignedByte();
        this.getStream().skip(1);
        this.substitutionRule = this.getStream().readUnsignedByte();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(8);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.continuationRule);
        out.writeUnsignedByte(0);
        out.writeUnsignedByte(this.reportingRule);
        out.writeUnsignedByte(0);
        out.writeUnsignedByte(this.substitutionRule);
        out.writeUnsignedByte(0);

        return out.toByteArray();
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


    /**
     * Returns the Substitution Rule.
     */
    public int getSubstitutionRule() {
        return this.substitutionRule;
    }

    /**
     * Sets the Substitution Rule.
     */
    public void setSubstitutionRule(final int substitutionRule) {
        this.substitutionRule = substitutionRule;
    }

    @Override
    public String toString() {
        return "ColorFidelity{" +
                "length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", continueRule=0x" + Integer.toHexString(this.continuationRule) +
                ", report=0x" + Integer.toHexString(this.reportingRule) +
                ", substitute=0x" + Integer.toHexString(this.substitutionRule) + '}';
    }
}
