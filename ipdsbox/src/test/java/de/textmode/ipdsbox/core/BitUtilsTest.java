package de.textmode.ipdsbox.core;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link BitUtils}.
 */
public final class BitUtilsTest extends TestCase {

    /**
     * Checks the BitUtils.isBitSet() method.
     */
    public void testIsBitSet() {
        assertFalse(BitUtils.isBitSet(0, (byte) 0x4F));
        assertTrue(BitUtils.isBitSet(0, (byte) 0x80));
        assertTrue(BitUtils.isBitSet(0, (byte) 0x81));

        assertFalse(BitUtils.isBitSet(1, (byte) 0x2F));
        assertTrue(BitUtils.isBitSet(1, (byte) 0x40));
        assertTrue(BitUtils.isBitSet(1, (byte) 0x41));

        assertFalse(BitUtils.isBitSet(2, (byte) 0x1F));
        assertTrue(BitUtils.isBitSet(2, (byte) 0x20));
        assertTrue(BitUtils.isBitSet(2, (byte) 0x21));

        assertFalse(BitUtils.isBitSet(3, (byte) 0x0F));
        assertTrue(BitUtils.isBitSet(3, (byte) 0x10));
        assertTrue(BitUtils.isBitSet(3, (byte) 0x11));

        assertFalse(BitUtils.isBitSet(4, (byte) 0x07));
        assertTrue(BitUtils.isBitSet(4, (byte) 0x08));
        assertTrue(BitUtils.isBitSet(4, (byte) 0x09));

        assertFalse(BitUtils.isBitSet(5, (byte) 0x03));
        assertTrue(BitUtils.isBitSet(5, (byte) 0x04));
        assertTrue(BitUtils.isBitSet(5, (byte) 0x05));

        assertFalse(BitUtils.isBitSet(6, (byte) 0x01));
        assertTrue(BitUtils.isBitSet(6, (byte) 0x02));
        assertTrue(BitUtils.isBitSet(6, (byte) 0x03));

        assertFalse(BitUtils.isBitSet(7, (byte) 0x00));
        assertTrue(BitUtils.isBitSet(7, (byte) 0x01));
        assertFalse(BitUtils.isBitSet(7, (byte) 0x02));
    }

}
