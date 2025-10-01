package mk.ipdsbox.ipds.commands;

import junit.framework.TestCase;
import java.util.HexFormat;

/**
 * JUnit tests of the {@link SetHomeStateCommand}.
 */
public final class SetHomeStateCommandTest extends TestCase {

    /**
     * Construction of a {@link SetHomeStateCommand} with valid data.
     */
    public void testHappyFlow() throws Exception {
        new SetHomeStateCommand(HexFormat.of().parseHex("0005D69700"));
    }
}
