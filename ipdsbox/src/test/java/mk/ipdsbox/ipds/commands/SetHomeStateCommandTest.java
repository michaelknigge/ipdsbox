package mk.ipdsbox.ipds.commands;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link SetHomeStateCommand}.
 */
public final class SetHomeStateCommandTest extends TestCase {

    /**
     * Construction of a {@link SetHomeStateCommand} with valid data.
     */
    public void testHappyFlow() throws Exception {
        new SetHomeStateCommand(DatatypeConverter.parseHexBinary("0005D69700"));
    }
}
