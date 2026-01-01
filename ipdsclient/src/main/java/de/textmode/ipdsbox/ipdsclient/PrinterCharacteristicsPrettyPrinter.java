package de.textmode.ipdsbox.ipdsclient;

import java.io.PrintStream;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.ipds.acknowledge.ObtainPrinterCharacteristicsAcknowledgeData;
import de.textmode.ipdsbox.ipds.sdf.ActivateResourceSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.ActiveSetupNameSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.AvailableFeaturesSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.BarCodeTypeSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.ColorantIdentificationSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.CommonBarCodeTypeSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.DeactivateFontDeactivationTypesSupportedSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.DeviceAppearanceSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.FinishingOperationsSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.FinishingOptionsSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.ImImageAndCodedFontResolutionSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.InstalledFeaturesSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.KeepGroupTogetherSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.MediaDestinationsSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.MediumModificationIdsSupportedSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.ObjectContainerTypeSupportSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.ObjectContainerVersionSupportSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.PfcTripletsSupportedSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.PrintQualitySupportSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.PrintableAreaSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.PrinterSetupSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.PrinterSpeedSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.ProductIdentifierSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.RecognizedGroupIdFormatsSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.ResidentSymbolSetSupportSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.SelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.SelfDefiningFieldVisitor;
import de.textmode.ipdsbox.ipds.sdf.StandardOcaColorValueSupportSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.StoragePoolsSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.SupportedDeviceResolutionsSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.SupportedGroupOperationsSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.SymbolSetSupportSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.UnknownSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.Up3iPaperInputMediaSelfDefiningField;
import de.textmode.ipdsbox.ipds.sdf.Up3iTupelSelfDefiningField;

/**
 * The {@link PrinterCharacteristicsPrettyPrinter} prints out the information within a
 * {@link ObtainPrinterCharacteristicsAcknowledgeData} in a nice readable format.
 */
final class PrinterCharacteristicsPrettyPrinter implements SelfDefiningFieldVisitor {

    private static final String INDENTION = "  ";

    private final ObtainPrinterCharacteristicsAcknowledgeData data;
    private final PrintStream out;

    /**
     * Constructor of this pretty printer.
     */
    PrinterCharacteristicsPrettyPrinter(final ObtainPrinterCharacteristicsAcknowledgeData data) {
        this.data = data;
        this.out = System.out;
    }

    /**
     * Constructor of this pretty printer.
     */
    PrinterCharacteristicsPrettyPrinter(
            final ObtainPrinterCharacteristicsAcknowledgeData data,
            final PrintStream out) {
        this.data = data;
        this.out = out;
    }

    /**
     * Pretty prints the printer characteristics.
     */
    void print() {
        for (final SelfDefiningField sdf : this.data.getSelfDefiningField()) {
            sdf.accept(this);
            this.out.println();
        }
    }

    /**
     * Prints a numeric value (decimal).
     */
    private void printField(final int level, final int width, final int value, final String name) {
        this.printFieldName(level, width, name);
        this.out.println("" + value);
    }

    /**
     * Prints a numeric value in decimal and hex.
     */
    private void printFieldWithHex(final int level, final int width, final int value, final String name) {
        this.printFieldName(level, width, name);

        final String hex = Integer.toHexString(value);
        if ((hex.length() & 1) == 0) {
            this.out.println("0x" + hex + " (" + value + ")");
        } else {
            this.out.println("0x0" + hex + " (" + value + ")");
        }
    }

    /**
     * Prints a numeric value (hex).
     */
    private void printFieldAsHex(final int level, final int width, final int value, final String name) {
        this.printFieldName(level, width, name);

        final String hex = Integer.toHexString(value);
        if ((hex.length() & 1) == 0) {
            this.out.println("0x" + hex);
        } else {
            this.out.println("0x0" + hex);
        }
    }

    /**
     * Prints a byte array as hex.
     */
    private void printFieldAsHex(final int level, final int width, final byte[] value, final String name) {
        this.printFieldName(level, width, name);
        this.out.println(StringUtils.toHexString(value));
    }

    /**
     * Prints the field name.
     */
    private void printFieldName(final int level, final int width, final String name) {
        this.indent(level);
        this.out.print(StringUtils.padRight(name, width));
        this.out.print(" : ");
    }


    /**
     * Prints the indention.
     */
    private void indent(final int level) {
        for (int ix = 0; ix < level; ix++) {
            this.out.print(INDENTION);
        }
    }

