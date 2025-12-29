package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field lists the resolution (or resolutions) controlled by the printer; this includes the resolution
 * to which sheet-side data is RIPped and the number of printed pels per inch (often called the print-head
 * resolution).
 */
public final class SupportedDeviceResolutionsSelfDefiningField extends SelfDefiningField {

    private int ripXpels;
    private int rinYpels;
    private int printHeadXpels;
    private int printHeadYpels;

    /**
     * Constructs the {@link SupportedDeviceResolutionsSelfDefiningField}.
     */
    public SupportedDeviceResolutionsSelfDefiningField() {
        super(SelfDefiningFieldId.SupportedDeviceResolutions);

        this.ripXpels = 01;
        this.rinYpels = 0x01;
        this.printHeadXpels = 0x01;
        this.printHeadYpels = 0x01;
    }
    /**
     * Constructs the {@link SupportedDeviceResolutionsSelfDefiningField}.
     */
    SupportedDeviceResolutionsSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.SupportedDeviceResolutions);

        this.ripXpels = ipds.readUnsignedInteger16();
        this.rinYpels = ipds.readUnsignedInteger16();
        this.printHeadXpels = ipds.readUnsignedInteger16();
        this.printHeadYpels = ipds.readUnsignedInteger16();
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(0x0C);
        ipds.writeUnsignedInteger16(SelfDefiningFieldId.SupportedDeviceResolutions.getId());

        ipds.writeUnsignedInteger16(this.ripXpels);
        ipds.writeUnsignedInteger16(this.rinYpels);
        ipds.writeUnsignedInteger16(this.printHeadXpels);
        ipds.writeUnsignedInteger16(this.printHeadYpels);
    }

    /**
     * Returns the resolution to which sheet-side data is RIPped for pels per inch
     * across the media.
     */
    public int getRipXpels() {
        return this.ripXpels;
    }

    /**
     * Sets the resolution to which sheet-side data is RIPped for pels per inch
     * across the media.
     */
    public void setRipXpels(final int ripXpels) {
        this.ripXpels = ripXpels;
    }

    /**
     * Returns the resolution to which sheet-side data is RIPped for pels per inch
     * in the direction of the media path.
     */
    public int getRinYpels() {
        return this.rinYpels;
    }

    /**
     * Sets the resolution to which sheet-side data is RIPped for pels per inch
     * in the direction of the media path.
     */
    public void setRinYpels(final int rinYpels) {
        this.rinYpels = rinYpels;
    }

    /**
     * Returns the number of printed pels per inch across the media.
     */
    public int getPrintHeadXpels() {
        return this.printHeadXpels;
    }

    /**
     * Sets the number of printed pels per inch across the media.
     */
    public void setPrintHeadXpels(final int printHeadXpels) {
        this.printHeadXpels = printHeadXpels;
    }

    /**
     * Returns the number of printed pels per inch in the direction of the media path.
     */
    public int getPrintHeadYpels() {
        return this.printHeadYpels;
    }

    /**
     * Sets the number of printed pels per inch in the direction of the media path.
     */
    public void setPrintHeadYpels(final int printHeadYpels) {
        this.printHeadYpels = printHeadYpels;
    }

    @Override
    public String toString() {
        return "SupportedDeviceResolutionsSelfDefiningField{"
                + "ripXpels=0x" + Integer.toHexString(this.ripXpels)
                + ", rinYpels=0x" + Integer.toHexString(this.rinYpels)
                + ", printHeadXpels=0x" + Integer.toHexString(this.printHeadXpels)
                + ", printHeadYpels=0x" + Integer.toHexString(this.printHeadYpels)
                + '}';
    }
}
