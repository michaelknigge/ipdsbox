package de.textmode.ipdsbox.ipds.triplets;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all Triplets used in IPDS commands.
 */
public enum TripletId {
    /**
     * The Group ID triplet (X'00') is used to keep things together as a print unit.
     */
    GroupID(0x00, "Group ID"),

    /**
     * The Coded Graphic Character Set Global Identifier (CGCSGID) triplet (X'01') specifies
     * the code page and character set used to interpret character data.
     */
    CodedGraphicCharacterSetGlobalIdentifier(0x01, "Coded Graphic Character Set Global Identifier"),

    /**
     * The Fully Qualified Name (X'02') triplet is used to specify a fully qualified name (FQN) and
     * how that name is to be used. The FQN Type field specifies how the FQN is to be used and the
     * FQN format field specifies how the FQN is encoded.
     */
    FullyQualifiedName(0x02, "Fully Qualified Name"),

    /**
     * The Color Specification triplet (X'4E') is used to specify a color value by defining a
     * color space and an encoding for that value.
     */
    ColorSpecification(0x4E, "Color Specification"),

    /**
     * The Encoding Scheme ID (X'50') triplet is used to specify the encoding scheme
     * used for character data to be printed.
     */
    EncodingSchemeID(0x50, "Encoding Scheme ID"),

    /**
     * The Object Offset (X'5A') triplet selects a paginated object within a multi-page resource
     * object (such as a multipage PDF file or multi-image TIFF file).
     */
    ObjectOffset(0x5A, "Object Offset"),

    /**
     * The last supported Local Date and Time Stamp triplet encountered is used to find the resource
     * to be activated; all other Local Date and Time Stamp triplets are ignored.
     */
    LocalDateandTimeStamp(0x62, "Local Date and Time Stamp"),

    /**
     * The Group Information (X'6E') triplet is used to provide information about a group of pages.
     */
    GroupInformation(0x6E, "Group Information"),

    /**
     * The Presentation Space Reset Mixing triplet (X'70') is used to specify whether or not a
     * presentation space is reset to the color of the medium prior to placing object data into
     * the presentation space.
     */
    PresentationSpaceResetMixing(0x70, "Presentation Space Reset Mixing"),

    /**
     * The Toner Saver triplet (X'74') is used to activate a toner saver mode for color printing.
     */
    TonerSaver(0x74, "Toner Saver"),

    /**
     * The Color Fidelity triplet (X'75') is used to specify the exception continuation and reporting
     * rules for color exceptions.
     */
    ColorFidelity(0x75, "Color Fidelity"),

    /**
     * This triplet supplies metric values that can be used to adjust some of the metrics in an
     * outline coded font.
     */
    MetricAdjustment(0x79, "Metric Adjustment"),

    /**
     * The last supported Font Resolution and Metric Technology triplet encountered will be used
     * to find the rasterfont resource to be activated; all other Font Resolution and Metric Technology
     * triplets are ignored.
     */
    FontResolutionandMetricTechnology(0x84, "Font Resolution and Metric Technology"),

    /**
     * The Finishing Operation triplet (X'85') specifies a specific finishing operation to be applied
     * to either a sheet or to a collection of sheets.
     */
    FinishingOperation(0x85, "Finishing Operation"),

    /**
     * The Text Fidelity (X'86') triplet is used to specify the exception continuation and reporting
     * rules when an unrecognized or unsupported text control sequence is encountered.
     */
    TextFidelity(0x86, "Text Fidelity"),

    /**
     * The Finishing Fidelity (X'88') triplet is used to specify the exception continuation and
     * reporting rules for finishing exceptions.
     */
    FinishingFidelity(0x88, "Finishing Fidelity"),

    /**
     * The Finishing Fidelity (X'88') triplet is used to specify the exception continuation and
     * reporting rules for finishing exceptions.
     */
    DataObjectFontDescriptor(0x8B, "Data Object Font Descriptor"),

    /**
     * The linked Font (X'8D') triplet is used to identify a TrueType/OpenType object to be
     * linked to a base font.
     */
    LinkedFont(0x8D, "Linked Font"),

    /**
     * The UP3I Finishing Operation (X'8E') triplet specifies a specific finishing operation to be
     * applied to either a sheet or to a collection of sheets, depending on the command containing
     * the triplet.
     */
    UP3IFinishingOperation(0x8E, "UP3I Finishing Operation"),

    /**
     * The Color Management Resource Descriptor (X'91') triplet specifies the processing mode for
     * a Color Management Resource.
     */
    ColorManagementResourceDescriptor(0x91, "Color Management Resource Descriptor"),

    /**
     * The Invoke CMR (X'92') triplet identifies a Color Management Resource to be invoked.
     */
    InvokeCMR(0x92, "Invoke CMR"),

    /**
     * The Rendering Intent (X'95') triplet specifies up to four rendering intents to be used when
     * processing presentation data.
     */
    RenderingIntent(0x95, "Rendering Intent"),

    /**
     * The CMR Tag Fidelity (X'96') triplet specifies the exception continuation and reporting rules
     * for Color Management Resource (CMR) tag exceptions.
     */
    CMRTagFidelity(0x96, "CMR Tag Fidelity"),

    /**
     * The Device Appearance (X'97') triplet selects one of a set of architected appearances for
     * the device to temporarily assume.
     */
    DeviceAppearance(0x97, "Device Appearance"),

    /**
     * The Image Resolution (X'9A') triplet specifies the resolution of a raster image.
     */
    ImageResolution(0x9A, "Image Resolution"),

    /**
     * The Object Container Presentation Space Size triplet specifies the presentation space size
     * for a PDF object.
     */
    ObjectContainerPresentationSpaceSize(0x9C, "Object Container Presentation Space Size"),

    /**
     * The Setup Name triplet specifies a setup name that encompasses some number of settings on a device.
     */
    SetupName(0x9E, "Setup Name"),

    /**
     * The Setup Name triplet specifies a setup name that encompasses some number of settings on a device.
     */
    InvokeTertiaryResource(0xA2, "Invoke Tertiary Resource");

    private static final Map<Integer, TripletId> REVERSE_MAP = new HashMap<>();

    static {
        for (final TripletId tripletId : values()) {
            REVERSE_MAP.put(tripletId.getId(), tripletId);
        }
    }

    private final int id;
    private final String name;

    /**
     * Constructor of the enum value.
     */
    TripletId(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the enum value for the given integer of <code>null</code> if the value is unknown.
     */
    public static TripletId getIfKnown(final int value) {
        return REVERSE_MAP.get(value);
    }

    /**
     * Gets the integer value of the Triplet Id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the name of the Triplet.
     */
    public String getName() {
        return this.name;
    }
}
