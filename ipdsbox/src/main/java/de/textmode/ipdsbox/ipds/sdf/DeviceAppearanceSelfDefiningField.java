package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field lists optional device-appearance values that are supported by the printer. A device
 * appearance can be selected with the Device Appearance (X'97') triplet in a Set Presentation Environment
 * (SPE) command. Support for the Device Appearance (X'97') triplet is indicated by property pair X'F206' in the
 * Device-Control command-set vector of an STM reply.
 */
public final class DeviceAppearanceSelfDefiningField extends SelfDefiningField {

    private List<Integer> appearances = new ArrayList<>();

    /**
     * Constructs a new {@link DeviceAppearanceSelfDefiningField}.
     */
    public DeviceAppearanceSelfDefiningField() {
        super(SelfDefiningFieldId.DeviceAppearance);
    }

    /**
     * Constructs a new {@link DeviceAppearanceSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    DeviceAppearanceSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.DeviceAppearance);

        while (ipds.bytesAvailable() > 0) {
            this.appearances.add(ipds.readUnsignedInteger16());
        }
    }

    /**
     * Writes this {@link DeviceAppearanceSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + (this.appearances.size() * 2));
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer appearance : this.appearances) {
            out.writeUnsignedInteger16(appearance);
        }
    }

    /**
     * Returns a list containing all appearances.
     */
    public List<Integer> getAppearances() {
        return this.appearances;
    }

    /**
     * Sets a list containing all appearances.
     */
    public void setAppearances(final List<Integer> appearances) {
        this.appearances = appearances;
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
        return "DeviceAppearanceSelfDefiningField{"
                + "appearances=" + this.appearances
                + '}';
    }
}
