package de.textmode.ipdsbox.ipds.sdf;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard OCA Color Value Support Self-Defining Field (page 216).
 *
 * <p>Parses the fields of the "Standard OCA Color Value Support self-defining field"
 * using an {@code IpdsByteArrayInputStream}. The field lists one or more color-table
 * entries. This self-defining field has been retired in the IPDS architecture and
 * is documented for informational purposes.
 *
 * <p>Reading methods by format/length:
 * <ul>
 *   <li>UBIN: 1→readUnsignedByte, 2→readUnsignedInteger16, 3→readUnsignedInteger24, 4→readUnsignedInteger32</li>
 *   <li>CODE: len 1→readUnsignedByte, len ≥2→readBytes(len)</li>
 *   <li>CHAR: readEbcdicString(len)</li>
 *   <li>SBIN: 1→readByte, 2→readInteger16, 3→readInteger24, 4→readInteger32</li>
 *   <li>BITS: len 1→readUnsignedByte, len ≥2→readBytes(len)</li>
 *   <li>UNDF: readBytes(len)</li>
 * </ul>
 *
 * <p>Writing uses the corresponding {@code IpdsByteArrayOutputStream} methods.
 */
public final class StandardOcaColorValueSupportSelfDefiningField extends SelfDefiningField {

    // 0–1: UBIN (2) — SDFlength
    private int SDFlength;

    // 2–3: CODE (2) — SDFID
    private byte[] SDFID;

    // 4..(SDFlength-1): one or more color-table entries:
    // +0–1: CODE (2) — Color value
    private List<byte[]> colorValue = new ArrayList<>();

    /**
     * Creates and parses the Standard OCA Color Value Support self-defining field from the stream.
     *
     * @param ipds the {@code IpdsByteArrayInputStream} positioned at the start of this self-defining field
     */
    public StandardOcaColorValueSupportSelfDefiningField(final IpdsByteArrayInputStream ipds) {
        // 0–1: UBIN (2) — SDFlength
        this.SDFlength = ipds.readUnsignedInteger16();

        // 2–3: CODE (2) — SDFID
        this.SDFID = ipds.readBytes(2);

        // Remaining payload contains one or more Color value entries (each CODE, 2 bytes)
        int consumed = 4;
        while (consumed + 2 <= this.SDFlength) {
            final byte[] cv = ipds.readBytes(2);
            this.colorValue.add(cv);
            consumed += 2;
        }
        // If SDFlength is odd or truncated, we silently stop at the last full 2-byte entry.
    }

    /** Returns the SDFlength. */
    public int getSDFlength() {
        return this.SDFlength;
    }

    /** Sets the SDFlength. */
    public void setSDFlength(final int SDFlength) {
        this.SDFlength = SDFlength;
    }

    /** Returns the SDFID. */
    public byte[] getSDFID() {
        return this.SDFID;
    }

    /** Sets the SDFID. */
    public void setSDFID(final byte[] SDFID) {
        this.SDFID = SDFID;
    }

    /** Returns the Color value. */
    public List<byte[]> getColorValue() {
        return this.colorValue;
    }

    /** Sets the Color value. */
    public void setColorValue(final List<byte[]> colorValue) {
        this.colorValue = (colorValue != null) ? colorValue : new ArrayList<>();
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     *
     * <p>Note: {@code SDFlength} should reflect the total length (including the length field).
     * If you change the {@code colorValue} list, update {@code SDFlength} accordingly:
     * {@code SDFlength = 4 + 2 * colorValue.size()}.
     *
     * @param ipds the {@code IpdsByteArrayOutputStream}
     */
    public void writeTo(final IpdsByteArrayOutputStream ipds) {
        // 0–1: UBIN (2) — SDFlength
        ipds.writeUnsignedInteger16(this.SDFlength);

        // 2–3: CODE (2) — SDFID
        ipds.writeBytes(this.SDFID);

        // +0–1 repeating: CODE (2) — Color value
        if (this.colorValue != null) {
            for (final byte[] cv : this.colorValue) {
                ipds.writeBytes(cv); // CODE len ≥2 → writeBytes
            }
        }
    }
}
