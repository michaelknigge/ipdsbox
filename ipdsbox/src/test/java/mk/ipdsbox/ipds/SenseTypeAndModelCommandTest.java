package mk.ipdsbox.ipds;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link SenseTypeAndModelCommand}.
 */
public final class SenseTypeAndModelCommandTest extends TestCase {

    /**
     * Construction of a {@link SenseTypeAndModelCommand} with valid data.
     */
    public void testHappyFlow() throws Exception {
        new SenseTypeAndModelCommand(DatatypeConverter.parseHexBinary("0005D6E480"));
    }
}
