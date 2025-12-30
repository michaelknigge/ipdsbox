package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Represents the Font Resolution and Metric Technology (X'84') triplet.
 * This triplet contains information about raster font resolution and metric technology.
 */
public final class FontResolutionAndMetricTechnologyTriplet extends Triplet {

    private int metricTechnology;
    private int unitBase;
    private int xUnitsPerUnitBase;
    private int yUnitsPerUnitBase;

    /**
     * Constructs a {@link FontResolutionAndMetricTechnologyTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    FontResolutionAndMetricTechnologyTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.FontResolutionandMetricTechnology);

        this.metricTechnology = ipds.readUnsignedByte();
        this.unitBase = ipds.readUnsignedByte();
        this.xUnitsPerUnitBase = ipds.readUnsignedInteger16();

        if (ipds.bytesAvailable() == 0) {
            this.yUnitsPerUnitBase = this.xUnitsPerUnitBase;
        } else {
            this.yUnitsPerUnitBase = ipds.readUnsignedInteger16();
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(8);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedByte(this.metricTechnology);
        out.writeUnsignedByte(this.unitBase);
        out.writeUnsignedInteger16(this.xUnitsPerUnitBase);
        out.writeUnsignedInteger16(this.yUnitsPerUnitBase);
    }

    /**
     * Returns the Metric technology.
     */
    public int getMetricTechnology() {
        return this.metricTechnology;
    }

    /**
     * Sets the Metric technology.
     */
    public void setMetricTechnology(final int metricTechnology) {
        this.metricTechnology = metricTechnology;
    }

    /**
     * Returns the Unit base.
     */
    public int getUnitBase() {
        return this.unitBase;
    }

    /**
     * Sets the Unit base.
     */
    public void setUnitBase(final int unitBase) {
        this.unitBase = unitBase;
    }

    /**
     * Returns the X units per unit base.
     */
    public int getXUnitsPerUnitBase() {
        return this.xUnitsPerUnitBase;
    }

    /**
     * Sets the X units per unit base.
     */
    public void setXUnitsPerUnitBase(final int xUnitsPerUnitBase) {
        this.xUnitsPerUnitBase = xUnitsPerUnitBase;
    }

    /**
     * Returns the Y units per unit base.
     */
    public int getYUnitsPerUnitBase() {
        return this.yUnitsPerUnitBase;
    }

    /**
     * Sets the Y units per unit base.
     */
    public void setYUnitsPerUnitBase(final int yUnitsPerUnitBase) {
        this.yUnitsPerUnitBase = yUnitsPerUnitBase;
    }

    /**
     * Returns a string representation of the triplet, including all field values.
     */
    @Override
    public String toString() {
        return "FontResolutionAndMetricTechnologyTriplet{"
                + "tid=0x" + String.format("%02X", this.getTripletId())
                + ", metricTechnology=0x" + String.format("%02X", this.metricTechnology)
                + ", unitBase=0x" + String.format("%02X", this.unitBase)
                + ", xUnitsPerUnitBase=" + this.xUnitsPerUnitBase
                + ", yUnitsPerUnitBase=" + this.yUnitsPerUnitBase
                + '}';
    }
}
