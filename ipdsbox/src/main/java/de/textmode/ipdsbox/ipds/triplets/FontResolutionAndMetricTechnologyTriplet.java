package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Represents the Font Resolution and Metric Technology (X'84') triplet.
 * This triplet contains information about raster font resolution and metric technology.
 */
public class FontResolutionAndMetricTechnologyTriplet extends Triplet {

    /** Metric technology used by the raster font. */
    private int metricTechnology;

    /** Raster-pattern resolution unit base. */
    private int unitBase;

    /** Raster-pattern resolution units per unit base in the X direction. */
    private int xUnitsPerUnitBase;

    /** Optional raster-pattern resolution units per unit base in the Y direction. */
    private int yUnitsPerUnitBase;

    public FontResolutionAndMetricTechnologyTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.FontResolutionandMetricTechnology);
        this.readFrom(this.getStream());
    }

    /**
     * Reads the triplet fields from the given byte array input stream using the specific methods
     * for UBIN, SBIN, CODE, CHAR, BITS, and UNDF formats, storing values in the required types.
     *
     * @param in the IPDS byte array input stream
     * @throws IOException if an I/O error occurs
     */
    private void readFrom(final IpdsByteArrayInputStream in) throws IOException {
        // Metric technology: CODE length 1 -> int
        this.metricTechnology = in.readUnsignedByte();

        // Unit base: CODE length 1 -> int
        this.unitBase = in.readUnsignedByte();

        // X units per unit base: UBIN length 2 -> int
        this.xUnitsPerUnitBase = in.readUnsignedInteger16();

        // Optional Y units per unit base: UBIN length 2 -> int
        if (this.getLength() == 8) {
            this.yUnitsPerUnitBase = in.readUnsignedInteger16();
        } else {
            this.yUnitsPerUnitBase = this.xUnitsPerUnitBase;
        }
    }

    /**
     * Converts the triplet fields into a byte array in the correct structure.
     *
     * @return the byte array representing the triplet
     * @throws IOException if an I/O error occurs
     */
    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        // Length: UBIN length 1
        out.writeUnsignedByte(8);

        // TID: CODE length 1
        out.writeUnsignedByte(this.getTripletId().getId());

        // Metric technology: CODE length 1
        out.writeUnsignedByte(this.metricTechnology);

        // Unit base: CODE length 1
        out.writeUnsignedByte(this.unitBase);

        // X units per unit base: UBIN length 2
        out.writeUnsignedInteger16(this.xUnitsPerUnitBase);

        // Y units per unit base: UBIN length 2
        out.writeUnsignedInteger16(this.yUnitsPerUnitBase);

        return out.toByteArray();
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
                "length=" + this.getLength() +
                ", tid=0x" + String.format("%02X", this.getTripletId().getId()) +
                ", metricTechnology=0x" + String.format("%02X", this.metricTechnology) +
                ", unitBase=0x" + String.format("%02X", this.unitBase) +
                ", xUnitsPerUnitBase=" + this.xUnitsPerUnitBase +
                ", yUnitsPerUnitBase=" + this.yUnitsPerUnitBase +
                '}';
    }
}
