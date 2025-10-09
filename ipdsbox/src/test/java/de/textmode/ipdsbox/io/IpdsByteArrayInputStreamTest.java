package de.textmode.ipdsbox.io;

import java.io.IOException;
import java.util.Arrays;
import java.util.HexFormat;

import junit.framework.TestCase;

/**
 * Unit-Tests for the class {@link IpdsByteArrayInputStream}.
 */
public final class IpdsByteArrayInputStreamTest extends TestCase {

    /**
     * Helper Method for creating a {@link IpdsByteArrayInputStream}.
     */
    private static IpdsByteArrayInputStream buildIpdsByteArrayInputStream(final String hexString) {
        return new IpdsByteArrayInputStream(HexFormat.of().parseHex(hexString));
    }

    /**
     * Checks reading unsigned 8 bit values.
     */
    public void testReadUnsignedByte() throws Exception {
        assertEquals(0, buildIpdsByteArrayInputStream("00").readUnsignedByte());
        assertEquals(1, buildIpdsByteArrayInputStream("01").readUnsignedByte());
        assertEquals(128, buildIpdsByteArrayInputStream("80").readUnsignedByte());
        assertEquals(255, buildIpdsByteArrayInputStream("FF").readUnsignedByte());
    }

    /**
     * Checks reading signed 8 bit values.
     */
    public void testReadSignedByte() throws Exception {
        assertEquals(0, buildIpdsByteArrayInputStream("00").readByte());
        assertEquals(1, buildIpdsByteArrayInputStream("01").readByte());
        assertEquals(-128, buildIpdsByteArrayInputStream("80").readByte());
        assertEquals(-1, buildIpdsByteArrayInputStream("FF").readByte());
    }

    /**
     * Checks reading unsigned 16 bit values.
     */
    public void testReadUnsignedInteger16() throws Exception {
        assertEquals(0, buildIpdsByteArrayInputStream("0000").readUnsignedInteger16());
        assertEquals(1, buildIpdsByteArrayInputStream("0001").readUnsignedInteger16());
        assertEquals(512, buildIpdsByteArrayInputStream("0200").readUnsignedInteger16());
        assertEquals(50176, buildIpdsByteArrayInputStream("C400").readUnsignedInteger16());
        assertEquals(65535, buildIpdsByteArrayInputStream("FFFF").readUnsignedInteger16());
    }

    /**
     * Checks reading signed 16 bit values.
     */
    public void testReadSignedInteger16() throws Exception {
        assertEquals(12345, buildIpdsByteArrayInputStream("3039").readInteger16());
        assertEquals(12288, buildIpdsByteArrayInputStream("3000").readInteger16());
        assertEquals(1024, buildIpdsByteArrayInputStream("0400").readInteger16());
        assertEquals(512, buildIpdsByteArrayInputStream("0200").readInteger16());
        assertEquals(1, buildIpdsByteArrayInputStream("0001").readInteger16());
        assertEquals(0, buildIpdsByteArrayInputStream("0000").readInteger16());
        assertEquals(-1, buildIpdsByteArrayInputStream("FFFF").readInteger16());
        assertEquals(-512, buildIpdsByteArrayInputStream("FE00").readInteger16());
        assertEquals(-1024, buildIpdsByteArrayInputStream("FC00").readInteger16());
        assertEquals(-12288, buildIpdsByteArrayInputStream("D000").readInteger16());
        assertEquals(-12345, buildIpdsByteArrayInputStream("CFC7").readInteger16());
    }

    /**
     * Checks reading unsigned 24 bit values.
     */
    public void testReadUnsignedInteger24() throws Exception {
        assertEquals(0, buildIpdsByteArrayInputStream("000000").readUnsignedInteger24());
        assertEquals(1, buildIpdsByteArrayInputStream("000001").readUnsignedInteger24());
        assertEquals(512, buildIpdsByteArrayInputStream("000200").readUnsignedInteger24());
        assertEquals(12845056, buildIpdsByteArrayInputStream("C40000").readUnsignedInteger24());
        assertEquals(16777215, buildIpdsByteArrayInputStream("FFFFFF").readUnsignedInteger24());
    }

    /**
     * Checks reading signed 24 bit values.
     */
    public void testReadSignedInteger24() throws Exception {
        assertEquals(0, buildIpdsByteArrayInputStream("000000").readInteger24());
        assertEquals(1, buildIpdsByteArrayInputStream("000001").readInteger24());
        assertEquals(512, buildIpdsByteArrayInputStream("000200").readInteger24());
        assertEquals(-3932160, buildIpdsByteArrayInputStream("C40000").readInteger24());
        assertEquals(-1, buildIpdsByteArrayInputStream("FFFFFF").readInteger24());
    }

