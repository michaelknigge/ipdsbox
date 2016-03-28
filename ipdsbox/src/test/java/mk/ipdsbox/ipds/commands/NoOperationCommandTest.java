package mk.ipdsbox.ipds.commands;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link NoOperationCommand}.
 */
public final class NoOperationCommandTest extends TestCase {

    /**
     * Construction of a {@link NoOperationCommand} with data.
     */
    public void testNopWithData() throws Exception {
        new NoOperationCommand(DatatypeConverter.parseHexBinary("0008D60300F1F2F3"));
    }

    /**
     * Construction of a {@link NoOperationCommand} without.
     */
    public void testNopWithoutData() throws Exception {
        new NoOperationCommand(DatatypeConverter.parseHexBinary("0005D60300"));
    }
}
