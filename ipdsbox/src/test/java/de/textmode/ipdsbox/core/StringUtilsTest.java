package de.textmode.ipdsbox.core;

import de.textmode.ipdsbox.ipds.commands.ExecuteOrderHomeStateCommand;
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
                .append("  continuationRule: 2,\n")
                .append("  reportingRule: 2,\n")
                .append("  substitutionRule: 1\n")
                .append("}\n")
                .toString();

        assertEquals(expected, actual);
    }

    /**
     * Tests the method StringUtils.ToPrettyString with a complex IPDS command.
     */
    public void testToPrettyStringCommand() {

        final DefineGroupBoundaryOrder order =
                new DefineGroupBoundaryOrder(0x00, 0x00);
                //new DefineGroupBoundaryOrder(DefineGroupBoundaryOrder.INITIATE_GROUP, 0x00);

        order.getTriplets().add(new CodedGraphicCharacterSetGlobalIdentifierTriplet(0x500));
        order.getTriplets().add(new FinishingOperationTriplet(FinishingOperationTriplet.OperationType.CornerStaple));

        final ExecuteOrderHomeStateCommand cmd = new ExecuteOrderHomeStateCommand(order);

        final String actual = StringUtils.toPrettyString(cmd);

        final String expected = new StringBuilder()
                .append("ExecuteOrderHomeStateCommand {\n")
                .append("  order: DefineGroupBoundaryOrder {\n")
                .append("    orderType: 0,\n")
                .append("    groupLevel: 0,\n")
                .append("    triplets: [\n")
                .append("      CodedGraphicCharacterSetGlobalIdentifierTriplet {\n")
                .append("        graphicCharacterSetGlobalIdentifier: 0,\n")
                .append("        codePageGlobalIdentifier: 0,\n")
                .append("        codedCharacterSetIdentifier: 1280\n")
                .append("      }\n")
                .append("      FinishingOperationTriplet {\n")
                .append("        operationType: CornerStaple,\n")
                .append("        finishingOption: 0,\n")
                .append("        reference: DefaultCorner,\n")
                .append("        count: 0,\n")
                .append("        axisOffset: 65535,\n")
                .append("        positions: [ ** empty ** ]\n")
                .append("      }\n")
                .append("    ]\n")
                .append("  }\n")
                .append("\n")
                .append("}\n")
                .toString();

        assertEquals(expected, actual);
    }
}
