package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class SenseTypeAndModelAcknowledgeData implements AcknowledgeData {

    private int type;
    private int model;
    private List<CommandSetVector> commandSetVectors = new ArrayList<>();

    SenseTypeAndModelAcknowledgeData(final IpdsByteArrayInputStream ipds) throws IOException {
        ipds.skip(1); // Skip the fix 0xFF

        this.type = ipds.readUnsignedInteger16();
        this.model = ipds.readUnsignedByte();

        ipds.skip(2); // Skip the fix 0x0000;

        while (ipds.bytesAvailable() > 0) {
            final int length = ipds.readUnsignedInteger16() - 2;
            final byte[] vecorBytes = ipds.readBytes(length);

            this.commandSetVectors.add(new CommandSetVector(new IpdsByteArrayInputStream(vecorBytes)));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(0xFF);
        out.writeUnsignedInteger16(this.type);
        out.writeByte(this.model);
        out.writeUnsignedInteger16(0x0000);

        for (final CommandSetVector commandSetVector : this.commandSetVectors) {
            commandSetVector.writeTo(out);
        }
    }

    /**
     * Returns device type of the printer, or of the printer that is being emulated
     * or mimicked. For example, X'3820' for the 3820 page printer.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Sets device type of the printer, or of the printer that is being emulated
     * or mimicked. For example, X'3820' for the 3820 page printer.
     */
    public void setType(final int type) {
        this.type = type;
    }

    /**
     * Returns the model number of the printer.
     */
    public int getModel() {
        return this.model;
    }

    /**
     * Sets the model number of the printer.
     */
    public void setModel(final int model) {
        this.model = model;
    }

    /**
     * Returns a {@link List} of Zero or more command-set vectors.
     */
    public List<CommandSetVector> getCommandSetVectors() {
        return this.commandSetVectors;
    }

    /**
     * Sets a {@link List} of Zero or more command-set vectors.
     */
    public void setCommandSetVectors(final List<CommandSetVector> commandSetVectors) {
        this.commandSetVectors = commandSetVectors;
    }

    @Override
    public String toString() {
        return "SenseTypeAndModelAcknowledgeData{"
                + "type=" + this.type
                + ", model=" + this.model
                + ", commandSetVectors=" + this.commandSetVectors
                + '}';
    }

    public static final class CommandSetVector {
        private int subsetIdOrCommandSetId;
        private int levelOrSubsetId;

        // The order of the property pairs might be important - so use a LinkedHashSet...
        private Set<Integer> propertyPairs = new LinkedHashSet<>();

        /**
         * Contructs the {@link CommandSetVector} from the given {@link IpdsByteArrayInputStream}.
         * Note that the length has alreay been consumed from the {@link IpdsByteArrayInputStream}.
         */
        CommandSetVector(final IpdsByteArrayInputStream ipds) throws IOException {

            this.subsetIdOrCommandSetId = ipds.readUnsignedInteger16();
            this.levelOrSubsetId = ipds.readUnsignedInteger16();

            while (ipds.bytesAvailable() > 0) {
                this.propertyPairs.add(ipds.readUnsignedInteger16());
            }
        }

        void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
            final int length = 6 + (this.propertyPairs.size() * 2);

            out.writeUnsignedInteger16(length);
            out.writeUnsignedInteger16(this.subsetIdOrCommandSetId);
            out.writeUnsignedInteger16(this.levelOrSubsetId);

            for (final Integer propertyPair : this.propertyPairs) {
                out.writeUnsignedInteger16(propertyPair);
            }
        }

        /**
         * For data command sets, returns the subset ID of a command set. For
         * other command sets, returns the command set ID.
         */
        public int getSubsetIdOrCommandSetId() {
            return this.subsetIdOrCommandSetId;
        }

        /**
         * For data command sets, sets the subset ID of a command set. For
         * other command sets, sets the command set ID.
         */
        public void setSubsetIdOrCommandSetId(final int subsetIdOrCommandSetId) {
            this.subsetIdOrCommandSetId = subsetIdOrCommandSetId;
        }

        /**
         * For data command sets, returns the level ID of a data tower. For other
         * command sets, returns the subset ID of a command set.
         */
        public int getLevelOrSubsetId() {
            return this.levelOrSubsetId;
        }

        /**
         * For data command sets, sets the level ID of a data tower. For other
         * command sets, sets the subset ID of a command set.
         */
        public void setLevelOrSubsetId(final int levelOrSubsetId) {
            this.levelOrSubsetId = levelOrSubsetId;
        }

        /**
         * Returns a {@link Set} of zero or more command-set property ID and data pairs.
         */
        public Set<Integer> getPropertyPairs() {
            return this.propertyPairs;
        }

        /**
         * Sets a {@link Set} of zero or more command-set property ID and data pairs.
         */
        public void setPropertyPairs(final Set<Integer> propertyPairs) {
            this.propertyPairs = propertyPairs;
        }

        @Override
        public String toString() {
            return "CommandSetVector{"
                    + "subsetIdOrCommandSetId=" + this.subsetIdOrCommandSetId
                    + ", levelOrSubsetId=" + this.levelOrSubsetId
                    + ", propertyPairs=" + this.propertyPairs
                    + '}';
        }
    }
}
