package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Trace order.
 */
public final class TraceOrder extends XohOrder {

    private int function;
    private int controlFlags;
    private byte[] options;

    /**
     * Constructs the {@link TraceOrder}.
     * @param data the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public TraceOrder(final byte[] data) throws UnknownXohOrderCode, IOException {
        super(data, XohOrderCode.Trace);

        final IpdsByteArrayInputStream stream = new IpdsByteArrayInputStream(data);
        stream.skip(2);

        this.function = stream.readUnsignedByte();
        this.controlFlags = stream.readUnsignedByte();
        this.options = stream.readRemainingBytes();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XohOrderCode.Trace.getValue());
        out.writeUnsignedByte(this.function);
        out.writeUnsignedByte(this.controlFlags);
        out.writeBytes(this.options);
    }

    /**
     * Returns the function.
     * @return the function.
     */
    public int getFunction() {
        return this.function;
    }

    /**
     * Sets the function.
     */
    public void setFunction(final int function) {
        this.function = function;
    }

    /**
     * Returns the control flags.
     * @return the control flags.
     */
    public int getControlFlags() {
        return this.controlFlags;
    }

    /**
     * Sets the control flags.
     */
    public void setControlFlags(final int controlFlags) {
        this.controlFlags = controlFlags;
    }

    /**
     * Returns the options.
     * @return the options.
     */
    public byte[] getOptions() {
        return this.options;
    }

    /**
     * Sets the options.
     */
    public void setOptions(final byte[] options) {
        this.options = options;
    }

    /**
     * Accept method for the {@link XohOrderVisitor}.
     * @param visitor the {@link XohOrderVisitor}.
     */
    @Override
    public void accept(final XohOrderVisitor visitor) {
        visitor.handle(this);
    }
}
