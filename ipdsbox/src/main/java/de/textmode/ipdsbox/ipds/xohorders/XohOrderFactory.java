package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
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
        throws IOException, InvalidIpdsCommandException {

        final int orderCodeId = ipds.readUnsignedInteger16();
        final XohOrderCode orderCode = XohOrderCode.getIfKnown(orderCodeId);

        return switch (orderCode) {
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

            // Unhandled and/or unknown (orderCode == null)...
            default -> new UnknownXohOrder(ipds, orderCodeId);
        };
    }
}
