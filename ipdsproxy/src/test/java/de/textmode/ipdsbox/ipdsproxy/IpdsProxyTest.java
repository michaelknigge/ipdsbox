package de.textmode.ipdsbox.ipdsproxy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import junit.framework.TestCase;

/**
 * Unit tests for {@link IpdsProxy}.
 */
public final class IpdsProxyTest extends TestCase {

    static IpdsByteArrayInputStream streamFromHex(final String hex) {
        final byte[] rawData = HexFormat.of().parseHex(hex);
        final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(rawData);

        return ipds;
    }


    /**
     * Tests decoding a single IPDS command.
     */
    public void testSingleIpdsCommand() throws Exception {
        // IPDS command "Sense Type and Model"
        final IpdsByteArrayInputStream is = streamFromHex("00000001000000050005D6E480");
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(os, true, StandardCharsets.UTF_8.name());

        IpdsProxy.decodeAndPrintCommands(is, ps);

        assertEquals("SenseTypeAndModelCommand{}\r\n", os.toString(StandardCharsets.UTF_8.name()));
    }

    /**
     * Tests decoding multiple IPDS commands.
     */
    public void testMultipleIpdsCommand() throws Exception {
        final IpdsByteArrayInputStream is = streamFromHex("000000010000008D0007D6974000080009D633400009F2000009D68F40000A05000009D68F40000B07000013D62E40000C000C000000000000000000200017D63F40000D017EFF00000000000000000000000000000AD68F40000EF50001000AD68F40000F1600000010D68F4000101700003840FFFFFFFF000BD68F40001103000125000BD68F400012030001800007D603C00013");
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(os, true, StandardCharsets.UTF_8.name());

        IpdsProxy.decodeAndPrintCommands(is, ps);

        final String actual = os.toString(StandardCharsets.UTF_8.name());
        final String expected = """
                SetHomeStateCommand{}\r
                ExecuteOrderAnyStateCommand{order=DiscardBufferedDataOrder{}}\r
                ExecuteOrderHomeStateCommand{order=EraseResidualPrintDataOrder{}}\r
                ExecuteOrderHomeStateCommand{order=EraseResidualFontDataOrder{}}\r
                ActivateResourceCommand{entries=[ActivateResourceEntry{resourceType=0x0, haid=0, sectionID=0x0, resourceIdFormat=0x0, fontInlineSequence=0x0, resourceClassFlags=0x20, resourceIdTriplets=[]}]}\r
                LoadFontEquivalenceCommand{entries=[LoadFontEquivalenceEntry{fontLocalId=0x1, haid=0x7eff, fontInlineSequence=0x0, GCSGID=0x0, CPGID=0x0, FGID=0x0, fontWidth=0x0, flags=0x0}]}\r
                ExecuteOrderHomeStateCommand{order=PageCountersControlOrder{counterUpdate=0x1}}\r
                ExecuteOrderHomeStateCommand{order=SetMediaOriginOrder{origin=0x0}}\r
                ExecuteOrderHomeStateCommand{order=SetMediaSizeOrder{unitBase=0x0, upub=14400, xmExtent=65535, ymExtent=65535}}\r
                ExecuteOrderHomeStateCommand{order=SpecifyGroupOperationOrder{operation=0x1, groupLevel=0x25}}\r
                ExecuteOrderHomeStateCommand{order=SpecifyGroupOperationOrder{operation=0x1, groupLevel=0x80}}\r
                NoOperationCommand{dataBytes=}\r
                """;

        assertEquals(expected, actual);
    }
}
