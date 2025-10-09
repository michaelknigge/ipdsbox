package de.textmode.ipdsbox.ipds.xoaorders;

import java.util.HashMap;
import java.util.Map;

/**
 * XOA (Execute Order Anystate) Order Code.
 */
public enum XoaOrderCode {
    /**
     * The XOA Activate Printer Alarm (APA) command signals the printer to activate its alarm mechanism (for
     * example, a beep, bell, or light) for a device specific amount of time.
     */
    ActivatePrinterAlarm(0x1000, "XOA Activate Printer Alarm"),

    /**
     * The XOA Alternate Offset Stacker (AOS) command signals the printer to jog the current sheet. If copies of the
     * current sheet are stacked in more than one media destination, the jogging occurs in each selected media
     * destination.
     */
    AlternateOffsetStacker(0x0A00, "XOA Alternate Offset Stacker"),

    /**
     * The XOA Control Edge Marks (CEM) command causes a printer that is using continuous-forms media to mark
     * the front side of the current sheet and the front side of the next sheet with edge marks. The mark can optionally
     * be placed on the back side of the sheet
     */
    ControlEdgeMarks(0x0C00, "XOA Control Edge Marks"),

    /**
     * The XOA Discard Buffered Data (DBD) command, sometimes called Discard Buffered Pages, deletes all
     * buffered data from the printer storage and returns the printer to home state. Any data currently being received
     * is deleted. If this order is syntactically correct, no exceptions can result from its execution. The DBD order does
     * not affect completely received resources, such as fonts, page segments, and overlays; however, if the printer
     * is in any resource state, the printer deletes the partial resource before returning to home state.
     */
    DiscardBufferedData(0xF200, "XOA Discard Buffered Data"),

    /**
     * The XOA Discard Unstacked Pages (DUP) command deletes all buffered data from the printer storage (just
     * like DBD), discards all printed but unstacked pages, and returns the printer to home state. Any data currently
     * being received is deleted. If this order is syntactically correct, no exceptions can result from its execution. The
     * DUP order does not affect completely received resources, such as fonts, page segments, and overlays;
     * however, if the printer is in any resource state, the printer deletes the partial resource before returning to home
     * state.
     */
    DiscardUnstackedPages(0xF500, "XOA Discard Unstacked Pages"),

    /**
     * TThe XOA Exception-Handling Control (EHC) command allows the host to control how the printer reports and
     * processes exceptions. A data-stream exception exists when the printer detects an invalid or unsupported
     * command, control, or parameter value in the data stream received from the host. The IPDS architecture
     * defines Alternate Exception Actions (AEAs) when a printer receives a valid request that is not supported by the
     * printer.
     */
    ExceptionHandlingControl(0xF600, "XOA Exception-Handling Control"),

    /**
     * The XOA Mark Form (MF) command causes the printer to mark the current or the next sheet with a devicespecific
     * form. This form is analogous to an overlay that is permanently stored in the printer; however, it is
     * invoked by the MF order. The printer defines the origin of the permanently-stored overlay (mark form) and the
     * host cannot change it; that is, it is not affected by changes to the media origin. The host might add more data to
     * the marked form, for example, job number or user ID, with a normal IPDS command sequence. The MF order
     * can be used to print job-separator sheets or exception-summary sheets.
     */
    MarkForm(0x0800, "XOA Mark Form"),

    /**
     * The XOA Obtain Additional Exception Information (OAEI) command requests that the printer return additional
     * information about an exception that the printer has just reported. The printer responds by placing available
     * information, if any, in the special data area of a subsequent Acknowledge Reply (or in a series of replies). If the
     * OAEI command is not sent with the Acknowledgement Required (ARQ) flag set, the command is treated as a
     * No Operation (NOP) command.
     */
    ObtainAdditionalExceptionInformation(0xF900, "XOA Obtain Additional Exception Information"),

    /**
     * The XOA Print-Quality Control (PQC) command transfers three bytes that indicate the level of quality with
     * which the following data is to be printed. For text data, this order specifies the level of quality the printer
     * achieves without changing fonts.
     */
    PrintQualityControl(0xF800, "XOA Print-Quality Control"),

    /**
     * The XOA Request Resource List (RRL) command requests information about the printer's current resources.
     * The printer responds by placing the requested information in the special data area of a subsequent
     * Acknowledge Reply (or in a series of replies).
     */
    RequestResourceList(0xF400, "XOA Request Resource List"),

    /**
     * TThe XOA Request Setup Name List (RSNL) command requests that the printer return information about the
     * setup names supported by the printer. The printer responds by placing available information, if any, in the
     * special data area of a subsequent Acknowledge Reply (or in a series of replies). If the RSNL command is not
     * sent with the Acknowledgement Required (ARQ) flag bit set, the command is treated as a No Operation (NOP)
     * command.
     */
    RequestSetupNameList(0xFA00, "XOA Request Setup Name List");

    private static final Map<Integer, XoaOrderCode> REVERSE_MAP = new HashMap<>();

    static {
        for (final XoaOrderCode code : values()) {
            REVERSE_MAP.put(code.getValue(), code);
        }
    }

    private final int code;
    private final String description;

    /**
     * Constructor of the enum value.
     */
    XoaOrderCode(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Gets the enum value for the given integer.
     * @return the enum value for the given integer.
     */
    public static XoaOrderCode getFor(final int value) throws UnknownXoaOrderCode {
        final XoaOrderCode result = REVERSE_MAP.get(value);
        if (result == null) {
            throw new UnknownXoaOrderCode(
                String.format("The XOA order code X'%1$s' is unknown.", Integer.toHexString(value)));
        }
        return result;
    }

    /**
     * Gets the integer value of the XOA Order Code.
     * @return the integer value of the order code.
     */
    public int getValue() {
        return this.code;
    }

    /**
     * Gets a textual description of the XOA Order Code.
     * @return A textual description of the XOA Order Code (i. e. "XOA Set Media Size").
     */
    public String getDescription() {
        return this.description;
    }
}
