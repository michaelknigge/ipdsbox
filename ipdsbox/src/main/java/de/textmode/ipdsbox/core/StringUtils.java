package de.textmode.ipdsbox.core;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * Some utility methods for handling {@link String} objects.
 */
public final class StringUtils {

    private static final String INDENTION = "  ";
    private static final String HEXES = "0123456789ABCDEF";

    /**
     * Private constructor to make checkstyle happy.
     */
    private StringUtils() {
    }

    /**
     * Append trailing spaces to a {@link String}.
     * @param s the {@link String} that is to be filled up with spaces
     * @param n the desired length of the string
     * @return a String filled up with spaces up to n bytes.
     */
    public static String padRight(final String s, final int n) {
        return n <= 0 ? s : String.format("%1$-" + n + "s", s);
    }

    /**
     * Builds a Hex-String from a byte array.
     * @param bytes an array containing bytes
     * @return a Hex-String of the contents of the supplied byte array.
     */
    public static String toHexString(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (final byte b : bytes) {
            sb.append(HEXES.charAt((b & 0xF0) >> 4));
            sb.append(HEXES.charAt(b & 0x0F));
        }
        return sb.toString();
    }

    /**
     * Builds a nicely formatted String from a Java object.
     * @param obj the object to be pretty-printed.
     * @return a nicely formatted String from a Java object.
     */
    public static String toPrettyString(final Object obj) {
        return toPrettyString(INDENTION, obj);
    }

    /**
     * Builds a nicely formatted String from a Java object.
     * @param obj the object to be pretty-printed.
     * @return a nicely formatted String from a Java object.
     */
    private static String toPrettyString(final String indention, final Object obj) {
        assert indention.length() >= 2;

        if (obj == null) {
            return "*** null ***";
        }

        final Class<?> clazz = obj.getClass();
        final StringJoiner joiner = new StringJoiner(",\n", clazz.getSimpleName() + " {\n", "\n" + indention.substring(0, indention.length() - INDENTION.length()) + "}\n");

        for (final Field field : clazz.getDeclaredFields()) {

            field.setAccessible(true);

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            try {
                final Object value = field.get(obj);

                if (isAllowedType(field.getType())) {
                    // TOOD: for "byte" and "int" that ends on "flag" or "flags", additionally output the
                    // set bits, i.e. "bits=0,5,7" or "bits=*none*"
                    if (field.getType().equals(int.class)) {
                        joiner.add(indention + field.getName() + ": 0x" + Integer.toHexString((int) value) + " (" + value + ")");
                    } else if (field.getType().equals(long.class)) {
                        joiner.add(indention + field.getName() + ": 0x" + Long.toHexString((long) value) + " (" + value + ")");
                    } else {
                        joiner.add(indention + field.getName() + ": " + value);
                    }
                } else if (isIpdsboxType(field.getType())) {
                    joiner.add(indention + field.getName() + ": " + toPrettyString(indention + INDENTION, value));
                } else if (Collection.class.isAssignableFrom(field.getType()) && value != null) {
                    if (((Collection) value).isEmpty()) {
                        joiner.add(indention + field.getName() + ": [ ** empty ** ]");
                    } else {
                        joiner.add(indention + field.getName() + ": [\n" + indention + INDENTION + listToString(indention + INDENTION, (Collection<?>) value) + indention + "]");
                    }
                }
            } catch (final IllegalAccessException e) {
                joiner.add(indention + field.getName() + ": <inaccessible>");
            }
        }

        return joiner.toString();
    }

    private static boolean isAllowedType(final Class<?> type) {
        return type.equals(byte.class) ||
                type.equals(int.class) ||
                type.equals(long.class) ||
                type.equals(String.class) ||
                type.isEnum();
    }

    private static boolean isIpdsboxType(final Class<?> type) {
        return type.getName().startsWith("de.textmode.ipdsbox.ipds.triplets.") ||
                type.getName().startsWith("de.textmode.ipdsbox.ipds.acknowledge.") ||
                type.getName().startsWith("de.textmode.ipdsbox.ipds.xohorders.") ||
                type.getName().startsWith("de.textmode.ipdsbox.ipds.commands");
    }

    private static String listToString(final String indention, final Collection<?> list) {
        final StringJoiner listJoiner = new StringJoiner(indention);

        for (final Object elem : list) {
            if (elem == null) {
                listJoiner.add(indention + "null");
            } else if (isAllowedType(elem.getClass())) {
                listJoiner.add(indention + elem.toString());
            } else {
                listJoiner.add(toPrettyString(indention + INDENTION, elem));
            }
        }

        return listJoiner.toString();
    }
}
