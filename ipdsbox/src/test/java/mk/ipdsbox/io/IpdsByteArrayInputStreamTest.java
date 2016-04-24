package mk.ipdsbox.io;

import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link IpdsByteArrayInputStreamTest}.
 */
public final class IpdsByteArrayInputStreamTest extends TestCase {

    /**
     * Tests the readByte() Method.
     */
    public void testReadByte() throws Exception {
        assertEquals(0x00, new IpdsByteArrayInputStream(new byte[] {0x00}).readByte());
        assertEquals(0xFF, new IpdsByteArrayInputStream(new byte[] {(byte) 0xFF}).readByte());

        try {
            new IpdsByteArrayInputStream(new byte[0]).readByte();
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 1 byte available.", e.getMessage());
        }
    }

    /**
     * Tests the readWord() Method.
     */
    public void testReadWord() throws Exception {
        assertEquals(0x00, new IpdsByteArrayInputStream(new byte[] {0x00, 0x00}).readWord());
        assertEquals(0xFF, new IpdsByteArrayInputStream(new byte[] {0x00, (byte) 0xFF}).readWord());
        assertEquals(0xFFFF, new IpdsByteArrayInputStream(new byte[] {(byte) 0xFF, (byte) 0xFF}).readWord());

        try {
            new IpdsByteArrayInputStream(new byte[1]).readWord();
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 2 bytes available.", e.getMessage());
        }
    }

    /**
     * Tests the readDoubleWord() Method.
     */
    public void testReadDoubleWord() throws Exception {
        assertEquals(0x00, new IpdsByteArrayInputStream(new byte[] {0x00, 0x00, 0x00, 0x00}).readDoubleWord());
        assertEquals(0xFF, new IpdsByteArrayInputStream(new byte[] {0x00, 0x00, 0x00, (byte) 0xFF}).readDoubleWord());
        assertEquals(0xFFFF,
            new IpdsByteArrayInputStream(new byte[] {0x00, 0x00, (byte) 0xFF, (byte) 0xFF}).readDoubleWord());
        assertEquals(0xFFFFFF,
            new IpdsByteArrayInputStream(new byte[] {0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}).readDoubleWord());
        assertEquals(0xFFFFFFFFL,
            new IpdsByteArrayInputStream(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF})
                .readDoubleWord());
        try {
            new IpdsByteArrayInputStream(new byte[0]).readDoubleWord();
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 4 bytes available.", e.getMessage());
        }

        try {
            new IpdsByteArrayInputStream(new byte[1]).readDoubleWord();
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 4 bytes available.", e.getMessage());
        }

        try {
            new IpdsByteArrayInputStream(new byte[2]).readDoubleWord();
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 4 bytes available.", e.getMessage());
        }

        try {
            new IpdsByteArrayInputStream(new byte[3]).readDoubleWord();
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 4 bytes available.", e.getMessage());
        }
    }

    /**
     * Tests the readQuadrupleWord() Method.
     */
    public void testReadQuadrupleWord() throws Exception {
        assertEquals(0x00, new IpdsByteArrayInputStream(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})
            .readQuadrupleWord());

        assertEquals(0xFF,
            new IpdsByteArrayInputStream(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF})
                .readQuadrupleWord());

        assertEquals(0xFFFF,
            new IpdsByteArrayInputStream(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF})
                .readQuadrupleWord());

        assertEquals(0xFFFFFF,
            new IpdsByteArrayInputStream(
                new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}).readQuadrupleWord());

        assertEquals(0xFFFFFFFFL,
            new IpdsByteArrayInputStream(
                new byte[] {0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF})
                    .readQuadrupleWord());

        assertEquals(0xFFFFFFFFFFL,
            new IpdsByteArrayInputStream(
                new byte[] {0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF})
                    .readQuadrupleWord());

        assertEquals(0xFFFFFFFFFFFFL,
            new IpdsByteArrayInputStream(
                new byte[] {0x00, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF})
                    .readQuadrupleWord());

        assertEquals(0xFFFFFFFFFFFFFFL,
            new IpdsByteArrayInputStream(
                new byte[] {0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xFF})
                        .readQuadrupleWord());

        assertEquals(0xFFFFFFFFFFFFFFFFL,
            new IpdsByteArrayInputStream(
                new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xFF})
                        .readQuadrupleWord());

        try {
            new IpdsByteArrayInputStream(new byte[0]).readQuadrupleWord();
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 8 bytes available.", e.getMessage());
        }

        for (int size = 1; size < 8; ++size) {
            try {
                new IpdsByteArrayInputStream(new byte[size]).readQuadrupleWord();
                fail();
            } catch (final IOException e) {
                assertEquals("Less than 8 bytes available.", e.getMessage());
            }
        }
    }

    /**
     * Tests the readAsciiEncodedString() Method.
     */
    public void testReadAsciiEncodedString() throws Exception {
        assertEquals("", new IpdsByteArrayInputStream(new byte[0]).readAsciiEncodedString(0));
        assertEquals(" ", new IpdsByteArrayInputStream(new byte[] {0x20}).readAsciiEncodedString(1));
        assertEquals("12", new IpdsByteArrayInputStream(new byte[] {0x31, 0x32}).readAsciiEncodedString(2));

        try {
            new IpdsByteArrayInputStream(new byte[] {0x20}).readAsciiEncodedString(2);
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 2 byte(s) available.", e.getMessage());
        }
    }

    /**
     * Tests the readEbcdicEncodedString() Method.
     */
    public void testReadEbcdicEncodedString() throws Exception {
        assertEquals("", new IpdsByteArrayInputStream(new byte[0]).readEbcdicEncodedString(0));
        assertEquals(" ", new IpdsByteArrayInputStream(new byte[] {0x40}).readEbcdicEncodedString(1));
        assertEquals("12",
            new IpdsByteArrayInputStream(new byte[] {(byte) 0xF1, (byte) 0xF2}).readEbcdicEncodedString(2));

        try {
            new IpdsByteArrayInputStream(new byte[] {0x20}).readEbcdicEncodedString(2);
            fail();
        } catch (final IOException e) {
            assertEquals("Less than 2 byte(s) available.", e.getMessage());
        }
    }

    /**
     * Tests the readTriplet() Method.
     */
    public void testReadTriplet() throws Exception {
        // Triplet without data
        final byte[] triplet1 = new byte[] {0x02, 0x01};
        assertTrue(Arrays.equals(triplet1, new IpdsByteArrayInputStream(triplet1).readTripletIfExists()));

        // Triplet with data
        final byte[] triplet2 = new byte[] {0x03, 0x01, 0x03};
        assertTrue(Arrays.equals(triplet2, new IpdsByteArrayInputStream(triplet2).readTripletIfExists()));

        // Triplet with data and length with first bit set to 1
        final byte[] triplet3 = new byte[255];
        triplet3[0] = (byte) 0xFF;
        for (int i = 1; i < 255; ++i) {
            triplet3[i] = (byte) i;
        }
        assertTrue(Arrays.equals(triplet3, new IpdsByteArrayInputStream(triplet3).readTripletIfExists()));

        // No Triplet available...
        assertNull(new IpdsByteArrayInputStream(new byte[0]).readTripletIfExists());
    }
}
