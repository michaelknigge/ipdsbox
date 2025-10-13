package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * A factory for all supported {@link SelfDefiningField}s.
 */
public final class SelfDefiningFieldFactory {

    /**
     * Private constructor to make checkstyle happy.
     */
    private SelfDefiningFieldFactory() {
    }

    /**
     * Creates a {@link SelfDefiningField} from the given byte array.
     */
    public static SelfDefiningField create(final byte[] data) throws IOException, UnknownSelfDefinedFieldException {

        final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(data);

        ipds.skip(2); // Skip the 2 byte length field...
        final SelfDefiningFieldId sdf = SelfDefiningFieldId.getFor(ipds.readUnsignedInteger16());
        ipds.rewind(4); // Rewind so the SelfDefiningField implementation reads the whole SelfDefiningField

        return switch (sdf) {
           //case PrintableArea -> new PrintableAreaSelfDefiningField(ipds);
           //case SymbolSetSupport -> new SymbolSetSupportSelfDefiningField(ipds);
           //case ImageAndCodedFontResolution -> new ImageAndCodedFontResolutionSelfDefiningField(ipds);
           //case StoragePools -> new StoragePoolsSelfDefiningField(ipds);
           //case StandardOcaColorValueSupport -> new StandardOcaColorValueSupportSelfDefiningField(ipds);
           //case InstalledFeatures -> new InstalledFeaturesSelfDefiningField(ipds);
           //case AvailableFeatures -> new AvailableFeaturesSelfDefiningField(ipds);
           //case ResidentSymbolSetSupport -> new ResidentSymbolSetSupportSelfDefiningField(ipds);
           //case PrintQualitySupport -> new PrintQualitySupportSelfDefiningField(ipds);
           //case ExecuteOrderAnystateRequestResidentResourceListSupport -> new ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField(ipds);
           //case ActivateResource -> new ActivateResourceSelfDefiningField(ipds);
           //case MediumModificationIdsSupported -> new MediumModificationIdsSupportedSelfDefiningField(ipds);
           //case CommonBarCodeType -> new CommonBarCodeTypeSelfDefiningField(ipds);
           //case BarCodeType -> new BarCodeTypeSelfDefiningField(ipds);
           //case MediaDestinations -> new MediaDestinationsSelfDefiningField(ipds);
           //case SupportedGroupOperations -> new SupportedGroupOperationsSelfDefiningField(ipds);
           //case ProductIdentifier -> new ProductIdentifierSelfDefiningField(ipds);
           //case ObjectContainerTypeSupport -> new ObjectContainerTypeSupportSelfDefiningField(ipds);
           //case DeactiveteFontDeactivationTypesSupported -> new DeactiveteFontDeactivationTypesSupportedSelfDefiningField(ipds);
           //case PfcTripletsSupported -> new PfcTripletsSupportedSelfDefiningField(ipds);
           case PrinterSetup -> new PrinterSetupSelfDefiningField(ipds, SelfDefiningFieldId.PrinterSetup);
           //case FinishingOperations -> new FinishingOperationsSelfDefiningField(ipds);
           //case Up3iTupel -> new Up3iTupelSelfDefiningField(ipds);
           //case Up3iPaperInputMedia -> new Up3iPaperInputMediaSelfDefiningField(ipds);
           //case ColorantIdentification -> new ColorantIdentificationSelfDefiningField(ipds);
           //case DeviceAppearance -> new DeviceAppearanceSelfDefiningField(ipds);
           //case KeepGroupTogether -> new KeepGroupTogetherSelfDefiningField(ipds);
           //case RecognizedGroupIdFormats -> new RecognizedGroupIdFormatsSelfDefiningField(ipds);
           //case SupportedDeviceResolutions -> new SupportedDeviceResolutionsSelfDefiningField(ipds);
           //case ObjectContainerVersionSupport -> new ObjectContainerVersionSupportSelfDefiningField(ipds);
           case FinishingOptions -> new FinishingOptionsSelfDefiningField(ipds, SelfDefiningFieldId.FinishingOptions);
           //case PrinterSpeed -> new PrinterSpeedSelfDefiningField(ipds);
           //case ActiveSetupName -> new ActiveSetupNameSelfDefiningField(ipds);

            default -> new RawSelfDefiningField(ipds, sdf);
        };
    }
}
