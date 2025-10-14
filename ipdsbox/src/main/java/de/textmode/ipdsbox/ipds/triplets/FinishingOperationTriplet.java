package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Finishing Operation triplet (X'85') specifies a specific finishing operation to be
 * applied to either a sheet or to a collection of sheets.
 */
public final class FinishingOperationTriplet extends Triplet {

    /**
     * Type of finishing operation.
     */
    public enum OperationType {
        /**
         * Cornerstaple.
         */
        CornerStaple(0x01, "Corner staple"),

        /**
         * Saddle-stitch out.
         */
        SaddleStitchOut(0x02, "Saddle-stitch out"),

        /**
         * Edge stitch.
         */
        EdgeStitch(0x03, "Edge stitch"),

        /**
         * Fold in.
         */
        FoldIn(0x04, "Fold in"),

        /**
         * Separation cut.
         */
        SeparationCut(0x05, "Separation cut"),

        /**
         * Perforation cut.
         */
        PerforationCut(0x06, "Perforation cut"),

        /**
         * Z fold.
         */
        ZFold(0x07, "Z fold"),

        /**
         * Center-fold in.
         */
        CenterColdIn(0x08, "Center-fold in"),

        /**
         * Trim after center fold or saddle stitch.
         */
        TrimAfterCenterFoldOrSaddleStitch(0x09, "Trim after center fold or saddle stitch"),

        /**
         * Punch.
         */
        Punch(0x0A, "Punch"),

        /**
         * Perfect bind.
         */
        PerfectBind(0x0C, "Perfect bind"),

        /**
         * Ring bind.
         */
        RingBind(0x0D, "Ring bind"),

        /**
         * C-fold in.
         */
        CFoldIn(0x0E, "C-fold in"),

        /**
         * Accordion-fold in.
         */
        AccordionFoldIn(0x0F, "Accordion-fold in"),

        /**
         * Saddle-stitch in.
         */
        SaddleStitchIn(0x12, "Saddle-stitch in"),

        /**
         * Center-fold out.
         */
        CenterFoldOut(0x18, "Center-fold out"),

        /**
         * C-fold out.
         */
        CFoldOut(0x1E, "C-fold out"),

        /**
         * Accordion-fold out.
         */
        AccordionFoldOut(0x1F, "Accordion-fold out"),

        /**
         * Double parallel-fold in.
         */
        DoubleParallelFoldIn(0x20, "Double parallel-fold in"),

        /**
         * Double gate-fold in.
         */
        DoubleGateFoldIn(0x21, "Double gate-fold in"),

        /**
         * Single gate-fold in.
         */
        SingleGateFoldIn(0x22, "Single gate-fold in"),

        /**
         * Double parallel-fold out.
         */
        DoubleParallelFoldOut(0x30, "Double parallel-fold out"),

        /**
         * Double gate-fold out.
         */
        DoubleGateFoldOut(0x31, "Double gate-fold out"),

        /**
         * Single gate-fold out.
         */
        SingleGateFoldOut(0x32, "Single gate-fold out");

        private static final Map<Integer, OperationType> REVERSE_MAP = new HashMap<>();

        static {
            for (final OperationType code : values()) {
                REVERSE_MAP.put(code.getValue(), code);
            }
        }

        private final int value;
        private final String description;

        /**
         * Constructor of the enum value.
         */
        OperationType(final int value, final String description) {
            this.value = value;
            this.description = description;
        }

        /**
         * Gets the enum value for the given integer.
         * @return the enum value for the given integer.
         */
        public static OperationType getFor(final int value) throws InvalidIpdsCommandException {
            final OperationType result = REVERSE_MAP.get(value);
            if (result == null) {
                throw new InvalidIpdsCommandException(
                    String.format("The value X'%1$s' for the parameter Operation Type is unknown.",
                        Integer.toHexString(value)));
            }
            return result;
        }

        /**
         * Gets the integer value of the OperationType.
         * @return the integer value of the OperationType.
         */
        public int getValue() {
            return this.value;
        }

        /**
         * Gets a textual description of the OperationType.
         * @return A textual description of the OperationType (i. e. "Z fold").
         */
        public String getDescription() {
            return this.description;
        }
    }

    /**
     * Reference corner and edge.
     */
    public enum Reference {
        /**
         * Bottom-right corner; bottom edge.
         */
        BottomRightCorner(0x00, "Bottom-right corner; bottom edge"),

