package de.textmode.ipdsbox.core;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link ExceptionHelper}.
 */
public final class ExceptionHelperTest extends TestCase {

    /**
     * Stacktrace to string with a simple (single) exception.
     */
    public void testStackTraceToStringWithSingleException() {
        final Exception e = new IOException("This is e1");
        final String s = ExceptionHelper.stackTraceToString(e);

        assertTrue(s.startsWith("java.io.IOException: This is e1"));
        assertTrue(s.contains("    at mk.ipdsbox.core.ExceptionHelperTest."));
    }

    /**
     * Stacktrace to string with a nested exception.
     */
    public void testStackTraceToStringWithNestedException() {
        final Exception e1 = new IOException("This is e1");
        final Exception e2 = new IOException("This is e2", e1);
        final String s = ExceptionHelper.stackTraceToString(e2);

        assertTrue(s.startsWith("java.io.IOException: This is e2"));
        assertTrue(s.contains("Caused by: java.io.IOException: This is e1"));
        assertTrue(s.contains("    at mk.ipdsbox.core.ExceptionHelperTest."));
    }
}
