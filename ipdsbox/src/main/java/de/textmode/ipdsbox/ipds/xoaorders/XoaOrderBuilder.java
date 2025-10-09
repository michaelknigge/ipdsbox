package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

/**
 * A builder for all supported {@link XoaOrder}s.
 */
public final class XoaOrderBuilder {

    /**
     * Private constructor to make checkstyle happy.
     */
    private XoaOrderBuilder() {
    }

    /**
     * Builds a {@link XoaOrder} from the given byte array.
     * @param ipds the raw IPDS data of the {@link XoaOrder}.
     * @return a concrete {@link XoaOrder} implementation
     * @throws UnknownXoaOrderCode if the given IPDS data describes an unknown {@link XoaOrder}.
     * @throws UnknownTripletException if the given IPDS data describes an unknown {@link Triplet}.
     * @throws InvalidIpdsCommandException if the given IPDS data is broken.
     * @throws IOException if the given IPDS data is incomplete.
     */
    public static XoaOrder build(final IpdsByteArrayInputStream ipds)
        throws UnknownXoaOrderCode, IOException, UnknownTripletException, InvalidIpdsCommandException {

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

            default -> throw new UnknownXoaOrderCode("No case for XoaOrderCode " + code);
        };
    }
}
