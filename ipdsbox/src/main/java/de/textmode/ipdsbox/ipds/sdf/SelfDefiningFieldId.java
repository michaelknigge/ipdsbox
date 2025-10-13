package de.textmode.ipdsbox.ipds.sdf;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all self-defining fields.
 */
public enum SelfDefiningFieldId {
    /**
     * The Printable-Area self-defining field returns information about the printer's physical media sources, hereafter
     * referred to simply as media sources or input media sources.
     */
    PrintableArea(0x0001, "Printable-Area"),

    /**
     * The Symbol-Set Support self-defining field specifies the limits of support for the Load Symbol Set command.
     */
    SymbolSetSupport(0x0002, "Symbol-Set Support"),

    /**
     * The IM-Image and Coded-Font Resolution self-defining field specifies the supported resolutions in pels per unit
     * base for IM Image and downloaded LF1-type and LF2-type coded-font pattern data.
     */
    ImageAndCodedFontResolution(0x0003, "IM-Image and Coded-Font Resolution"),

    /**
     * The Storage Pools self-defining field specifies storage pools within the printer.
     */
    StoragePools(0x0004, "Storage Pools"),

    /**
     * The Standard OCA Color Value Support self-defining field specifies the set of Standard OCA color values that
     * are supported by the printer.
     */
    StandardOcaColorValueSupport(0x0005, "Standard OCA Color Value Support"),

    /**
     * The Installed Features self-defining field specifies features installed in the device.
     */
    InstalledFeatures(0x0006, "Installed Features"),

    /**
     * The Available Features self-defining field specifies features immediately available in the device.
     */
    AvailableFeatures(0x0007, "Available Features"),

    /**
     * The Resident Symbol-Set Support self-defining field specifies that symbol sets are resident in the printer.
     */
    ResidentSymbolSetSupport(0x0008, "Resident Symbol-Set Support"),

    /**
     * The Print-Quality Support self-defining field specifies the minimum values for print quality supported by the
     * printer.
     */
    PrintQualitySupport(0x0009, "Print-Quality Support"),

    /**
     * The Execute Order Anystate RRL RT & RIDF Support self-defining field specifies the combinations of resource
     * types and resource ID formats that the printer supports in an XOA-RRL command.
     */
    ExecuteOrderAnystateRequestResidentResourceListSupport(0x000A, "XOA-RRL RT & RIDF Support"),

    /**
     * This self-defining field specifies the combinations of Resource Types and Resource ID Formats supported by
     * the printer, within the Activate Resource command.
     */
    ActivateResource(0x000B, "Activate Resource RT & RIDF Support"),

    /**
     * This self-defining field lists the medium modification IDs that are currently supported by the XOH-SMM
     * command.
     */
    MediumModificationIdsSupported(0x000D, "Medium Modification IDs Supported"),

    /**
     * The Common Bar Code Type/Modifier self-defining field lists those bar codes that are supported by the printer.
     * Deprecated. Use <i>BarCodeType</i> instead.
     */
    CommonBarCodeType(0x000E, "Common Bar Code Type/Modifier"),

    /**
     * The Bar Code Type/Modifier self-defining field lists the optional bar codes that are supported by the printer in
     * addition to those required by the listed BCOCA subset (either BCD1 or BCD2).
     */
    BarCodeType(0x000F, "Bar Code Type/Modifier"),

    /**
     * This self-defining field specifies the available media-destination IDs that can be selected by a Load Copy
     * Control command.
     */
    MediaDestinations(0x0010, "Media-Destinations"),

    /**
     * This self-defining field specifies the group operations supported by a printer, pre-processor, or post-processor
     * in the XOH Specify Group Operation command.
     */
    SupportedGroupOperations(0x0012, "Supported Group Operations"),

    /**
     * The Product Identifier self-defining field is an optional field that specifies parameters that contain
     * productidentification data.
     */
    ProductIdentifier(0x0013, "Product Identifier"),

    /**
     * This self-defining field lists the object containers supported by the printer and for each type of object indicates
     * whether the object is supported in home state, in page or overlay state, or in all three states.
     */
    ObjectContainerTypeSupport(0x0014, "Object-Container Type Support"),

