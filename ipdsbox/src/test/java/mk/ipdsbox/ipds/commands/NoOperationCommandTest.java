package mk.ipdsbox.ipds.commands;

import junit.framework.TestCase;

import java.util.HexFormat;

/**
 * JUnit tests of the {@link NoOperationCommand}.
 */
public final class NoOperationCommandTest extends TestCase {

    /**
     * Construction of a {@link NoOperationCommand} with data.
     */
    public void testNopWithData() throws Exception {
        new NoOperationCommand(HexFormat.of().parseHex("0008D60300F1F2F3"));
    }

    /**
     * Construction of a {@link NoOperationCommand} without.
     */
    public void testNopWithoutData() throws Exception {
        new NoOperationCommand(HexFormat.of().parseHex("0005D60300"));
    }
}
