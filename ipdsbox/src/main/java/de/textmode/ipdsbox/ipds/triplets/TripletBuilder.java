package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.core.IpdsboxRuntimeException;

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

        switch (triplet) {
        case CMRTagFidelity:
            throw new UnknownTripletException("currently unsupported");
        case CodedGraphicCharacterSetGlobalIdentifier:
            return new CodedGraphicCharacterSetGlobalIdentifierTriplet(data);
        case ColorFidelity:
            throw new UnknownTripletException("currently unsupported");
        case ColorManagementResourceDescriptor:
            throw new UnknownTripletException("currently unsupported");
        case ColorSpecification:
            return new ColorSpecificationTriplet(data);
        case DataObjectFontDescriptor:
            throw new UnknownTripletException("currently unsupported");
        case DeviceAppearance:
            throw new UnknownTripletException("currently unsupported");
        case EncodingSchemeID:
            return new EncodingSchemeIdTriplet(data);
        case FinishingFidelity:
            throw new UnknownTripletException("currently unsupported");
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
            throw new UnknownTripletException("currently unsupported");
        case InvokeCMR:
            throw new UnknownTripletException("currently unsupported");
        case InvokeTertiaryResource:
            throw new UnknownTripletException("currently unsupported");
        case LinkedFont:
            throw new UnknownTripletException("currently unsupported");
        case LocalDateandTimeStamp:
            return new LocalDateTimeStampTriplet(data);
        case MetricAdjustment:
            throw new UnknownTripletException("currently unsupported");
        case ObjectContainerPresentationSpaceSize:
            throw new UnknownTripletException("currently unsupported");
        case ObjectOffset:
            return new ObjectOffsetTriplet(data);
        case PresentationSpaceResetMixing:
            return new PresentationSpaceResetMixingTriplet(data);
        case RenderingIntent:
            throw new UnknownTripletException("currently unsupported");
        case SetupName:
            throw new UnknownTripletException("currently unsupported");
        case TextFidelity:
            throw new UnknownTripletException("currently unsupported");
        case TonerSaver:
            throw new UnknownTripletException("currently unsupported");
        case UP3IFinishingOperation:
            return new UP3IFinishingOperationTriplet(data);
        default:
            throw new IpdsboxRuntimeException("No case for TripletID " + triplet.toString());
        }
    }
}
