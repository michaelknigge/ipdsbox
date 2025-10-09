package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;

/**
 * A builder for all supported {@link Triplet}s.
 */
public final class TripletBuilder {

    /**
     * Private constructor to make checkstyle happy.
     */
    private TripletBuilder() {
    }

    /**
     * Builds a {@link Triplet} from the given byte array.
     * @param data the raw IPDS data of the {@link Triplet}.
     * @return a concrete {@link Triplet} implementation
     * @throws UnknownTripletException if the given IPDS data describes an unknown {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete.
     * @throws InvalidIpdsCommandException if the given IPDS data is invalid.
     */
    public static Triplet build(final byte[] data)
        throws UnknownTripletException, IOException, InvalidIpdsCommandException {

        final TripletId triplet = TripletId.getFor(data[1] & 0xFF);

        // TODO: Passs a IpdsByteArrayInputStream to the Triplet ???
        // TODO: Convert to "modern switch"
        switch (triplet) {
        case CMRTagFidelity:
            return new CmrTagFidelityTriplet(data);
        case CodedGraphicCharacterSetGlobalIdentifier:
            return new CodedGraphicCharacterSetGlobalIdentifierTriplet(data);
        case ColorFidelity:
            return new ColorFidelityTriplet(data);
        case ColorManagementResourceDescriptor:
            return new ColorManagementResourceDescriptorTriplet(data);
        case ColorSpecification:
            return new ColorSpecificationTriplet(data);
        case DataObjectFontDescriptor:
            return new DataObjectFontDescriptorTriplet(data);
        case DeviceAppearance:
            return new DeviceAppearanceTriplet(data);
        case EncodingSchemeID:
            return new EncodingSchemeIdTriplet(data);
        case FinishingFidelity:
            return new FinishingFidelityTriplet(data);
        case FinishingOperation:
            return new FinishingOperationTriplet(data);
        case FontResolutionandMetricTechnology:
            return new FontResolutionAndMetricTechnologyTriplet(data);
        case FullyQualifiedName:
            return new FullyQualifiedNameTriplet(data);
        case GroupID:
            return new GroupIdTriplet(data);
        case GroupInformation:
            return new GroupInformationTriplet(data);
        case ImageResolution:
            return new ImageResolutionTriplet(data);
        case InvokeCMR:
            return new InvokeCmrTriplet(data);
        case InvokeTertiaryResource:
            return new InvokeTertiaryResourceTriplet(data);
        case LinkedFont:
            return new LinkedFontTriplet(data);
        case LocalDateandTimeStamp:
            return new LocalDateTimeStampTriplet(data);
        case MetricAdjustment:
            return new MetricAdjustmentTriplet(data);
        case ObjectContainerPresentationSpaceSize:
            return new ObjectContainerPresentationSpaceSizeTriplet(data);
        case ObjectOffset:
            return new ObjectOffsetTriplet(data);
        case PresentationSpaceResetMixing:
            return new PresentationSpaceResetMixingTriplet(data);
        case RenderingIntent:
            return new RenderingIntentTriplet(data);
        case SetupName:
            return new SetupNameTriplet(data);
        case TextFidelity:
            return new TextFidelityTriplet(data);
        case TonerSaver:
            return new TonerSaverTriplet(data);
        case UP3IFinishingOperation:
            return new UP3IFinishingOperationTriplet(data);
        default:
            throw new UnknownTripletException("Unhandled Triplet 0x" + Integer.toHexString(triplet.getId()));
        }
    }
}
