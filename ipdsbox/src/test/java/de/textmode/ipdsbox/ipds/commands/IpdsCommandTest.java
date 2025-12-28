package de.textmode.ipdsbox.ipds.commands;

import java.util.Arrays;
import java.util.HexFormat;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import junit.framework.TestCase;

/**
 * JUnit tests of the {@link IpdsCommand}.
 */
public final class IpdsCommandTest extends TestCase {

    static IpdsByteArrayInputStream streamFromHex(final String hex) {
        final byte[] rawData = HexFormat.of().parseHex(hex);
        final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(rawData);

        return ipds;
    }

    /**
     * Just test a valid IPDS command.
     */
    public void testValidIpdsCommand() throws Exception {
        final IpdsCommand command = IpdsCommandFactory.create(streamFromHex("0005D6E480"));

        assertEquals("STM - Sense Type and Model", command.getDescription());

        //assertEquals(5, command.getCommandLength());
        assertEquals(IpdsCommandId.STM, command.getCommandCode());
        assertTrue(command.getCommandFlags().isAcknowledgmentRequired());

        assertFalse(command.getCommandFlags().hasCorrelationID());
        assertFalse(command.getCommandFlags().isAcknowledgmentContinuationRequested());
        assertFalse(command.getCommandFlags().isLongAcknowledgeReplyAccepted());
    }

    /**
     * An IPDS command with an invalid length in the first two length bytes.
     */
    public void testIpdsCommandWithInvalidLengthInLengthBytes() throws Exception {
        try {
            IpdsCommandFactory.create(streamFromHex("0004D6E480"));
            fail("Should fail because the length in the length field is too short.");
        } catch (final InvalidIpdsCommandException e) {
            assertEquals(
                "An IPDS command to be read seems to be 4 bytes long but the IPDS data stream ends after 5 bytes.",
                e.getMessage());
        }

        try {
            IpdsCommandFactory.create(streamFromHex("0006D6E480"));
            fail("Should fail because the length in the length field is too big.");
        } catch (final InvalidIpdsCommandException e) {
            assertEquals(
                "An IPDS command to be read seems to be 6 bytes long but the IPDS data stream ends after 5 bytes.",
                e.getMessage());
        }
    }

    /**
     * An IPDS command with an unknown IPDS command code.
     */
    public void testIpdsCommandWithUnknownIpdsCommandCode() throws Exception{
        final UnknownIpdsCommand command = (UnknownIpdsCommand) IpdsCommandFactory.create(streamFromHex("0005D61180"));
        assertEquals(null, command.getCommandCode());
        assertEquals(0xD611, command.getCommandCodeId());
    }

    /**
     * Getting the data portion of an IPDS command that has no correlation id.
     */
    public void testGetDataWithoutCorrelationId() throws Exception {
        final NoOperationCommand command = (NoOperationCommand) IpdsCommandFactory.create(streamFromHex("0008D60300F1F2F3"));
        assertEquals("NOP - No Operation", command.getDescription());
        assertEquals(IpdsCommandId.NOP, command.getCommandCode());

        assertFalse(command.getCommandFlags().hasCorrelationID());
        assertTrue(Arrays.equals(new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3}, command.getDataBytes()));
    }

    /**
     * Getting the data portion of an IPDS command that has a correlation id.
     */
    public void testNoOperationWithCorrelationId() throws Exception {
        final NoOperationCommand command = (NoOperationCommand) IpdsCommandFactory.create(streamFromHex("000AD603401234F1F2F3"));
        assertEquals("NOP - No Operation", command.getDescription());
        assertEquals(IpdsCommandId.NOP, command.getCommandCode());

        assertTrue(command.getCommandFlags().hasCorrelationID());
        assertEquals(0x1234, command.getCorrelationId());
        assertTrue(Arrays.equals(new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3}, command.getDataBytes()));
    }

    /**
     * Getting the data portion of an IPDS command that has no correlation id when there is no data portion.
     */
    public void testGetDataWithoutCorrelationIdWhenThereIsNoData() throws Exception {
        final NoOperationCommand command = (NoOperationCommand) IpdsCommandFactory.create(streamFromHex("0005D60300"));
        assertEquals("NOP - No Operation", command.getDescription());
        assertEquals(IpdsCommandId.NOP, command.getCommandCode());

        assertFalse(command.getCommandFlags().hasCorrelationID());
        assertEquals(0, command.getDataBytes().length);
    }

    /**
     * Getting the data portion of an IPDS command that has a correlation id when there is no data portion.
     */
    public void testGetDataWithCorrelationIdWhenThereIsNoData() throws Exception {
        final NoOperationCommand command = (NoOperationCommand) IpdsCommandFactory.create(streamFromHex("0007D603401234"));
        assertEquals("NOP - No Operation", command.getDescription());
        assertEquals(IpdsCommandId.NOP, command.getCommandCode());

        assertTrue(command.getCommandFlags().hasCorrelationID());
        assertEquals(0x1234, command.getCorrelationId());
        assertEquals(0, command.getDataBytes().length);
    }
}
