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
     * @param ipds the raw IPDS data of the order.
     * @throws UnknownXohOrderCode if the the IPDS data contains an unknown {@link XohOrderCode}.
     */
    public TraceOrder(final IpdsByteArrayInputStream ipds) throws UnknownXohOrderCode, IOException {
        super(ipds, XohOrderCode.Trace);

        this.function = ipds.readUnsignedByte();
        this.controlFlags = ipds.readUnsignedByte();
        this.options = ipds.readRemainingBytes();
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
