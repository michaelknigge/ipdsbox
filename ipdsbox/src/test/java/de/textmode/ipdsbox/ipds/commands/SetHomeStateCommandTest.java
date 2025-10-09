package de.textmode.ipdsbox.ipds.commands;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link SetHomeStateCommand}.
 */
public final class SetHomeStateCommandTest extends TestCase {

    /**
     * Construction of a {@link SetHomeStateCommand} with valid data.
     */
    public void testHappyFlow() throws Exception {
        new SetHomeStateCommand(IpdsCommandTest.streamFromHex("0005D69700"));
    }
}
