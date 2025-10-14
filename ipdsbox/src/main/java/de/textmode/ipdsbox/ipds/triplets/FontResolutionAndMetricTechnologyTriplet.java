package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Represents the Font Resolution and Metric Technology (X'84') triplet.
 * This triplet contains information about raster font resolution and metric technology.
 */
public class FontResolutionAndMetricTechnologyTriplet extends Triplet {

    private int metricTechnology;
    private int unitBase;
    private int xUnitsPerUnitBase;
    private int yUnitsPerUnitBase;

    public FontResolutionAndMetricTechnologyTriplet(final IpdsByteArrayInputStream ipds) throws IOException, UnknownTripletException {
        super(ipds, TripletId.FontResolutionandMetricTechnology);

        this.metricTechnology = ipds.readUnsignedByte();
        this.unitBase = ipds.readUnsignedByte();
        this.xUnitsPerUnitBase = ipds.readUnsignedInteger16();

        if (ipds.bytesAvailable() == 0) {
            this.yUnitsPerUnitBase = this.xUnitsPerUnitBase;
        } else {
            this.yUnitsPerUnitBase = ipds.readUnsignedInteger16();
        }
    }

    /**
     * Converts the triplet fields into a byte array in the correct structure.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(8);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.metricTechnology);
        out.writeUnsignedByte(this.unitBase);
        out.writeUnsignedInteger16(this.xUnitsPerUnitBase);
        out.writeUnsignedInteger16(this.yUnitsPerUnitBase);
    }

    /**
     * Returns the Metric technology.
     *
     * @return the metric technology
     */
    public int getMetricTechnology() {
        return this.metricTechnology;
    }

    /**
     * Sets the Metric technology.
     *
     * @param metricTechnology the metric technology
     */
    public void setMetricTechnology(final int metricTechnology) {
        this.metricTechnology = metricTechnology;
    }

    /**
     * Returns the Unit base.
     *
     * @return the unit base
     */
    public int getUnitBase() {
        return this.unitBase;
    }

    /**
     * Sets the Unit base.
     *
     * @param unitBase the unit base
     */
    public void setUnitBase(final int unitBase) {
        this.unitBase = unitBase;
    }

    /**
     * Returns the X units per unit base.
     *
     * @return the X units per unit base
     */
    public int getXUnitsPerUnitBase() {
        return this.xUnitsPerUnitBase;
    }

    /**
     * Sets the X units per unit base.
     *
     * @param xUnitsPerUnitBase the X units per unit base
     */
    public void setXUnitsPerUnitBase(final int xUnitsPerUnitBase) {
        this.xUnitsPerUnitBase = xUnitsPerUnitBase;
    }

    /**
     * Returns the Y units per unit base.
     *
     * @return the Y units per unit base
     */
    public int getYUnitsPerUnitBase() {
        return this.yUnitsPerUnitBase;
    }

    /**
     * Sets the Y units per unit base.
     *
     * @param yUnitsPerUnitBase the Y units per unit base
     */
    public void setYUnitsPerUnitBase(final int yUnitsPerUnitBase) {
        this.yUnitsPerUnitBase = yUnitsPerUnitBase;
    }

    /**
     * Returns a string representation of the triplet, including all field values.
     *
     * @return a string with all field values
     */
    @Override
    public String toString() {
        return "FontResolutionAndMetricTechnologyTriplet{" +
                "tid=0x" + String.format("%02X", this.getTripletId().getId()) +
                ", metricTechnology=0x" + String.format("%02X", this.metricTechnology) +
                ", unitBase=0x" + String.format("%02X", this.unitBase) +
                ", xUnitsPerUnitBase=" + this.xUnitsPerUnitBase +
                ", yUnitsPerUnitBase=" + this.yUnitsPerUnitBase +
                "}";
    }
}
