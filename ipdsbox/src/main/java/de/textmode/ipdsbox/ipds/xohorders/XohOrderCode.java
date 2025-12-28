package de.textmode.ipdsbox.ipds.xohorders;

import java.util.HashMap;
import java.util.Map;

/**
 * XOH (Execute Order Home State) Order Code.
 */
public enum XohOrderCode {
    /**
     * The XOH Deactivate Saved Page Group (DSPG) command directs the printer to
     * deactivate one or more previously saved page groups.
     */
    DeactivateSavedPageGroup(0x0200, "XOH Deactivate Saved Page Group"),

    /**
     * The XOH Define Group Boundary (DGB) command initiates or terminates a grouping of pages.
     */
    DefineGroupBoundary(0x0400, "XOH Define Group Boundary"),

    /**
     * For cut-sheet media, this order causes the next received page to be printed as
     * the first page of the next sheet.
     * <p>
     * For continuous-forms media, this order causes the next received page to be printed
     * as the first page of the next front-facing sheet.
     */
    EjectToFrontFacing(0x1300, "XOH Eject to Front Facing"),

    /**
     * The XOH Erase Residual Font Data (ERFD) order is a data security and privacy order that
     * prohibits access to residual downloaded font data.
     */
    EraseResidualFontData(0x0700, "XOH Erase Residual Font Data"),

    /**
     * The XOH Erase Residual Print Data (ERPD) order is a data security and privacy order that
     * prohibits access to residual print data..
     */
    EraseResidualPrintData(0x0500, "XOH Erase Residual Print Data"),

    /**
     * The XOH Obtain Printer Characteristics (OPC) command causes a set of self-identifying
     * fields that describe characteristics of the printer to be placed in the special data area
     * of the Acknowledge Reply (replies).
     */
    ObtainPrinterCharacteristics(0xF300, "XOH Obtain Printer Characteristics"),

    /**
     * The XOH Page Counters Control (PCC) command provides a counter-synchronization function
     * that should only be used to recover from an exception or after an XOA Discard Buffered
     * Data command.
     */
    PageCountersControl(0xF500, "XOH Page Counters Control"),

    /**
     * The XOH Print Buffered Data (PBD) command causes the printer to schedule all buffered data
     * for printing prior to sending an Acknowledge Reply, if requested.
     */
    PrintBufferedData(0x0100, "XOH Print Buffered Data"),

    /**
     * The XOH Remove Saved Page Group (RSPG) command directs the printer to deactivate and
     * remove one or more previously saved page groups.
     */
    RemoveSavedGroup(0x0A00, "XOH Remove Saved Page Group"),

    /**
     * The XOH Select Input Media Source (SIMS) command selects an input media source ID and
     * indirectly selects the physical media contained in the media source that is mapped to
     * this ID for subsequent sheets.
     */
    SelectInputMediaSource(0x1500, "XOH Select Input Media Source"),

    /**
     * The XOH Select Medium Modifications (SMM) command selects one or more medium modifications
     * to be either applied or inhibited on the current sheet of physical media.
     */
    SelectMediumModifications(0x0E00, "XOH Select Medium Modifications"),

    /**
     * The XOH Separate Continuous Forms (SCF) command signals the printer to separate the
     * continuous-forms media that is currently selected.
     */
    SeparateContinuousForms(0x0900, "XOH Separate Continuous Forms"),

    /**
     * The XOH Set Media Origin (SMO) command sets the origin of the Xm,Ym coordinate system to
     * one of the four corners of the medium presentation space.
     */
    SetMediaOrigin(0x1600, "XOH Set Media Origin"),

    /**
     * The XOH Set Media Size (SMS) command specifies a desired medium presentation space size
     * to be used for valid printable area calculations and N-up partitioning.
     */
    SetMediaSize(0x1700, "XOH Set Media Size"),

    /**
     * The XOH Specify Group Operation (SGO) command indicates to an attached printer,
     * pre-processor, or postprocessor that the specified processing option is to be performed upon
     * subsequent boundary groups of the group level identified in this command.
     */
    SpecifyGroupOperation(0x0300, "XOH Specify Group Operation"),

    /**
     * The XOH Stack Received Pages (SRP) command causes the printer to Eject to the next sheet (if not
     * already on a new sheet), perform an XOH Print Buffered Data and stack all pages that have been
     * committed for printing.
     */
    StackReceivedPages(0x0D00, "XOH Stack Received Pages"),

    /**
     * XOH Trace is a home state command used to start, stop, and obtain IPDS traces.
     */
    Trace(0xF200, "XOH Trace");

    private static final Map<Integer, XohOrderCode> REVERSE_MAP = new HashMap<>();

    static {
        for (final XohOrderCode code : values()) {
            REVERSE_MAP.put(code.getValue(), code);
        }
    }

    private final int code;
    private final String description;

    /**
     * Constructor of the enum value.
     */
    XohOrderCode(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Gets the enum value for the given integer.
     */
    public static XohOrderCode getFor(final int value) throws UnknownXohOrderCode {
        final XohOrderCode result = REVERSE_MAP.get(value);
        if (result == null) {
            throw new UnknownXohOrderCode(
                String.format("The XOH order code X'%1$s' is unknown.", Integer.toHexString(value)));
        }
        return result;
    }

    /**
     * Gets the integer value of the XOH Order Code.
     */
    public int getValue() {
        return this.code;
    }

    /**
     * Gets a textual description of the XOH Order Code.
     */
    public String getDescription() {
        return this.description;
    }
}
