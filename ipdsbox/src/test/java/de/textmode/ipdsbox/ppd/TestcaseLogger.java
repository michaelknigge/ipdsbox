package de.textmode.ipdsbox.ppd;

import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.LoggerInterface;

/**
 * The {@link TestcaseLogger} collects all log messages in an ArrayList
 * for later analysis by the JUnit test.
 */
public final class TestcaseLogger implements LoggerInterface {

    private final List<String> logs = new ArrayList<>();

    /**
     * Discard all collected log messages.
     */
    public void reset() {
        this.logs.clear();
    }

    /**
     * Returns the {@link List} with all collected log messages.
     * @return A {@link List} with all collected log messages
     */
    public List<String> getLoggesMessages() {
        return this.logs;
    }

    @Override
    public void debug(final Object message) {
        this.logs.add("DEBUG: " + message);
    }

    @Override
    public void debug(final Object message, final Throwable t) {
        this.logs.add("DEBUG: " + message);
        this.logs.add("DEBUG: " + t.getMessage());
    }

    @Override
    public void info(final Object message) {
        this.logs.add("INFO: " + message);
    }

    @Override
    public void info(final Object message, final Throwable t) {
        this.logs.add("INFO: " + message);
        this.logs.add("INFO: " + t.getMessage());
    }

    @Override
    public void warn(final Object message) {
        this.logs.add("WARN: " + message);
    }

    @Override
    public void warn(final Object message, final Throwable t) {
        this.logs.add("WARN: " + message);
        this.logs.add("WARN: " + t.getMessage());
    }

    @Override
    public void error(final Object message) {
        this.logs.add("ERROR: " + message);
    }

    @Override
    public void error(final Object message, final Throwable t) {
        this.logs.add("ERROR: " + message);
        this.logs.add("ERROR: " + t.getMessage());
    }

}
