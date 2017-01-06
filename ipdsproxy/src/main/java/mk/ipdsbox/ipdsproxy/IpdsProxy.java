package mk.ipdsbox.ipdsproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import mk.ipdsbox.ppd.PagePrinterRequest;
import mk.ipdsbox.ppd.PagePrinterRequestReader;

/**
 * This is the main class of {@link IpdsProxy}, a little tool that accepts connections
 * from a print server (i. e. IBM InfoPrint) and forwards all received data to a
 * printer.
 * <p>
 * The tool is more or less of little use. Its purpose is to demonstrate the use of
 * the low level class {@link PagePrinterRequestReader}. More functionality may be
 * added in the future, i. e. decoding IPDS commands to human readable descriptions.
 */
public final class IpdsProxy {

    private static final String IPDSPROXY = "ipdsproxy";
    private static final String IPDSPROXY_FOOTER = "\nSee https://github.com/michaelknigge/ipdsbox\n\n";
    private static final String IPDSPROXY_HEADER =
        "\nForwards incoming PPD/PPR (encapsulated IPDS) connections to a remote IPDS printer.\n\n";

    private static final String OPTION_HELP_SHORT = "h";
    private static final String OPTION_HELP_LONG = "help";
    private static final String OPTION_HELP_TEXT = "print this message";

    private static final String OPTION_PORT_SHORT = "p";
    private static final String OPTION_PORT_LONG = "port";
    private static final String OPTION_PORT_TEXT = "Portnumer to listen on (default 5001)";

    private static final String OPTION_PRINTER_SHORT = "t";
    private static final String OPTION_PRINTER_LONG = "to";
    private static final String OPTION_PRINTER_TEXT = "printer to forward to (portnumer may be specified if not 5001)";

    private static final int DEFAULT_PORT_NUMBER = 5001;

    private IpdsProxy() {
    }

    /**
     * This is the entry point of {@link IpdsProxy}.
     *
     * @param args the command line arguments passed to {@link IpdsProxy}.
     */
    public static void main(final String[] args) {
        System.exit(new IpdsProxy().realMain(args));
    }

    /**
     * This is the entry point of {@link IpdsProxy}.
     *
     * @param args the command line arguments passed to {@link IpdsProxy}.
     */
    int realMain(final String[] args) {
        final Options options = new Options();
        options.addOption(Option.builder(OPTION_HELP_SHORT)
                                .longOpt(OPTION_HELP_LONG)
                                .desc(OPTION_HELP_TEXT)
                                .build());

        options.addOption(Option.builder(OPTION_PORT_SHORT)
                                .longOpt(OPTION_PORT_LONG)
                                .desc(OPTION_PORT_TEXT)
                                .hasArg(true)
                                .argName("portnumber")
                                .build());

        options.addOption(Option.builder(OPTION_PRINTER_SHORT)
                                .longOpt(OPTION_PRINTER_LONG)
                                .desc(OPTION_PRINTER_TEXT)
                                .required()
                                .hasArg(true)
                                .argName("printer:port")
                                .build());

        final int localPortNumber;
        final int remotePortNumber;
        final String remotePrinterHost;
        try {
            final CommandLineParser parser = new DefaultParser();
            final CommandLine line = parser.parse(options, args);

            if (line.hasOption(OPTION_HELP_LONG)) {
                return showHelp(options);
            }

            if (line.hasOption(OPTION_PORT_LONG)) {
                localPortNumber = Integer.parseInt(line.getOptionValue(OPTION_PORT_LONG));
            } else {
                localPortNumber = DEFAULT_PORT_NUMBER;
            }

            remotePrinterHost = parsePrinterHost(line.getOptionValue(OPTION_PRINTER_LONG));
            remotePortNumber = parsePrinterPort(line.getOptionValue(OPTION_PRINTER_LONG));

        } catch (final ParseException exp) {
            System.err.println("IpdsProxy: " + exp.getMessage());
            return showHelp(options);
        }

        try (final ServerSocket passiveSocket = new ServerSocket(localPortNumber)) {
            this.accecptAndHandleConnections(passiveSocket, remotePrinterHost, remotePortNumber);
            return 0;
        } catch (final IOException e) {
            System.err.println(e.getMessage());
            return 1;
        }
    }

    /**
     * Waits for an incoming connection from a print server and handles the connection.
     * This method does this in an infinitive loop.
     *
     * @param passiveSocket the socket used to accept connections from
     * @param remotePrinterHost host name or IP address of the IPDS printer
     * @param remotePortNumber port number of the IPDS printer
     */
    private void accecptAndHandleConnections(final ServerSocket passiveSocket, final String remotePrinterHost,
        final int remotePortNumber) {

        while (true) {
            System.out.println("Waiting for connection from print server..."); //$NON-NLS-1$
            try (final Socket toServerSocket = passiveSocket.accept()) {
                System.out.println("Conection from print server accepted."); //$NON-NLS-1$

                System.out.println("Conecting printer..."); //$NON-NLS-1$
                try (final Socket toPrinterSocket = new Socket(remotePrinterHost, remotePortNumber)) {
                    System.out.println("Conection to printer established."); //$NON-NLS-1$
                    handleConnection(toServerSocket, toPrinterSocket);
                }
            } catch (final IOException e) {
                System.err.println(e.getMessage());
            }
        }
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
        formatter.printHelp(IPDSPROXY, IPDSPROXY_HEADER, options, IPDSPROXY_FOOTER, true);
        return 1;
    }

    /**
     * Handles the connection from the print server (which is the PPR, the Page Printer Requester).
     * All data is read from the socket and passed to the "real" IPDS printer.
     *
     * @param printServer the socket of the connection to the print server
     * @param printer the socket of the connection to the printer
     */
    private static void handleConnection(final Socket printServer, final Socket printer) throws IOException {

        System.out.println("Handling connection from " + printServer.getInetAddress());

        final InputStream streamFromPrintServer = printServer.getInputStream();
        final OutputStream streamToPrintServer = printServer.getOutputStream();

        final InputStream streamFromPrinter = printer.getInputStream();
        final OutputStream streamToPrinter = printer.getOutputStream();

        // This thread will read data from the printer and pass it to the print server...
        final Thread t = new Thread() {
            @Override
            public void run() {
                readAndWriteData(streamFromPrinter, streamToPrintServer);
            }
        };

        t.start();

        // Read data from the print server and pass it to the printer...
        readAndWriteData(streamFromPrintServer, streamToPrinter);
    }

    /**
     * Reads data from an {@link InputStream} (which may be the input side of the print server
     * or the printer) and writes all read data to an {@link OutputStream} (which is the counterpart
     * of the the {@link InputStream}.
     *
     * @param in {@link InputStream} to read from
     * @param out {@link OutputStream} to write to
     */
    private static void readAndWriteData(final InputStream in, final OutputStream out) {
        try {
            PagePrinterRequest req;
            while ((req = PagePrinterRequestReader.read(in)) != null) {
                out.write(req.getData());
            }
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
