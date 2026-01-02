package de.textmode.ipdsbox.ipdsclient;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
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
import de.textmode.ipdsbox.ipds.triplets.TripletId;

/**
 * The {@link PrinterCharacteristicsPrettyPrinter} prints out the information within a
 * {@link ObtainPrinterCharacteristicsAcknowledgeData} in a nice readable format.
 */
@SuppressWarnings("checkstyle:LineLength")
final class PrinterCharacteristicsPrettyPrinter implements SelfDefiningFieldVisitor {

    private static final Charset EBCDIC = Charset.forName("ibm-500");

    private static final HashMap<Integer, String> GROUP_OPERATIONS = new HashMap<>();

    static {
        GROUP_OPERATIONS.put(Integer.valueOf(0x01), "Keep group together as a print unit");
        GROUP_OPERATIONS.put(Integer.valueOf(0x02), "Keep group together for microfilm output");
        GROUP_OPERATIONS.put(Integer.valueOf(0x03), "Save pages");
        GROUP_OPERATIONS.put(Integer.valueOf(0x04), "Finish");
        GROUP_OPERATIONS.put(Integer.valueOf(0x05), "Identify named group");
        GROUP_OPERATIONS.put(Integer.valueOf(0x06), "Keep group together as a recovery unit");
    }

    private static final HashMap<Integer, String> RESOURCE_TYPE = new HashMap<>();

    static {
        RESOURCE_TYPE.put(Integer.valueOf(0x01), "Single-byte LF1-type and LF2-type coded font");
        RESOURCE_TYPE.put(Integer.valueOf(0x02), "Double-byte LF1-type coded font");
        RESOURCE_TYPE.put(Integer.valueOf(0x03), "Double-byte LF1-type coded-font section");
        RESOURCE_TYPE.put(Integer.valueOf(0x04), "Page segment");
        RESOURCE_TYPE.put(Integer.valueOf(0x05), "Overlay");
        RESOURCE_TYPE.put(Integer.valueOf(0x06), "Device-version code page");
        RESOURCE_TYPE.put(Integer.valueOf(0x07), "Font character set");
        RESOURCE_TYPE.put(Integer.valueOf(0x08), "Single-byte coded font index");
        RESOURCE_TYPE.put(Integer.valueOf(0x09), "Double-byte coded font section index");
        RESOURCE_TYPE.put(Integer.valueOf(0x10), "Coded font");
        RESOURCE_TYPE.put(Integer.valueOf(0x11), "Graphic character set supported in a font character set");
        RESOURCE_TYPE.put(Integer.valueOf(0x12), "Specific code page");
        RESOURCE_TYPE.put(Integer.valueOf(0x20), "Saved page group");
        RESOURCE_TYPE.put(Integer.valueOf(0x40), "Data object resource");
        RESOURCE_TYPE.put(Integer.valueOf(0x41), "Data-object font");
        RESOURCE_TYPE.put(Integer.valueOf(0x42), "Data-object-font components");
        RESOURCE_TYPE.put(Integer.valueOf(0xFF), "All-resources resource type");
    }

    private static final HashMap<Integer, String> RESOURCE_ID_FORMAT = new HashMap<>();

