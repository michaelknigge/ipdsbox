package mk.ipdsbox.ipds;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;
import mk.ipdsbox.core.InvalidIpdsCommandException;
import mk.ipdsbox.core.IpdsboxRuntimeException;

/**
 * JUnit tests of the {@link IpdsCommand}.
 */
public final class IpdsCommandTest extends TestCase {

    /**
     * Justs test with a valid IPDS command.
     */
    public void testValidIpdsCommand() throws Exception {
        final byte[] data = DatatypeConverter.parseHexBinary("0005D6E480");
        final IpdsCommand comand = new SenseTypeAndModelCommand(data);

        assertEquals(5, comand.getCommandLength());
        assertEquals(IpdsCommandId.STM, comand.getCommandCode());
        assertTrue(comand.getCommandFlags().isAcknowledgmentRequired());

        assertFalse(comand.getCommandFlags().hasCorrelationID());
        assertFalse(comand.getCommandFlags().isAcknowledgmentContinuationRequested());
        assertFalse(comand.getCommandFlags().isLongAcknowledgeReplyAccepted());
    }

    /**
     * An IPDS command with an invalid length in the first two length bytes.
     */
    public void testIpdsCommandWithInvalidLengthInLengthBytes() {
        try {
            new SenseTypeAndModelCommand(DatatypeConverter.parseHexBinary("0004D6E480"));
            fail("Should fail because the length in the length field is too short.");
        } catch (final InvalidIpdsCommandException e) {
            assertEquals(
                "The length of the IPDS command (5) does not match the length specified in the length field (4).",
                e.getMessage());
        }

        try {
            new SenseTypeAndModelCommand(DatatypeConverter.parseHexBinary("0006D6E480"));
            fail("Should fail because the length in the length field is too big.");
        } catch (final InvalidIpdsCommandException e) {
            assertEquals(
                "The length of the IPDS command (5) does not match the length specified in the length field (6).",
                e.getMessage());
        }
    }

    /**
     * An IPDS command with an unknown IPDS command code.
     */
    public void testIpdsCommandWithUnknownIpdsCommandCode() {
        try {
            new SenseTypeAndModelCommand(DatatypeConverter.parseHexBinary("0005D61180"));
            fail("Should fail because the IPDS command code is unknown.");
        } catch (final InvalidIpdsCommandException e) {
            assertEquals("The IPDS command has the command id X'd611' which is unknown", e.getMessage());
        }
    }

    /**
     * Construction of a concrete {@link IpdsCommand} with an wrong (but valid!) IPDS command id.
     */
    public void testIpdsCommandWithInvalidIpdsCommandCode() throws Exception {
        // This (D6EE) is !NOT! the command id od the STM command!
        final byte[] data = DatatypeConverter.parseHexBinary("0005D68F80");
        try {
            new SenseTypeAndModelCommand(data);
        } catch (final IpdsboxRuntimeException e) {
            assertEquals(
                "An IpdsCommand with command id X'd68f' was constructed but command id X'd6e4' was expected.",
                e.getMessage());
        }
    }
}
