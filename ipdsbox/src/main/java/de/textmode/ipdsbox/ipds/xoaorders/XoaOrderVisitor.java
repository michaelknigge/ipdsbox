package de.textmode.ipdsbox.ipds.xoaorders;

/**
 * Visitor for all concrete {@link XoaOrder} implementations.
 */
public interface XoaOrderVisitor {

    /**
     * Handle method for {@link ActivatePrinterAlarmOrder}.
     * @param order ActivatePrinterAlarmOrder object.
     */
    void handle(final ActivatePrinterAlarmOrder order);

    /**
     * Handle method for {@link AlternateOffsetStackerOrder}.
     * @param order AlternateOffsetStackerOrder object.
     */
    void handle(AlternateOffsetStackerOrder order);

    /**
     * Handle method for {@link ControlEdgeMarksOrder}.
     * @param order ControlEdgeMarksOrder object.
     */
    void handle(ControlEdgeMarksOrder order);

    /**
     * Handle method for {@link DiscardBufferedDataOrder}.
     * @param order DiscardBufferedDataOrder object.
     */
    void handle(DiscardBufferedDataOrder order);

    /**
     * Handle method for {@link DiscardUnstackedPagesOrder}.
     * @param order DiscardUnstackedPagesOrder object.
     */
    void handle(DiscardUnstackedPagesOrder order);

    /**
     * Handle method for {@link ExceptionHandlingControlOrder}.
     * @param order ExceptionHandlingControlOrder object.
     */
    void handle(ExceptionHandlingControlOrder order);

    /**
     * Handle method for {@link MarkFormOrder}.
     * @param order MarkFormOrder object.
     */
    void handle(MarkFormOrder order);

    /**
     * Handle method for {@link ObtainAdditionalExceptionInformationOrder}.
     * @param order ObtainAdditionalExceptionInformationOrder object.
     */
    void handle(ObtainAdditionalExceptionInformationOrder order);

    /**
     * Handle method for {@link PrintQualityControlOrder}.
     * @param order PrintQualityControlOrder object.
     */
    void handle(PrintQualityControlOrder order);

    /**
     * Handle method for {@link RequestResourceListOrder}.
     * @param order RequestResourceListOrder object.
     */
    void handle(RequestResourceListOrder order);

    /**
     * Handle method for {@link RequestSetupNameListOrder}.
     * @param order RequestSetupNameListOrder object.
     */
    void handle(RequestSetupNameListOrder order);
}