    @Override
    public void handle(final ActivateResourceSelfDefiningField sdf) {
        this.out.println("Activate Resource");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final ActiveSetupNameSelfDefiningField sdf) {
        this.out.println("Activate Setup");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final AvailableFeaturesSelfDefiningField sdf) {
        this.out.println("Available Features");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final BarCodeTypeSelfDefiningField sdf) {
        this.out.println("Bar Code Type");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final ColorantIdentificationSelfDefiningField sdf) {
        this.out.println("Colorant Identification");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final CommonBarCodeTypeSelfDefiningField sdf) {
        this.out.println("Common Bar Code Type");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final DeactivateFontDeactivationTypesSupportedSelfDefiningField sdf) {
        this.out.println("Deactivate Font Deactivation Types");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final DeviceAppearanceSelfDefiningField sdf) {
        this.out.println("Device Appearance");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField sdf) {
        this.out.println("XOA-RRL RT & RIDF Support");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final FinishingOperationsSelfDefiningField sdf) {
        this.out.println("Finishing Operations");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final FinishingOptionsSelfDefiningField sdf) {
        this.out.println("Finishing Options");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final ImImageAndCodedFontResolutionSelfDefiningField sdf) {
        this.out.println("IM-Image and Coded-Font Resolution");

        final int width = 20;

        this.printFieldAsHex(1, width, sdf.getUnitBase(), "Unit Base");
        this.printFieldAsHex(1, width, sdf.getFontResolutions(), "Font Resolutions");
        this.printFieldWithHex(1, width, sdf.getXPels(), "X pels per unit base");
        this.printFieldWithHex(1, width, sdf.getYPels(), "Y pels per unit base");
    }

    @Override
    public void handle(final InstalledFeaturesSelfDefiningField sdf) {
        this.out.println("Installed Features");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final KeepGroupTogetherSelfDefiningField sdf) {
        this.out.println("Keep Group Together");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final MediaDestinationsSelfDefiningField sdf) {
        this.out.println("Media Destinations");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final MediumModificationIdsSupportedSelfDefiningField sdf) {
        this.out.println("Medium Modification IDs");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final ObjectContainerTypeSupportSelfDefiningField sdf) {
        this.out.println("Object Container Type");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final ObjectContainerVersionSupportSelfDefiningField sdf) {
        this.out.println("Object Container Version");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final PfcTripletsSupportedSelfDefiningField sdf) {
        this.out.println("PFC Triplets");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    @SuppressWarnings("checkstyle:LineLength")
    public void handle(final PrintableAreaSelfDefiningField sdf) {
        this.out.println("Printable Area");

        final int width = 41;

        this.printFieldWithHex(1, width, sdf.getMediaSourceId(), "Media Source ID");
        this.printFieldAsHex(1, width, sdf.getUnitBase(), "Unit Base");
        this.printFieldWithHex(1, width, sdf.getUpub(), "Units per unit base");
        this.printFieldWithHex(1, width, sdf.getActualMediumPresentationSpaceWidth(), "Actual medium presentation space width");
        this.printFieldWithHex(1, width, sdf.getActualMediumPresentationSpaceLength(), "Actual medium presentation space length");
        this.printFieldWithHex(1, width, sdf.getXmPPAOffset(), "Xm offset of the physical printable media");
        this.printFieldWithHex(1, width, sdf.getYmPPAOffset(), "Ym offset of the physical printable media");
        this.printFieldWithHex(1, width, sdf.getXmPPAExtent(), "Xm extent of the physical printable media");
        this.printFieldWithHex(1, width, sdf.getYmPPAExtent(), "Ym extent of the physical printable media");
        this.printFieldAsHex(1, width, sdf.getInputMediaSourceCharacteristicFlags(), "Input media source characteristic flags");

        for (final PrintableAreaSelfDefiningField.MediaIdEntry entry : sdf.getMediaIdEntries()) {
            this.out.println();

            this.printFieldAsHex(2, width, entry.getMediaIdType(), "Media ID type");
            this.printFieldAsHex(2, width, entry.getMediaId(), "Input media ID");
        }

        // TODO: Print Media ID entries....
    }

    @Override
    public void handle(final PrinterSetupSelfDefiningField sdf) {
        this.out.println("Printer Setup");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final PrinterSpeedSelfDefiningField sdf) {
        this.out.println("Printer Speed");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final PrintQualitySupportSelfDefiningField sdf) {
        this.out.println("Print Quality");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final ProductIdentifierSelfDefiningField sdf) {
        this.out.println("Product Identifier");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final RecognizedGroupIdFormatsSelfDefiningField sdf) {
        this.out.println("Recognized Group ID Formats");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final ResidentSymbolSetSupportSelfDefiningField sdf) {
        this.out.println("Resident Symbol Set");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final StandardOcaColorValueSupportSelfDefiningField sdf) {
        this.out.println("Standard OCA Color Value");

        for (final Integer colorValue : sdf.getColorValues()) {
            // TODO: print OCA color ("black", "cyan", etc...)
            this.printFieldAsHex(1, 15, colorValue, "OCA color value");
        }
    }

    @Override
    public void handle(final StoragePoolsSelfDefiningField sdf) {
        this.out.println("Storage Pools");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final SupportedDeviceResolutionsSelfDefiningField sdf) {
        this.out.println("Supported Device Resolutions");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final SupportedGroupOperationsSelfDefiningField sdf) {
        this.out.println("Supported Group Operations");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final SymbolSetSupportSelfDefiningField sdf) {
        this.out.println("Symbol Set");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final UnknownSelfDefiningField sdf) {
        this.out.println("Unknown");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final Up3iPaperInputMediaSelfDefiningField sdf) {
        this.out.println("UP3I Paper Input");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final Up3iTupelSelfDefiningField sdf) {
        this.out.println("UP3I Tupel");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }
}
