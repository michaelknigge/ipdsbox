package de.textmode.ipdsbox.core;

/**
 * Some utility methods for handling bits in IPDS commands.
 */
public final class BitUtils {

    /**
     * Private constructor to make checkstyle happy.
     */
    private BitUtils() {
    }

    /**
     * Returns true if the given bit is set in the given byte. Note that this method
     * assumes that bit 0 is the left most bit.
     * @return <code>true</code> if and only the given bit is set.
     */
    public static boolean isBitSet(final int bit, final byte value) {
        return ((value >> (7 - bit)) & 1) == 1;
    }

}
