package de.textmode.ipdsbox.ipds.xohorders;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

/**
 * A builder for all supported {@link XohOrder}s.
 */
public final class XohOrderBuilder {

    /**
     * Private constructor to make checkstyle happy.
     */
    private XohOrderBuilder() {
    }

    /**
     * Builds a {@link XohOrder} from the given byte array.
     * @param ipds the raw IPDS data of the {@link XohOrder}.
     * @return a concrete {@link XohOrder} implementation
     * @throws UnknownXohOrderCode if the given IPDS data describes an unknown {@link XohOrder}.
     * @throws UnknownTripletException if the given IPDS data describes an unknown {@link Triplet}.
     * @throws InvalidIpdsCommandException if the given IPDS data is broken.
     * @throws IOException if the given IPDS data is incomplete.
     */
    public static XohOrder build(final IpdsByteArrayInputStream ipds)
        throws UnknownXohOrderCode, IOException, UnknownTripletException, InvalidIpdsCommandException {

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
