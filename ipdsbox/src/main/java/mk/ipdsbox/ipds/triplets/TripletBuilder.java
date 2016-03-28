package mk.ipdsbox.ipds.triplets;

import java.io.IOException;

import mk.ipdsbox.core.IpdsboxRuntimeException;

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
     */
    public static Triplet build(final byte[] data) throws UnknownTripletException, IOException {
        final TripletId triplet = TripletId.getFor(data[1] & 0xFF);
        switch (triplet) {
        case CMRTagFidelity:
            throw new UnknownTripletException("currently unsupported");
        case CodedGraphicCharacterSetGlobalIdentifier:
            throw new UnknownTripletException("currently unsupported");
        case ColorFidelity:
            throw new UnknownTripletException("currently unsupported");
        case ColorManagementResourceDescriptor:
            throw new UnknownTripletException("currently unsupported");
        case ColorSpecification:
            throw new UnknownTripletException("currently unsupported");
        case DataObjectFontDescriptor:
            throw new UnknownTripletException("currently unsupported");
        case DeviceAppearance:
            throw new UnknownTripletException("currently unsupported");
        case EncodingSchemeID:
            throw new UnknownTripletException("currently unsupported");
        case FinishingFidelity:
            throw new UnknownTripletException("currently unsupported");
        case FinishingOperation:
            throw new UnknownTripletException("currently unsupported");
        case FontResolutionandMetricTechnology:
            throw new UnknownTripletException("currently unsupported");
        case FullyQualifiedName:
            throw new UnknownTripletException("currently unsupported");
        case GroupID:
            return new GroupIdTriplet(data);
        case GroupInformation:
            throw new UnknownTripletException("currently unsupported");
        case ImageResolution:
            throw new UnknownTripletException("currently unsupported");
        case InvokeCMR:
            throw new UnknownTripletException("currently unsupported");
        case LinkedFont:
            throw new UnknownTripletException("currently unsupported");
        case LocalDateandTimeStamp:
            throw new UnknownTripletException("currently unsupported");
        case MetricAdjustment:
            throw new UnknownTripletException("currently unsupported");
        case ObjectContainerPresentationSpaceSize:
            throw new UnknownTripletException("currently unsupported");
        case ObjectOffset:
            throw new UnknownTripletException("currently unsupported");
        case PresentationSpaceResetMixing:
            throw new UnknownTripletException("currently unsupported");
        case RenderingIntent:
            throw new UnknownTripletException("currently unsupported");
        case TextFidelity:
            throw new UnknownTripletException("currently unsupported");
        case TonerSaver:
            throw new UnknownTripletException("currently unsupported");
        case UP3IFinishingOperation:
            throw new UnknownTripletException("currently unsupported");
        default:
            throw new IpdsboxRuntimeException("No case for TripletID " + triplet.toString());
        }
    }
}
