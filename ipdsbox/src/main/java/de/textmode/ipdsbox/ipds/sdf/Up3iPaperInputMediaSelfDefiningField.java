package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field reports the media attributes of all media that exist in the UP3I line. One of these
 * self-defining fields is returned for each available IPDS media source for which there is UP3I information.
 */
public final class Up3iPaperInputMediaSelfDefiningField extends SelfDefiningField {

    private int mediaSourceId;
    private byte[] up3iMediaInformation;

    /**
     * Constructs a new {@link Up3iPaperInputMediaSelfDefiningField}.
     */
    public Up3iPaperInputMediaSelfDefiningField() {
        super(SelfDefiningFieldId.Up3iPaperInputMedia);

        this.mediaSourceId = 0x00;
        this.up3iMediaInformation = ByteUtils.EMPTY_BYTE_ARRAY;
    }

    /**
     * Constructs the {@link Up3iPaperInputMediaSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    Up3iPaperInputMediaSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.Up3iPaperInputMedia);

        this.mediaSourceId = ipds.readUnsignedInteger16();
        this.up3iMediaInformation = ipds.readRemainingBytes();
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(6 + this.up3iMediaInformation.length);
        ipds.writeUnsignedInteger16(SelfDefiningFieldId.Up3iPaperInputMedia.getId());

        ipds.writeUnsignedInteger16(this.mediaSourceId);
        ipds.writeBytes(this.up3iMediaInformation);
    }

    /**
     * Returns the media source ID.
     */
    public int getMediaSourceId() {
        return this.mediaSourceId;
    }

    /**
     * Sets the media source ID.
     */
    public void setMediaSourceId(final int mediaSourceId) {
        this.mediaSourceId = mediaSourceId;
    }

    /**
     * Returns the UP3i media information.
     */
    public byte[] getUp3iMediaInformation() {
        return this.up3iMediaInformation;
    }

    /**
     * Sets the UP3i media information.
     */
    public void setUp3iMediaInformation(final byte[] up3iMediaInformation) {
        this.up3iMediaInformation = up3iMediaInformation;
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
                + "mediaSourceId=" + this.mediaSourceId
                + ", up3iMediaInformation=" + StringUtils.toHexString(this.up3iMediaInformation)
                + '}';
    }
}
