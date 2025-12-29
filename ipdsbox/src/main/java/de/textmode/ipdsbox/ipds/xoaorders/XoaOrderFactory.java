package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;

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
    public static XoaOrder create(final IpdsByteArrayInputStream ipds) throws IOException {

        final int orderCodeId = ipds.readUnsignedInteger16();
        final XoaOrderCode orderCode = XoaOrderCode.getIfKnown(orderCodeId);

        return switch (orderCode) {
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

            // Unhandled and/or unknown (orderCode == null)...
            default -> new UnknownXoaOrder(ipds, orderCodeId);
        };
    }
}
