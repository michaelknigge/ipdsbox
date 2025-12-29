package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field lists the medium modification IDs that are currently supported by the XOH-SMM
 * command. If this self-defining field is returned, the printer must also return the Select-Medium-Modificationssupport
 * property ID (X'900E') in the Sense Type and Model reply.
 */
public class MediumModificationIdsSupportedSelfDefiningField extends SelfDefiningField {

    private List<Integer> mediumModificationIds = new ArrayList<>();

    /**
     * Constructs the {@link MediumModificationIdsSupportedSelfDefiningField}.
     */
    public MediumModificationIdsSupportedSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.MediumModificationIdsSupported);
    }

    /**
     * Constructs the {@link MediumModificationIdsSupportedSelfDefiningField}.
     */
    public MediumModificationIdsSupportedSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.MediumModificationIdsSupported);

        while (ipds.bytesAvailable() > 0) {
            this.mediumModificationIds.add(ipds.readUnsignedInteger16());
        }
    }

    /**
     * Writes this {@link MediumModificationIdsSupportedSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + (this.mediumModificationIds.size() * 2));
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer id : this.mediumModificationIds) {
            out.writeUnsignedInteger16(id);
        }
    }

    /**
     * Returns a list containing all currently-supported medium modification IDs.
     */
    public List<Integer> getMediumModificationIds() {
        return this.mediumModificationIds;
    }

    /**
     * Sets a list containing all currently-supported medium modification IDs.
     */
    public void setMediumModificationIds(final List<Integer> boundaries) {
        this.mediumModificationIds = boundaries;
    }

    @Override
    public String toString() {
        return "MediumModificationIdsSupportedSelfDefiningField{"	
                + "mediumModificationIds=" + this.mediumModificationIds	
                + '}';
    }
}
