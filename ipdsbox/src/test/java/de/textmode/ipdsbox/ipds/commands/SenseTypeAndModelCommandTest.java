package de.textmode.ipdsbox.ipds.commands;

import java.util.HexFormat;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link SenseTypeAndModelCommand}.
 */
public final class SenseTypeAndModelCommandTest extends TestCase {

    /**
     * Construction of a {@link SenseTypeAndModelCommand} with valid data.
     */
    public void testHappyFlow() throws Exception {
        new SenseTypeAndModelCommand(HexFormat.of().parseHex("0005D6E480"));
    }
}
