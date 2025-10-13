package de.textmode.ipdsbox.ipdsclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HexFormat;
import java.util.LinkedList;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.acknowledge.AcknowledgeReply;
import de.textmode.ipdsbox.ipds.commands.*;
import de.textmode.ipdsbox.ipds.xohorders.SetMediaOriginOrder;
import de.textmode.ipdsbox.ipds.xohorders.SetMediaSizeOrder;
import de.textmode.ipdsbox.ipds.xohorders.UnknownXohOrderCode;
import de.textmode.ipdsbox.ppd.PagePrinterRequest;
import de.textmode.ipdsbox.ppd.PagePrinterRequestReader;
import org.apache.commons.cli.*;

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

    private static final String OPTION_PRINTER_SHORT = "t";
    private static final String OPTION_PRINTER_LONG = "to";
    private static final String OPTION_PRINTER_TEXT = "printer to forward to (portnumer may be specified if not 5001)";

    private static final int DEFAULT_PORT_NUMBER = 5001;

    // PagePrinterRequest's from the printer are stored in this LinkedList and processed in order...
    private final LinkedList<PagePrinterRequest> fifo = new LinkedList<>();

    private IpdsClient() {
    }

    /**
     * This is the entry point of {@link IpdsClient}.
     *
     * @param args the command line arguments passed to {@link IpdsClient}.
     */
    public static void main(final String[] args) {
        System.exit(new IpdsClient().realMain(args));
    }

    /**
     * This is the entry point of {@link IpdsClient}.
     *
     * @param args the command line arguments passed to {@link IpdsClient}.
     */
    int realMain(final String[] args) {
        final Options options = new Options();
        options.addOption(Option.builder(OPTION_HELP_SHORT)
                                .longOpt(OPTION_HELP_LONG)
                                .desc(OPTION_HELP_TEXT)
                                .build());

        options.addOption(Option.builder(OPTION_INFO_SHORT)
                                .longOpt(OPTION_INFO_LONG)
                                .desc(OPTION_INFO_TEXT)
                                .argName("info")
                                .build());

        options.addOption(Option.builder(OPTION_WRITE_SHORT)
                .longOpt(OPTION_WRITE_LONG)
                .desc(OPTION_WRITE_TEXT)
                .hasArg(true)
                .argName("write")
                .build());

        options.addOption(Option.builder(OPTION_PRINTER_SHORT)
                                .longOpt(OPTION_PRINTER_LONG)
                                .desc(OPTION_PRINTER_TEXT)
                                .required()
                                .hasArg(true)
                                .argName("printer:port")
                                .build());

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

        System.out.println("|o|                                                 |o|"); //$NON-NLS-1$
        System.out.println("|o|                                                 |o|"); //$NON-NLS-1$
        System.out.println("|o|    I P D S B O X   D E M O N S T R A T I O N    |o|"); //$NON-NLS-1$
        System.out.println("|o|                                                 |o|"); //$NON-NLS-1$
        System.out.println("|o|                                                 |o|"); //$NON-NLS-1$
        System.out.println("|o|              I P D S - C L I E N T              |o|"); //$NON-NLS-1$
        System.out.println("|o|                                                 |o|"); //$NON-NLS-1$
        System.out.println("|o|                                                 |o|"); //$NON-NLS-1$
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("Conecting printer..."); //$NON-NLS-1$
        try (final Socket toPrinterSocket = new Socket(remotePrinterHost, remotePortNumber)) {
            System.out.println("Conection to printer established."); //$NON-NLS-1$
            this.handleConnection(toPrinterSocket);

            if (obtainPrinterInfo) {
                this.obtainPrinterInfo(toPrinterSocket);
            } else if (writeText != null) {
                this.writeText(toPrinterSocket, writeText);
            }

            System.out.println("Done."); //$NON-NLS-1$
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
    private void obtainPrinterInfo(final Socket printer) throws IOException, InvalidIpdsCommandException {

        final OutputStream streamToPrinter = printer.getOutputStream();

        // "00000010 00000001 00000001 00000002"  --> Don't know what this means. a packet trace showed
        // that this is the first block of data that is sent to the printer...
        PagePrinterRequest requestOut = new PagePrinterRequest(0x01, HexFormat.of().parseHex("0000000100000002"));
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 1: Send X'00000010 00000001 00000001 00000002'");

        // The printer responses with "00000010 00000002 00000001 00000002"
        System.out.println("Step 2: Wait for X'00000010 00000002 00000001 00000002'");
        PagePrinterRequest requestIn = this.waitForServer();

        // "00000008 00000005"  --> Don't know what this means. a packet trace showed
        // that this is the second block of data that is sent to the printer...
        requestOut = new PagePrinterRequest(0x05);
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 3: Send X'00000008 00000005'");

        // The printer responses with "00000008 00000006"
        System.out.println("Step 4: Wait for X'00000008 00000006'");
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
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 5: Send STM command");

        System.out.println("Step 6: Wait for Ackknowledge Reply");
        requestIn = this.waitForServer();
        System.out.println("Step 7: Done!");

        final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(requestIn.getData());
        ipds.skip(8);
        final AcknowledgeReply acknowledgeReply = new AcknowledgeReply(ipds);

        System.out.println(StringUtils.toPrettyString(acknowledgeReply));
    }

    /**
     * Obtain printer information using the STM (Sense and type model) command.
     */
    private void writeText(final Socket printer, final String text) throws IOException, InvalidIpdsCommandException, UnknownXohOrderCode {

        final OutputStream streamToPrinter = printer.getOutputStream();

        // "00000010 00000001 00000001 00000002"  --> Don't know what this means. a packet trace showed
        // that this is the first block of data that is sent to the printer...
        PagePrinterRequest requestOut = new PagePrinterRequest(0x01, HexFormat.of().parseHex("0000000100000002"));
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 1: Send X'00000010 00000001 00000001 00000002'");

        // The printer responses with "00000010 00000002 00000001 00000002"
        System.out.println("Step 2: Wait for X'00000010 00000002 00000001 00000002'");
        PagePrinterRequest requestIn = this.waitForServer();

        // "00000008 00000005"  --> Don't know what this means. a packet trace showed
        // that this is the second block of data that is sent to the printer...
        requestOut = new PagePrinterRequest(0x05);
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 3: Send X'00000008 00000005'");

        // The printer responses with "00000008 00000006"
        System.out.println("Step 4: Wait for X'00000008 00000006'");
        requestIn = this.waitForServer();

        requestOut = new PagePrinterRequest(new SetHomeStateCommand());
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 5: Send SHS command");

        requestOut = new PagePrinterRequest(new ExecuteOrderHomeStateCommand(new SetMediaOriginOrder()));
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 6: Send XOH command (Media Origin)");

        requestOut = new PagePrinterRequest(new ExecuteOrderHomeStateCommand(new SetMediaSizeOrder()));
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 7: Send XOH command (Media Size)");

        requestOut = new PagePrinterRequest(new LogicalPageDescriptorCommand());
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 8: Send LPD command");

        /*********************
        final LoadCopyControlCommand.Keyword kw = new LoadCopyControlCommand.Keyword(0xE1, 0x01);

        final LoadCopyControlCommand lcc = new LoadCopyControlCommand();
        lcc.getCopySubgroups().add(new C)
        requestOut = new PagePrinterRequest(new LoadCopyControlCommand());
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 8: Send LCC command");
         */

        requestOut = new PagePrinterRequest(new LogicalPagePositionCommand());
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 8: Send LPP command");

        requestOut = new PagePrinterRequest(new BeginPageCommand());
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 8: Send CP command");

        final String ami = "2BD304C600FF";
        final String amb = "2BD304D200FF";
        final String trn = "2BD307F1F2F3F4F5";

        requestOut = new PagePrinterRequest(new WriteTextCommand(HexFormat.of().parseHex(ami + amb + trn)));
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 8: Send WT command");

        final EndPageCommand endPageCommand = new EndPageCommand();
        endPageCommand.getCommandFlags().isAcknowledgmentRequired(true);
        requestOut = new PagePrinterRequest(endPageCommand);
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 8: Send EP command");

        System.out.println("Step 8b: Wait for Ackknowledge Reply");
        requestIn = this.waitForServer();

        requestOut = new PagePrinterRequest(new SetHomeStateCommand());
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 8: Send SHS command");


        // At end...


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
        requestOut.writeTo(streamToPrinter);
        System.out.println("Step 5: Send STM command");

        System.out.println("Step 6: Wait for Ackknowledge Reply");
        requestIn = this.waitForServer();
        System.out.println("Step 7: Done!");

        final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(requestIn.getData());
        ipds.skip(8);
        final AcknowledgeReply acknowledgeReply = new AcknowledgeReply(ipds);

        System.out.println(StringUtils.toPrettyString(acknowledgeReply));
    }

    /**
     * Determines the host name or IP address of the specified printer.
     *
     * @param value the host name or ip address from the command line, optionally with port number
     * @return the host name without port number
     * @throws ParseException if the specified string is no valid host name or ip address (IPv4 or IPv6).
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
     *
     * @param value the host name or ip address from the command line, optionally with port number
     * @return the port number to be used. If the string does not contain a port number, 5001 will be returned
     * @throws ParseException if the specified string is no valid host name or ip address (IPv4 or IPv6).
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
     *
     * @param value the value (hostname and/or ip address with an optional port number) from the command line
     * @return a new {@link ParseException}
     */
    private static ParseException createParseExceptionForPrinterOption(final String value) {
        return new ParseException("Specified printer \"" + value + "\" is not valid");
    }

    /**
     * Shows the help on the standard output.
     *
     * @param options all known commandline options
     * @return always 1 so the caller can do a <code>return showHelp(options)</code>.
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
     *
     * @param printer the socket of the connection to the printer
     */
    private void handleConnection(final Socket printer) throws IOException {

        System.out.println("Handling connection to " + printer.getInetAddress());

        final InputStream streamFromPrinter = printer.getInputStream();

        // This thread will read data from the printer and pass it to the print server...
        final Thread t = new Thread() {
            @Override
            public void run() {
                IpdsClient.this.readFromPrinter(streamFromPrinter);
            }
        };

        t.start();
    }


    private void readFromPrinter(final InputStream in) {

        try {
            PagePrinterRequest req;
            while ((req = PagePrinterRequestReader.read(in)) != null) {
                this.addToFiFo(req);
            }
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

    void addToFiFo(final PagePrinterRequest request) {
        synchronized (this.fifo) {
            this.fifo.add(request);
        }
    }

    synchronized PagePrinterRequest popFromFiFo() {
        synchronized (this.fifo) {
            return this.fifo.isEmpty() ? null : this.fifo.removeFirst();
        }
    }

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
}
