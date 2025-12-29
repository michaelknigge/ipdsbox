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
    private int exceptionPresentationProcessingFlagsFlags;

    /**
     * Constructs the {@link ExceptionHandlingControlOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    ExceptionHandlingControlOrder(final IpdsByteArrayInputStream ipds) throws IOException {
        super(XoaOrderCode.ExceptionHandlingControl);

        this.exceptionReportingFlags = ipds.readUnsignedByte();
        this.automaticRecoveryFlags = ipds.readUnsignedByte();
        this.exceptionPresentationProcessingFlagsFlags = ipds.readUnsignedByte();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.ExceptionHandlingControl.getValue());
        out.writeUnsignedByte(this.exceptionReportingFlags);
        out.writeUnsignedByte(this.automaticRecoveryFlags);
        out.writeUnsignedByte(this.exceptionPresentationProcessingFlagsFlags);
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
    public int getExceptionPresentationProcessingFlagsFlags() {
        return this.exceptionPresentationProcessingFlagsFlags;
    }

    /**
     * Sets the Exception-presentation processing flags.
     */
    public void setExceptionPresentationProcessingFlagsFlags(final int exceptionPresentationProcessingFlagsFlags) {
        this.exceptionPresentationProcessingFlagsFlags = exceptionPresentationProcessingFlagsFlags;
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }
}
