package de.textmode.ipdsbox.core;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Helper class that provides various utility methods for the ipdsbox JUnit-Tests.
 */
public final class TestHelper {

    private static final Charset EBCDIC = Charset.forName("IBM500");
    private static final Charset ASCII = Charset.forName("IBM850");

    /**
     * Private constructor to make checkstyle happy.
     */
    private TestHelper() {
    }

    /**
     * Writes a {@link String} into an {@link OutputStream} in EBCDIC encoding.
     * @param out the {@link OutputStream} to write to
     * @param str the {@link String} to be written.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeEbcdicString(final OutputStream out, final String str) throws IOException {
        out.write(str.getBytes(EBCDIC));
    }

    /**
     * Writes a {@link String} into an {@link OutputStream} in EBCDIC encoding.
     * @param out the {@link OutputStream} to write to
     * @param str the {@link String} to be written.
     * @param length the {@link String} will be padded with spaces up to this length.
     * @throws IOException if an I/O error occurs or the supplied string is longer than length.
     */
    public static void writeEbcdicString(final OutputStream out, final String str, final int length)
        throws IOException {
        if (str.length() > length) {
            throw new IOException("String too long");
        } else {
            out.write(StringUtils.padRight(str, length).getBytes(EBCDIC));
        }
    }

    /**
     * Writes a {@link String} into an {@link OutputStream} in ASCII encoding.
     * @param out the {@link OutputStream} to write to
     * @param str the {@link String} to be written.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeAsciiString(final OutputStream out, final String str) throws IOException {
        out.write(str.getBytes(ASCII));
    }

    /**
     * Writes a {@link String} into an {@link OutputStream} in EBCDIC encoding.
     * @param out the {@link OutputStream} to write to
     * @param str the {@link String} to be written.
     * @param length the {@link String} will be padded with spaces up to this length.
     * @throws IOException if an I/O error occurs or the supplied string is longer than length.
     */
    public static void writeAsciiString(final OutputStream out, final String str, final int length) throws IOException {
        if (str.length() > length) {
            throw new IOException("String too long");
        } else {
            out.write(StringUtils.padRight(str, length).getBytes(ASCII));
        }
    }
}
