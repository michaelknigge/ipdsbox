package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field specifies the group ID formats that are recognized by the printer in the Group ID (X'00')
 * triplet. The printer must accept all formats (but unrecognized formats are ignored and don't need to be
 * supplied); this self-defining field can help a host program to determine which Group ID formats to supply.
 */
public class RecognizedGroupIdFormatsSelfDefiningField extends SelfDefiningField {

    private List<Integer> groupIdFormats = new ArrayList<>();

    /**
     * Constructs a new {@link RecognizedGroupIdFormatsSelfDefiningField}.
     */
    public RecognizedGroupIdFormatsSelfDefiningField() {
        super(SelfDefiningFieldId.RecognizedGroupIdFormats);
    }

    /**
     * Constructs the {@link RecognizedGroupIdFormatsSelfDefiningField} from the given {@link IpdsByteArrayInputStream}.
     */
    RecognizedGroupIdFormatsSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {

        super(SelfDefiningFieldId.RecognizedGroupIdFormats);

        while (ipds.bytesAvailable() > 0) {
            this.groupIdFormats.add(ipds.readUnsignedByte());
        }
    }

    /**
     * Writes this {@link RecognizedGroupIdFormatsSelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(4 + this.groupIdFormats.size());
        out.writeUnsignedInteger16(this.getSelfDefiningFieldId());

        for (final Integer operationType : this.groupIdFormats) {
            out.writeUnsignedByte(operationType);
        }
    }

    /**
     * Returns a list with all group ID formats.
     */
    public List<Integer> getGroupIdFormats() {
        return this.groupIdFormats;
    }

    /**
     * Sets a list with all group ID formats.
     */
    public void setGroupIdFormats(final List<Integer> groupIdFormats) {
        this.groupIdFormats = groupIdFormats;
    }

    @Override
    public String toString() {
        return "RecognizedGroupIdFormatsSelfDefiningField{"
                + "groupIdFormats=" + this.groupIdFormats
                + '}';
    }
}