    /**
     * Checks reading unsigned 32 bit values.
     */
    public void testReadUnsignedInteger32() throws Exception {
        assertEquals(12345, buildIpdsByteArrayInputStream("00003039").readUnsignedInteger32());
        assertEquals(12288, buildIpdsByteArrayInputStream("00003000").readUnsignedInteger32());
        assertEquals(1024, buildIpdsByteArrayInputStream("00000400").readUnsignedInteger32());
        assertEquals(512, buildIpdsByteArrayInputStream("00000200").readUnsignedInteger32());
        assertEquals(1, buildIpdsByteArrayInputStream("00000001").readUnsignedInteger32());
        assertEquals(0, buildIpdsByteArrayInputStream("00000000").readUnsignedInteger32());
        assertEquals(4294967295L, buildIpdsByteArrayInputStream("FFFFFFFF").readUnsignedInteger32());
        assertEquals(4294966784L, buildIpdsByteArrayInputStream("FFFFFE00").readUnsignedInteger32());
        assertEquals(4294966272L, buildIpdsByteArrayInputStream("FFFFFC00").readUnsignedInteger32());
    }

    /**
     * Checks reading signed 32 bit values.
     */
    public void testReadSignedInteger32() throws Exception {
        assertEquals(12345, buildIpdsByteArrayInputStream("00003039").readInteger32());
        assertEquals(12288, buildIpdsByteArrayInputStream("00003000").readInteger32());
        assertEquals(1024, buildIpdsByteArrayInputStream("00000400").readInteger32());
        assertEquals(512, buildIpdsByteArrayInputStream("00000200").readInteger32());
        assertEquals(1, buildIpdsByteArrayInputStream("00000001").readInteger32());
        assertEquals(0, buildIpdsByteArrayInputStream("00000000").readInteger32());
        assertEquals(-1, buildIpdsByteArrayInputStream("FFFFFFFF").readInteger32());
        assertEquals(-512, buildIpdsByteArrayInputStream("FFFFFE00").readInteger32());
        assertEquals(-1024, buildIpdsByteArrayInputStream("FFFFFC00").readInteger32());
        assertEquals(-12288, buildIpdsByteArrayInputStream("FFFFD000").readInteger32());
        assertEquals(-12345, buildIpdsByteArrayInputStream("FFFFCFC7").readInteger32());
    }

    /**
     * Checks reading multiple values.
     */
    public void testReadMultipleValues() throws Exception {
        final IpdsByteArrayInputStream is =
                buildIpdsByteArrayInputStream("01000100000100000001F4F5F64A5AF1F2F3010203");

        assertEquals(1, is.readUnsignedByte());
        assertEquals(1, is.readUnsignedInteger16());
        assertEquals(1, is.readUnsignedInteger24());
        assertEquals(1, is.readUnsignedInteger32());
        assertEquals("456", is.readEbcdicString(3));
        assertEquals("[]", is.readEbcdicString(2));
        assertTrue(Arrays.equals(HexFormat.of().parseHex("F1F2F3"), is.readBytes(3)));

        assertTrue(Arrays.equals(HexFormat.of().parseHex("010203"), is.readRemainingBytes()));
        assertEquals(0, is.readRemainingBytes().length); // will return an empty array if no bytes left to read!

        // All bytes are consumed - reading even one more byte should throw an exception....
        try {
            is.readUnsignedByte();
            fail("read after the end of the stream...");
        } catch (final IOException e) {
            assertTrue(e.getMessage().contains("0 bytes left to parse."));
        }
    }

    /**
     * Checks reading multiple values (staring at non-zero offset and some padding bytes at the end).
     */
    public void testReadMultipleValuesWithPaddingBytesAndNonZeroOffset() throws Exception {
        final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(
                HexFormat.of().parseHex("FFFFFF01000100000100000001FFFF"),
                3,
                2);

        assertEquals(1, is.readUnsignedByte());
        assertEquals(1, is.readUnsignedInteger16());
        assertEquals(1, is.readUnsignedInteger24());
        assertEquals(1, is.readUnsignedInteger32());

        // All bytes are consumed - reading even one more byte should throw an exception....
        try {
            is.readUnsignedByte();
            fail("read after the end of the stream...");
        } catch (final IOException e) {
            assertTrue(e.getMessage().contains("0 bytes left to parse."));
        }
    }

    /**
     * Tests reading with specifying an offset.
     */
    public void testReadWithOffset() throws Exception {
        final IpdsByteArrayInputStream is = buildIpdsByteArrayInputStream("00112233445566778899");

        assertTrue((Arrays.equals(HexFormat.of().parseHex("4455"), is.readBytes(4, 2))));
        assertEquals(4, is.bytesAvailable());

        assertTrue((Arrays.equals(HexFormat.of().parseHex("22334455"), is.readBytes(2, 4))));
        assertEquals(4, is.bytesAvailable());

        assertTrue((Arrays.equals(HexFormat.of().parseHex("00"), is.readBytes(0, 1))));
        assertEquals(9, is.bytesAvailable());
    }

    /**
     * Tests reading and rewinding.
     */
    public void testReadAndRewind() throws Exception {
        final IpdsByteArrayInputStream is = buildIpdsByteArrayInputStream("00112233445566778899");

        assertTrue(is.readUnsignedByte() == 0x00);
        assertTrue(is.readUnsignedByte() == 0x11);
        assertTrue(is.readUnsignedByte() == 0x22);

        is.rewind(1);
        assertTrue(is.readUnsignedByte() == 0x22);

        is.rewind(2);
        assertTrue(is.readUnsignedByte() == 0x11);

    }
}
