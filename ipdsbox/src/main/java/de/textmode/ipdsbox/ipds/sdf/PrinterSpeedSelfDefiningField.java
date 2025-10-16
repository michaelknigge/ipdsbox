package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field reports the speed of the printer.
 */
public final class PrinterSpeedSelfDefiningField extends SelfDefiningField {

    private long ppm;
    private long fpm;

    /**
     * Constructs the {@link PrinterSpeedSelfDefiningField}.
     */
    public PrinterSpeedSelfDefiningField() {
        super(SelfDefiningFieldId.PrinterSpeed);

        this.ppm = 0;
        this.fpm = 0;
    }
    /**
     * Constructs the {@link PrinterSpeedSelfDefiningField}.
     */
    PrinterSpeedSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.PrinterSpeed);

        this.ppm = ipds.readUnsignedInteger32();
        this.fpm = ipds.readUnsignedInteger32();
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger16(0x0C);
        ipds.writeUnsignedInteger16(SelfDefiningFieldId.PrinterSpeed.getId());

        ipds.writeUnsignedInteger32(this.ppm);
        ipds.writeUnsignedInteger32(this.fpm);
    }

    /**
     * Returns the pages per minute.
     */
    public long getPpm() {
        return this.ppm;
    }

    /**
     * Sets the pages per minute.
     */
    public void setPpm(final long ppm) {
        this.ppm = ppm;
    }

    /**
     * Returns the feet per minute.
     */
    public long getFpm() {
        return this.fpm;
    }

    /**
     * Sets the feet per minute.
     */
    public void setFpm(final long fpm) {
        this.fpm = fpm;
    }

    @Override
    public String toString() {
        return "PrinterSpeedSelfDefiningField{" +
                "ppm=" + this.ppm +
                ", fpm=" + this.fpm +
                '}';
    }
}
