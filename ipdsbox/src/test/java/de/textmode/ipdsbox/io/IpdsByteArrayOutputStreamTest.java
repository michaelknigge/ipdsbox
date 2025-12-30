package de.textmode.ipdsbox.io;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Unit-Tests for the class {@link IpdsByteArrayOutputStream}.
 */
public final class IpdsByteArrayOutputStreamTest extends TestCase {

    /**
     * Checks writing some bytes.
     */
    public void testWriteBytes() throws Exception {
        final byte[] buffer = new byte[3];
        buffer[0] = 0x00;
        buffer[1] = 0x01;
        buffer[2] = 0x02;

        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeBytes(buffer);

        final byte[] written = os.toByteArray();
        assertEquals(3, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        final byte[] read = is.readBytes(3);

        assertTrue(Arrays.equals(written, read));
    }

    /**
     * Checks writing an EBCDIC-International encoded String.
     */
    public void testWriteEbcdicString() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeEbcdicString("TEST ME");

        final byte[] written = os.toByteArray();
        assertEquals(7, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        final String read = is.readEbcdicString(7);

        assertEquals("TEST ME", read);
    }

    /**
     * Checks writing an ASCII encoded single byte character String.
     */
    public void testWriteAsciiString() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeAsciiString("HELLO WORLD");

        final byte[] written = os.toByteArray();
        assertEquals(11, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        final String read = is.readAsciiString(11);

        assertEquals("HELLO WORLD", read);
    }

    /**
     * Checks writing one byte. The byte is treated as an unsigned value.
     */
    public void testWriteUnsignedByte() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeUnsignedByte(200);

        final byte[] written = os.toByteArray();
        assertEquals(1, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        final int read = is.readUnsignedByte();

        assertEquals(200, read);
    }

    /**
     * Checks writing one input byte. The byte is treated as a signed value.
     */
    public void testWriteByte() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeByte(1);
        os.writeByte(-1);

        final byte[] written = os.toByteArray();
        assertEquals(2, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);

        assertEquals(1, is.readByte());
        assertEquals(-1, is.readByte());
    }

    /**
     * Checks writing a 16 Bit value. The value is treated as an unsigned value.
     */
    public void testWriteUnsignedInteger16() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeUnsignedInteger16(500);

        final byte[] written = os.toByteArray();
        assertEquals(2, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        assertEquals(500, is.readUnsignedInteger16());
    }

    /**
     * Checks writing a 16 Bit value. The value is treated as a signed value.
     */
    public void testWriteInteger16() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeInteger16(500);
        os.writeInteger16(-500);

        final byte[] written = os.toByteArray();
        assertEquals(4, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        assertEquals(500, is.readInteger16());
        assertEquals(-500, is.readInteger16());
    }

    /**
     * Checks writing a 24 Bit value. The value is treated as an unsigned value.
     */
    public void testWriteUnsignedInteger24() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeUnsignedInteger24(15733026); // 0xF01122

        final byte[] written = os.toByteArray();
        assertEquals(3, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        assertEquals(15733026, is.readUnsignedInteger24());
    }

    /**
     * Checks writing a 24 Bit value. The value is treated as a signed value.
     */
    public void testWriteInteger24() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeInteger24(7344418);
        os.writeInteger24(-7344418);

        final byte[] written = os.toByteArray();
        assertEquals(6, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        assertEquals(7344418, is.readInteger24());
        assertEquals(-7344418, is.readInteger24());
    }

    /**
     * Checks writing a 32 Bit value. The value is treated as an unsigned value.
     */
    public void testWriteUnsignedInteger32() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeUnsignedInteger32(4059231220L); // 0xF1F2F3F4

        final byte[] written = os.toByteArray();
        assertEquals(4, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        assertEquals(4059231220L, is.readUnsignedInteger32());
    }

    /**
     * Checks writing a 32 Bit value. The value is treated as a signed value.
     */
    public void testWriteInteger32() throws Exception {
        final IpdsByteArrayOutputStream os = new IpdsByteArrayOutputStream();
        os.writeInteger32(1632781107);
        os.writeInteger32(-1632781107);

        final byte[] written = os.toByteArray();
        assertEquals(8, written.length);

        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(written);
        assertEquals(1632781107, is.readInteger32());
        assertEquals(-1632781107, is.readInteger32());
    }
}
