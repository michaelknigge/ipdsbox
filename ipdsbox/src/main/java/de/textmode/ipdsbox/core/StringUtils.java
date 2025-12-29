package de.textmode.ipdsbox.core;

/**
 * Some utility methods for handling {@link String} objects.
 */
public final class StringUtils {

    private static final String HEXES = "0123456789ABCDEF";
    private static final String EMPTY = "";

    /**
     * Private constructor to make checkstyle happy.
     */
    private StringUtils() {
    }

    /**
     * Append trailing spaces to a {@link String}.
     */
    public static String padRight(final String s, final int n) {
        return n <= 0 ? s : String.format("%1$-" + n + "s", s);
    }

    /**
     * Builds a Hex-String from a byte array.
     */
    public static String toHexString(final byte[] bytes) {
        if (bytes == null) {
            return EMPTY;
        }

        final StringBuilder sb = new StringBuilder();
        for (final byte b : bytes) {
            sb.append(HEXES.charAt((b & 0xF0) >> 4));
            sb.append(HEXES.charAt(b & 0x0F));
        }
        return sb.toString();
    }
}
