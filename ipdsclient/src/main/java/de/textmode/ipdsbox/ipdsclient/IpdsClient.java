package de.textmode.ipdsbox.ipdsclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.acknowledge.AcknowledgeReply;
import de.textmode.ipdsbox.ipds.acknowledge.ObtainPrinterCharacteristicsAcknowledgeData;
import de.textmode.ipdsbox.ipds.acknowledge.SenseDataAcknowledgeData;
import de.textmode.ipdsbox.ipds.acknowledge.SenseTypeAndModelAcknowledgeData;
import de.textmode.ipdsbox.ipds.commands.ExecuteOrderHomeStateCommand;
import de.textmode.ipdsbox.ipds.commands.IpdsCommandFactory;
import de.textmode.ipdsbox.ipds.commands.IpdsCommandFlags;
import de.textmode.ipdsbox.ipds.commands.LoadCopyControlCommand;
import de.textmode.ipdsbox.ipds.commands.LogicalPageDescriptorCommand;
import de.textmode.ipdsbox.ipds.commands.LogicalPagePositionCommand;
import de.textmode.ipdsbox.ipds.commands.NoOperationCommand;
import de.textmode.ipdsbox.ipds.commands.SenseTypeAndModelCommand;
import de.textmode.ipdsbox.ipds.commands.SetHomeStateCommand;
import de.textmode.ipdsbox.ipds.xohorders.ObtainPrinterCharacteristicsOrder;
import de.textmode.ipdsbox.ipds.xohorders.UnknownXohOrderCode;
import de.textmode.ipdsbox.ppd.PagePrinterRequest;
import de.textmode.ipdsbox.ppd.PagePrinterRequestReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public final class IpdsClient {

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

    private static final String OPTION_WRITE_SHORT = "w";
    private static final String OPTION_WRITE_LONG = "write";
    private static final String OPTION_WRITE_TEXT = "Write text";

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

            writeText = line.hasOption(OPTION_WRITE_LONG)
                    ? line.getOptionValue(OPTION_WRITE_LONG)
                    : null;

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
            final AcknowledgeReply ackReply = this.obtainPrinterInfo(toPrinterSocket, isDebugMode);

            if (obtainPrinterInfo) {
                if (ackReply.getAcknowledgeType() == 0x01 || ackReply.getAcknowledgeType() == 0x41) {
                    this.printSenseTypeAndModelAcknowledgeData((SenseTypeAndModelAcknowledgeData) ackReply.getAcknowledgeData());
                } else {
                    System.out.println(String.format(
                            "Expected acknowledge type 0x01 or 0x41 but received acknowledge type %02X",
                            ackReply.getAcknowledgeType()));
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
        } catch (final UnknownXohOrderCode e) {
            System.err.println(e.getMessage());
            return 1;
        }

        return 0;
    }

    /**
     * Obtain printer information using the STM (Sense and type model) command.
     */
    private AcknowledgeReply obtainPrinterInfo(final Socket printer, final boolean isDebugMode) throws IOException, InvalidIpdsCommandException, UnknownXohOrderCode {

        final OutputStream streamToPrinter = printer.getOutputStream();

        // "00000010 00000001 00000001 00000002"  --> Don't know what this means. a packet trace showed
        // that this is the first block of data that is sent to the printer...
        PagePrinterRequest requestOut = new PagePrinterRequest(0x01, HexFormat.of().parseHex("0000000100000002"));
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send X'00000010 00000001 00000001 00000002'");

        // The printer responses with "00000010 00000002 00000001 00000002"
        System.out.println("Wait for X'00000010 00000002 00000001 00000002'");
        PagePrinterRequest requestIn = this.waitForServer();

        // "00000008 00000005"  --> Don't know what this means. a packet trace showed
        // that this is the second block of data that is sent to the printer...
        requestOut = new PagePrinterRequest(0x05);
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send X'00000008 00000005'");

        // The printer responses with "00000008 00000006"
        System.out.println("Wait for X'00000008 00000006'");
        requestIn = this.waitForServer();

        // Now finally send the STM...
        // "00000015 0000000E 00000001 00000005 0005D6E480"
        //     ^        ^        ^        ^         ^
        //     |        |        |        |         |
        //     |        |        |        |         +-- 0005 = length, D6E4 = IPDS command code, 80 = Flag (ACK requested)
        //     |        |        |        +-- Length of the following IPDS command. Guess more than one can be sent at once
        //     |        |        +-- Count of IPDS Commands? Or some flags?
        //     |        +-- Maybe an "operation code", 0x0E is "IPDS data"?
        //     +-- Complete length (incl. itself)
        requestOut = new PagePrinterRequest(new SenseTypeAndModelCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send STM command");

        System.out.println("Wait for Acknowledge Reply");
        requestIn = this.waitForServer();

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
    private AcknowledgeReply obtainPrinterCharacteristics(final Socket printer, final boolean isDebugMode) throws IOException, InvalidIpdsCommandException, UnknownXohOrderCode {

        final OutputStream streamToPrinter = printer.getOutputStream();

        PagePrinterRequest requestOut = new PagePrinterRequest(new SetHomeStateCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send SHS command");

        final ExecuteOrderHomeStateCommand xoh = new ExecuteOrderHomeStateCommand(new ObtainPrinterCharacteristicsOrder());
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
            throws IOException, InvalidIpdsCommandException, UnknownXohOrderCode {

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

        PagePrinterRequest requestOut = new PagePrinterRequest(new SetHomeStateCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send SHS command");

        // Logical Page Descriptor (LPD)
        final LogicalPageDescriptorCommand logicalPageDescriptorCommand = new LogicalPageDescriptorCommand();
        // TODO: set parameters with values from printerCharacteristics... Needed?
        requestOut = new PagePrinterRequest(logicalPageDescriptorCommand);
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LPD command");

        // Logical Page Position (LPP)
        requestOut = new PagePrinterRequest(new LogicalPagePositionCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LPP command");

        // Load Copy Control (LCC)
        final LoadCopyControlCommand.CopySubgroup copySubgroup = new LoadCopyControlCommand.CopySubgroup();
        // TODO... Is setting the media source required?
        final LoadCopyControlCommand loadCopyControlCommand = new LoadCopyControlCommand();
        loadCopyControlCommand.getCopySubgroups().add(copySubgroup);

        requestOut = new PagePrinterRequest(new LoadCopyControlCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LCC command");

        // Load Font Equivalence (LFE) with ARQ
        // --> Wait for ACK

        final NoOperationCommand nop = new NoOperationCommand();
        nop.getCommandFlags().isAcknowledgmentRequired(true);
        nop.getCommandFlags().isLongAcknowledgeReplyAccepted(true);
        requestOut = new PagePrinterRequest(nop);
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send NOP+ARQ");

        System.out.println("Wait for Acknowledge Reply");
        final PagePrinterRequest requestIn = this.waitForServer();

        ackReply = (AcknowledgeReply) IpdsCommandFactory.create(requestIn);
        System.out.println("Received acknowledge type " + Integer.toHexString(ackReply.getAcknowledgeType()));

        // Begin Page (BP)
        // Write Text (WT) Send
        // End Page (EP) with ARQ




        /*

        requestOut = new PagePrinterRequest(new ExecuteOrderHomeStateCommand(new SetMediaOriginOrder()));
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send XOH command (Media Origin)");

        requestOut = new PagePrinterRequest(new ExecuteOrderHomeStateCommand(new SetMediaSizeOrder()));
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send XOH command (Media Size)");

        requestOut = new PagePrinterRequest(new LogicalPageDescriptorCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LPD command");
*/
        /*********************
        final LoadCopyControlCommand.Keyword kw = new LoadCopyControlCommand.Keyword(0xE1, 0x01);

        final LoadCopyControlCommand lcc = new LoadCopyControlCommand();
        lcc.getCopySubgroups().add(new C)
        requestOut = new PagePrinterRequest(new LoadCopyControlCommand());
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 8: Send LCC command");
         */

        /*
        requestOut = new PagePrinterRequest(new LogicalPagePositionCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send LPP command");

        requestOut = new PagePrinterRequest(new BeginPageCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send CP command");

        final String ami = "2BD304C600FF";
        final String amb = "2BD304D200FF";
        final String trn = "2BD307F1F2F3F4F5";

        requestOut = new PagePrinterRequest(new WriteTextCommand(HexFormat.of().parseHex(ami + amb + trn)));
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send WT command");

        final EndPageCommand endPageCommand = new EndPageCommand();
        endPageCommand.getCommandFlags().isAcknowledgmentRequired(true);
        requestOut = new PagePrinterRequest(endPageCommand);
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send EP command");

        System.out.println("Wait for Acknowledge Reply");
        final PagePrinterRequest requestIn = this.waitForServer();

        requestOut = new PagePrinterRequest(new SetHomeStateCommand());
        requestOut.writeTo(streamToPrinter, isDebugMode);
        System.out.println("Send SHS command");
        */
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
    private void readFromPrinter(final InputStream in, final OutputStream out, final boolean isDebugMode) {

        try {
            PagePrinterRequest req;
            final List<PagePrinterRequest> requests = new ArrayList<PagePrinterRequest>();

            // We could implement a "low level" PagePrinterRequestReader (like the current implementation)
            // and a "high level" PagePrinterRequestReader, that automatically handles the PPD/PPR low-level
            // requests/stuff automatically (i. e. send 0x0D on exception X'1000..00' automatically) and that
            // collects/concats ack-replies if the "AcknowledgmentContinuationRequested"....
            while ((req = PagePrinterRequestReader.read(in, isDebugMode)) != null) {
                if (req.getRequest() != 0x0E) {
                    this.addToFiFo(req);
                } else {
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
        } catch (final InvalidIpdsCommandException | UnknownXohOrderCode e) {
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
    synchronized PagePrinterRequest waitForServer() {

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

        System.out.println(String.format("Device Type: 0x%04X", ackData.getType()));
        System.out.println(String.format("Model      : 0x%02X", ackData.getModel()));
        System.out.println(" ");

        for (final SenseTypeAndModelAcknowledgeData.CommandSetVector vector : ackData.getCommandSetVectors()) {

            System.out.println(String.format(
                    "Command Set ID : 0x%04X (%s)",
                    vector.getSubsetIdOrCommandSetId(),
                    decodeCommandSetId(vector)));

            System.out.println(String.format(
                    "Level ID       : 0x%04X (%s)",
                    vector.getLevelOrSubsetId(),
                    decodeLevel(vector)));

            for (final Integer propertyPair : vector.getPropertyPairs()) {
                System.out.println(String.format(
                        "  Property pair : 0x%04X (%s)",
                        propertyPair,
                        this.decodePropertyPair(vector, propertyPair)));
            }

            System.out.println(" ");
        }
    }

    private void printObtainPrinterCharacteristicsAcknowledgeData(final ObtainPrinterCharacteristicsAcknowledgeData data) {
        System.out.println(" ");
        System.out.println("Printer Characteristics from XOH-OPC command");
        System.out.println("--------------------------------------------------------------------------------");

        System.out.println(data.toString());

    }

    /**
     * Returns a textual description of the Command Set ID or Subset ID.
     */
    private static String decodeCommandSetId(final SenseTypeAndModelAcknowledgeData.CommandSetVector vector) {
        return switch (vector.getSubsetIdOrCommandSetId()) {
            case 0xC4C3 -> "Device Control Command Set";
            case 0xD7E3 -> "Text Command Set";
            case 0xC9D4 -> "IM-Image Command Set";
            case 0xC9D6 -> "IO-Image Command Set";
            case 0xE5C7 -> "Graphics Command Set";
            case 0xC2C3 -> "Bar Code Command Set";
            case 0xD6C3 -> "Object Container Command Set";
            case 0xD4C4 -> "MO1 Subset of the Metadata Command Set";
            case 0xD6D3 -> "Overlay Command Set";
            case 0xD7E2 -> "Page Segment Command Set";
            case 0xC3C6 -> "Loaded Font Command Set";
            default -> "** Unknown Command Set **";
        };
    }

    /**
     * Returns a textual description of the Subset ID or Level ID.
     */
    private static String decodeLevel(final SenseTypeAndModelAcknowledgeData.CommandSetVector vector) {
        if (vector.getSubsetIdOrCommandSetId() == 0xC4C3) { // Device Control Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "DC1 subset ID" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD7E3) { // Text Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF10 -> "PTOCA PT1 data";
                case 0xFF20 -> "PTOCA PT2 data";
                case 0xFF30 -> "PTOCA PT3 data";
                case 0xFF40 -> "PTOCA PT4 data";
                default -> "** Unknown Level **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC9D4) { // IM-Image Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "IMD1 data" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC9D6) { // IO-Image Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF10 -> "IOCA FS10 data";
                case 0xFF11 -> "IOCA FS11 data";
                case 0xFF14 -> "IOCA FS14 data";
                case 0xFF40 -> "IOCA FS40 data";
                case 0xFF42 -> "IOCA FS42 data";
                case 0xFF45 -> "IOCA FS45 data";
                case 0xFF48 -> "IOCA FS48 data";
                case 0x0010 -> "Subset of IOCA FS10 data";
                case 0x0011 -> "Subset of IOCA FS11 data";
                default -> "** Unknown Level **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xE5C7) { // Graphics Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF20 -> "GOCA DR/2V0 data";
                case 0xFF30 -> "GOCA GRS3 data";
                default -> "** Unknown Level **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC2C3) { // Bar Code Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF10 -> "BCOCA BCD1 data";
                case 0xFF20 -> "BCOCA BCD2 data";
                default -> "** Unknown Level **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD6C3) { // Object Container Command Set
            return "No levels defined";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD4C4) { // MO1 Subset of the Metadata Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "MOCA MS1 data" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD6D3) { // Overlay Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "OL1 subset" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD7E2) { // Page Segment Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "PS1 subset" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC3C6) { // Loaded Font Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF10 -> "LF1 subset ID; fully described font plus font index";
                case 0xFF20 -> "LF2 subset ID; symbol set coded font";
                case 0xFF30 -> "LF3 subset ID; code page plus font character set";
                case 0xFF40 -> "LF4 subset ID; code page";
                default -> "** Unknown Level **";
            };
        }

        return "** Unknown Level **";
    }

    /**
     * Returns a textual description of a property pair.
     */
    private static String decodePropertyPair(final SenseTypeAndModelAcknowledgeData.CommandSetVector vector, final Integer propertyPair) {

        if (vector.getSubsetIdOrCommandSetId() == 0xC4C3) { // Device Control Command Set
            return switch(propertyPair) {
                case 0x6001 -> "Multiple copy & copy-subgroup support in LCC";
                case 0x6002 -> "Media-source-selection support in LCC";
                case 0x6003 -> "Media-destination-selection support in LCC";
                case 0x6004 -> "Full-length LCC commands supported";
                case 0x6005 -> "Full range of font local IDs supported in LFE and LPD commands";
                case 0x6006 -> "Font-modification flags supported in LFE commands (byte 14, bits 3-7)";
                case 0x6007 -> "Short LPD commands supported";
                case 0x6008 -> "Full range of logical-page-offset values supported in LPP commands";
                case 0x6009 -> "Empty LFE commands supported";
                case 0x6101 -> "Explicit page placement and orientation support in the LPP command";
                case 0x6201 -> "Logical page and object area coloring support";
                case 0x7001 -> "Manage IPDS Dialog (MID) command support";
                case 0x7002 -> "Apply Finishing Operations (AFO) command";
                case 0x7008 -> "Set Presentation Environment (SPE) command support";
                case 0x700A -> "Activate Setup Name (ASN) command support";
                case 0x701C -> "Retired item 134";
                case 0x702E -> "Activate Resource command support";
                case 0x7034 -> "Presentation Fidelity Control command support";
                case 0x706B -> "Invoke CMR (ICMR) command support";
                case 0x707B -> "Rasterize Presentation Object (RPO) command support";
                case 0x707E -> "Include Saved Page (ISP) command support";
                case 0x70CE -> "DUA command-support property ID";
                case 0x8000 -> "Retired item 108";
                case 0x8001 -> "Retired item 109";
                case 0x8002 -> "Retired item 110";
                case 0x8006 -> "Retired item 111";
                case 0x8008 -> "Mark Form";
                case 0x800A -> "Alternate Offset Stacker";
                case 0x800C -> "Control Edge Marks";
                case 0x8010 -> "Activate Printer Alarm";
                case 0x80F1 -> "Retired item 112";
                case 0x80F2 -> "Discard Buffered Data";
                case 0x80F3 -> "Retired item 113";
                case 0x80F4 -> "Request Resource List";
                case 0x80F5 -> "Discard Unstacked Pages";
                case 0x80F6 -> "Exception-Handling Control";
                case 0x80F8 -> "Print-Quality Control";
                case 0x80F9 -> "Obtain Additional Exception";
                case 0x80FA -> "Request Setup Name List";
                case 0x9000 -> "Retired item 114";
                case 0x9001 -> "Print Buffered Data";
                case 0x9002 -> "Deactivate Saved Page Group";
                case 0x9003 -> "Specify Group Operation";
                case 0x9004 -> "Define Group Boundary";
                case 0x9005 -> "Erase Residual Print Data";
                case 0x9007 -> "Erase Residual Font Data";
                case 0x9009 -> "Separate Continuous Forms";
                case 0x900A -> "Remove Saved Page Group";
                case 0x900B -> "Retired item 115";
                case 0x900D -> "Stack Received Pages";
                case 0x900E -> "Select Medium Modifications";
                case 0x9013 -> "Eject to Front Facing";
                case 0x9015 -> "Select Input Media Source";
                case 0x9016 -> "Set Media Origin";
                case 0x9017 -> "Set Media Size";
                case 0x90D0 -> "Retired item 126";
                case 0x90F2 -> "Trace";
                case 0x90F3 -> "Obtain Printer Characteristics";
                case 0x90F4 -> "Retired item 116";
                case 0x90F5 -> "Page Counters Control";
                case 0xE000 -> "CMRs can be captured";
                case 0xE001 -> "Host-activated link color-conversion (subset “LK”) CMRs supported";
                case 0xE002 -> "Host-activated, non-generic halftone CMRs supported";
                case 0xE003 -> "Host-activated, non-generic tone-transfer-curve CMRs supported";
                case 0xE004 -> "Host-activated indexed CMRs supported";
                case 0xE006 -> "Host-activated ICC DeviceLink CMRs supported";
                case 0xE100 -> "CMRs can be reliably applied to all EPS/PDF objects";
                case 0xE102 -> "Pass-through audit color-conversion CMRs supported";
                case 0xF001 -> "End Persistent NACK without leaving IPDS mode";
                case 0xF002 -> "Blank sheets are emitted when paper movement is stopped";
                case 0xF003 -> "Long ACK support, up to 65,535 byte long Acknowledge Replies";
                case 0xF004 -> "Grayscale simulation supported";
                case 0xF005 -> "Grayscale simulation supported for device-default-monochrome device appearance";
                case 0xF100 -> "An IPDS intermediate device is present";
                case 0xF101 -> "UP3I finishing supported";
                case 0xF102 -> "Media feed direction returned in the XOH-OPC reply Printable-Area self-defining field";
                case 0xF200 -> "Local Date and Time Stamp (X'62') triplets supported in AR commands";
                case 0xF201 -> "Activation-failed NACK support";
                case 0xF202 -> "Font Resolution and Metric Technology (X'84') triplets supported in AR commands";
                case 0xF203 -> "Metric Adjustment (X'79') triplets supported in AR commands";
                case 0xF204 -> "Data-object font support";
                case 0xF205 -> "Color Management triplet support in IDO, LPD, RPO, SPE, WBCC, WGC, WIC2, WOCC, and WTC commands";
                case 0xF206 -> "Device Appearance (X'97') triplet support";
                case 0xF209 -> "Extended copy set number format supported in the Group Information (X'6E') triplet";
                case 0xF211 -> "Character-encoded object names in AR commands";
                case 0xF212 -> "QR Code with Image tertiary resource support";
                case 0xF401 -> "XOA-RRL Multiple Entry Query Support";
                case 0xF402 -> "Retired";
                case 0xF403 -> "Detailed settings support in XOA RSNL";
                case 0xF601 -> "Position-Check Highlighting Support in XOA EHC";
                case 0xF602 -> "Independent Exception Page-Print in XOA EHC";
                case 0xF603 -> "Support for operator-directed recovery in XOA EHC";
                case 0xF604 -> "Support for Page-Continuation Actions";
                case 0xF605 -> "Support for Skip-and-Continue Actions";
                case 0xF701 -> "Simplex 1-up supported in the LCC command";
                case 0xF702 -> "Simplex 2-up supported in the LCC command";
                case 0xF703 -> "Simplex 3-up supported in the LCC command";
                case 0xF704 -> "Simplex 4-up supported in the LCC command";
                case 0xF801 -> "Simplex and duplex 1-up supported in the LCC command";
                case 0xF802 -> "Simplex and duplex 2-up supported in the LCC command";
                case 0xF803 -> "Simplex and duplex 3-up supported in the LCC command";
                case 0xF804 -> "Simplex and duplex 4-up supported in the LCC command";
                case 0xF902 -> "Basic cut-sheet emulation mode supported";
                case 0xFA00 -> "XOH PCC X'02' counter update support";
                case 0xFB00 -> "All architected units of measure supported";
                case 0xFC00 -> "All function listed for IS/3 is supported";
                case 0xFC01 -> "All function listed for MO:DCA GA is supported";
                case 0xFF01 -> "Positioning Exception Sense Format Supported";
                case 0xFF02 -> "Three-Byte Sense Data Support";
                case 0xFF03 -> "Internal rendering intent support in XOH Trace";

                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD7E3) { // Text Command Set
            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0x5000 && propertyPair <= 0x50FF) {
                return "Multiple text-orientation support for all supported media origins";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WTC-TAP object area orientation support";
            }

            return switch(propertyPair) {
                case 0x1000 -> "Optimum performance if text data is in an ordered page";
                case 0x1001 -> "Unordered text supported";
                case 0x2001 -> "Text object support; includes support for the WTC command";
                case 0x2002 -> "Full range of text suppression IDs supported in LCC and LE commands";
                case 0x4303 -> "Support for PTOCA glyph layout controls";
                case 0x4304 -> "Support of encrypted text string control sequences";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC9D4) { // IM-Image Command Set
            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "Orientation support";
            }

            return switch(propertyPair) {
                case 0x1000 -> "Optimum performance when IM Image is in an ordered page";
                case 0x1001 -> "IM-Image objects may be sent in any order";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC9D6) { // IO-Image Command Set
            if (propertyPair >= 0x2000 && propertyPair <= 0x20FF) {
                return "Retired item 15";
            }

            if (propertyPair >= 0x3000 && propertyPair <= 0x30FF) {
                return "Retired item 16";
            }

            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WIC2-IAP object area orientation support";
            }

            return switch(propertyPair) {
                case 0x1001 -> "IO-Image objects may be sent in any order";
                case 0x1202 -> "IO-Image objects can be downloaded in home state as resources";
                case 0x1206 -> "IO-Image support for LPD extents";
                case 0x1208 -> "Negative object-area positioning";
                case 0x4401 -> "Extended IOCA bilevel color support";
                case 0x4402 -> "Extended IOCA Tile-Set-Color support";
                case 0x4403 -> "Bilevel IO-Image color support on the RPO command";
                case 0x5001 -> "Modified ITU-TSS Modified READ Algorithm (IBM MMR)";
                case 0x5003 -> "Uncompressed image";
                case 0x5006 -> "Run-Length 4 Compression Algorithm";
                case 0x5008 -> "ABIC (bilevel Q-coder) Compression Algorithm (ABIC)";
                case 0x500A -> "Concatenated ABIC";
                case 0x500D -> "TIFF LZW";
                case 0x500E -> "TIFF LZW with Differencing Predictor";
                case 0x5020 -> "Solid Fill Rectangle";
                case 0x5080 -> "ITU-TSS T.4 Facsimile Coding Scheme";
                case 0x5081 -> "ITU-TSS T.4 Facsimile Coding Scheme";
                case 0x5082 -> "ITU-TSS T.6 Facsimile Coding Scheme (G4 MMR)";
                case 0x5083 -> "ISO/ITU-TSS JPEG algorithms";
                case 0x5084 -> "JBIG2 Compression Algorithm";
                case 0x5101 -> "Bit ordering supported in the IOCA Image Encoding Parameter";
                case 0x5204 -> "Unpadded RIDIC recording algorithm";
                case 0x5308 -> "IDE size = 8 supported";
                case 0x5501 -> "Transparency masks";
                case 0x5505 -> "Multiple image content support";
                case 0x5506 -> "nColor Names parameter supported";
                case 0xF300 -> "Replicate-and-trim mapping supported";
                case 0xF301 -> "Scale-to-fill mapping supported";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xE5C7) { // Graphics Command Set
            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WGC-GAP object area orientation support";
            }

            return switch(propertyPair) {
                case 0x1001 -> "Graphics objects may be sent in any order";
                case 0x1207 -> "Support for GOCA image resolution in the WGC-GDD";
                case 0x1208 -> "Negative object-area positioning";
                case 0x4100 -> "Set Process Color drawing order supported";
                case 0x4101 -> "Box drawing orders supported";
                case 0x4102 -> "Partial Arc drawing orders supported";
                case 0x4106 -> "Set Fractional Line Width drawing order supported";
                case 0x4107 -> "Cubic Bézier Curve drawing orders";
                case 0x4108 -> "Set default support in GDD for Normal Line Width";
                case 0x4109 -> "Set default support in GDD for Process Color";
                case 0x4110 -> "Set Line End drawing order supported";
                case 0x4111 -> "Set Line Join drawing order supported";
                case 0x4112 -> "Clockwise full and partial arcs supported";
                case 0x4113 -> "Nonzero Winding mode supported";
                case 0x4114 -> "Clockwise boxes supported";
                case 0x4115 -> "Custom line types supported";
                case 0x4116 -> "Font positioning method used for GOCA character positioning";
                case 0x4117 -> "Cell positioning method used for GOCA character positioning";
                case 0x4130 -> "Custom patterns supported";
                case 0x4131 -> "Gradients supported for area fill";
                case 0x4132 -> "Marker size supported";
                case 0xF300 -> "Retired item 135";
                case 0xF301 -> "Scale-to-fill mapping supported";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC2C3) { // Bar Code Command Set
            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WBCC-BCAP object area orientation support";
            }

            return switch(propertyPair) {
                case 0x1001 -> "Bar code objects may be sent in any order";
                case 0x1208 -> "Negative object-area positioning";
                case 0x1300 -> "Small-symbol support";
                case 0x1301 -> "Retired item 139";
                case 0x1302 -> "Desired-symbol-width parameter supported in the Bar Code Symbol Descriptor";
                case 0x1303 -> "Data Matrix encodation scheme support";
                case 0x1304 -> "Full range of font local IDs supported in WBCC-BCDD";
                case 0x1305 -> "Support for bar code suppression";
                case 0x1306 -> "Support for the too-much-data flag in the QR Code Special-Function Parameters";
                case 0x1307 -> "Support for the too-much-data flag in the Data Matrix Special-Function Parameters";
                case 0x4400 -> "Extended bar code color support";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD6C3) { // Object Container Command Set
            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WOCC-OCAP and IDO-DOAP object area orientation support";
            }

            return switch(propertyPair) {
                case 0x1201 -> "Data-object-resource support";
                case 0x1203 -> "Object Container Presentation Space Size (X'9C') triplet supported for PDF objects in IDO, RPO, and WOCC commands";
                case 0x1204 -> "Remove Resident Resource (RRR) command support";
                case 0x1205 -> "Request Resident Resource List (RRRL) command support";
                case 0x1208 -> "Negative object-area positioning";
                case 0x1209 -> "Object Container Presentation Space Size (X'9C') triplet supported for SVG objects in IDO, RPO, and WOCC commands";
                case 0x120A -> "Extension entries supported in the DORE command";
                case 0x120B -> "Retired item 149";
                case 0x120D -> "TrueType/OpenType Fonts supported as secondary resources in the DORE2 command";
                case 0x120E -> "Data Object Resource Equivalence 2 (DORE2) command support";
                case 0x5800 -> "Image Resolution (X'9A') triplet supported in IDO, RPO, and WOCC commands";
                case 0x5801 -> "Bilevel and grayscale image color support for object containers";
                case 0xF301 -> "Scale-to-fill mapping supported";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD4C4) { // MO1 Subset of the Metadata Command Set
            return switch(propertyPair) {
                case 0xD001 -> "Support for the AFP Tagging format";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD6D3) { // Overlay Command Set
            if (propertyPair > 0x1501 && propertyPair < 0x15FF) {
                return String.format("Overlay nesting up to %d levels is supported", (propertyPair & 0xFF));
            }

            return switch(propertyPair) {
                case 0x1102 -> "Extended overlay support; up to 32511 overlays can be activated at one time";
                case 0x1501 -> "No overlay nesting is supported";
                case 0x15FF -> "255 or more levels of overlay nesting supported";
                case 0x1600 -> "Preprinted form overlay support in LCC and IO commands";
                case 0xA004 -> "Page-overlay-rotation support; all 4 orientations supported in the IO command";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD7E2) { // Page Segment Command Set
            return switch(propertyPair) {
                case 0x1101 -> "Extended page segment support; up to 32511 page segments can be activated at one time";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC3C6) { // Loaded Font Command Set
            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "Orientation support";
            }

            return switch(propertyPair) {
                case 0xB001 -> "Double-byte coded fonts and code pages";
                case 0xB002 -> "Underscore width and position parameters in the LFI command are used by printer";
                case 0xB003 -> "GRID-parts fields allowed in the LFC, LFCSC, and LCPC commands";
                case 0xB004 -> "Default character parameters supported in the LCPC command";
                case 0xB005 -> "Extended (Unicode mapping) code page support";
                case 0xC005 -> "Coded-font pattern-technology; Bounded-box raster-font technology";
                case 0xC01E -> "Coded-font pattern-technology; CID-keyed outline-font technology";
                case 0xC01F -> "Coded-font pattern-technology; Type 1 PFB outline-font technology";
                case 0xC100 -> "Coded-font metric-technology; Fixed metrics";
                case 0xC101 -> "Coded-font metric-technology; Relative metrics";
                default -> "** Unknown **";
            };
        }

        return "** Unknown **";
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
