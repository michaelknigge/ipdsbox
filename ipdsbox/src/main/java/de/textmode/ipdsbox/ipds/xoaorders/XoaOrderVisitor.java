package de.textmode.ipdsbox.ipds.xoaorders;

/**
 * Visitor for all concrete {@link XoaOrder} implementations.
 */
public interface XoaOrderVisitor {

    /**
     * Handle method for {@link ActivatePrinterAlarmOrder}.
     */
    void handle(final ActivatePrinterAlarmOrder order);

    /**
     * Handle method for {@link AlternateOffsetStackerOrder}.
     */
    void handle(AlternateOffsetStackerOrder order);

    /**
     * Handle method for {@link ControlEdgeMarksOrder}.
     */
    void handle(ControlEdgeMarksOrder order);

    /**
     * Handle method for {@link DiscardBufferedDataOrder}.
     */
    void handle(DiscardBufferedDataOrder order);

    /**
     * Handle method for {@link DiscardUnstackedPagesOrder}.
     */
    void handle(DiscardUnstackedPagesOrder order);

    /**
     * Handle method for {@link ExceptionHandlingControlOrder}.
     */
    void handle(ExceptionHandlingControlOrder order);

    /**
     * Handle method for {@link MarkFormOrder}.
     */
    void handle(MarkFormOrder order);

    /**
     * Handle method for {@link ObtainAdditionalExceptionInformationOrder}.
     */
    void handle(ObtainAdditionalExceptionInformationOrder order);

    /**
     * Handle method for {@link PrintQualityControlOrder}.
     */
    void handle(PrintQualityControlOrder order);

    /**
     * Handle method for {@link RequestResourceListOrder}.
     */
    void handle(RequestResourceListOrder order);

    /**
     * Handle method for {@link RequestSetupNameListOrder}.
     */
    void handle(RequestSetupNameListOrder order);
}
