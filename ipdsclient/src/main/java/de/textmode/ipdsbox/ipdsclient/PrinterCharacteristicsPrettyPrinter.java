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

    private static final HashMap<Integer, String> GROUP_ID_FORMATS = new HashMap<>();

    static {
        GROUP_ID_FORMATS.put(Integer.valueOf(0x01), "MVS and VSE print-data format");
        GROUP_ID_FORMATS.put(Integer.valueOf(0x02), "VM print-data format");
        GROUP_ID_FORMATS.put(Integer.valueOf(0x03), "OS/400 print-data format");
        GROUP_ID_FORMATS.put(Integer.valueOf(0x04), "MVS and VSE COM-data format");
        GROUP_ID_FORMATS.put(Integer.valueOf(0x05), "AIX and OS/2 COM-data format");
        GROUP_ID_FORMATS.put(Integer.valueOf(0x06), "AIX and Windows print-data");
        GROUP_ID_FORMATS.put(Integer.valueOf(0x08), "Variable-length Group ID format");
        GROUP_ID_FORMATS.put(Integer.valueOf(0x13), "Extended OS/400 print-data format");
    }

    private static final HashMap<Integer, String> CPGIDS = new HashMap<>();

    static {
        CPGIDS.put(Integer.valueOf(37), "USA/Canada - CECP");
        CPGIDS.put(Integer.valueOf(256), "International #1");
        CPGIDS.put(Integer.valueOf(259), "Symbols, Set 7");
        CPGIDS.put(Integer.valueOf(273), "Germany F.R./Austria - CECP");
        CPGIDS.put(Integer.valueOf(274), "Old Belgium Code Page");
        CPGIDS.put(Integer.valueOf(275), "Brazil - CECP");
        CPGIDS.put(Integer.valueOf(276), "Canada (French) - 94");
        CPGIDS.put(Integer.valueOf(277), "Denmark, Norway - CECP");
        CPGIDS.put(Integer.valueOf(278), "Finland, Sweden - CECP");
        CPGIDS.put(Integer.valueOf(280), "Italy - CECP");
        CPGIDS.put(Integer.valueOf(281), "Japan (Latin) - CECP");
        CPGIDS.put(Integer.valueOf(282), "Portugal - CECP");
        CPGIDS.put(Integer.valueOf(284), "Spain/Latin America - CECP");
        CPGIDS.put(Integer.valueOf(285), "United Kingdom - CECP");
        CPGIDS.put(Integer.valueOf(286), "Austria/Germany F.R., Alternate (3270)");
        CPGIDS.put(Integer.valueOf(290), "Japanese (Katakana) Extended");
        CPGIDS.put(Integer.valueOf(293), "APL (USA)");
        CPGIDS.put(Integer.valueOf(297), "France - CECP");
        CPGIDS.put(Integer.valueOf(300), "Japan (Kanji) - Host, DBCS");
        CPGIDS.put(Integer.valueOf(301), "Japan (Kanji) - PC, DBCS");
        CPGIDS.put(Integer.valueOf(310), "Graphic Escape APL/TN");
        CPGIDS.put(Integer.valueOf(367), "ASCII");
        CPGIDS.put(Integer.valueOf(420), "Arabic Bilingual");
        CPGIDS.put(Integer.valueOf(421), "Maghreb/French");
        CPGIDS.put(Integer.valueOf(423), "Greece - 183");
        CPGIDS.put(Integer.valueOf(424), "Israel (Hebrew)");
        CPGIDS.put(Integer.valueOf(425), "Arabic/Latin for OS/390 Open Edition");
        CPGIDS.put(Integer.valueOf(437), "Personal Computer");
        CPGIDS.put(Integer.valueOf(500), "International #5");
        CPGIDS.put(Integer.valueOf(720), "MS DOS Arabic (Transparent ASMO)");
        CPGIDS.put(Integer.valueOf(737), "MS DOS Greek");
        CPGIDS.put(Integer.valueOf(775), "MS DOS Baltic Rim");
        CPGIDS.put(Integer.valueOf(803), "Hebrew Character Set A");
        CPGIDS.put(Integer.valueOf(806), "PC Indian Script Code (ISCII-91)");
        CPGIDS.put(Integer.valueOf(808), "PC Data, Cyrillic, Russian with euro");
        CPGIDS.put(Integer.valueOf(813), "Greece ISO 8859-7");
        CPGIDS.put(Integer.valueOf(819), "ISO/ANSI Multilingual");
        CPGIDS.put(Integer.valueOf(833), "Korean Extended");
        CPGIDS.put(Integer.valueOf(834), "Korean Hangul - Host, DBCS with UDCs");
        CPGIDS.put(Integer.valueOf(835), "Traditional Chinese DBCS - Host");
        CPGIDS.put(Integer.valueOf(836), "Simplified Chinese Extended");
        CPGIDS.put(Integer.valueOf(837), "Simplified Chinese DBCS-HOST");
        CPGIDS.put(Integer.valueOf(838), "Thai with Low Tone Marks & Ancient Characters");
        CPGIDS.put(Integer.valueOf(848), "PC, Cyrillic, Ukrainian with euro");
        CPGIDS.put(Integer.valueOf(849), "PC Data, Cyrillic, Belorussian with euro");
        CPGIDS.put(Integer.valueOf(850), "Personal Computer - Multilingual Page");
        CPGIDS.put(Integer.valueOf(851), "Greece - Personal Computer");
        CPGIDS.put(Integer.valueOf(852), "Latin 2 - Personal Computer");
        CPGIDS.put(Integer.valueOf(853), "Latin 3 - Personal Computer");
        CPGIDS.put(Integer.valueOf(855), "Cyrillic - Personal Computer");
        CPGIDS.put(Integer.valueOf(856), "Hebrew - Personal Computer");
        CPGIDS.put(Integer.valueOf(857), "Latin #5, Turkey - Personal Computer");
        CPGIDS.put(Integer.valueOf(858), "Personal Computer - Multilingual with euro");
        CPGIDS.put(Integer.valueOf(859), "PC Latin 9");
        CPGIDS.put(Integer.valueOf(860), "Portugal - Personal Computer");
        CPGIDS.put(Integer.valueOf(861), "Iceland - Personal Computer");
        CPGIDS.put(Integer.valueOf(862), "Israel - Personal Computer");
        CPGIDS.put(Integer.valueOf(863), "Canadian French - Personal Computer");
        CPGIDS.put(Integer.valueOf(864), "Arabic - Personal Computer");
        CPGIDS.put(Integer.valueOf(865), "Nordic - Personal Computer");
        CPGIDS.put(Integer.valueOf(866), "PC Data, Cyrillic, Russian");
        CPGIDS.put(Integer.valueOf(867), "Israel - Personal Computer");
        CPGIDS.put(Integer.valueOf(868), "Urdu - Personal Computer");
        CPGIDS.put(Integer.valueOf(869), "Greece - Personal Computer");
        CPGIDS.put(Integer.valueOf(870), "Latin 2 - EBCDIC Multilingual");
        CPGIDS.put(Integer.valueOf(871), "Iceland - CECP");
        CPGIDS.put(Integer.valueOf(872), "Cyrillic - PC with euro");
        CPGIDS.put(Integer.valueOf(874), "Thai with Low Tone Marks & Ancient Chars - PC");
        CPGIDS.put(Integer.valueOf(875), "Greece");
        CPGIDS.put(Integer.valueOf(878), "Russian internet koi8-r");
        CPGIDS.put(Integer.valueOf(880), "Cyrillic, Multilingual");
        CPGIDS.put(Integer.valueOf(891), "Korea - Personal Computer");
        CPGIDS.put(Integer.valueOf(892), "EBCDIC, OCR A");
        CPGIDS.put(Integer.valueOf(893), "EBCDIC, OCR B");
        CPGIDS.put(Integer.valueOf(895), "Japan 7-Bit Latin");
        CPGIDS.put(Integer.valueOf(896), "Japan 7-Bit Katakana Extended");
        CPGIDS.put(Integer.valueOf(897), "Japan PC #1");
        CPGIDS.put(Integer.valueOf(899), "Symbol - Personal Computer");
        CPGIDS.put(Integer.valueOf(901), "PC Baltic Multi with Euro");
        CPGIDS.put(Integer.valueOf(902), "8-bit Estonia with Euro");
        CPGIDS.put(Integer.valueOf(903), "People's Republic of China (PRC)-PC");
        CPGIDS.put(Integer.valueOf(904), "Taiwan - Personal Computer");
        CPGIDS.put(Integer.valueOf(905), "Latin 3 - EBCDIC");
        CPGIDS.put(Integer.valueOf(912), "Latin 2 - ISO");
        CPGIDS.put(Integer.valueOf(913), "Latin 3 - ISO");
        CPGIDS.put(Integer.valueOf(914), "Latin 4");
        CPGIDS.put(Integer.valueOf(915), "Cyrillic, 8-Bit");
        CPGIDS.put(Integer.valueOf(916), "Hebrew (Latin)");
        CPGIDS.put(Integer.valueOf(918), "Urdu Bilingual");
        CPGIDS.put(Integer.valueOf(920), "Latin #5 - Turkey");
        CPGIDS.put(Integer.valueOf(921), "Baltic - Multilingual, superset of ISO 8859-13");
        CPGIDS.put(Integer.valueOf(922), "Estonia, similar to ISO 8859-1");
        CPGIDS.put(Integer.valueOf(923), "Latin 9");
        CPGIDS.put(Integer.valueOf(924), "Latin 9 EBCDIC");
        CPGIDS.put(Integer.valueOf(926), "Korean PC Data Double-Byte incl. 1880 UDC");
        CPGIDS.put(Integer.valueOf(927), "T-Ch PC Data Double-Byte incl. 6204 UDC");
        CPGIDS.put(Integer.valueOf(928), "S-Ch PC Data Double-Byte incl. 1880 UDC");
        CPGIDS.put(Integer.valueOf(941), "Japanese DBCS PC for Open environment");
        CPGIDS.put(Integer.valueOf(947), "Pure DBCS for Big-5");
        CPGIDS.put(Integer.valueOf(951), "Korean DBCS-PC (10104 characters)");
        CPGIDS.put(Integer.valueOf(952), "Japanese EUC for JIS X 0208 including 83 NEC selected chars + 940 UDC");
        CPGIDS.put(Integer.valueOf(953), "Japanese EUC for JIS X 0212 + IBM Select + UDC");
        CPGIDS.put(Integer.valueOf(955), "Japanese TCP, JIS X0208-1978");
        CPGIDS.put(Integer.valueOf(960), "Traditional Chinese DBCS-EUC SICGCC Primary set (1st plane)");
        CPGIDS.put(Integer.valueOf(961), "Traditional Chinese TBCS-EUC SICGCC Full set + IBM Select + UDC");
        CPGIDS.put(Integer.valueOf(963), "T-Ch TCP, CNS 11643 plane 2 only");
        CPGIDS.put(Integer.valueOf(971), "Korean EUC, DBCS EUC (G1, KSC 5601)");
        CPGIDS.put(Integer.valueOf(1002), "DCF Release 2 Compatibility");
        CPGIDS.put(Integer.valueOf(1004), "Latin-1 Extended, Desk Top Publishing/Windows");
        CPGIDS.put(Integer.valueOf(1006), "Urdu, 8-Bit");
        CPGIDS.put(Integer.valueOf(1008), "Arabic 8-Bit ISO/ASCII");
        CPGIDS.put(Integer.valueOf(1009), "ISO IRV");
        CPGIDS.put(Integer.valueOf(1010), "7-Bit France");
        CPGIDS.put(Integer.valueOf(1011), "7-Bit Germany F.R.");
        CPGIDS.put(Integer.valueOf(1012), "7-Bit Italy");
        CPGIDS.put(Integer.valueOf(1013), "7-Bit United Kingdom");
        CPGIDS.put(Integer.valueOf(1014), "7-Bit Spain");
        CPGIDS.put(Integer.valueOf(1015), "7-Bit Portugal");
        CPGIDS.put(Integer.valueOf(1016), "7-Bit Norway");
        CPGIDS.put(Integer.valueOf(1017), "7-Bit Denmark");
        CPGIDS.put(Integer.valueOf(1018), "7-Bit Finland/Sweden");
        CPGIDS.put(Integer.valueOf(1019), "7-Bit Netherlands");
        CPGIDS.put(Integer.valueOf(1020), "Canadian (French) Variant");
        CPGIDS.put(Integer.valueOf(1021), "Switzerland Variant");
        CPGIDS.put(Integer.valueOf(1023), "Spain Variant");
        CPGIDS.put(Integer.valueOf(1025), "Cyrillic, Multilingual");
        CPGIDS.put(Integer.valueOf(1026), "Latin #5 - Turkey");
        CPGIDS.put(Integer.valueOf(1027), "Japanese (Latin) Extended");
        CPGIDS.put(Integer.valueOf(1040), "Korean Extended - Personal Computer");
        CPGIDS.put(Integer.valueOf(1041), "Japanese Extended - Personal Computer");
        CPGIDS.put(Integer.valueOf(1042), "Simplified Chinese Extended - PC");
        CPGIDS.put(Integer.valueOf(1043), "Traditional Chinese Extended - PC");
        CPGIDS.put(Integer.valueOf(1046), "Arabic Extended-Euro");
        CPGIDS.put(Integer.valueOf(1047), "Latin 1/Open Systems");
        CPGIDS.put(Integer.valueOf(1051), "H-P Emulation, Roman 8");
        CPGIDS.put(Integer.valueOf(1070), "USA/Canada - CECP");
        CPGIDS.put(Integer.valueOf(1079), "Spain/Latin America - CECP");
        CPGIDS.put(Integer.valueOf(1081), "France - CECP");
        CPGIDS.put(Integer.valueOf(1084), "International #5");
        CPGIDS.put(Integer.valueOf(1088), "Revised Korean - Personal Computer");
        CPGIDS.put(Integer.valueOf(1089), "Arabic Code Page, Data Storage & Interchange");
        CPGIDS.put(Integer.valueOf(1097), "Farsi Bilingual - EBCDIC");
        CPGIDS.put(Integer.valueOf(1098), "Farsi - Personal Computer");
        CPGIDS.put(Integer.valueOf(1100), "Multinational Emulation");
        CPGIDS.put(Integer.valueOf(1101), "British NRC Set");
        CPGIDS.put(Integer.valueOf(1102), "Dutch NRC Set");
        CPGIDS.put(Integer.valueOf(1103), "Finnish NRC Set");
        CPGIDS.put(Integer.valueOf(1104), "French NRC Set");
        CPGIDS.put(Integer.valueOf(1105), "Norwegian/Danish NRC Set");
        CPGIDS.put(Integer.valueOf(1106), "Swedish NRC Set");
        CPGIDS.put(Integer.valueOf(1107), "Norwegian/Danish NRC Alternate");
        CPGIDS.put(Integer.valueOf(1112), "Baltic - Multilingual, EBCDIC");
        CPGIDS.put(Integer.valueOf(1114), "Taiwan - Personal Computer");
        CPGIDS.put(Integer.valueOf(1115), "People's Republic of China (PRC)-PC");
        CPGIDS.put(Integer.valueOf(1122), "Estonia, EBCDIC");
        CPGIDS.put(Integer.valueOf(1123), "Cyrillic, Ukraine");
        CPGIDS.put(Integer.valueOf(1124), "Cyrillic, Ukraine");
        CPGIDS.put(Integer.valueOf(1125), "PC, Cyrillic, Ukrainian");
        CPGIDS.put(Integer.valueOf(1126), "Korean - Personal Computer for Windows");
        CPGIDS.put(Integer.valueOf(1127), "Arabic/French - Personal Computer");
        CPGIDS.put(Integer.valueOf(1129), "Vietnamese ISO-8");
        CPGIDS.put(Integer.valueOf(1130), "Vietnamese EBCDIC");
        CPGIDS.put(Integer.valueOf(1131), "PC Data, Cyrillic, Belorussian");
        CPGIDS.put(Integer.valueOf(1132), "Lao EBCDIC");
        CPGIDS.put(Integer.valueOf(1133), "Lao ISO-8");
        CPGIDS.put(Integer.valueOf(1137), "Devanagari EBCDIC");
        CPGIDS.put(Integer.valueOf(1140), "USA, Canada, etc. ECECP");
        CPGIDS.put(Integer.valueOf(1141), "Austria, Germany ECECP");
        CPGIDS.put(Integer.valueOf(1142), "Denmark, Norway ECECP");
        CPGIDS.put(Integer.valueOf(1143), "Finland, Sweden ECECP");
        CPGIDS.put(Integer.valueOf(1144), "Italy ECECP");
        CPGIDS.put(Integer.valueOf(1145), "Spain, Latin America (Spanish) ECECP");
        CPGIDS.put(Integer.valueOf(1146), "UK ECECP");
        CPGIDS.put(Integer.valueOf(1147), "France ECECP");
        CPGIDS.put(Integer.valueOf(1148), "International ECECP");
        CPGIDS.put(Integer.valueOf(1149), "Iceland ECECP");
        CPGIDS.put(Integer.valueOf(1153), "EBCDIC Latin 2 Multilingual with euro");
        CPGIDS.put(Integer.valueOf(1154), "EBCDIC Cyrillic, Multilingual with euro");
        CPGIDS.put(Integer.valueOf(1155), "EBCDIC Turkey with euro");
        CPGIDS.put(Integer.valueOf(1156), "EBCDIC Baltic Multi with euro");
        CPGIDS.put(Integer.valueOf(1157), "EBCDIC Estonia with euro");
        CPGIDS.put(Integer.valueOf(1158), "EBCDIC Cyrillic, Ukraine with euro");
        CPGIDS.put(Integer.valueOf(1159), "T-Chinese EBCDIC");
        CPGIDS.put(Integer.valueOf(1160), "Thai with Low Tone Marks & Ancient Characters");
        CPGIDS.put(Integer.valueOf(1161), "Thai with Low Tone Marks & Ancient Chars - PC");
        CPGIDS.put(Integer.valueOf(1162), "Thai MS Windows");
        CPGIDS.put(Integer.valueOf(1163), "Vietnamese ISO-8 with euro");
        CPGIDS.put(Integer.valueOf(1164), "Vietnamese EBCDIC with euro");
        CPGIDS.put(Integer.valueOf(1165), "Latin 2 EBCDIC/Open Systems");
        CPGIDS.put(Integer.valueOf(1166), "EBCDIC Cyrillic, Multilingual with euro");
        CPGIDS.put(Integer.valueOf(1167), "Belarusian/Ukrainian KOI8-RU");
        CPGIDS.put(Integer.valueOf(1168), "Ukrainian KOI8-U");
        CPGIDS.put(Integer.valueOf(1250), "Windows, Latin 2");
        CPGIDS.put(Integer.valueOf(1251), "Windows, Cyrillic");
        CPGIDS.put(Integer.valueOf(1252), "Windows, Latin 1");
        CPGIDS.put(Integer.valueOf(1253), "Windows, Greek");
        CPGIDS.put(Integer.valueOf(1254), "Windows, Turkish");
        CPGIDS.put(Integer.valueOf(1255), "Windows, Hebrew");
        CPGIDS.put(Integer.valueOf(1256), "Windows, Arabic");
        CPGIDS.put(Integer.valueOf(1257), "Windows, Baltic Rim");
        CPGIDS.put(Integer.valueOf(1258), "Windows, Vietnamese");
        CPGIDS.put(Integer.valueOf(1275), "Apple, Latin 1");
        CPGIDS.put(Integer.valueOf(1276), "Adobe (PostScript) Standard Encoding");
        CPGIDS.put(Integer.valueOf(1277), "Adobe (PostScript) Latin 1");
        CPGIDS.put(Integer.valueOf(1280), "Apple Greek");
        CPGIDS.put(Integer.valueOf(1281), "Apple Turkish");
        CPGIDS.put(Integer.valueOf(1282), "Apple Central European");
        CPGIDS.put(Integer.valueOf(1283), "Apple Cyrillic");
        CPGIDS.put(Integer.valueOf(1284), "Apple, Croatian");
        CPGIDS.put(Integer.valueOf(1285), "Apple, Romanian");
        CPGIDS.put(Integer.valueOf(1286), "Apple, Icelandic");
        CPGIDS.put(Integer.valueOf(1287), "DEC Greek 8-Bit");
        CPGIDS.put(Integer.valueOf(1288), "DEC Turkish 8-Bit");
        CPGIDS.put(Integer.valueOf(1351), "DBCS-PC, including 940 HP UDCs, Japanese");
        CPGIDS.put(Integer.valueOf(1362), "Korean Hangul - PC, DBCS with UDCs");
        CPGIDS.put(Integer.valueOf(1372), "MS T-Chinese Big-5 (Special for DB2)");
        CPGIDS.put(Integer.valueOf(1374), "DB Big-5 extension for HKSCS");
        CPGIDS.put(Integer.valueOf(1376), "Traditional Chinese DBCS-Host extension for HKSCS");
        CPGIDS.put(Integer.valueOf(1380), "Simplified Chinese GB PC-DATA");
        CPGIDS.put(Integer.valueOf(1382), "Simplified Chinese EUC");
        CPGIDS.put(Integer.valueOf(1385), "Simplified Chinese 2 Byte, growing CS for GB18030, also used for GBK PC-DATA");
        CPGIDS.put(Integer.valueOf(1391), "Simplified Chinese 4 Byte, growing CS for GB18030");
        CPGIDS.put(Integer.valueOf(1393), "Shift_JISX0213 DBCS");
        CPGIDS.put(Integer.valueOf(1400), "ISO 10646 UCS-BMP (Based on Unicode V6.0)");
        CPGIDS.put(Integer.valueOf(1401), "ISO 10646 UCS-SMP (Based on Unicode V6.0)");
        CPGIDS.put(Integer.valueOf(1402), "ISO 10646 UCS-SIP (Based on Unicode V6.0)");
        CPGIDS.put(Integer.valueOf(1414), "ISO 10646 UCS-SSP (Based on Unicode 4.0)");
        CPGIDS.put(Integer.valueOf(1445), "IBM AFP PUA No. 1");
        CPGIDS.put(Integer.valueOf(1446), "ISO 10646 UCS-PUP15 (Based on Unicode 4.0)");
        CPGIDS.put(Integer.valueOf(1447), "ISO 10646 UCS-PUP16 (Based on Unicode 4.0)");
        CPGIDS.put(Integer.valueOf(1448), "UCS-BMP (Generic UDC)");
        CPGIDS.put(Integer.valueOf(1449), "IBM default PUA");
        CPGIDS.put(Integer.valueOf(65520), "Empty Unicode Plane");
    }

    private static final HashMap<Integer, String> FGIDS = new HashMap<>();

    static {
        FGIDS.put(Integer.valueOf(2), "Delegate");
        FGIDS.put(Integer.valueOf(3), "OCR-B");
        FGIDS.put(Integer.valueOf(5), "Rhetoric/Orator");
        FGIDS.put(Integer.valueOf(8), "Scribe/Symbol");
        FGIDS.put(Integer.valueOf(10), "Cyrillic 22");
        FGIDS.put(Integer.valueOf(11), "Courier");
        FGIDS.put(Integer.valueOf(12), "Prestige");
        FGIDS.put(Integer.valueOf(13), "Artisan");
        FGIDS.put(Integer.valueOf(18), "Courier Italic");
        FGIDS.put(Integer.valueOf(19), "OCR-A");
        FGIDS.put(Integer.valueOf(20), "Pica");
        FGIDS.put(Integer.valueOf(21), "Katakana");
        FGIDS.put(Integer.valueOf(25), "Presentor");
        FGIDS.put(Integer.valueOf(26), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(30), "Symbol");
        FGIDS.put(Integer.valueOf(31), "Aviv");
        FGIDS.put(Integer.valueOf(36), "Letter Gothic");
        FGIDS.put(Integer.valueOf(38), "Orator Bold");
        FGIDS.put(Integer.valueOf(39), "Gothic Bold");
        FGIDS.put(Integer.valueOf(40), "Gothic");
        FGIDS.put(Integer.valueOf(41), "Roman Text");
        FGIDS.put(Integer.valueOf(42), "Serif");
        FGIDS.put(Integer.valueOf(43), "Serif Italic");
        FGIDS.put(Integer.valueOf(44), "Katakana Gothic");
        FGIDS.put(Integer.valueOf(46), "Courier Bold");
        FGIDS.put(Integer.valueOf(49), "Shalom");
        FGIDS.put(Integer.valueOf(50), "Shalom Bold");
        FGIDS.put(Integer.valueOf(51), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(52), "Courier");
        FGIDS.put(Integer.valueOf(55), "Aviv Bold");
        FGIDS.put(Integer.valueOf(61), "Naseem");
        FGIDS.put(Integer.valueOf(62), "Naseem Italic");
        FGIDS.put(Integer.valueOf(63), "Naseem Bold");
        FGIDS.put(Integer.valueOf(64), "Naseem Italic Bold");
        FGIDS.put(Integer.valueOf(66), "Gothic");
        FGIDS.put(Integer.valueOf(68), "Gothic Italic");
        FGIDS.put(Integer.valueOf(69), "Gothic Bold");
        FGIDS.put(Integer.valueOf(70), "Serif");
        FGIDS.put(Integer.valueOf(71), "Serif Italic");
        FGIDS.put(Integer.valueOf(72), "Serif Bold");
        FGIDS.put(Integer.valueOf(74), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(75), "Courier");
        FGIDS.put(Integer.valueOf(76), "APL");
        FGIDS.put(Integer.valueOf(78), "Katakana");
        FGIDS.put(Integer.valueOf(80), "Symbol");
        FGIDS.put(Integer.valueOf(84), "Script");
        FGIDS.put(Integer.valueOf(85), "Courier");
        FGIDS.put(Integer.valueOf(86), "Prestige");
        FGIDS.put(Integer.valueOf(87), "Letter Gothic");
        FGIDS.put(Integer.valueOf(91), "Light Italic");
        FGIDS.put(Integer.valueOf(92), "Courier Italic");
        FGIDS.put(Integer.valueOf(95), "Adjudant");
        FGIDS.put(Integer.valueOf(96), "Old World");
        FGIDS.put(Integer.valueOf(98), "Shalom");
        FGIDS.put(Integer.valueOf(99), "Aviv");
        FGIDS.put(Integer.valueOf(101), "Shalom Bold");
        FGIDS.put(Integer.valueOf(102), "Aviv Bold");
        FGIDS.put(Integer.valueOf(103), "Nasseem");
        FGIDS.put(Integer.valueOf(109), "Letter Gothic Italic");
        FGIDS.put(Integer.valueOf(110), "Letter Gothic Bold");
        FGIDS.put(Integer.valueOf(111), "Prestige Bold");
        FGIDS.put(Integer.valueOf(112), "Prestige Italic");
        FGIDS.put(Integer.valueOf(154), "Essay");
        FGIDS.put(Integer.valueOf(155), "Boldface Italic");
        FGIDS.put(Integer.valueOf(157), "Title");
        FGIDS.put(Integer.valueOf(158), "Modern");
        FGIDS.put(Integer.valueOf(159), "Boldface");
        FGIDS.put(Integer.valueOf(160), "Essay");
        FGIDS.put(Integer.valueOf(162), "Essay Italic");
        FGIDS.put(Integer.valueOf(163), "Essay Bold");
        FGIDS.put(Integer.valueOf(164), "Prestige");
        FGIDS.put(Integer.valueOf(167), "Barak");
        FGIDS.put(Integer.valueOf(168), "Barak Bold");
        FGIDS.put(Integer.valueOf(173), "Essay");
        FGIDS.put(Integer.valueOf(174), "Gothic");
        FGIDS.put(Integer.valueOf(175), "Document");
        FGIDS.put(Integer.valueOf(178), "Barak");
        FGIDS.put(Integer.valueOf(179), "Barak Bold");
        FGIDS.put(Integer.valueOf(180), "Barak");
        FGIDS.put(Integer.valueOf(181), "Barak Mixed Bold");
        FGIDS.put(Integer.valueOf(182), "Barak");
        FGIDS.put(Integer.valueOf(183), "Barak Bold");
        FGIDS.put(Integer.valueOf(186), "Press Roman");
        FGIDS.put(Integer.valueOf(187), "Press Roman Bold");
        FGIDS.put(Integer.valueOf(188), "Press Roman Italic");
        FGIDS.put(Integer.valueOf(189), "Press Roman Italic Bold");
        FGIDS.put(Integer.valueOf(190), "Foundry");
        FGIDS.put(Integer.valueOf(191), "Foundry Bold");
        FGIDS.put(Integer.valueOf(194), "Foundry Italic");
        FGIDS.put(Integer.valueOf(195), "Foundry Italic Bold");
        FGIDS.put(Integer.valueOf(203), "Data 1");
        FGIDS.put(Integer.valueOf(204), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(205), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(211), "Shalom");
        FGIDS.put(Integer.valueOf(212), "Shalom Bold");
        FGIDS.put(Integer.valueOf(221), "Prestige");
        FGIDS.put(Integer.valueOf(222), "Gothic");
        FGIDS.put(Integer.valueOf(223), "Courier");
        FGIDS.put(Integer.valueOf(225), "Symbol");
        FGIDS.put(Integer.valueOf(226), "Shalom");
        FGIDS.put(Integer.valueOf(229), "Serif");
        FGIDS.put(Integer.valueOf(230), "Gothic");
        FGIDS.put(Integer.valueOf(232), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(233), "Matrix Courier");
        FGIDS.put(Integer.valueOf(234), "Shalom Bold");
        FGIDS.put(Integer.valueOf(244), "Courier Double Wide");
        FGIDS.put(Integer.valueOf(245), "Courier Bold Double Wide");
        FGIDS.put(Integer.valueOf(247), "Shalom Bold");
        FGIDS.put(Integer.valueOf(248), "Shalom");
        FGIDS.put(Integer.valueOf(249), "Katakana");
        FGIDS.put(Integer.valueOf(252), "Courier");
        FGIDS.put(Integer.valueOf(253), "Courier Bold");
        FGIDS.put(Integer.valueOf(254), "Courier");
        FGIDS.put(Integer.valueOf(255), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(256), "Prestige");
        FGIDS.put(Integer.valueOf(258), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(259), "Matrix Gothic");
        FGIDS.put(Integer.valueOf(279), "Nasseem");
        FGIDS.put(Integer.valueOf(281), "Gothic Text");
        FGIDS.put(Integer.valueOf(282), "Aviv");
        FGIDS.put(Integer.valueOf(283), "Letter Gothic");
        FGIDS.put(Integer.valueOf(285), "Letter Gothic");
        FGIDS.put(Integer.valueOf(290), "Gothic Text");
        FGIDS.put(Integer.valueOf(300), "Gothic");
        FGIDS.put(Integer.valueOf(304), "Gothic Text");
        FGIDS.put(Integer.valueOf(305), "OCR-A");
        FGIDS.put(Integer.valueOf(306), "OCR-B");
        FGIDS.put(Integer.valueOf(307), "APL");
        FGIDS.put(Integer.valueOf(318), "Prestige Bold");
        FGIDS.put(Integer.valueOf(319), "Prestige Italic");
        FGIDS.put(Integer.valueOf(322), "APL Bold");
        FGIDS.put(Integer.valueOf(400), "Gothic");
        FGIDS.put(Integer.valueOf(404), "Letter Gothic Bold");
        FGIDS.put(Integer.valueOf(416), "Courier Roman Medium");
        FGIDS.put(Integer.valueOf(420), "Courier Roman Bold");
        FGIDS.put(Integer.valueOf(424), "Courier Roman Italic");
        FGIDS.put(Integer.valueOf(428), "Courier Roman Italic Bold");
        FGIDS.put(Integer.valueOf(432), "Prestige");
        FGIDS.put(Integer.valueOf(434), "Orator Bold");
        FGIDS.put(Integer.valueOf(435), "Orator Bold");
        FGIDS.put(Integer.valueOf(751), "Sonoran Serif");
        FGIDS.put(Integer.valueOf(752), "Nasseem");
        FGIDS.put(Integer.valueOf(753), "Nasseem Bold");
        FGIDS.put(Integer.valueOf(754), "Nasseem Bold");
        FGIDS.put(Integer.valueOf(755), "Nasseem Bold");
        FGIDS.put(Integer.valueOf(756), "Nasseem Italic");
        FGIDS.put(Integer.valueOf(757), "Nasseem Bold Italic");
        FGIDS.put(Integer.valueOf(758), "Nasseem Bold Italic");
        FGIDS.put(Integer.valueOf(759), "Nasseem Bold Italic");
        FGIDS.put(Integer.valueOf(760), "Times Roman");
        FGIDS.put(Integer.valueOf(761), "Times Roman Bold");
        FGIDS.put(Integer.valueOf(762), "Times Roman Bold");
        FGIDS.put(Integer.valueOf(763), "Times Roman Italic");
        FGIDS.put(Integer.valueOf(764), "Times Roman Bold Italic");
        FGIDS.put(Integer.valueOf(765), "Times Roman Bold Italic");
        FGIDS.put(Integer.valueOf(1051), "Sonoran Serif");
        FGIDS.put(Integer.valueOf(1053), "Sonoran Serif Bold");
        FGIDS.put(Integer.valueOf(1056), "Sonoran Serif Italic");
        FGIDS.put(Integer.valueOf(1351), "Sonoran Serif");
        FGIDS.put(Integer.valueOf(1653), "Sonoran Serif Bold");
        FGIDS.put(Integer.valueOf(1803), "Sonoran Serif Bold");
        FGIDS.put(Integer.valueOf(2103), "Sonoran Serif Bold");
        FGIDS.put(Integer.valueOf(2304), "Helvetica Roman Medium");
        FGIDS.put(Integer.valueOf(2305), "Helvetica Roman Bold");
        FGIDS.put(Integer.valueOf(2306), "Helvetica Roman Italic");
        FGIDS.put(Integer.valueOf(2307), "Helvetica Roman Italic Bold");
        FGIDS.put(Integer.valueOf(2308), "Times New Roman Medium");
        FGIDS.put(Integer.valueOf(2309), "Times New Roman Bold");
        FGIDS.put(Integer.valueOf(2310), "Times New Roman Italic");
        FGIDS.put(Integer.valueOf(2311), "Times New Roman Italic Bold");
        FGIDS.put(Integer.valueOf(4407), "Sonoran Serif");
        FGIDS.put(Integer.valueOf(4427), "Sonoran Serif Bold");
        FGIDS.put(Integer.valueOf(4535), "Sonoran Serif Italic");
        FGIDS.put(Integer.valueOf(4919), "Goudy");
        FGIDS.put(Integer.valueOf(4939), "Goudy Bold");
        FGIDS.put(Integer.valueOf(5047), "Goudy Italic");
        FGIDS.put(Integer.valueOf(5047), "Goudy Bold Italic");
        FGIDS.put(Integer.valueOf(5687), "Times Roman");
        FGIDS.put(Integer.valueOf(5707), "Times Roman Bold");
        FGIDS.put(Integer.valueOf(5815), "Times Roman Italic");
        FGIDS.put(Integer.valueOf(5835), "Times Roman Italic Bold");
        FGIDS.put(Integer.valueOf(5943), "University");
        FGIDS.put(Integer.valueOf(6199), "Palatino");
        FGIDS.put(Integer.valueOf(6219), "Palatino Bold");
        FGIDS.put(Integer.valueOf(6327), "Palatino Italic");
        FGIDS.put(Integer.valueOf(6347), "Palatino Italic Bold");
        FGIDS.put(Integer.valueOf(8503), "Baskerville");
        FGIDS.put(Integer.valueOf(8523), "Baskerville Bold");
        FGIDS.put(Integer.valueOf(8631), "Baskerville Italic");
        FGIDS.put(Integer.valueOf(8651), "Baskerville Italic Bold");
        FGIDS.put(Integer.valueOf(8759), "Nasseem");
        FGIDS.put(Integer.valueOf(8779), "Nasseem Bold");
        FGIDS.put(Integer.valueOf(8887), "Nasseem Italic");
        FGIDS.put(Integer.valueOf(8907), "Nasseem Italic Bold");
        FGIDS.put(Integer.valueOf(12855), "Narkisim");
        FGIDS.put(Integer.valueOf(12875), "Narkisim Bold");
        FGIDS.put(Integer.valueOf(16951), "Century Schoolbook");
        FGIDS.put(Integer.valueOf(16971), "Century Schoolbook Bold");
        FGIDS.put(Integer.valueOf(17079), "Century Schoolbook Italic");
        FGIDS.put(Integer.valueOf(17099), "Century Schoolbook Italic Bold");
        FGIDS.put(Integer.valueOf(20224), "Boldface");
        FGIDS.put(Integer.valueOf(33335), "Optima");
        FGIDS.put(Integer.valueOf(33355), "Optima Bold");
        FGIDS.put(Integer.valueOf(33463), "Optima Italic");
        FGIDS.put(Integer.valueOf(33483), "Optima Italic Bold");
        FGIDS.put(Integer.valueOf(33591), "Futura");
        FGIDS.put(Integer.valueOf(33601), "Futura Bold");
        FGIDS.put(Integer.valueOf(33719), "Futura Italic");
        FGIDS.put(Integer.valueOf(33729), "Futura Italic Bold");
        FGIDS.put(Integer.valueOf(34103), "Helvetica");
        FGIDS.put(Integer.valueOf(34123), "Helvetica Bold");
        FGIDS.put(Integer.valueOf(34231), "Helvetica Italic");
        FGIDS.put(Integer.valueOf(34251), "Helvetica Italic Bold");
        FGIDS.put(Integer.valueOf(37431), "Old English");
        FGIDS.put(Integer.valueOf(41783), "Coronet Cursive");
        FGIDS.put(Integer.valueOf(41803), "Coronet Cursive Bold");
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

        if (sdf.getGroupIdFormats().isEmpty()) {
            this.out.println("None");
        } else {
            for (final Integer id : sdf.getGroupIdFormats()) {
                final String format = GROUP_ID_FORMATS.get(id);
                final String hint = format == null ? "unknown" : format;

                this.printFieldAsHex(1, 15, id, hint, "Group ID format");
            }
        }
    }

    @Override
    public void handle(final ResidentSymbolSetSupportSelfDefiningField sdf) {
        this.out.println("Resident Symbol Set");

        for (final ResidentSymbolSetSupportSelfDefiningField.ResidentSymbolSet symbolSet : sdf.getResidentSymbolSets()) {
            this.out.println("");
            this.printFieldAsHex(1, 23, symbolSet.getCodePageId(), "Code Page ID");

            for (final Integer cpgid : symbolSet.getCpgids()) {
                final String cpName = CPGIDS.get(cpgid);
                final String hint = cpName == null ? "unknown" : cpName;

                this.printFieldAsHex(1, 23, cpgid, hint, "Code Page Golbal ID");
            }

            for (final Integer fgid : symbolSet.getFgids()) {
                final String fontName = FGIDS.get(fgid);
                final String hint = fontName == null ? "unknown" : fontName;

                this.printFieldAsHex(1, 23, fgid, hint, "Font Typeface Golbal ID");
            }
        }
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

        final int width = 17;
        this.printFieldAsHex(1, width, sdf.getRipXpels(), "RIP X pels");
        this.printFieldAsHex(1, width, sdf.getRipYpels(), "RIP Y pels");
        this.printFieldAsHex(1, width, sdf.getPrintHeadXpels(), "Print head X pels");
        this.printFieldAsHex(1, width, sdf.getPrintHeadYpels(), "Print head X pels");
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

        final int width = 18;
        for (final SymbolSetSupportSelfDefiningField.SymbolSetSupportEntry entry : sdf.getEntries()) {

            if (entry.getValueEntryId() == 0x01) {
                this.printFieldAsHex(1, width, entry.getValueEntryId(), "Fixed-box size", "Value entry ID");
                this.printFieldAsHex(1, width, entry.getXBoxSize(), "X box size");
                this.printFieldAsHex(1, width, entry.getYBoxSize(), "Y box size");

            } else if (entry.getValueEntryId() == 0x02) {
                this.printFieldAsHex(1, width, entry.getValueEntryId(), "Variable-box size", "Value entry ID");
                this.printFieldAsHex(1, width, entry.getUnitBase(), "Unit base");
                this.printFieldAsHex(1, width, entry.getPpub(), "Pels per unit base");
                this.printFieldAsHex(1, width, entry.getMaximumSize(), "Maximum size");
                this.printFieldAsHex(1, width, entry.getUniformSize(), "Uniform size");
            }

            for (final Integer fgid : entry.getFgids()) {
                final String fontName = FGIDS.get(fgid);
                final String hint = fontName == null ? "unknown" : fontName;

                this.printFieldAsHex(2, 23, fgid, hint, "Font Typeface Golbal ID");
            }
        }
    }

    @Override
    public void handle(final UnknownSelfDefiningField sdf) {
        this.out.println("Unknown");
        this.printFieldAsHex(2, 12, sdf.getRawData(), "Unknown data");
    }

    @Override
    public void handle(final Up3iPaperInputMediaSelfDefiningField sdf) {
        this.out.println("UP3I Paper Input");

        this.printFieldAsHex(1, 22, sdf.getMediaSourceId(), "Media source ID");
        this.printFieldAsHex(1, 22, sdf.getUp3iMediaInformation(), "UP3I media information");
    }

    @Override
    public void handle(final Up3iTupelSelfDefiningField sdf) {
        this.out.println("UP3I Tupel");

        this.printFieldAsHex(1, 23, sdf.getTupelId(), "Tupel ID");
        this.printFieldAsHex(1, 23, sdf.getUp3iDeviceInformation(), "UP3I device information");
    }
}
