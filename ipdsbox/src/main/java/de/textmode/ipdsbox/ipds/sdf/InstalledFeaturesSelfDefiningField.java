package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Installed Features Self-Defining Field.
 */
public final class InstalledFeaturesSelfDefiningField extends SelfDefiningField {

    private List<Integer> featureIds = new ArrayList<>();

    /**
     * Creates a new {@link InstalledFeaturesSelfDefiningField}.
     */
    public InstalledFeaturesSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.InstalledFeatures);
    }

    /**
     * Creates a {@link InstalledFeaturesSelfDefiningField} from the stream.
     */
    InstalledFeaturesSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.InstalledFeatures);

        while (ipds.bytesAvailable() > 0) {
            this.featureIds.add(ipds.readUnsignedInteger16());
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(4 + (this.featureIds.size() * 2));
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer operationType : this.featureIds) {
            ipds.writeUnsignedInteger16(operationType);
        }
    }

    /**
     * Returns the Feature IDs.
     */
    public List<Integer> getFeatureIds() {
        return this.featureIds;
    }

    /**
     * Sets the Feature IDs.
     */
    public void setFeatureIds(final List<Integer> featureIds) {
        this.featureIds = featureIds;
    }

    @Override
    public String toString() {
        return "InstalledFeaturesSelfDefiningField{"
                + "featureIds=" + this.featureIds
                + '}';
    }
}
