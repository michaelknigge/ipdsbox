package mk.ipdsbox.ipds.commands;

import java.nio.charset.Charset;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;
import mk.ipdsbox.core.InvalidIpdsCommandException;
import mk.ipdsbox.core.IpdsboxRuntimeException;

/**
 * JUnit tests of the {@link IpdsCommand}.
 */
public final class IpdsCommandTest extends TestCase {

    /**
     * Just test a valid IPDS command.
     */
    public void testValidIpdsCommand() throws Exception {
        final byte[] data = DatatypeConverter.parseHexBinary("0005D6E480");
        final IpdsCommand command = new SenseTypeAndModelCommand(data);

        assertEquals("STM - Sense Type and Model", command.toString());

        assertEquals(5, command.getCommandLength());
        assertEquals(IpdsCommandId.STM, command.getCommandCode());
        assertTrue(command.getCommandFlags().isAcknowledgmentRequired());

        assertFalse(command.getCommandFlags().hasCorrelationID());
        assertFalse(command.getCommandFlags().isAcknowledgmentContinuationRequested());
        assertFalse(command.getCommandFlags().isLongAcknowledgeReplyAccepted());
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
        // This (D6EE) is !NOT! the command id of the STM command!
        final byte[] data = DatatypeConverter.parseHexBinary("0005D68F80");
        try {
            new SenseTypeAndModelCommand(data);
        } catch (final IpdsboxRuntimeException e) {
            assertEquals(
                "An IpdsCommand with command id X'd68f' was constructed but command id X'd6e4' was expected.",
                e.getMessage());
        }
    }

    /**
     * Getting the data portion of an IPDS command that has no correlation id.
     */
    public void testGetDataWithoutCorrelationId() throws Exception {
        final IpdsCommand command = new NoOperationCommand(DatatypeConverter.parseHexBinary("0008D60300F1F2F3"));
        assertEquals("NOP - No Operation", command.toString());
        assertEquals(IpdsCommandId.NOP, command.getCommandCode());

        assertFalse(command.getCommandFlags().hasCorrelationID());
        assertTrue(Arrays.equals(new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3}, command.getData()));
        assertEquals("123", command.getDataAsString());
        assertEquals("123", command.getDataAsString("IBM500"));
        assertEquals("123", command.getDataAsString(Charset.forName("IBM500")));
    }

    /**
     * Getting the data portion of an IPDS command that has a correlation id.
     */
    public void testNoOperationWithCorrelationId() throws Exception {
        final IpdsCommand command = new NoOperationCommand(DatatypeConverter.parseHexBinary("000AD603401234F1F2F3"));
        assertEquals("NOP - No Operation", command.toString());
        assertEquals(IpdsCommandId.NOP, command.getCommandCode());

        assertTrue(command.getCommandFlags().hasCorrelationID());
        assertTrue(Arrays.equals(new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3}, command.getData()));
        assertEquals("123", command.getDataAsString());
        assertEquals("123", command.getDataAsString("IBM500"));
        assertEquals("123", command.getDataAsString(Charset.forName("IBM500")));
    }

    /**
     * Getting the data portion of an IPDS command that has no correlation id when there is no data portion.
     */
    public void testGetDataWithoutCorrelationIdWhenThereIsNoData() throws Exception {
        final IpdsCommand command = new NoOperationCommand(DatatypeConverter.parseHexBinary("0005D60300"));
        assertEquals("NOP - No Operation", command.toString());
        assertEquals(IpdsCommandId.NOP, command.getCommandCode());

        assertFalse(command.getCommandFlags().hasCorrelationID());
        assertEquals(0, command.getData().length);
        assertTrue(command.getDataAsString().isEmpty());
        assertTrue(command.getDataAsString("IBM500").isEmpty());
        assertTrue(command.getDataAsString(Charset.forName("IBM500")).isEmpty());
    }

    /**
     * Getting the data portion of an IPDS command that has a correlation id when there is no data portion.
     */
    public void testGetDataWithCorrelationIdWhenThereIsNoData() throws Exception {
        final IpdsCommand command = new NoOperationCommand(DatatypeConverter.parseHexBinary("0007D603401234"));
        assertEquals("NOP - No Operation", command.toString());
        assertEquals(IpdsCommandId.NOP, command.getCommandCode());

        assertTrue(command.getCommandFlags().hasCorrelationID());
        assertEquals(0, command.getData().length);
        assertTrue(command.getDataAsString().isEmpty());
        assertTrue(command.getDataAsString("IBM500").isEmpty());
        assertTrue(command.getDataAsString(Charset.forName("IBM500")).isEmpty());
    }
}
