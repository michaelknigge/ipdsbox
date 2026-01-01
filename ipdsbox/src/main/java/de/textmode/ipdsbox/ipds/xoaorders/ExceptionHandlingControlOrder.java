package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Exception-Handling Control order.
 */
public final class ExceptionHandlingControlOrder extends XoaOrder {

    private int exceptionReportingFlags;
    private int automaticRecoveryFlags;
    private int exceptionPresentationProcessingFlags;

    /**
     * Constructs the {@link ExceptionHandlingControlOrder} with default values.
     */
    public ExceptionHandlingControlOrder() throws IOException {
        super(XoaOrderCode.ExceptionHandlingControl);

        this.exceptionReportingFlags = 0x01; // Only "Report all other exceptions with AEAs"
        this.automaticRecoveryFlags = 0x00;
        this.exceptionPresentationProcessingFlags = 0x00;
    }

    /**
     * Constructs the {@link ExceptionHandlingControlOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    ExceptionHandlingControlOrder(final IpdsByteArrayInputStream ipds) throws IOException {
        super(XoaOrderCode.ExceptionHandlingControl);

        this.exceptionReportingFlags = ipds.readUnsignedByte();
        this.automaticRecoveryFlags = ipds.readUnsignedByte();
        this.exceptionPresentationProcessingFlags = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(this.getOrderCodeId());
        out.writeUnsignedByte(this.exceptionReportingFlags);
        out.writeUnsignedByte(this.automaticRecoveryFlags);
        out.writeUnsignedByte(this.exceptionPresentationProcessingFlags);
    }

    /**
     * Returns the Exception Reporting Flags.
     */
    public int getExceptionReportingFlags() {
        return this.exceptionReportingFlags;
    }

    /**
     * Sets the Exception Reporting Flags.
     */
    public void setExceptionReportingFlags(final int exceptionReportingFlags) {
        this.exceptionReportingFlags = exceptionReportingFlags;
    }

    /**
     * Returns the Automatic Recovery Flags.
     */
    public int getAutomaticRecoveryFlags() {
        return this.automaticRecoveryFlags;
    }

    /**
     * Sets the Automatic Recovery Flags.
     */
    public void setAutomaticRecoveryFlags(final int automaticRecoveryFlags) {
        this.automaticRecoveryFlags = automaticRecoveryFlags;
    }

    /**
     * Returns the Exception-presentation processing flags.
     */
    public int getExceptionPresentationProcessingFlags() {
        return this.exceptionPresentationProcessingFlags;
    }

    /**
     * Sets the Exception-presentation processing flags.
     */
    public void setExceptionPresentationProcessingFlags(final int exceptionPresentationProcessingFlags) {
        this.exceptionPresentationProcessingFlags = exceptionPresentationProcessingFlags;
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public String toString() {
        return "ExceptionHandlingControlOrder{"
                + "exceptionReportingFlags=0x" + Integer.toHexString(this.exceptionReportingFlags)
                + ", automaticRecoveryFlags=0x" + Integer.toHexString(this.automaticRecoveryFlags)
                + ", exceptionPresentationProcessingFlags=0x" + Integer.toHexString(
                        this.exceptionPresentationProcessingFlags)
                + '}';
    }
}
