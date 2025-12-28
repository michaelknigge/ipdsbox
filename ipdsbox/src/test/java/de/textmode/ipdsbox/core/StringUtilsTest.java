package de.textmode.ipdsbox.core;

import de.textmode.ipdsbox.ipds.acknowledge.AcknowledgeReply;
import de.textmode.ipdsbox.ipds.acknowledge.SenseDataAcknowledgeData;
import de.textmode.ipdsbox.ipds.commands.ExecuteOrderHomeStateCommand;
import de.textmode.ipdsbox.ipds.commands.IpdsCommandId;
import de.textmode.ipdsbox.ipds.triplets.CodedGraphicCharacterSetGlobalIdentifierTriplet;
import de.textmode.ipdsbox.ipds.triplets.ColorFidelityTriplet;
import de.textmode.ipdsbox.ipds.triplets.FinishingOperationTriplet;
import de.textmode.ipdsbox.ipds.xohorders.DefineGroupBoundaryOrder;
import junit.framework.TestCase;

/**
 * JUnit tests of the {@link StringUtils}.
 */
public final class StringUtilsTest extends TestCase {

    /**
     * Tests the method StringUtils.padRight.
     */
    public void testPadRight() {
        assertEquals("", StringUtils.padRight("", 0));
        assertEquals(" ", StringUtils.padRight("", 1));
        assertEquals("A  ", StringUtils.padRight("A", 3));
        assertEquals("ABC", StringUtils.padRight("ABC", 3));
        assertEquals("ABC", StringUtils.padRight("ABC", 2));
    }

    /**
     * Tests the method StringUtils.toHexString.
     */
    public void testToHexString() {
        assertEquals("", StringUtils.toHexString(new byte[0]));
        assertEquals("31", StringUtils.toHexString(new byte[] {0x31}));
        assertEquals("10FF20", StringUtils.toHexString(new byte[] {0x10, (byte) 0xFF, 0x20}));
    }

    /**
     * Tests the method StringUtils.ToPrettyString with a simple Triplet.
     */
    public void testToPrettyStringTriplet() {
        final String actual = StringUtils.toPrettyString(new ColorFidelityTriplet());

        final String expected = new StringBuilder()
                .append("ColorFidelityTriplet {\n")
                .append("  continuationRule: 0x2 (2),\n")
                .append("  reportingRule: 0x2 (2),\n")
                .append("  substitutionRule: 0x1 (1)\n")
                .append("}\n")
                .toString();

        assertEquals(expected, actual);
    }

    /**
     * Tests the method StringUtils.ToPrettyString with a complex IPDS command.
     */
    public void testToPrettyStringCommand() throws Exception {

        final DefineGroupBoundaryOrder order =
                new DefineGroupBoundaryOrder(0x00, 0x00);
                //new DefineGroupBoundaryOrder(DefineGroupBoundaryOrder.INITIATE_GROUP, 0x00);

        order.getTriplets().add(new CodedGraphicCharacterSetGlobalIdentifierTriplet(0x500));
        order.getTriplets().add(new FinishingOperationTriplet(0x01));

        final ExecuteOrderHomeStateCommand cmd = new ExecuteOrderHomeStateCommand(order);

        final String actual = StringUtils.toPrettyString(cmd);

        final String expected = new StringBuilder()
                .append("ExecuteOrderHomeStateCommand {\n")
                .append("  order: DefineGroupBoundaryOrder {\n")
                .append("    orderType: 0x0 (0),\n")
                .append("    groupLevel: 0x0 (0),\n")
                .append("    triplets: [\n")
                .append("      CodedGraphicCharacterSetGlobalIdentifierTriplet {\n")
                .append("        graphicCharacterSetGlobalIdentifier: 0x0 (0),\n")
                .append("        codePageGlobalIdentifier: 0x0 (0),\n")
                .append("        codedCharacterSetIdentifier: 0x500 (1280)\n")
                .append("      }\n")
                .append("      FinishingOperationTriplet {\n")
                .append("        operationType: 0x1 (1),\n")
                .append("        finishingOption: 0x0 (0),\n")
                .append("        reference: 0xff (255),\n")
                .append("        count: 0x0 (0),\n")
                .append("        axisOffset: 0xffff (65535),\n")
                .append("        positions: [ ** empty ** ]\n")
                .append("      }\n")
                .append("    ]\n")
                .append("  }\n")
                .append("\n")
                .append("}\n")
                .toString();

        assertEquals(expected, actual);
    }

