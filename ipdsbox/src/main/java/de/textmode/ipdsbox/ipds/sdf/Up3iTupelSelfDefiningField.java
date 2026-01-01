package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field reports the physical order and properties of the UP3I devices connected to the printer.
 * One of these self-defining fields is returned for each possible paper path combination in the line of UP3I
 * devices; the combination of devices is called a tupel.
 */
public final class Up3iTupelSelfDefiningField extends SelfDefiningField {

    private int tupelId;
    private byte[] up3iDeviceInformation;

    /**
     * Constructs a new {@link Up3iTupelSelfDefiningField}.
     */
    public Up3iTupelSelfDefiningField() {
        super(SelfDefiningFieldId.Up3iTupel);

        this.tupelId = 0x00;
        this.up3iDeviceInformation = ByteUtils.EMPTY_BYTE_ARRAY;
    }

    /**
     * Constructs the {@link Up3iTupelSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    Up3iTupelSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.Up3iTupel);

        this.tupelId = ipds.readUnsignedInteger16();
        this.up3iDeviceInformation = ipds.readRemainingBytes();
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(6 + this.up3iDeviceInformation.length);
        ipds.writeUnsignedInteger16(SelfDefiningFieldId.Up3iTupel.getId());

        ipds.writeUnsignedInteger16(this.tupelId);
        ipds.writeBytes(this.up3iDeviceInformation);
    }

    /**
     * Returns the tupel ID.
     */
    public int getTupelId() {
        return this.tupelId;
    }

    /**
     * Sets the tupel ID.
     */
    public void setTupelId(final int tupelId) {
        this.tupelId = tupelId;
    }

    /**
     * Returns the UP3i device information.
     */
    public byte[] getUp3iDeviceInformation() {
        return this.up3iDeviceInformation;
    }

    /**
     * Sets the UP3i device information.
     */
    public void setUp3iDeviceInformation(final byte[] up3iDeviceInformation) {
        this.up3iDeviceInformation = up3iDeviceInformation;
    }

    /**
     * Accept method for the {@link SelfDefiningFieldVisitor}.
     */
    @Override
    public void accept(final SelfDefiningFieldVisitor visitor) {
        visitor.handle(this);
    }
    
    @Override
    public String toString() {
        return "Up3iTupelSelfDefiningField{"
                + "tupelId=" + this.tupelId
                + ", up3iDeviceInformation=" + StringUtils.toHexString(this.up3iDeviceInformation)
                + '}';
    }
}
