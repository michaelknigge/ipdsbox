package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field identifies the maximum number of sheets allowed within a recovery-unit group; these
 * sheets include sheets containing pages and copies of such sheets. Support for this group operation is
 * indicated by the Supported Group Operations self-defining field.
 */
public final class KeepGroupTogetherSelfDefiningField extends SelfDefiningField {

    private int maximumNumberOfSheets;
    private int unitBase;
    private int upub;
    private int maximumTotalGroupLength;

    /**
     * Constructs a new {@link KeepGroupTogetherSelfDefiningField}.
     */
    public KeepGroupTogetherSelfDefiningField() {
        super(SelfDefiningFieldId.KeepGroupTogether);

        this.maximumNumberOfSheets = 0;
        this.unitBase = 0x00;
        this.upub = 0x3840; // 14.400
        this.maximumTotalGroupLength = 0;
    }

    /**
     * Constructs a new {@link KeepGroupTogetherSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    KeepGroupTogetherSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.KeepGroupTogether);

        this.maximumNumberOfSheets = ipds.readUnsignedInteger16();
        this.unitBase = ipds.readUnsignedByte();
        this.upub = ipds.readUnsignedInteger16();
        this.maximumTotalGroupLength = ipds.readUnsignedInteger16();
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(0x0B);
        ipds.writeUnsignedInteger16(SelfDefiningFieldId.KeepGroupTogether.getId());

        ipds.writeUnsignedInteger16(this.maximumNumberOfSheets);
        ipds.writeUnsignedByte(this.unitBase);
        ipds.writeUnsignedInteger16(this.upub);
        ipds.writeUnsignedInteger16(this.maximumTotalGroupLength);
    }

    /**
     * Returns the number of sheets allowed within a recovery-unit group.
     */
    public int getMaximumNumberOfSheets() {
        return this.maximumNumberOfSheets;
    }

    /**
     * Sets the number of sheets allowed within a recovery-unit group.
     */
    public void setMaximumNumberOfSheets(final int maximumNumberOfSheets) {
        this.maximumNumberOfSheets = maximumNumberOfSheets;
    }

    /**
     * Returns the unit base for this self-defining field.
     */
    public int getUnitBase() {
        return this.unitBase;
    }

    /**
     * Sets the unit base for this self-defining field.
     */
    public void setUnitBase(final int unitBase) {
        this.unitBase = unitBase;
    }

    /**
     * Returns the units per unit base value for this self-defining field.
     */
    public int getUpub() {
        return this.upub;
    }

    /**
     * Sets the units per unit base value for this self-defining field.
     */
    public void setUpub(final int upub) {
        this.upub = upub;
    }

    /**
     * Returns the maximum length of media that can be kept together as a recovery unit.
     */
    public int getMaximumTotalGroupLength() {
        return this.maximumTotalGroupLength;
    }

    /**
     * Sets the maximum length of media that can be kept together as a recovery unit.
     */
    public void setMaximumTotalGroupLength(final int maximumTotalGroupLength) {
        this.maximumTotalGroupLength = maximumTotalGroupLength;
    }

    @Override
    public String toString() {
        return "KeepGroupTogetherSelfDefiningField{"
                + "maximumNumberOfSheets=" + this.maximumNumberOfSheets
                + ", unitBase=0x" + Integer.toHexString(this.unitBase)
                + ", upub=0x" + Integer.toHexString(this.upub)
                + ", maximumTotalGroupLength=" + this.maximumTotalGroupLength
                + '}';
    }
}
