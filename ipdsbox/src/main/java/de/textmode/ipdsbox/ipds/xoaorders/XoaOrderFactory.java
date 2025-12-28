package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * A factory for all supported {@link XoaOrder}s.
 */
public final class XoaOrderFactory {

    /**
     * Private constructor to make checkstyle happy.
     */
    private XoaOrderFactory() {
    }

    /**
     * Creates a {@link XoaOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    public static XoaOrder create(final IpdsByteArrayInputStream ipds)
        throws UnknownXoaOrderCode, IOException,InvalidIpdsCommandException {

        // TODO do not throw an exception.... create a "UnknownXoaOrder" instead

        // TODO
        //  1. do not rewind...
        //  2. the constructors do not need te read the order code...

        // implement like SelfDefiningFieldFactory ... implement the same "style"....
        // i. e. use "modern switch"...


        // getIfKnown !!!
        final XoaOrderCode code = XoaOrderCode.getFor(ipds.readUnsignedInteger16());
        ipds.rewind(2); // Go back so the Order-Implementations will read the complete order...

        return switch (code) {
            case ActivatePrinterAlarm -> new ActivatePrinterAlarmOrder(ipds);
            case AlternateOffsetStacker -> new AlternateOffsetStackerOrder(ipds);
            case ControlEdgeMarks -> new ControlEdgeMarksOrder(ipds);
            case DiscardBufferedData -> new DiscardBufferedDataOrder(ipds);
            case DiscardUnstackedPages -> new DiscardUnstackedPagesOrder(ipds);
            case ExceptionHandlingControl -> new ExceptionHandlingControlOrder(ipds);
            case MarkForm -> new MarkFormOrder(ipds);
            case ObtainAdditionalExceptionInformation -> new ObtainAdditionalExceptionInformationOrder(ipds);
            case PrintQualityControl -> new PrintQualityControlOrder(ipds);
            case RequestResourceList -> new RequestResourceListOrder(ipds);
            case RequestSetupNameList -> new RequestSetupNameListOrder(ipds);

            // TODO do not throw an exception.... create a "UnknownXoaOrder" instead
            default -> throw new UnknownXoaOrderCode("No case for XoaOrderCode " + code);
        };
    }
}
