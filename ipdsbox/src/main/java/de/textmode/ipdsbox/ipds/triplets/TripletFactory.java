package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * A factory for all supported {@link Triplet}s.
 */
public final class TripletFactory {

    /**
     * Private constructor to make checkstyle happy.
     */
    private TripletFactory() {
    }

    /**
     * Creates a {@link Triplet} from the given {@link IpdsByteArrayInputStream}.
     */
    public static Triplet create(final byte[] data) throws IOException {

        final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(data);

        // The implementation requires that the byte array is exactly as large
        // as specified in the length field.
        final int length = ipds.readUnsignedByte();
        if (length != data.length) {
            throw new IOException(String.format(
                    "A triplet to be read seems to be %1$d bytes long but the IPDS data stream ends after %2$d bytes",
                    length,
                    data.length));
        }

        final int tripletId = ipds.readUnsignedByte();
        final TripletId triplet = TripletId.getIfKnown(tripletId);
        if (triplet == null) {
            return new UnknownTriplet(ipds, tripletId);
        }

        return switch (triplet) {
            case CMRTagFidelity -> new CmrTagFidelityTriplet(ipds);
            case CodedGraphicCharacterSetGlobalIdentifier -> new CodedGraphicCharacterSetGlobalIdentifierTriplet(ipds);
            case ColorFidelity -> new ColorFidelityTriplet(ipds);
            case ColorManagementResourceDescriptor -> new ColorManagementResourceDescriptorTriplet(ipds);
            case ColorSpecification -> new ColorSpecificationTriplet(ipds);
            case DataObjectFontDescriptor -> new DataObjectFontDescriptorTriplet(ipds);
            case DeviceAppearance -> new DeviceAppearanceTriplet(ipds);
            case EncodingSchemeID -> new EncodingSchemeIdTriplet(ipds);
            case FinishingFidelity -> new FinishingFidelityTriplet(ipds);
            case FinishingOperation -> new FinishingOperationTriplet(ipds);
            case FontResolutionandMetricTechnology -> new FontResolutionAndMetricTechnologyTriplet(ipds);
            case FullyQualifiedName -> new FullyQualifiedNameTriplet(ipds);
            case GroupID -> new GroupIdTriplet(ipds);
            case GroupInformation -> new GroupInformationTriplet(ipds);
            case ImageResolution -> new ImageResolutionTriplet(ipds);
            case InvokeCMR -> new InvokeCmrTriplet(ipds);
            case InvokeTertiaryResource -> new InvokeTertiaryResourceTriplet(ipds);
            case LinkedFont -> new LinkedFontTriplet(ipds);
            case LocalDateandTimeStamp -> new LocalDateTimeStampTriplet(ipds);
            case MetricAdjustment -> new MetricAdjustmentTriplet(ipds);
            case ObjectContainerPresentationSpaceSize -> new ObjectContainerPresentationSpaceSizeTriplet(ipds);
            case ObjectOffset -> new ObjectOffsetTriplet(ipds);
            case PresentationSpaceResetMixing -> new PresentationSpaceResetMixingTriplet(ipds);
            case RenderingIntent -> new RenderingIntentTriplet(ipds);
            case SetupName -> new SetupNameTriplet(ipds);
            case TextFidelity -> new TextFidelityTriplet(ipds);
            case TonerSaver -> new TonerSaverTriplet(ipds);
            case UP3IFinishingOperation -> new UP3IFinishingOperationTriplet(ipds);

            default -> new UnknownTriplet(ipds, tripletId);
        };
    }
}
