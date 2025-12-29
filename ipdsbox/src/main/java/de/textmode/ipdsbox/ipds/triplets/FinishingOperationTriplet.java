package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Finishing Operation triplet (X'85') specifies a specific finishing operation to be
 * applied to either a sheet or to a collection of sheets.
 */
public final class FinishingOperationTriplet extends Triplet {

    private final int operationType;
    private final int finishingOption;
    private final int reference;
    private final int count;
    private final int axisOffset;
    private final List<Integer> positions;


    /**
     * Constructs a {@link FinishingOperationTriplet}.
     */
    public FinishingOperationTriplet(final int operationType) {
        super(TripletId.FinishingOperation);

        this.operationType = operationType;
        this.finishingOption = 0x00;
        this.reference = 0xFF;
        this.count = 0x00;
        this.axisOffset = 0xFFFF;
        this.positions = new ArrayList<>();
    }

    /**
     * Constructs a {@link FinishingOperationTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    public FinishingOperationTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.FinishingOperation);

        this.operationType = ipds.readUnsignedByte();
        this.finishingOption = ipds.readUnsignedByte();
        ipds.skip(1);
        this.reference = ipds.readUnsignedByte();
        this.count = ipds.readUnsignedByte();
        this.axisOffset = ipds.readUnsignedInteger16();

        this.positions = new ArrayList<>();
        while (ipds.bytesAvailable() != 0) {
            this.positions.add(ipds.readUnsignedInteger16());
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(0x09 + (this.positions.size() * 2));
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedByte(this.getOperationType());
        out.writeUnsignedByte(this.finishingOption);
        out.writeUnsignedByte(0x00);
        out.writeUnsignedByte(this.getReference());
        out.writeUnsignedByte(this.getCount());
        out.writeUnsignedInteger16(this.axisOffset);

        for (final Integer position : this.positions) {
            out.writeUnsignedInteger16(position.intValue());
        }
    }

    /**
     * Returns the type of the finishing operation.
     */
    public int getOperationType() {
        return this.operationType;
    }

    /**
     * Returns the finishing option.
     */
    public int getFinishingOption() {
        return this.finishingOption;
    }

    /**
     * Returns the reference corner and edge.
     */
    public int getReference() {
        return this.reference;
    }

    /**
     * Returns the finishing operation count (note that for a corner-staple operation,
     * this field is ignored; a single staple is used).
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Returns the finishing operation axis offset.
     */
    public int getAxisOffset() {
        return this.axisOffset;
    }

    /**
     * Returns a list of finishing operation positions (the list may be empty).
     */
    public List<Integer> getPositions() {
        return this.positions;
    }

    @Override
    public String toString() {
        return "FinishingOperationTriplet{"
                + "tid=0x" + String.format("%02X", this.getTripletId())
                + ", operationType=" + this.operationType
                + ", finishingOption=0x" + Integer.toHexString(this.finishingOption)
                + ", reference=" + this.reference
                + ", count=" + this.count
                + ", axisOffset=" + this.axisOffset
                + ", positions=" + this.positions
                + '}';
    }
}
