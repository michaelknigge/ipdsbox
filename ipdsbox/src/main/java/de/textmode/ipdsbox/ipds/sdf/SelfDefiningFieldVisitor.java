package de.textmode.ipdsbox.ipds.sdf;

/**
 * Visitor for all concrete {@link SelfDefiningField} implementations.
 */
public interface SelfDefiningFieldVisitor {

    /**
     * Handle method for {@link ActivateResourceSelfDefiningField}.
     */
    void handle(final ActivateResourceSelfDefiningField sdf);

    /**
     * Handle method for {@link ActiveSetupNameSelfDefiningField}.
     */
    void handle(final ActiveSetupNameSelfDefiningField sdf);

    /**
     * Handle method for {@link AvailableFeaturesSelfDefiningField}.
     */
    void handle(final AvailableFeaturesSelfDefiningField sdf);

    /**
     * Handle method for {@link BarCodeTypeSelfDefiningField}.
     */
    void handle(final BarCodeTypeSelfDefiningField sdf);

    /**
     * Handle method for {@link ColorantIdentificationSelfDefiningField}.
     */
    void handle(final ColorantIdentificationSelfDefiningField sdf);

    /**
     * Handle method for {@link CommonBarCodeTypeSelfDefiningField}.
     */
    void handle(final CommonBarCodeTypeSelfDefiningField sdf);

    /**
     * Handle method for {@link DeactivateFontDeactivationTypesSupportedSelfDefiningField}.
     */
    void handle(final DeactivateFontDeactivationTypesSupportedSelfDefiningField sdf);

    /**
     * Handle method for {@link DeviceAppearanceSelfDefiningField}.
     */
    void handle(final DeviceAppearanceSelfDefiningField sdf);

    /**
     * Handle method for {@link ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField}.
     */
    void handle(final ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField sdf);

    /**
     * Handle method for {@link FinishingOperationsSelfDefiningField}.
     */
    void handle(final FinishingOperationsSelfDefiningField sdf);

    /**
     * Handle method for {@link FinishingOptionsSelfDefiningField}.
     */
    void handle(final FinishingOptionsSelfDefiningField sdf);

    /**
     * Handle method for {@link ImImageAndCodedFontResolutionSelfDefiningField}.
     */
    void handle(final ImImageAndCodedFontResolutionSelfDefiningField sdf);

    /**
     * Handle method for {@link InstalledFeaturesSelfDefiningField}.
     */
    void handle(final InstalledFeaturesSelfDefiningField sdf);

    /**
     * Handle method for {@link KeepGroupTogetherSelfDefiningField}.
     */
    void handle(final KeepGroupTogetherSelfDefiningField sdf);

    /**
     * Handle method for {@link MediaDestinationsSelfDefiningField}.
     */
    void handle(final MediaDestinationsSelfDefiningField sdf);

    /**
     * Handle method for {@link MediumModificationIdsSupportedSelfDefiningField}.
     */
    void handle(final MediumModificationIdsSupportedSelfDefiningField sdf);

    /**
     * Handle method for {@link ObjectContainerTypeSupportSelfDefiningField}.
     */
    void handle(final ObjectContainerTypeSupportSelfDefiningField sdf);

    /**
     * Handle method for {@link ObjectContainerVersionSupportSelfDefiningField}.
     */
    void handle(final ObjectContainerVersionSupportSelfDefiningField sdf);

    /**
     * Handle method for {@link PfcTripletsSupportedSelfDefiningField}.
     */
    void handle(final PfcTripletsSupportedSelfDefiningField sdf);

    /**
     * Handle method for {@link PrintableAreaSelfDefiningField}.
     */
    void handle(final PrintableAreaSelfDefiningField sdf);

    /**
     * Handle method for {@link PrinterSetupSelfDefiningField}.
     */
    void handle(final PrinterSetupSelfDefiningField sdf);

    /**
     * Handle method for {@link PrinterSpeedSelfDefiningField}.
     */
    void handle(final PrinterSpeedSelfDefiningField sdf);

    /**
     * Handle method for {@link PrintQualitySupportSelfDefiningField}.
     */
    void handle(final PrintQualitySupportSelfDefiningField sdf);

    /**
     * Handle method for {@link ProductIdentifierSelfDefiningField}.
     */
    void handle(final ProductIdentifierSelfDefiningField sdf);

    /**
     * Handle method for {@link RecognizedGroupIdFormatsSelfDefiningField}.
     */
    void handle(final RecognizedGroupIdFormatsSelfDefiningField sdf);

    /**
     * Handle method for {@link ResidentSymbolSetSupportSelfDefiningField}.
     */
    void handle(final ResidentSymbolSetSupportSelfDefiningField sdf);

    /**
     * Handle method for {@link StandardOcaColorValueSupportSelfDefiningField}.
     */
    void handle(final StandardOcaColorValueSupportSelfDefiningField sdf);

    /**
     * Handle method for {@link StoragePoolsSelfDefiningField}.
     */
    void handle(final StoragePoolsSelfDefiningField sdf);

    /**
     * Handle method for {@link SupportedDeviceResolutionsSelfDefiningField}.
     */
    void handle(final SupportedDeviceResolutionsSelfDefiningField sdf);

    /**
     * Handle method for {@link SupportedGroupOperationsSelfDefiningField}.
     */
    void handle(final SupportedGroupOperationsSelfDefiningField sdf);

    /**
     * Handle method for {@link SymbolSetSupportSelfDefiningField}.
     */
    void handle(final SymbolSetSupportSelfDefiningField sdf);

    /**
     * Handle method for {@link UnknownSelfDefiningField}.
     */
    void handle(final UnknownSelfDefiningField sdf);

    /**
     * Handle method for {@link Up3iPaperInputMediaSelfDefiningField}.
     */
    void handle(final Up3iPaperInputMediaSelfDefiningField sdf);

    /**
     * Handle method for {@link Up3iTupelSelfDefiningField}.
     */
    void handle(final Up3iTupelSelfDefiningField sdf);
}
