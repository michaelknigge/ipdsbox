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
     * Creates a {@link SelfDefiningField} from the given byte array. Note that the byte array must be
     * exactly as log as the self-defining field. If not an {@link IOException} wil be thrown.
     */
    public static SelfDefiningField create(final byte[] data) throws IOException {

        final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(data);

        // The implementation requires that the byte array is exactly as large
        // as specified in the length field.
        final int length = ipds.readUnsignedInteger16();
        if (length != data.length) {
            throw new IOException(String.format(
                    "A self-defining field to be read seems to be %1$d bytes long but the IPDS data stream ends after %2$d bytes",
                    length,
                    data.length));
        }

        final int sdfId = ipds.readUnsignedInteger16();
        final SelfDefiningFieldId sdf = SelfDefiningFieldId.getIfKnown(sdfId);
        if (sdf == null) {
            return new UnknownSelfDefiningField(ipds, sdfId);
        }

        return switch (sdf) {
            case PrintableArea -> new PrintableAreaSelfDefiningField(ipds);
            case SymbolSetSupport -> new SymbolSetSupportSelfDefiningField(ipds);
            case ImImageAndCodedFontResolution -> new ImImageAndCodedFontResolutionSelfDefiningField(ipds);
            case StoragePools -> new StoragePoolsSelfDefiningField(ipds);
            case StandardOcaColorValueSupport -> new StandardOcaColorValueSupportSelfDefiningField(ipds);
            case InstalledFeatures -> new InstalledFeaturesSelfDefiningField(ipds);
            case AvailableFeatures -> new AvailableFeaturesSelfDefiningField(ipds);
            case ResidentSymbolSetSupport -> new ResidentSymbolSetSupportSelfDefiningField(ipds);
            case PrintQualitySupport -> new PrintQualitySupportSelfDefiningField(ipds);
            case ExecuteOrderAnystateRequestResidentResourceListSupport -> new ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField(ipds);
            case ActivateResource -> new ActivateResourceSelfDefiningField(ipds);
            case MediumModificationIdsSupported -> new MediumModificationIdsSupportedSelfDefiningField(ipds);
            case CommonBarCodeType -> new CommonBarCodeTypeSelfDefiningField(ipds);
            case BarCodeType -> new BarCodeTypeSelfDefiningField(ipds);
            case MediaDestinations -> new MediaDestinationsSelfDefiningField(ipds);
            case SupportedGroupOperations -> new SupportedGroupOperationsSelfDefiningField(ipds);
            case ProductIdentifier -> new ProductIdentifierSelfDefiningField(ipds);
            case ObjectContainerTypeSupport -> new ObjectContainerTypeSupportSelfDefiningField(ipds);
            case DeactiveteFontDeactivationTypesSupported -> new DeactiveteFontDeactivationTypesSupportedSelfDefiningField(ipds);
            case PfcTripletsSupported -> new PfcTripletsSupportedSelfDefiningField(ipds);
            case PrinterSetup -> new PrinterSetupSelfDefiningField(ipds);
            case FinishingOperations -> new FinishingOperationsSelfDefiningField(ipds);
            case Up3iTupel -> new Up3iTupelSelfDefiningField(ipds);
            case Up3iPaperInputMedia -> new Up3iPaperInputMediaSelfDefiningField(ipds);
            case ColorantIdentification -> new ColorantIdentificationSelfDefiningField(ipds);
            case DeviceAppearance -> new DeviceAppearanceSelfDefiningField(ipds);
            case KeepGroupTogether -> new KeepGroupTogetherSelfDefiningField(ipds);
            case RecognizedGroupIdFormats -> new RecognizedGroupIdFormatsSelfDefiningField(ipds);
            case SupportedDeviceResolutions -> new SupportedDeviceResolutionsSelfDefiningField(ipds);
            case ObjectContainerVersionSupport -> new ObjectContainerVersionSupportSelfDefiningField(ipds);
            case FinishingOptions -> new FinishingOptionsSelfDefiningField(ipds);
            case PrinterSpeed -> new PrinterSpeedSelfDefiningField(ipds);
            case ActiveSetupName -> new ActiveSetupNameSelfDefiningField(ipds);

            default -> new UnknownSelfDefiningField(ipds, sdfId);
        };
    }
}
