package de.textmode.ipdsbox.ipds.commands;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link IpdsCommandId}.
 */
public final class IpdsCommandIdTest extends TestCase {

    /**
     * Checks if the reverse mapping of all {@link IpdsCommandId}s works.
     */
    public void testAllCommandIds() {
        for (final IpdsCommandId code : IpdsCommandId.values()) {
            final IpdsCommandId toCheck = IpdsCommandId.getForIfExists(code.getValue());

            assertEquals(code, toCheck);
            assertEquals(code.getValue(), toCheck.getValue());
            assertEquals(code.getDescription(), toCheck.getDescription());
        }
    }

    /**
     * Checks if the reverse mapping of all {@link IpdsCommandId}s works.
     */
    public void testUnknownCommandId() {
        assertNull(IpdsCommandId.getForIfExists(0xD611));
    }
}
