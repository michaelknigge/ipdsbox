package mk.ipdsbox.core;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link StringUtils}.
 */
public final class StringUtilsTest extends TestCase {

    /**
     * Tests the method StringUtils.padRight.
     */
    public void testPadRight() {
        assertEquals("", StringUtils.padRight("", 0));
        assertEquals(" ", StringUtils.padRight("", 1));
        assertEquals("A  ", StringUtils.padRight("A", 3));
        assertEquals("ABC", StringUtils.padRight("ABC", 3));
        assertEquals("ABC", StringUtils.padRight("ABC", 2));
    }

    /**
     * Tests the method StringUtils.toHexString.
     */
    public void testToHexString() {
        assertEquals("", StringUtils.toHexString(new byte[0]));
        assertEquals("31", StringUtils.toHexString(new byte[] {0x31}));
        assertEquals("10FF20", StringUtils.toHexString(new byte[] {0x10, (byte) 0xFF, 0x20}));
    }
}
