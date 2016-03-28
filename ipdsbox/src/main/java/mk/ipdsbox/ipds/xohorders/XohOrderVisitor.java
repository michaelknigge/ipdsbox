package mk.ipdsbox.ipds.xohorders;

/**
 * Visitor for all concrete {@link XohOrder} implementations.
 */
public interface XohOrderVisitor {

    /**
     * Handle method for {@link DeactivateSavedPageGroupOrder}.
     * @param order DeactivateSavedPageGroupOrder object.
     */
    void handle(final DeactivateSavedPageGroupOrder order);

    /**
     * Handle method for {@link DefineGroupBoundaryOrder}.
     * @param order DefineGroupBoundaryOrder object.
     */
    void handle(DefineGroupBoundaryOrder order);

    /**
     * Handle method for {@link EjectToFrontFacingOrder}.
     * @param order EjectToFrontFacingOrder object.
     */
    void handle(EjectToFrontFacingOrder order);

    /**
     * Handle method for {@link EraseResidualFontDataOrder}.
     * @param order EraseResidualFontDataOrder object.
     */
    void handle(EraseResidualFontDataOrder order);

    /**
     * Handle method for {@link EraseResidualPrintDataOrder}.
     * @param order EraseResidualPrintDataOrder object.
     */
    void handle(EraseResidualPrintDataOrder order);

    /**
     * Handle method for {@link ObtainPrinterCharacteristicsOrder}.
     * @param order ObtainPrinterCharacteristicsOrder object.
     */
    void handle(ObtainPrinterCharacteristicsOrder order);

    /**
     * Handle method for {@link PageCountersControlOrder}.
     * @param order PageCountersControlOrder object.
     */
    void handle(PageCountersControlOrder order);

    /**
     * Handle method for {@link PrintBufferedDataOrder}.
     * @param order PrintBufferedDataOrder object.
     */
    void handle(PrintBufferedDataOrder order);

    /**
     * Handle method for {@link RemoveSavedPageGroupOrder}.
     * @param order RemoveSavedPageGroupOrder object.
     */
    void handle(RemoveSavedPageGroupOrder order);

    /**
     * Handle method for {@link SelectInputMediaSourceOrder}.
     * @param order SelectInputMediaSourceOrder object.
     */
    void handle(SelectInputMediaSourceOrder order);

    /**
     * Handle method for {@link SelectMediumModificationsOrder}.
     * @param order SelectMediumModificationsOrder object.
     */
    void handle(SelectMediumModificationsOrder order);

    /**
     * Handle method for {@link SeparateContinuousFormsOrder}.
     * @param order SeparateContinuousFormsOrder object.
     */
    void handle(SeparateContinuousFormsOrder order);

    /**
     * Handle method for {@link SetMediaOriginOrder}.
     * @param order SetMediaOriginOrder object.
     */
    void handle(SetMediaOriginOrder order);

    /**
     * Handle method for {@link SetMediaSizeOrder}.
     * @param order SetMediaSizeOrder object.
     */
    void handle(SetMediaSizeOrder order);

    /**
     * Handle method for {@link SpecifyGroupOperationOrder}.
     * @param order SpecifyGroupOperationOrder object.
     */
    void handle(SpecifyGroupOperationOrder order);

    /**
     * Handle method for {@link StackReceivedPagesOrder}.
     * @param order StackReceivedPagesOrder object.
     */
    void handle(StackReceivedPagesOrder order);

    /**
     * Handle method for {@link TraceOrder}.
     * @param order TraceOrder object.
     */
    void handle(TraceOrder order);
}