    /**
     * The DF Deactivation Types Supported self-defining field lists the optional deactivation types that are supported
     * by the printer.
     */
    DeactiveteFontDeactivationTypesSupported(0x0015, "DF Deactivation Types Supported"),

    /**
     * The PFC Triplets Supported self-defining field lists the optional triplets that are supported by the printer on
     * the Presentation Fidelity Control command.
     */
    PfcTripletsSupported(0x0016, "PFC Triplets Supported"),

    /**
     * The Printer Setup self-defining field lists all setup IDs that are currently active in the printer.
     */
    PrinterSetup(0x0017, "Printer Setup"),

    /**
     * The Finishing Operations self-defining field lists all the different types of finishing operations that the
     * printer supports with the Finishing Operation (X'85') triplet.
     */
    FinishingOperations(0x0018, "Finishing Operations"),

    /**
     * This self-defining field reports the physical order and properties of the UP3I devices connected to the printer.
     */
    Up3iTupel(0x0019, "UP3I Tupel"),

    /**
     * This self-defining field reports the media attributes of all media that exist in the UP3I line.
     */
    Up3iPaperInputMedia(0x001A, "UP3I Paper Input Media"),

    /**
     * This self-defining field lists all colorants available in the printer.
     */
    ColorantIdentification(0x0021, "Colorant-Identification"),

    /**
     * This self-defining field lists optional device-appearance values that are supported by the printer.
     */
    DeviceAppearance(0x0022, "Device-Appearance"),

    /**
     * This self-defining field identifies the maximum number of sheets allowed within a recovery-unit group; these
     * sheets include sheets containing pages and copies of such sheets.
     */
    KeepGroupTogether(0x0024, "Keep-Group-Together-as-a-Recovery-Unit"),

    /**
     * This self-defining field specifies the group ID formats that are recognized by the printer in the
     * Group ID (X'00') triplet.
     */
    RecognizedGroupIdFormats(0x0025, "Recognized Group ID Formats"),

    /**
     * This self-defining field lists the resolution (or resolutions) controlled by the printer; this includes the
     * resolution to which sheet-side data is RIPped and the number of printed pels per inch (often called the
     * print-head resolution).
     */
    SupportedDeviceResolutions(0x0026, "Supported Device Resolutions"),

    /**
     * This self-defining field lists the object container versions supported by the printer.
     */
    ObjectContainerVersionSupport(0x0027, "Object-Container Version Support"),

    /**
     * The Finishing Options self-defining field lists all the finishing options that the printer supports with the
     * Finishing Operation (X'85') triplet.
     */
    FinishingOptions(0x0028, "Finishing Options"),

    /**
     * This self-defining field reports the speed of the printer.
     */
    PrinterSpeed(0x0029, "Printer Speed"),

    /**
     * This self-defining field reports the active setup name on the printer, if any.
     */
    ActiveSetupName(0x002A, "Active Setup Name");

    private static final Map<Integer, SelfDefiningFieldId> REVERSE_MAP = new HashMap<>();

    static {
        for (final SelfDefiningFieldId sdf : values()) {
            REVERSE_MAP.put(sdf.getId(), sdf);
        }
    }

    private final int id;
    private final String name;

    /**
     * Constructor of the enum value.
     */
    SelfDefiningFieldId(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the enum value for the given integer.
     *
     * @return the enum value for the given integer.
     */
    public static SelfDefiningFieldId getFor(final int value) throws UnknownSelfDefinedFieldException {
        final SelfDefiningFieldId result = REVERSE_MAP.get(value);
        if (result == null) {
            throw new UnknownSelfDefinedFieldException(
                    String.format("The self-defining field with ID X'%1$s' is unknown.", Integer.toHexString(value)));
        }
        return result;
    }

    /**
     * Gets the integer value of the self-defining field.
     *
     * @return the integer value of the self-defining field.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the name of the self-defining field.
     *
     * @return The name of the self-defining field (i. e. "Printer Setup").
     */
    public String getName() {
        return this.name;
    }
}

