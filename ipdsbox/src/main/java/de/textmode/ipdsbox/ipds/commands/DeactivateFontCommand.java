package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Activate Resource (AR) command, previously known as Load Resource Equivalence (LRE), requests the
 * activation of resident resources in the printer or intermediate device.
 */
public final class DeactivateFontCommand extends IpdsCommand {

    private int deactivationType;
    private int haid;
    private int sectionID;
    private int fontInlineSequence;

    /**
     * Constructs the {@link DeactivateFontCommand}.
     */
    public DeactivateFontCommand() {
        super(IpdsCommandId.DF);
    }

    /**
     * Constructs the {@link DeactivateFontCommand}.
     */
    DeactivateFontCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.DF);

        this.deactivationType = ipds.readUnsignedByte();
        this.haid = ipds.readUnsignedInteger16();
        this.sectionID = ipds.readUnsignedByte();
        this.fontInlineSequence = ipds.readUnsignedInteger16();
    }


    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedByte(this.deactivationType);
        ipds.writeUnsignedInteger16(this.haid);
        ipds.writeUnsignedByte(this.sectionID);
        ipds.writeUnsignedInteger16(this.fontInlineSequence);
    }

    /**
     * Returns the deactivation type.
     */
    public int getDeactivationType() {
        return this.deactivationType;
    }

    /**
     * Sets the deactivation type.
     */
    public void setDeactivationType(final int resourceType) {
        this.deactivationType = resourceType;
    }

    /**
     * Returns the HAID.
     */
    public int getHaid() {
        return this.haid;
    }

    /**
     * Sets the HAID.
     */
    public void setHaid(final int haid) {
        this.haid = haid;
    }

    /**
     * Returns the Section ID.
     */
    public int getSectionID() {
        return this.sectionID;
    }

    /**
     * Sets the Section ID.
     */
    public void setSectionID(final int sectionID) {
        this.sectionID = sectionID;
    }

    /**
     * Returns the FIS.
     */
    public int getFontInlineSequence() {
        return this.fontInlineSequence;
    }

    /**
     * Sets the FIS.
     */
    public void setFontInlineSequence(final int fontInlineSequence) {
        this.fontInlineSequence = fontInlineSequence;
    }

    @Override
    public String toString() {
        return "DeactivateFontCommand{"
                + "deactivationType=0x" + Integer.toHexString(this.deactivationType)
                + ", haid=" + this.haid
                + ", sectionID=0x" + Integer.toHexString(this.sectionID)
                + ", fontInlineSequence=0x" + Integer.toHexString(this.fontInlineSequence)
                + '}';
    }
}
