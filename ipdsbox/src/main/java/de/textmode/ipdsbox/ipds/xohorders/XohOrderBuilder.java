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
     * @param data the raw IPDS data of the {@link XohOrder}.
     * @return a concrete {@link XohOrder} implementation
     * @throws UnknownXohOrderCode if the given IPDS data describes an unknown {@link XohOrder}.
     * @throws UnknownTripletException if the given IPDS data describes an unknown {@link Triplet}.
     * @throws InvalidIpdsCommandException if the given IPDS data is broken.
     * @throws IOException if the given IPDS data is incomplete.
     */
    public static XohOrder build(final byte[] data)
        throws UnknownXohOrderCode, IOException, UnknownTripletException, InvalidIpdsCommandException {
        final IpdsByteArrayInputStream in = new IpdsByteArrayInputStream(data);
        final XohOrderCode code = XohOrderCode.getFor(in.readUnsignedInteger16());

        switch (code) {
        case DeactivateSavedPageGroup:
            return new DeactivateSavedPageGroupOrder(data);
        case DefineGroupBoundary:
            return new DefineGroupBoundaryOrder(data);
        case EjectToFrontFacing:
            return new EjectToFrontFacingOrder(data);
        case EraseResidualFontData:
            throw new UnknownXohOrderCode("currently unsupported");
        case EraseResidualPrintData:
            throw new UnknownXohOrderCode("currently unsupported");
        case ObtainPrinterCharacteristics:
            throw new UnknownXohOrderCode("currently unsupported");
        case PageCountersControl:
            throw new UnknownXohOrderCode("currently unsupported");
        case PrintBufferedData:
            throw new UnknownXohOrderCode("currently unsupported");
        case RemoveSavedGroup:
            throw new UnknownXohOrderCode("currently unsupported");
        case SelectInputMediaSource:
            throw new UnknownXohOrderCode("currently unsupported");
        case SelectMediumModifications:
            throw new UnknownXohOrderCode("currently unsupported");
        case SeparateContinuousForms:
            throw new UnknownXohOrderCode("currently unsupported");
        case SetMediaOrigin:
            throw new UnknownXohOrderCode("currently unsupported");
        case SetMediaSize:
            throw new UnknownXohOrderCode("currently unsupported");
        case SpecifyGroupOperation:
            throw new UnknownXohOrderCode("currently unsupported");
        case StackReceivedPages:
            throw new UnknownXohOrderCode("currently unsupported");
        case Trace:
            throw new UnknownXohOrderCode("currently unsupported");
        default:
            throw new IpdsboxRuntimeException("No case for XohOrderCode " + code.toString());
        }
    }
}
