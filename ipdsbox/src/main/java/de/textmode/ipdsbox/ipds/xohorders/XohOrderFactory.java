package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * A factory for all supported {@link XohOrder}s.
 */
public final class XohOrderFactory {

    /**
     * Private constructor to make checkstyle happy.
     */
    private XohOrderFactory() {
    }

    /**
     * Creates a {@link XohOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    public static XohOrder create(final IpdsByteArrayInputStream ipds)
        throws UnknownXohOrderCode, IOException,InvalidIpdsCommandException {

        // TODO do not throw an exception.... create a "UnknownXohOrder" instead
        // getIfKnown !!!
        final XohOrderCode code = XohOrderCode.getFor(ipds.readUnsignedInteger16());
        ipds.rewind(2); // Go back so the Order-Implementations will read the complete order...

        // TODO
        //  1. do not rewind...
        //  2. the constructors do not need te read the order code...
        // implement like SelfDefiningFieldFactory ... implement the same "style"....
        // i. e. use "modern switch"...


        return switch (code) {
            case DeactivateSavedPageGroup -> new DeactivateSavedPageGroupOrder(ipds);
            case DefineGroupBoundary -> new DefineGroupBoundaryOrder(ipds);
            case EjectToFrontFacing -> new EjectToFrontFacingOrder(ipds);
            case EraseResidualFontData -> new EraseResidualFontDataOrder(ipds);
            case EraseResidualPrintData -> new EraseResidualPrintDataOrder(ipds);
            case ObtainPrinterCharacteristics -> new ObtainPrinterCharacteristicsOrder(ipds);
            case PageCountersControl -> new PageCountersControlOrder(ipds);
            case PrintBufferedData -> new PrintBufferedDataOrder(ipds);
            case RemoveSavedGroup -> new RemoveSavedPageGroupOrder(ipds);
            case SelectInputMediaSource -> new SelectInputMediaSourceOrder(ipds);
            case SelectMediumModifications -> new SelectMediumModificationsOrder(ipds);
            case SeparateContinuousForms -> new SeparateContinuousFormsOrder(ipds);
            case SetMediaOrigin -> new SetMediaOriginOrder(ipds);
            case SetMediaSize -> new SetMediaSizeOrder(ipds);
            case SpecifyGroupOperation -> new SpecifyGroupOperationOrder(ipds);
            case StackReceivedPages -> new StackReceivedPagesOrder(ipds);
            case Trace -> new TraceOrder(ipds);

            // TODO do not throw an exception.... create a "UnknownXohOrder" instead
            default -> throw new IpdsboxRuntimeException("No case for XohOrderCode " + code.toString());
        };
    }
}