    static {
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x00), "Host-Assigned Resource ID");
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x03), "GRID-parts format");
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x04), "Remote PrintManager MVS naming format");
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x05), "Extended Remote PrintManager MVS naming format");
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x06), "MVS Host Unalterable Remote Font Environment");
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x07), "Coded-font format");
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x08), "Variable-length Group ID (X'00') triplet");
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x09), "Object-OID format");
        RESOURCE_ID_FORMAT.put(Integer.valueOf(0x0A), "Data-object-font format");
    }


    private static final HashMap<Integer, String> OBJECT_IDS = new HashMap<>();

    static {
        OBJECT_IDS.put(Integer.valueOf(0x0007), "Symbol sets");
        OBJECT_IDS.put(Integer.valueOf(0x0011), "Page graphics data");
        OBJECT_IDS.put(Integer.valueOf(0x0012), "Page image data");
        OBJECT_IDS.put(Integer.valueOf(0x0013), "Page text data");
        OBJECT_IDS.put(Integer.valueOf(0x0014), "Page bar code data");
        OBJECT_IDS.put(Integer.valueOf(0x0021), "Overlay graphics data");
        OBJECT_IDS.put(Integer.valueOf(0x0022), "Overlay image data");
        OBJECT_IDS.put(Integer.valueOf(0x0023), "Overlay text data");
        OBJECT_IDS.put(Integer.valueOf(0x0024), "Overlay bar code data");
        OBJECT_IDS.put(Integer.valueOf(0x0031), "Page segment graphics data");
        OBJECT_IDS.put(Integer.valueOf(0x0032), "Page segment image data");
        OBJECT_IDS.put(Integer.valueOf(0x0033), "Page segment text data");
        OBJECT_IDS.put(Integer.valueOf(0x0034), "Page segment bar code data");
        OBJECT_IDS.put(Integer.valueOf(0x0040), "Single-byte coded-font index tables");
        OBJECT_IDS.put(Integer.valueOf(0x0041), "Single-byte coded-font descriptors");
        OBJECT_IDS.put(Integer.valueOf(0x0042), "Single-byte coded-font patterns");
        OBJECT_IDS.put(Integer.valueOf(0x0048), "Double-byte coded-font index tables");
        OBJECT_IDS.put(Integer.valueOf(0x0049), "Double-byte coded-font descriptors");
        OBJECT_IDS.put(Integer.valueOf(0x004A), "Double-byte coded-font patterns");
        OBJECT_IDS.put(Integer.valueOf(0x0050), "Code pages");
        OBJECT_IDS.put(Integer.valueOf(0x0060), "Font character sets");
        OBJECT_IDS.put(Integer.valueOf(0x0070), "Coded fonts");
    }

    private static final HashMap<Integer, String> OCA_COLORS = new HashMap<>();

    static {
        OCA_COLORS.put(Integer.valueOf(0x0001), "Blue");
        OCA_COLORS.put(Integer.valueOf(0x0002), "Red");
        OCA_COLORS.put(Integer.valueOf(0x0003), "Pink/magenta");
        OCA_COLORS.put(Integer.valueOf(0x0004), "Green");
        OCA_COLORS.put(Integer.valueOf(0x0005), "Turquoise/cyan");
        OCA_COLORS.put(Integer.valueOf(0x0006), "Yellow");
        OCA_COLORS.put(Integer.valueOf(0x0007), "White / Color of medium");
        OCA_COLORS.put(Integer.valueOf(0x0008), "Black");
        OCA_COLORS.put(Integer.valueOf(0x0009), "Dark blue");
        OCA_COLORS.put(Integer.valueOf(0x000A), "Orange");
        OCA_COLORS.put(Integer.valueOf(0x000B), "Purple");
        OCA_COLORS.put(Integer.valueOf(0x000C), "Dark green");
        OCA_COLORS.put(Integer.valueOf(0x000D), "Dark turquoise");
        OCA_COLORS.put(Integer.valueOf(0x000E), "Mustard");
        OCA_COLORS.put(Integer.valueOf(0x000F), "Gray");
        OCA_COLORS.put(Integer.valueOf(0x0010), "Brown");
    }

    private static final HashMap<Integer, String> FEATURES = new HashMap<>();

    static {
        FEATURES.put(Integer.valueOf(0x0100), "Duplex");
        FEATURES.put(Integer.valueOf(0x0200), "Manual two-channel switch");
        FEATURES.put(Integer.valueOf(0x0201), "Tightly coupled two-channel switch");
        FEATURES.put(Integer.valueOf(0x0202), "Retired item 39");
        FEATURES.put(Integer.valueOf(0x0300), "Cut-sheet output");
        FEATURES.put(Integer.valueOf(0x0400), "Retired item 40");
        FEATURES.put(Integer.valueOf(0x0600), "Offset stacker");
        FEATURES.put(Integer.valueOf(0x0700), "Envelopes");
        FEATURES.put(Integer.valueOf(0x0800), "MICR: capable of printing toned pels that are impregnated with a magnetic material");
        FEATURES.put(Integer.valueOf(0x0900), "Burster-trimmer-stacker or cutter-trimmer-stacker");
        FEATURES.put(Integer.valueOf(0x0B00), "Continuous-forms output");
        FEATURES.put(Integer.valueOf(0x0C00), "Continuous-forms separation capability");
        FEATURES.put(Integer.valueOf(0x0D00), "PTOCA text decryption capability");
    }

    private static final HashMap<Integer, String> COMMON_BAR_CODE_TYPE = new HashMap<>();

    static {
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x0D), "Codabar, modifier-byte options X'01' and X'02'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x11), "Code 128, modifier-byte option X'02'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x18), "POSTNET (deprecated), modifier-byte options X'00' through X'03'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x1A), "RM4SCC, modifier-byte option X'00'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x1B), "Japan Postal Bar Code, modifier-byte options X'00' and X'01'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x1C), "Data Matrix, modifier-byte option X'00'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x1D), "MaxiCode, modifier-byte option X'00'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x1E), "PDF417, modifier-byte options X'00' and X'01'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x1F), "Australia Post Bar Code, modifier-byte options X'01' through X'08'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x20), "QR Code, modifier-byte option X'02'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x21), "Code 93, modifier-byte option X'00'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x22), "Intelligent Mail Barcode, modifier-byte options X'00' through X'03'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x23), "Royal Mail RED TAG (deprecated), modifier-byte option X'00'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x24), "GS1 DataBar, modifier-byte options X'00' through X'04' and X'11' through X'1B'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x86), "UPC-Two-digit Supplemental, modifier-byte options X'01' and X'02'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x87), "UPC_Five-digit Supplemental, modifier-byte options X'01' and X'02'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x8C), "Interleaved 2-of-5, modifier-byte options X'03' and X'04'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x91), "Code 128, modifier-byte option X'03'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x92), "Code 128, modifier-byte option X'04'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x93), "Code 128 (Intelligent Mail Container Barcode), modifier-byte option X'05'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x96), "EAN Two-digit Supplemental, modifier-byte option X'01'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x97), "EAN Five-digit Supplemental, modifier-byte option X'01'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x98), "POSTNET (PLANET, deprecated), modifier-byte option X'04'");
        COMMON_BAR_CODE_TYPE.put(Integer.valueOf(0x9A), "RM4SCC, modifier-byte option X'01'");
    }

    private static final HashMap<Integer, String> BAR_CODE_TYPE = new HashMap<>();

    static {
        BAR_CODE_TYPE.put(Integer.valueOf(0x06), "UPC - Two-digit Supplemental");
        BAR_CODE_TYPE.put(Integer.valueOf(0x07), "UPC - Five-digit Supplemental");
        BAR_CODE_TYPE.put(Integer.valueOf(0x0A), "Industrial 2-of-5");
        BAR_CODE_TYPE.put(Integer.valueOf(0x0B), "Matrix 2-of-5");
        BAR_CODE_TYPE.put(Integer.valueOf(0x0C), "Interleaved 2-of-5, ITF-14, and AIM USS-I 2/5");
        BAR_CODE_TYPE.put(Integer.valueOf(0x0D), "Codabar, 2-of-7 and AIM USS-Codabar");
        BAR_CODE_TYPE.put(Integer.valueOf(0x11), "Code 128 – GS1-128, UCC/EAN 128, Intelligent Mail Container Barcode, Intelligent Mail Package Barcode, and AIM USS-128");
        BAR_CODE_TYPE.put(Integer.valueOf(0x16), "EAN Two-digit Supplemental");
        BAR_CODE_TYPE.put(Integer.valueOf(0x17), "EAN Five-digit Supplemental");
        BAR_CODE_TYPE.put(Integer.valueOf(0x18), "POSTNET and PLANET (deprecated)");
        BAR_CODE_TYPE.put(Integer.valueOf(0x1A), "RM4SCC and Dutch KIX");
        BAR_CODE_TYPE.put(Integer.valueOf(0x1B), "Japan Postal Bar Code");
        BAR_CODE_TYPE.put(Integer.valueOf(0x1C), "Data Matrix (2D bar code)");
        BAR_CODE_TYPE.put(Integer.valueOf(0x1D), "MaxiCode (2D bar code)");
        BAR_CODE_TYPE.put(Integer.valueOf(0x1E), "PDF417 (2D bar code)");
        BAR_CODE_TYPE.put(Integer.valueOf(0x1F), "Australia Post Bar Code");
        BAR_CODE_TYPE.put(Integer.valueOf(0x20), "QR Code, QR Code with Image (2D bar code)");
        BAR_CODE_TYPE.put(Integer.valueOf(0x21), "Code 93");
        BAR_CODE_TYPE.put(Integer.valueOf(0x22), "Intelligent Mail Barcode");
        BAR_CODE_TYPE.put(Integer.valueOf(0x23), "Royal Mail RED TAG (deprecated)");
        BAR_CODE_TYPE.put(Integer.valueOf(0x24), "GS1 DataBar");
        BAR_CODE_TYPE.put(Integer.valueOf(0x25), "Royal Mail Mailmark");
        BAR_CODE_TYPE.put(Integer.valueOf(0x26), "Aztec Code");
    }

    private static final HashMap<String, String> MEDIA_TYPES = new HashMap<>();

    static {
        MEDIA_TYPES.put("06072B120004030100", "ISO A4 white (210 x 297 mm)");
        MEDIA_TYPES.put("06072B120004030101", "ISO A4 colored");
        MEDIA_TYPES.put("06072B120004030102", "ISO A4 transparent");
        MEDIA_TYPES.put("06072B120004030105", "ISO 1/3 A4");
        MEDIA_TYPES.put("06072B120004030107", "ISO A4 tab (225 x 297 mm)");
        MEDIA_TYPES.put("06072B12000403010A", "ISO A3 white (297 x 420 mm)");
        MEDIA_TYPES.put("06072B12000403010B", "ISO A3 colored");
        MEDIA_TYPES.put("06072B120004030114", "ISO A5 white (148.5 x 210 mm)");
        MEDIA_TYPES.put("06072B120004030115", "ISO A5 colored");
        MEDIA_TYPES.put("06072B12000403011E", "ISO B4 white (250 x 353 mm)");
        MEDIA_TYPES.put("06072B12000403011F", "ISO B4 colored");
        MEDIA_TYPES.put("06072B120004030128", "ISO B5 white (176 x 250 mm)");
        MEDIA_TYPES.put("06072B120004030129", "ISO B5 colored");
        MEDIA_TYPES.put("06072B12000403012A", "JIS B4 (257 x 364 mm)");
        MEDIA_TYPES.put("06072B12000403012B", "JIS B5 (182 x 257 mm)");
        MEDIA_TYPES.put("06072B120004030132", "North American letter white (8.5 x 11 inch)");
        MEDIA_TYPES.put("06072B120004030133", "North American letter colored");
        MEDIA_TYPES.put("06072B120004030134", "North American letter transparent");
        MEDIA_TYPES.put("06072B12000403013C", "North American legal white (8.5 x 14 inch)");
        MEDIA_TYPES.put("06072B12000403013D", "North American legal colored");
        MEDIA_TYPES.put("06072B12000403013F", "North American legal 13 (Folio) (8.5 x 13 inch)");
        MEDIA_TYPES.put("06072B120004030141", "North American executive (7.25 x 10.5 inch)");
        MEDIA_TYPES.put("06072B120004030143", "North American ledger (11 x 17 inch)");
        MEDIA_TYPES.put("06072B120004030145", "North American statement (5.5 x 8.5 inch)");
        MEDIA_TYPES.put("06072B120004030149", "ISO B5 envelope (176 x 250 mm)");
        MEDIA_TYPES.put("06072B12000403014B", "Com10 envelope (9.5 x 4.125 inch)");
        MEDIA_TYPES.put("06072B12000403014C", "Monarch envelope (7.5 x 3.875 inch)");
        MEDIA_TYPES.put("06072B12000403014D", "DL envelope (220 x 110 mm)");
        MEDIA_TYPES.put("06072B12000403014F", "C5 envelope (229 x 162 mm)");
        MEDIA_TYPES.put("06072B120004030150", "Japan postcard envelope (200 x 150 mm)");
        MEDIA_TYPES.put("06072B120004030151", "Japan postcard (Hagaki) (100 x 148 mm)");
        MEDIA_TYPES.put("06072B120004030153", "ISO B4 envelope (250 x 353 mm)");
        MEDIA_TYPES.put("06072B12000403015D", "ISO C4 envelope (229 x 324 mm)");
        MEDIA_TYPES.put("06072B120004030167", "ISO C5 envelope (162 x 229 mm)");
        MEDIA_TYPES.put("06072B120004030171", "ISO long envelope");
        MEDIA_TYPES.put("06072B12000403017B", "North American 10 x 13 envelope");
        MEDIA_TYPES.put("06082B12000403018105", "North American 9 x 12 envelope");
        MEDIA_TYPES.put("06082B1200040301810F", "North American business envelope (9.5 x 4.125 inch)");
        MEDIA_TYPES.put("06082B12000403018111", "Letter tab (9 x 11 inch)");
        MEDIA_TYPES.put("06082B12000403018112", "Legal tab (9 x 14 inch)");
        MEDIA_TYPES.put("06082B12000403018113", "Manual (9 x 12 inch)");
        MEDIA_TYPES.put("06082B12000403018114", "Media (8 x 10.5 inch)");
        MEDIA_TYPES.put("06082B12000403018115", "Media (9 x 14 inch)");
        MEDIA_TYPES.put("06082B12000403018116", "Index Card");
        MEDIA_TYPES.put("06082B12000403018117", "US Postcard");
        MEDIA_TYPES.put("06082B12000403018118", "ISO A6 Postcard (105 x 148 mm)");
        MEDIA_TYPES.put("06082B12000403018119", "Oversize A3 (16.923 x 12.007 inch)");
        MEDIA_TYPES.put("06082B1200040301811A", "Media (14 x 17 inch)");
        MEDIA_TYPES.put("06082B1200040301811B", "Media (12 x 18 inch)");
        MEDIA_TYPES.put("06082B1200040301811C", "Media (14 x 18 inch)");
        MEDIA_TYPES.put("06082B1200040301811D", "Media (8.5 x 10 inch)");
        MEDIA_TYPES.put("06082B12000403018120", "Media (8 x 10 inch)");
        MEDIA_TYPES.put("06082B12000403018122", "Oversize A4 (8.465 x 12.007 inch)");
        MEDIA_TYPES.put("06082B12000403018123", "Media (8 x 13 in)");
        MEDIA_TYPES.put("06082B12000403018124", "Media (8.25 x 13 in)");
        MEDIA_TYPES.put("06082B12000403018125", "Media (8.25 x 14 in)");
        MEDIA_TYPES.put("06082B12000403018126", "Media (8.5 x 12.4 in)");
        MEDIA_TYPES.put("06082B12000403018127", "Media (10 x 14 in)");
        MEDIA_TYPES.put("06082B12000403018128", "Media (10 x 15 in)");
        MEDIA_TYPES.put("06082B12000403018129", "Media (11 x 14 in)");
        MEDIA_TYPES.put("06082B1200040301812A", "Media (11 x 15 in)");
        MEDIA_TYPES.put("06082B1200040301812B", "ISO B6 (128 x 182 mm)");
        MEDIA_TYPES.put("06082B1200040301812C", "Reply-paid PC (148 x 200 mm)");
        MEDIA_TYPES.put("06082B1200040301812D", "Media (170 x 210 mm)");
        MEDIA_TYPES.put("06082B1200040301812E", "Media (182 x 210 mm)");
        MEDIA_TYPES.put("06082B1200040301812F", "Media (210 x 340 mm)");
        MEDIA_TYPES.put("06082B12000403018130", "8KAI Media (267 x 390 mm)");
        MEDIA_TYPES.put("06082B12000403018131", "16KAI Media (195 x 267 mm)");
    }

    private static final HashMap<String, String> OBJECT_CONTAINER_TYPES = new HashMap<>();

    static {
        OBJECT_CONTAINER_TYPES.put("06072B12000401010500000000000000", "IOCA FS10");
        OBJECT_CONTAINER_TYPES.put("06072B12000401010B00000000000000", "IOCA FS11");
        OBJECT_CONTAINER_TYPES.put("06072B12000401010C00000000000000", "IOCA FS45");
        OBJECT_CONTAINER_TYPES.put("06072B12000401010D00000000000000", "EPS");
        OBJECT_CONTAINER_TYPES.put("06072B12000401010E00000000000000", "TIFF");
        OBJECT_CONTAINER_TYPES.put("06072B12000401010F00000000000000", "COM Set-up");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011000000000000000", "Tape Label Set-up");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011100000000000000", "DIB, Windows Version");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011200000000000000", "DIB, OS/2 PM Version");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011300000000000000", "PCX");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011400000000000000", "Color Mapping Table (CMT)");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011600000000000000", "GIF");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011700000000000000", "AFPC JPEG Subset");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011800000000000000", "AnaStak Control Record");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011900000000000000", "PDF Single-page Object");
        OBJECT_CONTAINER_TYPES.put("06072B12000401011A00000000000000", "PDF Resource Object");
        OBJECT_CONTAINER_TYPES.put("06072B12000401012200000000000000", "PCL Page Object");
        OBJECT_CONTAINER_TYPES.put("06072B12000401012D00000000000000", "IOCA FS42");
        OBJECT_CONTAINER_TYPES.put("06072B12000401012E00000000000000", "Resident Color Profile");
        OBJECT_CONTAINER_TYPES.put("06072B12000401012F00000000000000", "IOCA FS45 Tile Resource");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013000000000000000", "EPS with Transparency");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013100000000000000", "PDF with Transparency");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013300000000000000", "TrueType/OpenType Font");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013500000000000000", "TrueType/OpenType Font Collection");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013600000000000000", "Resource Access Table");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013700000000000000", "IOCA FS40");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013800000000000000", "UP3i Print Data");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013900000000000000", "Color Management Resource (CMR)");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013A00000000000000", "JPEG2000 (JP2) File Format");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013C00000000000000", "TIFF without Transparency");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013D00000000000000", "TIFF Multiple Image File");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013E00000000000000", "TIFF Multiple Image - without Transparency - File");
        OBJECT_CONTAINER_TYPES.put("06072B12000401013F00000000000000", "PDF Multiple Page File");
        OBJECT_CONTAINER_TYPES.put("06072B12000401014000000000000000", "PDF Multiple Page - with Transparency - File");
        OBJECT_CONTAINER_TYPES.put("06072B12000401014100000000000000", "AFPC PNG Subset");
        OBJECT_CONTAINER_TYPES.put("06072B12000401014200000000000000", "AFPC TIFF Subset");
        OBJECT_CONTAINER_TYPES.put("06072B12000401014300000000000000", "Metadata Object");
        OBJECT_CONTAINER_TYPES.put("06072B12000401014400000000000000", "AFPC SVG Subset");
        OBJECT_CONTAINER_TYPES.put("06072B12000401014500000000000000", "Non-OCA Resource Object");
    }

    private static final HashMap<Integer, String> FONT_DEACTIVATION_TYPES = new HashMap<>();

    static {
        FONT_DEACTIVATION_TYPES.put(Integer.valueOf(0x22), "Deactivate a font index for a double-byte coded font section");
        FONT_DEACTIVATION_TYPES.put(Integer.valueOf(0x50), "Deactivate a coded font");
        FONT_DEACTIVATION_TYPES.put(Integer.valueOf(0x51), "Deactivate a coded font and all associated components");
        FONT_DEACTIVATION_TYPES.put(Integer.valueOf(0x5D), "Deactivate all resident coded fonts and all associated components");
        FONT_DEACTIVATION_TYPES.put(Integer.valueOf(0x5E), "Deactivate all coded fonts");
        FONT_DEACTIVATION_TYPES.put(Integer.valueOf(0x5F), "Deactivate all coded fonts and all associated components");
    }

    private static final HashMap<Integer, String> FINISHING_OPERATIONS = new HashMap<>();

    static {
        FINISHING_OPERATIONS.put(Integer.valueOf(0x01), "Corner staple");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x02), "Saddle-stitch out");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x03), "Edge stitch");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x04), "Fold in");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x05), "Separation cut");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x06), "Perforation cut");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x07), "Z fold");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x08), "Center-fold in");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x09), "Trim after center fold or saddle stitch");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x0A), "Punch");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x0C), "Perfect bind");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x0D), "Ring bind");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x0E), "C-fold in");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x0F), "Accordion-fold in");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x12), "Saddle-stitch in");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x14), "Fold out");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x18), "Center-fold out");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x19), "Trim");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x1E), "C-fold out");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x1F), "Accordion-fold out");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x20), "Double parallel-fold in");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x21), "Double gate-fold in");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x22), "Single gate-fold in");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x30), "Double parallel-fold out");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x31), "Double gate-fold out");
        FINISHING_OPERATIONS.put(Integer.valueOf(0x32), "Single gate-fold out");
    }


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
     * Prints a string value.
     */
    private void printField(final int level, final int width, final String value, final String name) {
        this.printFieldName(level, width, name);
        this.out.println(value);
    }

    /**
     * Prints a numeric value (decimal).
     */
    private void printField(final int level, final int width, final long value, final String name) {
        this.printFieldName(level, width, name);
        this.out.println("" + value);
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
     * Prints a numeric value (hex) together with a hint.
     */
    private void printFieldAsHex(
            final int level,
            final int width,
            final int value,
            final String hint,
            final String name) {

        this.printFieldName(level, width, name);

        final String hex = Integer.toHexString(value);
        if ((hex.length() & 1) == 0) {
            this.out.println("0x" + hex + " (" + hint + ")");
        } else {
            this.out.println("0x0" + hex + " (" + hint + ")");
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
     * Prints a byte array as hex together with a hint.
     */
    private void printFieldAsHex(final int level, final int width, final byte[] value, final String hint, final String name) {
        this.printFieldName(level, width, name);
        this.out.println(StringUtils.toHexString(value) + " (" + hint + ")");
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

        for (final ActivateResourceSelfDefiningField.ActivateResourceEntry entry : sdf.getEntries()) {
            this.out.println();
            final String rt = RESOURCE_TYPE.get(entry.getResourceType());
            final String rtHint = rt == null ? "" : rt;
            this.printFieldAsHex(1, 18, entry.getResourceType(), rtHint, "Resource type");

            final String ridf = RESOURCE_ID_FORMAT.get(entry.getResourceIdFormat());
            final String ridfHint = rt == null ? "" : ridf;
            this.printFieldAsHex(1, 18, entry.getResourceIdFormat(), ridfHint, "Resource ID format");
        }
    }

    @Override
    public void handle(final ActiveSetupNameSelfDefiningField sdf) {
        this.out.println("Active Setup");
        this.printField(1, 10, sdf.getActiveSetupName() == null ? "none" : sdf.getActiveSetupName().getSetupName(), "Setup name");
    }

    @Override
    public void handle(final AvailableFeaturesSelfDefiningField sdf) {
        this.out.println("Available Features");

        for (final Integer featureId : sdf.getFeatureIds()) {
            final String feature = FEATURES.get(featureId);
            final String hint = feature == null ? "unknown" : feature;

            this.printFieldAsHex(1, 10, featureId, hint, "Feature ID");
        }
    }

    @Override
    public void handle(final BarCodeTypeSelfDefiningField sdf) {
        this.out.println("Bar Code Type");

        if (sdf.getBcocaSubset() == 0xFF10) {
            this.printFieldAsHex(1, 12, sdf.getBcocaSubset(), "BCD1", "BCOCA subset");
        } else if (sdf.getBcocaSubset() == 0xFF20) {
            this.printFieldAsHex(1, 12, sdf.getBcocaSubset(), "BCD2", "BCOCA subset");
        } else {
            this.printFieldAsHex(1, 12, sdf.getBcocaSubset(), "unknown", "BCOCA subset");
        }

        for (final BarCodeTypeSelfDefiningField.BarCodeEntry entry : sdf.getEntries()) {
            final String type = BAR_CODE_TYPE.get(entry.getBarCodeType());
            final String hint = type == null ? "unknown" : type;

            this.printFieldAsHex(2, 13, entry.getBarCodeType(), hint, "Bar code type");
            this.printFieldAsHex(2, 13, entry.getModifiers(), hint, "Modifier code");
        }

    }

    @Override
    public void handle(final ColorantIdentificationSelfDefiningField sdf) {
        this.out.println("Colorant Identification");

        for (final ColorantIdentificationSelfDefiningField.ColorantIdentificationEntry entry : sdf.getEntries()) {
            this.out.println();

            this.printFieldAsHex(1, 27, entry.getEntryType(), "Entry type");
            this.printFieldAsHex(1, 27, entry.getColorantAvailabilityFlags(), "Colorant availability flags");
            this.printField(1, 27, entry.getColorantName(), "Colorant name");
        }
    }

    @Override
    public void handle(final CommonBarCodeTypeSelfDefiningField sdf) {
        this.out.println("Common Bar Code Type");

        for (final Integer combination : sdf.getCombinations()) {
            final String barCode = COMMON_BAR_CODE_TYPE.get(combination);
            final String hint = barCode == null ? "unknown" : barCode;

            this.printFieldAsHex(1, 11, combination, hint, "Combination");
        }
    }

    @Override
    public void handle(final DeactivateFontDeactivationTypesSupportedSelfDefiningField sdf) {
        this.out.println("Deactivate Font Deactivation Types");

        for (final Integer type : sdf.getTypes()) {
            final String deactivationType = FONT_DEACTIVATION_TYPES.get(type);
            final String hint = deactivationType == null ? "unknown" : deactivationType;
            this.printFieldAsHex(1, 4, type, hint, "Type");
        }
    }

    @Override
    public void handle(final DeviceAppearanceSelfDefiningField sdf) {
        this.out.println("Device Appearance");

        for (final Integer type : sdf.getAppearances()) {
            this.printFieldAsHex(1, 10, type, "Appearance");
        }
    }

    @Override
    public void handle(final ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField sdf) {
        this.out.println("XOA-RRL RT & RIDF Support");
        for (final ExecuteOrderAnystateRequestResidentResourceListSupportSelfDefiningField.ResourceSupportEntry entry : sdf.getEntries()) {
            this.out.println();
            final String rt = RESOURCE_TYPE.get(entry.getResourceType());
            final String rtHint = rt == null ? "" : rt;
            this.printFieldAsHex(1, 18, entry.getResourceType(), rtHint, "Resource type");

            final String ridf = RESOURCE_ID_FORMAT.get(entry.getResourceIdFormat());
            final String ridfHint = rt == null ? "" : ridf;
            this.printFieldAsHex(1, 18, entry.getResourceIdFormat(), ridfHint, "Resource ID format");
        }
    }

    @Override
    public void handle(final FinishingOperationsSelfDefiningField sdf) {
        this.out.println("Finishing Operations");

        for (final Integer type : sdf.getOperationTypes()) {
            final String operationType = FINISHING_OPERATIONS.get(type);
            final String hint = operationType == null ? "unknown" : operationType;

            this.printFieldAsHex(1, 14, type, hint, "Operation type");
        }
    }

    @Override
    public void handle(final FinishingOptionsSelfDefiningField sdf) {
        this.out.println("Finishing Options");

        for (final Integer type : sdf.getOptionTypes()) {
            this.printFieldAsHex(1, 11, type, "Option type");
        }
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

        for (final Integer featureId : sdf.getFeatureIds()) {
            final String color = FEATURES.get(featureId);
            final String hint = color == null ? "unknown" : color;

            this.printFieldAsHex(1, 10, featureId, hint, "Feature ID");
        }
    }

    @Override
    public void handle(final KeepGroupTogetherSelfDefiningField sdf) {
        this.out.println("Keep Group Together");

        final int width = 26;
        this.printFieldAsHex(1, width, sdf.getMaximumNumberOfSheets(), "Maximum number of sheets");
        this.printFieldAsHex(1, width, sdf.getUnitBase(), "Unit base");
        this.printFieldAsHex(1, width, sdf.getUpub(), "Units per unit base");
        this.printFieldAsHex(1, width, sdf.getMaximumTotalGroupLength(), "Maximum total group length");
    }

    @Override
    public void handle(final MediaDestinationsSelfDefiningField sdf) {
        this.out.println("Media Destinations");

        this.printFieldWithHex(1, 10, sdf.getDefaultId(), "Default ID");
        for (final MediaDestinationsSelfDefiningField.MediaDestinationsEntry entry : sdf.getEntries()) {
            this.out.println();
            this.printFieldWithHex(2, 8, entry.getFirst(), "First ID");
            this.printFieldWithHex(2, 8, entry.getLast(), "Last ID");
        }
    }

    @Override
    public void handle(final MediumModificationIdsSupportedSelfDefiningField sdf) {
        this.out.println("Medium Modification IDs");

        for (final Integer mmid : sdf.getMediumModificationIds()) {
            this.printFieldWithHex(1, 22, mmid, "Medium Modification ID");
        }
    }

    @Override
    public void handle(final ObjectContainerTypeSupportSelfDefiningField sdf) {
        this.out.println("Object Container Type");

        for (final ObjectContainerTypeSupportSelfDefiningField.TypeRecord typeRecord : sdf.getTypeRecords()) {
            this.out.println();
            if (typeRecord.getType() == 0x01) {
                this.printFieldAsHex(1, 4, typeRecord.getType(), "Page or overlay state", "Type");
            } else if (typeRecord.getType() == 0x02) {
                this.printFieldAsHex(1, 4, typeRecord.getType(), "Home state", "Type");
            } else {
                this.printFieldAsHex(1, 4, typeRecord.getType(), "Unknown", "Type");
            }

            for (final byte[] regId : typeRecord.getRegIds()) {
                final String objectType = OBJECT_CONTAINER_TYPES.get(StringUtils.toHexString(regId));
                final String hint = objectType == null ? "unknown" : objectType;

                this.printFieldAsHex(2, 27, regId, hint, "MO:DCA-registered object ID");
            }
        }
    }

    @Override
    public void handle(final ObjectContainerVersionSupportSelfDefiningField sdf) {
        this.out.println("Object Container Version");

        final int width = 27;

        for (final ObjectContainerVersionSupportSelfDefiningField.VersionRecord version : sdf.getVersionRecords()) {
            this.out.println();

            final String regId = OBJECT_CONTAINER_TYPES.get(StringUtils.toHexString(version.getRegId()));
            final String hint = regId == null ? "unknown" : regId;

            this.printFieldAsHex(1, width, version.getRegId(), hint, "MO:DCA-registered object ID");
            this.printFieldAsHex(1, width, version.getFlags(), "Flags");
            this.printFieldAsHex(1, width, version.getMajorVersion(), "MKajor version");
            this.printFieldAsHex(1, width, version.getMinorVersion(), "Minor version");
            this.printFieldAsHex(1, width, version.getSubminorVersion(), "Subminor version");
            this.printField(1, width, version.getVersionName(), "Version name");
        }
    }

    @Override
    public void handle(final PfcTripletsSupportedSelfDefiningField sdf) {
        this.out.println("PFC Triplets");

        for (final Integer tripletId : sdf.getTripletIds()) {
            final TripletId triplet = TripletId.getIfKnown(tripletId);
            final String hint = tripletId == null ? "unknown" : triplet.getName();

            this.printFieldAsHex(1, 10, tripletId, hint, "Triplet ID");
        }
    }

    @Override
    public void handle(final PrintableAreaSelfDefiningField sdf) {
        this.out.println("Printable Area");

        final int primaryWidth = 41;
        final int secondaryWidth = 14;

        this.printFieldWithHex(1, primaryWidth, sdf.getMediaSourceId(), "Media Source ID");
        this.printFieldAsHex(1, primaryWidth, sdf.getUnitBase(), "Unit Base");
        this.printFieldWithHex(1, primaryWidth, sdf.getUpub(), "Units per unit base");
        this.printFieldWithHex(1, primaryWidth, sdf.getActualMediumPresentationSpaceWidth(), "Actual medium presentation space width");
        this.printFieldWithHex(1, primaryWidth, sdf.getActualMediumPresentationSpaceLength(), "Actual medium presentation space length");
        this.printFieldWithHex(1, primaryWidth, sdf.getXmPPAOffset(), "Xm offset of the physical printable media");
        this.printFieldWithHex(1, primaryWidth, sdf.getYmPPAOffset(), "Ym offset of the physical printable media");
        this.printFieldWithHex(1, primaryWidth, sdf.getXmPPAExtent(), "Xm extent of the physical printable media");
        this.printFieldWithHex(1, primaryWidth, sdf.getYmPPAExtent(), "Ym extent of the physical printable media");
        this.printFieldAsHex(1, primaryWidth, sdf.getInputMediaSourceCharacteristicFlags(), "Input media source characteristic flags");

        for (final PrintableAreaSelfDefiningField.MediaIdEntry entry : sdf.getMediaIdEntries()) {
            this.out.println();

            if (entry.getMediaIdType() == 0x00) {
                this.printFieldAsHex(2, secondaryWidth, entry.getMediaIdType(), "user defined", "Media ID type");
                this.printFieldAsHex(2, secondaryWidth, entry.getMediaId(), EBCDIC.decode(ByteBuffer.wrap(entry.getMediaId())).toString(), "Input media ID");
            } else if (entry.getMediaIdType() == 0x01) {
                this.printFieldAsHex(2, secondaryWidth, entry.getMediaIdType(), "ISO/DPA registered media value", "Media ID type");
                this.printFieldAsHex(2, secondaryWidth, entry.getMediaId(), EBCDIC.decode(ByteBuffer.wrap(entry.getMediaId())).toString(), "Input media ID");
            } else if (entry.getMediaIdType() == 0x10) {
                this.printFieldAsHex(2, secondaryWidth, entry.getMediaIdType(), "MO:DCA media type OID", "Media ID type");
                final String mediaType = MEDIA_TYPES.get(StringUtils.toHexString(entry.getMediaId()));
                if (mediaType != null) {
                    this.printFieldAsHex(2, secondaryWidth, entry.getMediaId(), mediaType, "Input media ID"); // ????
                } else {
                    this.printFieldAsHex(2, secondaryWidth, entry.getMediaId(), "Input media ID");
                }
            } else {
                this.printFieldAsHex(2, secondaryWidth, entry.getMediaIdType(), "unknown", "Media ID type");
                this.printFieldAsHex(2, secondaryWidth, entry.getMediaId(), "Input media ID");

            }
        }
    }

    @Override
    public void handle(final PrinterSetupSelfDefiningField sdf) {
        this.out.println("Printer Setup");

        for (final Integer id : sdf.getSetupIds()) {
            this.printFieldWithHex(1, 8, id, "Setup ID");
        }
    }

    @Override
    public void handle(final PrinterSpeedSelfDefiningField sdf) {
        this.out.println("Printer Speed");

        this.printField(1, 52, sdf.getPpm(), "Letter-sized pages per minute");
        this.printField(1, 52, sdf.getFpm(), "Number of feet of continuous-forms media per minute");
    }

    @Override
    public void handle(final PrintQualitySupportSelfDefiningField sdf) {
        this.out.println("Print Quality");

        for (final Integer boundary : sdf.getBoundaries()) {
            this.printFieldWithHex(1, 8, boundary, "Boundary");
        }
    }

    @Override
    public void handle(final ProductIdentifierSelfDefiningField sdf) {
        this.out.println("Product Identifier");

        final int width = 12;
        for (final ProductIdentifierSelfDefiningField.ProductIdentifierEntry entry : sdf.getEntries()) {
            this.out.println();

            // Retired item 50 - There is no parameter value for this parameter ID (says the IPDS spec).
            if (entry.getParameterId() == 0x0000) {
                this.printFieldAsHex(1, width, entry.getParameterId(), "Parameter ID");
                this.printFieldAsHex(1, width, entry.getParameterValue(), "Parameter value");
                continue;
            }

            // Unique Product Identifier
            if (entry.getParameterId() == 0x0001) {
                this.printFieldAsHex(1, width, entry.getParameterId(), "Unique Product Identifier", "Parameter ID");
                this.printFieldAsHex(1, width, entry.getParameterValue(), "Parameter value");

                final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(entry.getParameterValue());
                final int secondaryWidth = 24;
                try {
                    this.printField(2, secondaryWidth, is.readEbcdicString(6), "Device type");
                    this.printField(2, secondaryWidth, is.readEbcdicString(3), "Model number");
                    this.printField(2, secondaryWidth, is.readEbcdicString(3), "Manufacturer");

                    // Plant of manufacture is 0x0000 if not available...
                    if (is.readUnsignedInteger16() != 0x0000) {
                        is.rewind(2);
                        this.printField(2, secondaryWidth, is.readEbcdicString(2), "Plant of manufacture");
                    }

                    this.printField(2, secondaryWidth, is.readEbcdicString(12), "Sequence number");
                    this.printFieldAsHex(2, secondaryWidth, is.readUnsignedInteger16(), "Tag");
                    this.printField(2, secondaryWidth, is.readEbcdicString(9), "Engineering change level");

                    if (is.bytesAvailable() > 0) {
                        this.printFieldAsHex(2, width, is.readRemainingBytes(), "Device-specific information");
                    }
                } catch (final IOException e) {
                    // Ignore...
                }

                continue;
            }

            // IPDS Intermediate Device Identifier
            if (entry.getParameterId() == 0x0002) {
                this.printFieldAsHex(1, width, entry.getParameterId(), "IPDS Intermediate Device Identifier", "Parameter ID");
                this.printFieldAsHex(1, width, entry.getParameterValue(), "Parameter value");

                final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(entry.getParameterValue());
                final int secondaryWidth = 40;
                try {
                    this.printFieldAsHex(2, secondaryWidth, is.readUnsignedInteger16(), "Intermediate-device characteristic flags");
                    this.printFieldAsHex(2, secondaryWidth, is.readUnsignedInteger16(), "Device type");
                    this.printField(2, secondaryWidth, is.readEbcdicString(9), "Engineering change level");
                    this.printFieldAsHex(2, secondaryWidth, is.readUnsignedByte(), "Ordering parameter");
                    this.printFieldAsHex(2, secondaryWidth, is.readRemainingBytes(), "Device-specific information");
                } catch (final IOException e) {
                    // Ignore...
                }

                continue;
            }

            // Printer name
            if (entry.getParameterId() == 0x0003) {
                this.printFieldAsHex(1, width, entry.getParameterId(), "Printer name", "Parameter ID");
                this.printFieldAsHex(1, width, entry.getParameterValue(), "Parameter value");

                this.printField(2, 12, EBCDIC.decode(ByteBuffer.wrap(entry.getParameterValue())).toString(), "Printer name");
                continue;
            }

            // Subsystem information
            if (entry.getParameterId() == 0x0004) {
                this.printFieldAsHex(1, width, entry.getParameterId(), "Subsystem information", "Parameter ID");
                this.printFieldAsHex(1, width, entry.getParameterValue(), "Parameter value");

                final IpdsByteArrayInputStream is = new IpdsByteArrayInputStream(entry.getParameterValue());
                final int secondaryWidth = 30;
                try {
                    final int subsystemNameLen = is.readUnsignedByte();
                    this.printField(2, secondaryWidth, is.readEbcdicString(subsystemNameLen), "Subsystem name");

                    final int ecLevelLen = is.readUnsignedByte();
                    this.printField(2, secondaryWidth, is.readEbcdicString(ecLevelLen), "Engineering change level");

                    final int infoLen = is.readUnsignedByte();
                    this.printField(2, secondaryWidth, is.readEbcdicString(infoLen), "Subsystem-specific information");
                } catch (final IOException e) {
                    // Ignore...
                }

                continue;
            }

            this.printFieldAsHex(1, width, entry.getParameterId(), "unknown", "Parameter ID");
            this.printFieldAsHex(1, width, entry.getParameterValue(), "Parameter value");
        }
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
            final String color = OCA_COLORS.get(colorValue);
            final String hint = color == null ? "unknown" : color;

            this.printFieldAsHex(1, 15, colorValue, hint, "OCA color value");
        }
    }

    @Override
    public void handle(final StoragePoolsSelfDefiningField sdf) {
        this.out.println("Storage Pools");

        final int width = 15;
        for (final StoragePoolsSelfDefiningField.StoragePoolEntry entry : sdf.getStoragePoolEntries()) {
            this.out.println();

            this.printFieldAsHex(1, width, entry.getEntryId(), "Entry ID");
            this.printFieldAsHex(1, width, entry.getStoragePoolId(), "Storage pool ID");
            this.printField(1, width, entry.getEmptySize(), "Size in Bytes");

            for (final Integer id : entry.getObjectIds()) {
                final String objectId = OBJECT_IDS.get(id);
                final String hint = objectId == null ? "unknown" : objectId;
                this.printFieldAsHex(2, 9, id, hint, "Object ID");
            }
        }
    }

    @Override
    public void handle(final SupportedDeviceResolutionsSelfDefiningField sdf) {
        this.out.println("Supported Device Resolutions");
        this.out.println(INDENTION + sdf.toString()); // TODO: pretty print
    }

    @Override
    public void handle(final SupportedGroupOperationsSelfDefiningField sdf) {
        this.out.println("Supported Group Operations");

        for (final Integer operation : sdf.getOperationTypes()) {
            final String groupOperation = GROUP_OPERATIONS.get(operation);
            final String hint = groupOperation == null ? "unknown" : groupOperation;

            this.printFieldAsHex(1, 9, operation, hint, "Operation");
        }
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
