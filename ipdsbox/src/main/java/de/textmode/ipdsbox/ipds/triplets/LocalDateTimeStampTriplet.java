package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class LocalDateTimeStampTriplet extends Triplet {

    private int stampType;
    private String yearPart1;
    private String yearPart2;
    private String day;
    private String hour;
    private String minute;
    private String second;
    private String hundredth;

    public LocalDateTimeStampTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.ObjectOffset);
        this.readFrom(new IpdsByteArrayInputStream(raw));
    }

    @Override
    public void readFrom(final IpdsByteArrayInputStream in) throws IOException {
        this.stampType = in.readUnsignedByte();
        this.yearPart1 = in.readEbcdicString(1);
        this.yearPart2 = in.readEbcdicString(2);
        this.day = in.readEbcdicString(3);
        this.hour = in.readEbcdicString(2);
        this.minute = in.readEbcdicString(2);
        this.second = in.readEbcdicString(2);
        this.hundredth = in.readEbcdicString(2);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();
        out.writeUnsignedByte(0x11);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.stampType);
        out.writeEbcdicString(this.yearPart1, 1);
        out.writeEbcdicString(this.yearPart2, 2);
        out.writeEbcdicString(this.day, 3);
        out.writeEbcdicString(this.hour, 2);
        out.writeEbcdicString(this.minute, 2);
        out.writeEbcdicString(this.second, 2);
        out.writeEbcdicString(this.hundredth, 2);

        return out.toByteArray();
    }

    /**
     * Returns the StampType.
     */
    public int getStampType() {
        return this.stampType;
    }

    /**
     * Sets the StampType.
     */
    public void setStampType(final int stampType) {
        this.stampType = stampType;
    }

    /**
     * Returns the Year (part 1).
     */
    public String getYearPart1() {
        return this.yearPart1;
    }

    /**
     * Sets the Year (part 1).
     */
    public void setYearPart1(final String yearPart1) {
        this.yearPart1 = yearPart1;
    }

    /**
     * Returns the Year (part 2).
     */
    public String getYearPart2() {
        return this.yearPart2;
    }

    /**
     * Sets the Year (part 2).
     */
    public void setYearPart2(final String yearPart2) {
        this.yearPart2 = yearPart2;
    }

    /**
     * Returns the Day.
     */
    public String getDay() {
        return this.day;
    }

    /**
     * Sets the Day.
     */
    public void setDay(final String day) {
        this.day = day;
    }

    /**
     * Returns the Hour.
     */
    public String getHour() {
        return this.hour;
    }

    /**
     * Sets the Hour.
     */
    public void setHour(final String hour) {
        this.hour = hour;
    }

    /**
     * Returns the Minute.
     */
    public String getMinute() {
        return this.minute;
    }

    /**
     * Sets the Minute.
     */
    public void setMinute(final String minute) {
        this.minute = minute;
    }

    /**
     * Returns the Second.
     */
    public String getSecond() {
        return this.second;
    }

    /**
     * Sets the Second.
     */
    public void setSecond(final String second) {
        this.second = second;
    }

    /**
     * Returns the Hundredth.
     */
    public String getHundredth() {
        return this.hundredth;
    }

    /**
     * Sets the Hundredth.
     */
    public void setHundredth(final String hundredth) {
        this.hundredth = hundredth;
    }

    @Override
    public String toString() {
        return "LocalDateTimeStamp{len=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", stampType=0x" + Integer.toHexString(this.stampType) +
                ", yearPart1=" + this.yearPart1 +
                ", yearPart2=" + this.yearPart2 +
                ", day=" + this.day +
                ", hour=" + this.hour +
                ", minute=" + this.minute +
                ", second=" + this.second +
                ", hundredth=" + this.hundredth + "}";
    }
}
