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

        // TODO do not throw an exception.... create a "RawXohOrder" instead
        final XohOrderCode code = XohOrderCode.getFor(ipds.readUnsignedInteger16());
        ipds.rewind(2); // Go back so the Order-Implementations will read the complete order...

        switch (code) {
        case DeactivateSavedPageGroup:
            return new DeactivateSavedPageGroupOrder(ipds);
        case DefineGroupBoundary:
            return new DefineGroupBoundaryOrder(ipds);
        case EjectToFrontFacing:
            return new EjectToFrontFacingOrder(ipds);
        case EraseResidualFontData:
            return new EraseResidualFontDataOrder(ipds);
        case EraseResidualPrintData:
            return new EraseResidualPrintDataOrder(ipds);
        case ObtainPrinterCharacteristics:
            return new ObtainPrinterCharacteristicsOrder(ipds);
        case PageCountersControl:
            return new PageCountersControlOrder(ipds);
        case PrintBufferedData:
            return new PrintBufferedDataOrder(ipds);
        case RemoveSavedGroup:
            return new RemoveSavedPageGroupOrder(ipds);
        case SelectInputMediaSource:
            return new SelectInputMediaSourceOrder(ipds);
        case SelectMediumModifications:
            return new SelectMediumModificationsOrder(ipds);
        case SeparateContinuousForms:
            return new SeparateContinuousFormsOrder(ipds);
        case SetMediaOrigin:
            return new SetMediaOriginOrder(ipds);
        case SetMediaSize:
            return new SetMediaSizeOrder(ipds);
        case SpecifyGroupOperation:
            return new SpecifyGroupOperationOrder(ipds);
        case StackReceivedPages:
            return new StackReceivedPagesOrder(ipds);
        case Trace:
            return new TraceOrder(ipds);
        default:
            throw new IpdsboxRuntimeException("No case for XohOrderCode " + code.toString());
        }
    }
}