    /**
     * Tests the method StringUtils.ToPrettyString with a complex Acknowledge Reply.
     */
    public void testToPrettyStringAcknowledgeReply() {

        final SenseDataAcknowledgeData senseData = new SenseDataAcknowledgeData(0x01E400);
        senseData.setActionCode(0x22);
        senseData.setSenseDetail(0xDE);
        senseData.setFormatId(0x00);
        senseData.setOverlayId(0x1122);
        senseData.setPageSegmentId(0x3344);
        senseData.setCommandInProcess(IpdsCommandId.BP.getValue());
        senseData.setObjectId(0x4455);
        senseData.setExceptionSpecificInformation(0x6677);
        senseData.setObjectType(0xAA);
        senseData.setPageIdentifier(0x123456);

        final AcknowledgeReply ack = new AcknowledgeReply();
        ack.setAcknowledgeType(0xC0);
        ack.setReceivedPageCounter(1);
        ack.setCommittedPageCounter(2);
        ack.setCommittedCopyCounter(3);
        ack.setOperatorViewingPageCounter(4);
        ack.setOperatorViewingCopyCounter(5);
        ack.setJamRecoveryPageCounter(6);
        ack.setJamRecoveryCopyCounter(7);
        ack.setStackedCopyCounter(8);
        ack.setStackedPageCounter(9);
        ack.setAcknowledgeData(senseData);

        final String actual = StringUtils.toPrettyString(ack);

        final String expected = new StringBuilder()
                .append("AcknowledgeReply {\n")
                .append("  acktype: 0xc0 (192),\n")
                .append("  stackedPageCounter: 0x9 (9),\n")
                .append("  stackedCopyCounter: 0x8 (8),\n")
                .append("  receivedPageCounter: 0x1 (1),\n")
                .append("  committedPageCounter: 0x2 (2),\n")
                .append("  committedCopyCounter: 0x3 (3),\n")
                .append("  operatorViewingPageCounter: 0x4 (4),\n")
                .append("  operatorViewingCopyCounter: 0x5 (5),\n")
                .append("  jamRecoveryPageCounter: 0x6 (6),\n")
                .append("  jamRecoveryCopyCounter: 0x7 (7),\n")
                .append("  acknowledgeData: SenseDataAcknowledgeData {\n")
                .append("    exceptionId: 0x1e400 (123904),\n")
                .append("    actionCode: 0x22 (34),\n")
                .append("    formatId: 0x0 (0),\n")
                .append("    senseDetail: 0xde (222),\n")
                .append("    count: 0x0 (0),\n")
                .append("    overlayId: 0x1122 (4386),\n")
                .append("    pageSegmentId: 0x3344 (13124),\n")
                .append("    commandInProcess: 0xd6af (54959),\n")
                .append("    objectId: 0x4455 (17493),\n")
                .append("    exceptionSpecificInformation: 0x6677 (26231),\n")
                .append("    objectType: 0xaa (170),\n")
                .append("    pageIdentifier: 0x123456 (1193046),\n")
                .append("    textPositionExceptionCount: 0x0 (0),\n")
                .append("    imagePositionExceptionCount: 0x0 (0),\n")
                .append("    rulePositionExceptionCount: 0x0 (0),\n")
                .append("    graphicsPositionExceptionCount: 0x0 (0),\n")
                .append("    up3iErrorCode: 0x0 (0),\n")
                .append("    up3iPaperSequenceId: 0x0 (0)\n")
                .append("  }\n")
                .append("\n")
                .append("}\n")
                .toString();

        assertEquals(expected, actual);
    }
}
