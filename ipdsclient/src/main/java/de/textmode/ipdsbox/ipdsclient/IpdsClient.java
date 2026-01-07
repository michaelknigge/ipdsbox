package de.textmode.ipdsbox.ipdsclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.acknowledge.AcknowledgeReply;
import de.textmode.ipdsbox.ipds.acknowledge.ObtainPrinterCharacteristicsAcknowledgeData;
import de.textmode.ipdsbox.ipds.acknowledge.SenseDataAcknowledgeData;
import de.textmode.ipdsbox.ipds.acknowledge.SenseTypeAndModelAcknowledgeData;
import de.textmode.ipdsbox.ipds.commands.BeginPageCommand;
import de.textmode.ipdsbox.ipds.commands.EndPageCommand;
import de.textmode.ipdsbox.ipds.commands.ExecuteOrderHomeStateCommand;
import de.textmode.ipdsbox.ipds.commands.IpdsCommandFactory;
import de.textmode.ipdsbox.ipds.commands.IpdsCommandFlags;
import de.textmode.ipdsbox.ipds.commands.LoadCopyControlCommand;
import de.textmode.ipdsbox.ipds.commands.LoadFontEquivalenceCommand;
import de.textmode.ipdsbox.ipds.commands.LogicalPageDescriptorCommand;
import de.textmode.ipdsbox.ipds.commands.LogicalPagePositionCommand;
import de.textmode.ipdsbox.ipds.commands.NoOperationCommand;
import de.textmode.ipdsbox.ipds.commands.SenseTypeAndModelCommand;
import de.textmode.ipdsbox.ipds.commands.SetHomeStateCommand;
import de.textmode.ipdsbox.ipds.commands.WriteTextCommand;
import de.textmode.ipdsbox.ipds.xohorders.ObtainPrinterCharacteristicsOrder;
import de.textmode.ipdsbox.ppd.PagePrinterRequest;
import de.textmode.ipdsbox.ppd.PagePrinterRequestReader;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public final class IpdsClient {

    private static final Charset EBCDIC = Charset.forName("ibm-500");

    private static final String IPDSCLIENT = "ipdsclient";
    private static final String IPDSCLIENT_FOOTER = "\nSee https://github.com/michaelknigge/ipdsbox\n\n";
    private static final String IPDSCLIENT_HEADER =
        "\nSends IPDS commands to a remote IPDS printer.\n\n";

    private static final String OPTION_HELP_SHORT = "h";
    private static final String OPTION_HELP_LONG = "help";
    private static final String OPTION_HELP_TEXT = "print this message";

    private static final String OPTION_INFO_SHORT = "i";
    private static final String OPTION_INFO_LONG = "info";
    private static final String OPTION_INFO_TEXT = "Obtain printer information";

    private static final String OPTION_CHARACTERISTICS_SHORT = "c";
    private static final String OPTION_CHARACTERISTICS_LONG = "characteristics";
    private static final String OPTION_CHARACTERISTICS_TEXT = "Obtain printer Characteristics";

    private static final String OPTION_WRITE_SHORT = "w";
    private static final String OPTION_WRITE_LONG = "write";
    private static final String OPTION_WRITE_TEXT = "Write text (max 200 chars, uses EBCDIC-500 codepage)";

    private static final String OPTION_PRINTER_SHORT = "p";
    private static final String OPTION_PRINTER_LONG = "printer";
    private static final String OPTION_PRINTER_TEXT = "printer to forward to (portnumer may be specified if not 5001)";

    private static final String OPTION_DEBUG_SHORT = "d";
    private static final String OPTION_DEBUG_LONG = "debug";
    private static final String OPTION_DEBUG_TEXT = "debug mode (print ipds data on stderr)";

    private static final int DEFAULT_PORT_NUMBER = 5001;

    // PagePrinterRequest's from the printer are stored in this LinkedList and processed in order...
    private final LinkedList<PagePrinterRequest> fifo = new LinkedList<>();

    private IpdsClient() {
    }

    /**
     * This is the entry point of {@link IpdsClient}.
     */
    public static void main(final String[] args) {
        System.exit(new IpdsClient().realMain(args));
    }

    /**
     * This is the real entry point of {@link IpdsClient}.
     */
    int realMain(final String[] args) {
        final Options options = new Options();
        options.addOption(Option.builder(OPTION_HELP_SHORT)
                .longOpt(OPTION_HELP_LONG)
                .desc(OPTION_HELP_TEXT)
                .build());

        options.addOption(Option.builder(OPTION_DEBUG_SHORT)
                .longOpt(OPTION_DEBUG_LONG)
                .desc(OPTION_DEBUG_TEXT)
                .argName(OPTION_DEBUG_LONG)
                .build());

        options.addOption(Option.builder(OPTION_INFO_SHORT)
                .longOpt(OPTION_INFO_LONG)
                .desc(OPTION_INFO_TEXT)
                .argName(OPTION_INFO_LONG)
                .build());

        options.addOption(Option.builder(OPTION_CHARACTERISTICS_SHORT)
                .longOpt(OPTION_CHARACTERISTICS_LONG)
                .desc(OPTION_CHARACTERISTICS_TEXT)
                .argName(OPTION_CHARACTERISTICS_LONG)
                .build());

        options.addOption(Option.builder(OPTION_WRITE_SHORT)
                .longOpt(OPTION_WRITE_LONG)
                .desc(OPTION_WRITE_TEXT)
                .hasArg(true)
                .argName(OPTION_WRITE_LONG)
                .build());

        options.addOption(Option.builder(OPTION_PRINTER_SHORT)
                .longOpt(OPTION_PRINTER_LONG)
                .desc(OPTION_PRINTER_TEXT)
                .required()
                .hasArg(true)
                .argName("printer:port")
                .build());

        final boolean isDebugMode;
        final boolean obtainPrinterInfo;
        final boolean obtainPrinterCharacteristics;
        final String writeText;
        final int remotePortNumber;
        final String remotePrinterHost;

        try {
            final CommandLineParser parser = new DefaultParser();
            final CommandLine line = parser.parse(options, args);

            if (line.hasOption(OPTION_HELP_LONG)) {
                return showHelp(options);
            }

            isDebugMode = line.hasOption(OPTION_DEBUG_SHORT);
            obtainPrinterInfo = line.hasOption(OPTION_INFO_LONG);
            obtainPrinterCharacteristics = line.hasOption(OPTION_CHARACTERISTICS_LONG);

            writeText = line.hasOption(OPTION_WRITE_LONG)
                    ? line.getOptionValue(OPTION_WRITE_LONG)
                    : null;

            // Limit to 200 chars... this is more than enough for this demonstration purposes and
            // we can handle the string more easily later (no need to care about max length
            // in PTOCA TRN)...
            if (writeText != null && writeText.length() > 200) {
                System.err.println("IpdsClient: " + "text is longer than 200 chars");
                return showHelp(options);
            }

            remotePrinterHost = parsePrinterHost(line.getOptionValue(OPTION_PRINTER_LONG));
            remotePortNumber = parsePrinterPort(line.getOptionValue(OPTION_PRINTER_LONG));

        } catch (final ParseException exp) {
            System.err.println("IpdsClient: " + exp.getMessage());
            return showHelp(options);
        }

        System.out.println("|o|                                                 |o|");
        System.out.println("|o|                                                 |o|");
        System.out.println("|o|    I P D S B O X   D E M O N S T R A T I O N    |o|");
        System.out.println("|o|                                                 |o|");
        System.out.println("|o|                                                 |o|");
        System.out.println("|o|              I P D S - C L I E N T              |o|");
        System.out.println("|o|                                                 |o|");
        System.out.println("|o|                                                 |o|");
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("Conecting printer...");
        try (final Socket toPrinterSocket = new Socket(remotePrinterHost, remotePortNumber)) {
            System.out.println("Conection to printer established");
            this.handleConnection(toPrinterSocket, isDebugMode);

            // No matter what we want to do - we at least need to initiate a "good connection" with
            // the printer... This is done by STM (Sense Type and Model).
            final AcknowledgeReply stmAckReply = this.obtainPrinterInfo(toPrinterSocket, isDebugMode);

            if (obtainPrinterInfo) {
                if (stmAckReply.getAcknowledgeType() == 0x01 || stmAckReply.getAcknowledgeType() == 0x41) {
                    this.printSenseTypeAndModelAcknowledgeData(
                            (SenseTypeAndModelAcknowledgeData) stmAckReply.getAcknowledgeData());
                } else {
                    System.out.println(String.format(
                            "Expected acknowledge type 0x01 or 0x41 but received acknowledge type %02X",
                            stmAckReply.getAcknowledgeType()));
                }

                System.out.println(" ");
            }

            // No matter if we want to determine the printer characteristics of write text - we need to issue
            // (at least to the IPDS manual) a XOA OPC ("Execute Order Any State" with the order "Obtain Printer
            // Characteristics"...
            final AcknowledgeReply opcAckReply = this.obtainPrinterCharacteristics(toPrinterSocket, isDebugMode);

            if (obtainPrinterCharacteristics) {
                if (opcAckReply.getAcknowledgeType() == 0x06 || opcAckReply.getAcknowledgeType() == 0x46) {
                    this.printObtainPrinterCharacteristicsAcknowledgeData(
                            (ObtainPrinterCharacteristicsAcknowledgeData) opcAckReply.getAcknowledgeData());
                } else {
                    System.out.println(String.format(
                            "Expected acknowledge type 0x06 or 0x46 but received acknowledge type %02X",
                            opcAckReply.getAcknowledgeType()));
                }

                System.out.println(" ");
            }

            if (writeText != null) {
                this.writeText(toPrinterSocket, writeText, isDebugMode);
            }

            System.out.println("Done.");
        } catch (final IOException e) {
            System.err.println(e.getMessage());
            return 1;
        } catch (final InvalidIpdsCommandException e) {
            System.err.println(e.getMessage());
            return 1;
        }

        return 0;
    }

    /**
     * Obtain printer information using the STM (Sense and type model) command.
     */
    private AcknowledgeReply obtainPrinterInfo(
            final Socket printer,
            final boolean isDebugMode) throws IOException, InvalidIpdsCommandException {

        final OutputStream streamToPrinter = printer.getOutputStream();

        // "00000010 00000001 00000001 00000002"  --> Don't know what this means. a packet trace showed
        // that this is the first block of data that is sent to the printer...
        PagePrinterRequest requestOut = new PagePrinterRequest(
                0x01,
                HexFormat.of().parseHex("0000000100000002"));

        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send X'00000010 00000001 00000001 00000002'");

        // The printer responses with "00000010 00000002 00000001 00000002"
        System.out.println("Wait for X'00000010 00000002 00000001 00000002'");
        PagePrinterRequest requestIn = this.waitForServer();
        if (isDebugMode) {
            System.out.println("Received: " + requestIn.toString());
        }

        // "00000008 00000005"  --> Don't know what this means. a packet trace showed
        // that this is the second block of data that is sent to the printer...
        requestOut = new PagePrinterRequest(0x05);
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send X'00000008 00000005'");

        // The printer responses with "00000008 00000006"
        System.out.println("Wait for X'00000008 00000006'");
        requestIn = this.waitForServer();
        if (isDebugMode) {
            System.out.println("Received: " + requestIn.toString());
        }

        // Now finally send the STM...
        // "00000015 0000000E 00000001 00000005 0005D6E480"
        //     ^        ^        ^        ^         ^
        //     |        |        |        |         |
        //     |        |        |        |         +-- 0005 = length, D6E4 = IPDS command code, 80 = Flag (ACK req.)
        //     |        |        |        +-- Length of the following IPDS command.
        //     |        |        +-- Count of IPDS Commands? Or some flags?
        //     |        +-- Maybe an "operation code", 0x0E is "IPDS data"?
        //     +-- Complete length (incl. itself)
        requestOut = new PagePrinterRequest(new SenseTypeAndModelCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send STM command");

        System.out.println("Wait for Acknowledge Reply");
        requestIn = this.waitForServer();
        if (isDebugMode) {
            System.out.println("Received: " + requestIn.toString());
        }

        AcknowledgeReply ackReply = (AcknowledgeReply) IpdsCommandFactory.create(requestIn);
        if (ackReply.getAcknowledgeType() == 0xC0 || ackReply.getAcknowledgeType() == 0x80) {
            final SenseDataAcknowledgeData ackData = (SenseDataAcknowledgeData) ackReply.getAcknowledgeData();

            // Exception X'1000..00' (normal printer reset) is okay / expected...
            System.out.println("Received NACK with exception " + formatExceptionId(ackData.getExceptionId()));

            // Don't know what this means... Maybe a "continue session" command...
            requestOut = new PagePrinterRequest(0x0D);
            requestOut.writeTo(streamToPrinter, isDebugMode);

            requestOut = new PagePrinterRequest(new SenseTypeAndModelCommand());
            requestOut.writeTo(streamToPrinter, isDebugMode);
            System.out.println("Send STM command (again after NACK)");

            System.out.println("Wait for Acknowledge Reply (again after NACK)");
            requestIn = this.waitForServer();
            if (isDebugMode) {
                System.out.println("Received: " + requestIn.toString());
            }

            ackReply = (AcknowledgeReply) IpdsCommandFactory.create(requestIn);
        }

        if (isDebugMode) {
            System.err.println(ackReply);
        }

        return ackReply;
    }

    /**
     * Obtain printer characteristics using the XOH-OPC command.
     */
    private AcknowledgeReply obtainPrinterCharacteristics(
            final Socket printer,
            final boolean isDebugMode) throws IOException, InvalidIpdsCommandException {

        final OutputStream streamToPrinter = printer.getOutputStream();

        PagePrinterRequest requestOut = new PagePrinterRequest(new SetHomeStateCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send SHS command");

        final ExecuteOrderHomeStateCommand xoh = new ExecuteOrderHomeStateCommand(
                new ObtainPrinterCharacteristicsOrder());

        xoh.getCommandFlags().isAcknowledgmentRequired(true);
        xoh.getCommandFlags().isLongAcknowledgeReplyAccepted(true);
        requestOut = new PagePrinterRequest(xoh);
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send XOH command (Obtain Printer Characteristic)");

        System.out.println("Wait for Acknowledge Reply");
        final PagePrinterRequest requestIn = this.waitForServer();

        return (AcknowledgeReply) IpdsCommandFactory.create(requestIn);
    }

    /**
     * Print text on a page.
     */
    private void writeText(final Socket printer, final String text, final boolean isDebugMode)
            throws IOException, InvalidIpdsCommandException {

        final OutputStream streamToPrinter = printer.getOutputStream();

        AcknowledgeReply ackReply = this.obtainPrinterCharacteristics(printer, isDebugMode);

        if (ackReply.getAcknowledgeType() != 0x06 && ackReply.getAcknowledgeType() != 0x46) {
            System.out.println(String.format(
                    "Expected acknowledge type 0x06 or 0x46 but received acknowledge type %02X",
                    ackReply.getAcknowledgeType()));

            return;
        }

        final ObtainPrinterCharacteristicsAcknowledgeData printerCharacteristics =
                (ObtainPrinterCharacteristicsAcknowledgeData) ackReply.getAcknowledgeData();

        if (isDebugMode) {
            this.printObtainPrinterCharacteristicsAcknowledgeData(printerCharacteristics);
            System.out.println(" ");
        }

        // The sequence of commands is taken fromn the IPDS manual...
        //
        // Initializaion:
        //   STM + ARQ
        //   XOH Obtain Printer Characteristics + ARQ
        //   SHS
        //   LPD
        //   LPP
        //   LCC
        //   LFE + ARQ
        //
        // Print Page:
        //   BP
        //   WT
        //   EP + ARQ
        PagePrinterRequest requestOut = new PagePrinterRequest(new SetHomeStateCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send SHS command");

        // Logical Page Descriptor (LPD)
        requestOut = new PagePrinterRequest(new LogicalPageDescriptorCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LPD command");

        // Logical Page Position (LPP)
        requestOut = new PagePrinterRequest(new LogicalPagePositionCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LPP command");

        // Load Copy Control (LCC)
        requestOut = new PagePrinterRequest(new LoadCopyControlCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LCC command");

        // Load Font Equivalence (LFE)
        //
        // Note: An empty LFE command sent in home state can be used to reset all previously established LID-to-HAID
        // mappings. Property pair X'6009' in the Device-Control command-set vector of an STM reply indicates support
        // for empty LFE commands.
        //
        // Some IPDS printers require at least one LFE entry. These printers generate exception ID X'0202..02' if
        // an empty LFE command is received.
        // Logical Page Position (LPP)
        requestOut = new PagePrinterRequest(new LoadFontEquivalenceCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LFE command");


        // NOP + ARQ (to check if everything up to here is okay). IPDS manual states that in
        // the initialization sequence a "LFE+ARQ" should be sent... LFE followed by NOP+ARQ
        // is the same...
        final NoOperationCommand nop = new NoOperationCommand();
        nop.getCommandFlags().isAcknowledgmentRequired(true);
        nop.getCommandFlags().isLongAcknowledgeReplyAccepted(true);
        requestOut = new PagePrinterRequest(nop);
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send NOP+ARQ");

        System.out.println("Wait for Acknowledge Reply");
        PagePrinterRequest requestIn = this.waitForServer();

        ackReply = (AcknowledgeReply) IpdsCommandFactory.create(requestIn);
        System.out.println("Received acknowledge type " + Integer.toHexString(ackReply.getAcknowledgeType()));
        if (ackReply.getAcknowledgeType() != 0x00 && ackReply.getAcknowledgeType() != 0x40) {
            System.out.println(String.format(
                    "Expected acknowledge type 0x00 or 0x40 but received acknowledge type %02X",
                    ackReply.getAcknowledgeType()));

            System.out.println(ackReply);
            return;
        }

        // Begin Page (BP)
        requestOut = new PagePrinterRequest(new BeginPageCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send BP command");

        // Write Text (WT) Send
        final String ami = "2BD304C60400"; // Inline position = 0x0400
        final String amb = "2BD304D20400"; // baseline position = 0x0400
        final String txtInHex = StringUtils.toHexString(EBCDIC.encode(text).array());
        final String lenInHex = Integer.toHexString(2 + (txtInHex.length() / 2));

        final String trn = "2BD3" + (lenInHex.length() == 1 ? "0" : "") + lenInHex + "DA" + txtInHex;
        final String ptoca = ami + amb + trn;

        requestOut = new PagePrinterRequest(new WriteTextCommand(HexFormat.of().parseHex(ptoca)));
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send WT command");
        if (isDebugMode) {
            System.out.println("PTOCA: AMI=" + ami + " AMB=" + amb + " TRN=" + trn);
        }

        // End Page (EP) with ARQ
        final EndPageCommand endPageCommand = new EndPageCommand();
        endPageCommand.getCommandFlags().isAcknowledgmentRequired(true);
        requestOut = new PagePrinterRequest(endPageCommand);
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send EP command");

        System.out.println("Wait for Acknowledge Reply");
        requestIn = this.waitForServer();

        ackReply = (AcknowledgeReply) IpdsCommandFactory.create(requestIn);
        System.out.println("Received acknowledge type " + Integer.toHexString(ackReply.getAcknowledgeType()));

        if (ackReply.getAcknowledgeType() != 0x00 && ackReply.getAcknowledgeType() != 0x40) {
            System.out.println(String.format(
                    "Expected acknowledge type 0x00 or 0x40 but received acknowledge type %02X",
                    ackReply.getAcknowledgeType()));

            System.out.println(ackReply);
            return;
        }

        requestOut = new PagePrinterRequest(new SetHomeStateCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send SHS command");
    }

    /**
     * Determines the host name or IP address of the specified printer.
     */
    private static String parsePrinterHost(final String value) throws ParseException {
        try {
            // Use the URI class for separating the host name from the (optional) port number
            // so we do not have to care if the the user has specified a real host name, an
            // IPv4 address or an IPv6 address. The URI class will do all the hard work...
            final URI uri = new URI("http://" + value);
            final String result = uri.getHost();
            if (result == null) {
                throw createParseExceptionForPrinterOption(value);
            } else {
                return result;
            }
        } catch (final URISyntaxException e) {
            throw createParseExceptionForPrinterOption(value);
        }
    }

    /**
     * Determines the port number of the specified printer.
     */
    private static int parsePrinterPort(final String value) throws ParseException {
        try {
            // Use the URI class for separating the host name from the (optional) port number
            // so we do not have to care if the the user has specified a real host name, an
            // IPv4 address or an IPv6 address. The URI class will do all the hard work...
            final URI uri = new URI("http://" + value);
            final int result = uri.getPort();
            return result == -1 ? DEFAULT_PORT_NUMBER : result;
        } catch (final URISyntaxException e) {
            throw createParseExceptionForPrinterOption(value);
        }
    }

    /**
     * Creates a {@link ParseException} with a generic message that the hostname or ip address
     * is invalid.
     */
    private static ParseException createParseExceptionForPrinterOption(final String value) {
        return new ParseException("Specified printer \"" + value + "\" is not valid");
    }

    /**
     * Shows the help on the standard output.
     */
    private static int showHelp(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(IPDSCLIENT, IPDSCLIENT_HEADER, options, IPDSCLIENT_FOOTER, true);
        return 1;
    }

    /**
     * Handles the connection from the print server (which is the PPR, the Page Printer Requester).
     * Data received from the printer is put in a queue so the responses can be processed from
     * "the main logic".
     */
    private void handleConnection(final Socket printer, final boolean isDebugMode) throws IOException {

        System.out.println("Handling connection to " + printer.getInetAddress());

        final InputStream streamFromPrinter = printer.getInputStream();
        final OutputStream streamToPrinter = printer.getOutputStream();

        // This thread will read data from the printer and pass it to the print server...
        final Thread t = new Thread() {
            @Override
            public void run() {
                IpdsClient.this.readFromPrinter(streamFromPrinter, streamToPrinter, isDebugMode);
            }
        };

        t.start();
    }

    /**
     * Reads the next {@link PagePrinterRequest} from the printer. This should be only Acknowledge replies.
     */
    @SuppressFBWarnings("THROWS_METHOD_THROWS_RUNTIMEEXCEPTION")
    private void readFromPrinter(final InputStream in, final OutputStream out, final boolean isDebugMode) {

        try {
            PagePrinterRequest req;
            final List<PagePrinterRequest> requests = new ArrayList<PagePrinterRequest>();

            // We could implement a "low level" PagePrinterRequestReader (like the current implementation)
            // and a "high level" PagePrinterRequestReader, that automatically handles the PPD/PPR low-level
            // requests/stuff automatically (i. e. send 0x0D on exception X'1000..00' automatically) and that
            // collects/concats ack-replies if the "AcknowledgmentContinuationRequested"....
            while ((req = PagePrinterRequestReader.read(in)) != null) {

                if (req.getRequest() != 0x0E) {
                    this.addToFiFo(req);
                } else {
                    // Note: Implementation is not really correct... the 0x0E request *MAY* contain
                    // multiple IPDS commands... here we assume that re receive just one ACK per request,
                    // which is probaly fine in the real world...
                    final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(req.getData());
                    is.skip(8);
                    is.skip(4);
                    final IpdsCommandFlags flags = new IpdsCommandFlags((byte) is.readUnsignedByte());

                    if (!flags.isAcknowledgmentContinuationRequested()) {
                        if (requests.size() == 0) {
                            this.addToFiFo(req);
                        } else {
                            requests.add(req);
                            this.addToFiFo(new PagePrinterRequest(IpdsCommandFactory.create(requests)));
                        }
                        requests.clear();
                    } else {
                        // buffer the printer request...
                        requests.add(req);

                        final NoOperationCommand nop = new NoOperationCommand();
                        nop.getCommandFlags().isAcknowledgmentRequired(true);
                        nop.getCommandFlags().isLongAcknowledgeReplyAccepted(true);
                        nop.getCommandFlags().isAcknowledgmentContinuationRequested(true);
                        final PagePrinterRequest requestOut = new PagePrinterRequest(nop);
                        requestOut.writeTo(out, isDebugMode);
                        System.out.println("Send NOP+ARQ (for ACK-Continue)");
                    }
                }
            }
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        } catch (final InvalidIpdsCommandException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a {@link PagePrinterRequest} to the internal "FIFO-Queue".
     */
    void addToFiFo(final PagePrinterRequest request) {
        synchronized (this.fifo) {
            this.fifo.add(request);
        }
    }

    /**
     * Waits until the server has sent a {@link PagePrinterRequest}.
     */
    @SuppressFBWarnings("THROWS_METHOD_THROWS_RUNTIMEEXCEPTION")
    PagePrinterRequest waitForServer() {

        while (true) {
            synchronized (this.fifo) {
                if (!this.fifo.isEmpty()) {
                    return this.fifo.removeFirst();
                }
            }
            try {
                Thread.sleep(10);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Prints all information from the STM to the standard out (prints the capabilities of the printer).
     */
    private void printSenseTypeAndModelAcknowledgeData(final SenseTypeAndModelAcknowledgeData ackData) {
        System.out.println(" ");
        System.out.println("Printer Information from STM command");
        System.out.println("--------------------------------------------------------------------------------");

        new TypeAndModelPrettyPrinter(ackData, System.out).print();
    }

    private void printObtainPrinterCharacteristicsAcknowledgeData(
            final ObtainPrinterCharacteristicsAcknowledgeData data) {

        System.out.println(" ");
        System.out.println("Printer Characteristics from XOH-OPC command");
        System.out.println("--------------------------------------------------------------------------------");

        new PrinterCharacteristicsPrettyPrinter(data, System.out).print();
    }

    /**
     * Formats a exception ID to the format used in the IPDS manual.
     */
    private static String formatExceptionId(final int exceptionId) {
        return String.format(
                "X'%04X..%02X'",
                (exceptionId & 0xFFFF00) >>> 8,
                (exceptionId & 0xFF));

    }
}
