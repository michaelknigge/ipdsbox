package mk.ipdsbox.core;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class contains utility methods for exceptions.
 */
public final class ExceptionHelper {

    /**
     * Private constructor to make checkstyle happy.
     */
    private ExceptionHelper() {
    }

    /**
     * Takes an {@link Throwable} and returns the stack trace as a {@link String}.
     *
     * @param t the throwable.
     * @return A {@link String} that contains the stack trace.
     */
    public static String stackTraceToString(final Throwable t) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString().replace("\t", "    ");
    }
}
