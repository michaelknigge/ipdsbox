package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class ColorFidelityTriplet extends Triplet {

    private int continuationRule;
    private int reportingRule;
    private int substitutionRule;

    /**
     * Creates a {@link ColorFidelityTriplet} with default values.
     */
    public ColorFidelityTriplet() {
        super(TripletId.ColorFidelity);

        this.continuationRule = 0x02;
        this.reportingRule = 0x02;
        this.substitutionRule = 0x01;
    }

    /**
     * Creates a {@link ColorFidelityTriplet} from the given data.
     */
    public ColorFidelityTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.ColorFidelity);

        this.continuationRule = ipds.readUnsignedByte();
        ipds.skip(1);
        this.reportingRule = ipds.readUnsignedByte();
        ipds.skip(1);
        this.substitutionRule = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(8);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedByte(this.continuationRule);
        out.writeUnsignedByte(0);
        out.writeUnsignedByte(this.reportingRule);
        out.writeUnsignedByte(0);
        out.writeUnsignedByte(this.substitutionRule);
        out.writeUnsignedByte(0);
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
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", continueRule=0x" + Integer.toHexString(this.continuationRule) +
                ", report=0x" + Integer.toHexString(this.reportingRule) +
                ", substitute=0x" + Integer.toHexString(this.substitutionRule) +
                "}";
    }
}
