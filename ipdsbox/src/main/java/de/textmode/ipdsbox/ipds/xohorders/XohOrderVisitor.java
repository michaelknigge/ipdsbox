package de.textmode.ipdsbox.ipds.xohorders;

/**
 * Visitor for all concrete {@link XohOrder} implementations.
 */
public interface XohOrderVisitor {

    /**
     * Handle method for {@link DeactivateSavedPageGroupOrder}.
     */
    void handle(final DeactivateSavedPageGroupOrder order);

    /**
     * Handle method for {@link DefineGroupBoundaryOrder}.
     */
    void handle(DefineGroupBoundaryOrder order);

    /**
     * Handle method for {@link EjectToFrontFacingOrder}.
     */
    void handle(EjectToFrontFacingOrder order);

    /**
     * Handle method for {@link EraseResidualFontDataOrder}.
     */
    void handle(EraseResidualFontDataOrder order);

    /**
     * Handle method for {@link EraseResidualPrintDataOrder}.
     */
    void handle(EraseResidualPrintDataOrder order);

    /**
     * Handle method for {@link ObtainPrinterCharacteristicsOrder}.
     */
    void handle(ObtainPrinterCharacteristicsOrder order);

    /**
     * Handle method for {@link PageCountersControlOrder}.
     */
    void handle(PageCountersControlOrder order);

    /**
     * Handle method for {@link PrintBufferedDataOrder}.
     */
    void handle(PrintBufferedDataOrder order);

    /**
     * Handle method for {@link RemoveSavedPageGroupOrder}.
     */
    void handle(RemoveSavedPageGroupOrder order);

    /**
     * Handle method for {@link SelectInputMediaSourceOrder}.
     */
    void handle(SelectInputMediaSourceOrder order);

    /**
     * Handle method for {@link SelectMediumModificationsOrder}.
     */
    void handle(SelectMediumModificationsOrder order);

    /**
     * Handle method for {@link SeparateContinuousFormsOrder}.
     */
    void handle(SeparateContinuousFormsOrder order);

    /**
     * Handle method for {@link SetMediaOriginOrder}.
     */
    void handle(SetMediaOriginOrder order);

    /**
     * Handle method for {@link SetMediaSizeOrder}.
     */
    void handle(SetMediaSizeOrder order);

    /**
     * Handle method for {@link SpecifyGroupOperationOrder}.
     */
    void handle(SpecifyGroupOperationOrder order);

    /**
     * Handle method for {@link StackReceivedPagesOrder}.
     */
    void handle(StackReceivedPagesOrder order);

    /**
     * Handle method for {@link TraceOrder}.
     */
    void handle(TraceOrder order);

    /**
     * Handle method for {@link UnknownXohOrder}.
     */
    void handle(UnknownXohOrder order);
}