        /**
         * Top-right corner; right edge.
         */
        TopRightCorner(0x01, "Top-right corner; right edge"),

        /**
         * Top-left corner; top edge.
         */
        TopLeftCorner(0x02, "Top-left corner; top edge"),

        /**
         * Bottom-left corner; left edge.
         */
        BottomLeftCorner(0x03, "Bottom-left corner; left edge"),

        /**
         * Default corner; default edge.
         */
        DefaultCorner(0xFF, "Default corner; default edge");

        private static final Map<Integer, Reference> REVERSE_MAP = new HashMap<>();

        static {
            for (final Reference code : values()) {
                REVERSE_MAP.put(code.getValue(), code);
            }
        }

        private final int value;
        private final String description;

        /**
         * Constructor of the enum value.
         */
        Reference(final int value, final String description) {
            this.value = value;
            this.description = description;
        }

        /**
         * Gets the enum value for the given integer.
         * @return the enum value for the given integer.
         */
        public static Reference getFor(final int value) throws InvalidIpdsCommandException {
            final Reference result = REVERSE_MAP.get(value);
            if (result == null) {
                throw new InvalidIpdsCommandException(
                    String.format("The value X'%1$s' for the parameter Reference is unknown.",
                        Integer.toHexString(value)));
            }
            return result;
        }

        /**
         * Gets the integer value of the Reference.
         * @return the integer value of the Reference.
         */
        public int getValue() {
            return this.value;
        }

        /**
         * Gets a textual description of the Reference.
         * @return A textual description of the Reference (i. e. "Bottom-right corner; bottom edge").
         */
        public String getDescription() {
            return this.description;
        }
    }

    private final OperationType operationType;
    private final int finishingOption;
    private final Reference reference;
    private final int count;
    private final int axisOffset;
    private final List<Integer> positions;

    public FinishingOperationTriplet(final OperationType operationType) {
        super(TripletId.FinishingOperation);

        this.operationType = operationType;
        this.finishingOption = 0x00;
        this.reference = Reference.DefaultCorner;
        this.count = 0x00;
        this.axisOffset = 0xFFFF;
        this.positions = new ArrayList<>();
    }

    /**
     * Constructs a {@link FinishingOperationTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    public FinishingOperationTriplet(final IpdsByteArrayInputStream ipds) throws IOException, InvalidIpdsCommandException, UnknownTripletException {
        super(ipds, TripletId.FinishingOperation);

        this.operationType = OperationType.getFor(ipds.readUnsignedByte());
        this.finishingOption = ipds.readUnsignedByte();
        ipds.skip(1);
        this.reference = Reference.getFor(ipds.readUnsignedByte());
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
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.getOperationType().getValue());
        out.writeUnsignedByte(this.finishingOption);
        out.writeUnsignedByte(0x00);
        out.writeUnsignedByte(this.getReference().getValue());
        out.writeUnsignedByte(this.getCount());
        out.writeUnsignedInteger16(this.axisOffset);

        for (final Integer position : this.positions) {
            out.writeUnsignedInteger16(position.intValue());
        }
    }

    /**
     * Returns the type of the finishing operation.
     * @return the type of the finishing operation.
     */
    public OperationType getOperationType() {
        return this.operationType;
    }

    /**
     * Returns the finishing option.
     * @return the finishing option.
     */
    public int getFinishingOption() {
        return this.finishingOption;
    }

    /**
     * Returns the reference corner and edge.
     * @return the reference corner and edge.
     */
    public Reference getReference() {
        return this.reference;
    }

    /**
     * Returns the finishing operation count (note that for a corner-staple operation,
     * this field is ignored; a single staple is used).
     * @return the finishing operation count.
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Returns the finishing operation axis offset.
     * @return the finishing operation axis offset.
     */
    public int getAxisOffset() {
        return this.axisOffset;
    }

    /**
     * Returns a list of finishing operation positions (the list may be empty).
     * @return a list of finishing operation positions (the list may be empty).
     */
    public List<Integer> getPositions() {
        return this.positions;
    }

    @Override
    public String toString() {
        return "FinishingOperationTriplet{" +
                "tid=0x" + String.format("%02X", this.getTripletId().getId()) +
                ", operationType=" + operationType +
                ", finishingOption=0x" + Integer.toHexString(finishingOption) +
                ", reference=" + reference +
                ", count=" + count +
                ", axisOffset=" + axisOffset +
                ", positions=" + positions +
                "}";
    }
}
