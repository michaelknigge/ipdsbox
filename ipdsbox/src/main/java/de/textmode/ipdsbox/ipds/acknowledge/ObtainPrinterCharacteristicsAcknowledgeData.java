package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.sdf.SelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.SelfDefiningFieldFactory;

public final class ObtainPrinterCharacteristicsAcknowledgeData implements AcknowledgeData {

    private List<SelfDefiningField> selfDefiningFields = new ArrayList<>();

    ObtainPrinterCharacteristicsAcknowledgeData(final IpdsByteArrayInputStream ipds) throws IOException {
        while (ipds.bytesAvailable() > 0) {
            final int length = ipds.readUnsignedInteger16();
            ipds.rewind(2);

            this.selfDefiningFields.add(SelfDefiningFieldFactory.create(ipds.readBytes(length)));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        for (final SelfDefiningField selfDefiningField : this.selfDefiningFields) {
            selfDefiningField.writeTo(out);
        }
    }

    /**
     * Returns a {@link List} of {@link SelfDefiningField}s that descrbe printer characteristics.
     */
    public List<SelfDefiningField> getType() {
        return this.selfDefiningFields;
    }

    /**
     * Sets a {@link List} of {@link SelfDefiningField}s that descrbe printer characteristics.
     */
    public void setType(final List<SelfDefiningField> selfDefiningFields) {
        this.selfDefiningFields = selfDefiningFields;
    }

    @Override
    public String toString() {
        return "ObtainPrinterCharacteristicsAcknowledgeData{" +
                "selfDefiningFields=" + this.selfDefiningFields +
                '}';
    }
}
