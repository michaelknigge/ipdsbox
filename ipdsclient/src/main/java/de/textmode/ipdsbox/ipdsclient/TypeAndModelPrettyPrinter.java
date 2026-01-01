package de.textmode.ipdsbox.ipdsclient;

import java.io.PrintStream;

import de.textmode.ipdsbox.ipds.acknowledge.SenseTypeAndModelAcknowledgeData;

/**
 * The {@link TypeAndModelPrettyPrinter} prints out the information within a
 * {@link SenseTypeAndModelAcknowledgeData} in a nice readable format.
 */
final class TypeAndModelPrettyPrinter {

    private static final String INDENTION = "  ";

    private final SenseTypeAndModelAcknowledgeData data;
    private final PrintStream out;

    /**
     * Constructor of this pretty printer.
     */
    TypeAndModelPrettyPrinter(final SenseTypeAndModelAcknowledgeData data) {
        this.data = data;
        this.out = System.out;
    }

    /**
     * Constructor of this pretty printer.
     */
    TypeAndModelPrettyPrinter(
            final SenseTypeAndModelAcknowledgeData data,
            final PrintStream out) {
        this.data = data;
        this.out = out;
    }

    /**
     * Pretty prints the printer characteristics.
     */
    void print() {
        System.out.println(String.format("Device Type: 0x%04X", this.data.getType()));
        System.out.println(String.format("Model      : 0x%02X", this.data.getModel()));
        System.out.println(" ");

        for (final SenseTypeAndModelAcknowledgeData.CommandSetVector vector : this.data.getCommandSetVectors()) {

            System.out.println(String.format(
                    "Command Set ID : 0x%04X (%s)",
                    vector.getSubsetIdOrCommandSetId(),
                    decodeCommandSetId(vector)));

            System.out.println(String.format(
                    "Level ID       : 0x%04X (%s)",
                    vector.getLevelOrSubsetId(),
                    decodeLevel(vector)));

            for (final Integer propertyPair : vector.getPropertyPairs()) {
                System.out.println(String.format(
                        "  Property pair : 0x%04X (%s)",
                        propertyPair,
                        this.decodePropertyPair(vector, propertyPair)));
            }

            System.out.println(" ");
        }
    }

    /**
     * Returns a textual description of the Command Set ID or Subset ID.
     */
    private static String decodeCommandSetId(final SenseTypeAndModelAcknowledgeData.CommandSetVector vector) {
        return switch (vector.getSubsetIdOrCommandSetId()) {
            case 0xC4C3 -> "Device Control Command Set";
            case 0xD7E3 -> "Text Command Set";
            case 0xC9D4 -> "IM-Image Command Set";
            case 0xC9D6 -> "IO-Image Command Set";
            case 0xE5C7 -> "Graphics Command Set";
            case 0xC2C3 -> "Bar Code Command Set";
            case 0xD6C3 -> "Object Container Command Set";
            case 0xD4C4 -> "MO1 Subset of the Metadata Command Set";
            case 0xD6D3 -> "Overlay Command Set";
            case 0xD7E2 -> "Page Segment Command Set";
            case 0xC3C6 -> "Loaded Font Command Set";
            default -> "** Unknown Command Set **";
        };
    }

    /**
     * Returns a textual description of the Subset ID or Level ID.
     */
    private static String decodeLevel(final SenseTypeAndModelAcknowledgeData.CommandSetVector vector) {
        if (vector.getSubsetIdOrCommandSetId() == 0xC4C3) { // Device Control Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "DC1 subset ID" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD7E3) { // Text Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF10 -> "PTOCA PT1 data";
                case 0xFF20 -> "PTOCA PT2 data";
                case 0xFF30 -> "PTOCA PT3 data";
                case 0xFF40 -> "PTOCA PT4 data";
                default -> "** Unknown Level **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC9D4) { // IM-Image Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "IMD1 data" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC9D6) { // IO-Image Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF10 -> "IOCA FS10 data";
                case 0xFF11 -> "IOCA FS11 data";
                case 0xFF14 -> "IOCA FS14 data";
                case 0xFF40 -> "IOCA FS40 data";
                case 0xFF42 -> "IOCA FS42 data";
                case 0xFF45 -> "IOCA FS45 data";
                case 0xFF48 -> "IOCA FS48 data";
                case 0x0010 -> "Subset of IOCA FS10 data";
                case 0x0011 -> "Subset of IOCA FS11 data";
                default -> "** Unknown Level **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xE5C7) { // Graphics Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF20 -> "GOCA DR/2V0 data";
                case 0xFF30 -> "GOCA GRS3 data";
                default -> "** Unknown Level **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC2C3) { // Bar Code Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF10 -> "BCOCA BCD1 data";
                case 0xFF20 -> "BCOCA BCD2 data";
                default -> "** Unknown Level **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD6C3) { // Object Container Command Set
            return "No levels defined";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD4C4) { // MO1 Subset of the Metadata Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "MOCA MS1 data" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD6D3) { // Overlay Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "OL1 subset" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD7E2) { // Page Segment Command Set
            return vector.getLevelOrSubsetId() == 0xFF10 ? "PS1 subset" : "** Unknown Level **";
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC3C6) { // Loaded Font Command Set
            return switch (vector.getLevelOrSubsetId()) {
                case 0xFF10 -> "LF1 subset ID; fully described font plus font index";
                case 0xFF20 -> "LF2 subset ID; symbol set coded font";
                case 0xFF30 -> "LF3 subset ID; code page plus font character set";
                case 0xFF40 -> "LF4 subset ID; code page";
                default -> "** Unknown Level **";
            };
        }

        return "** Unknown Level **";
    }

    /**
     * Returns a textual description of a property pair.
     */
    private static String decodePropertyPair(
            final SenseTypeAndModelAcknowledgeData.CommandSetVector vector,
            final Integer propertyPair) {

        if (vector.getSubsetIdOrCommandSetId() == 0xC4C3) { // Device Control Command Set
            return switch (propertyPair) {
                case 0x6001 -> "Multiple copy & copy-subgroup support in LCC";
                case 0x6002 -> "Media-source-selection support in LCC";
                case 0x6003 -> "Media-destination-selection support in LCC";
                case 0x6004 -> "Full-length LCC commands supported";
                case 0x6005 -> "Full range of font local IDs supported in LFE and LPD commands";
                case 0x6006 -> "Font-modification flags supported in LFE commands (byte 14, bits 3-7)";
                case 0x6007 -> "Short LPD commands supported";
                case 0x6008 -> "Full range of logical-page-offset values supported in LPP commands";
                case 0x6009 -> "Empty LFE commands supported";
                case 0x6101 -> "Explicit page placement and orientation support in the LPP command";
                case 0x6201 -> "Logical page and object area coloring support";
                case 0x7001 -> "Manage IPDS Dialog (MID) command support";
                case 0x7002 -> "Apply Finishing Operations (AFO) command";
                case 0x7008 -> "Set Presentation Environment (SPE) command support";
                case 0x700A -> "Activate Setup Name (ASN) command support";
                case 0x701C -> "Retired item 134";
                case 0x702E -> "Activate Resource command support";
                case 0x7034 -> "Presentation Fidelity Control command support";
                case 0x706B -> "Invoke CMR (ICMR) command support";
                case 0x707B -> "Rasterize Presentation Object (RPO) command support";
                case 0x707E -> "Include Saved Page (ISP) command support";
                case 0x70CE -> "DUA command-support property ID";
                case 0x8000 -> "Retired item 108";
                case 0x8001 -> "Retired item 109";
                case 0x8002 -> "Retired item 110";
                case 0x8006 -> "Retired item 111";
                case 0x8008 -> "Mark Form";
                case 0x800A -> "Alternate Offset Stacker";
                case 0x800C -> "Control Edge Marks";
                case 0x8010 -> "Activate Printer Alarm";
                case 0x80F1 -> "Retired item 112";
                case 0x80F2 -> "Discard Buffered Data";
                case 0x80F3 -> "Retired item 113";
                case 0x80F4 -> "Request Resource List";
                case 0x80F5 -> "Discard Unstacked Pages";
                case 0x80F6 -> "Exception-Handling Control";
                case 0x80F8 -> "Print-Quality Control";
                case 0x80F9 -> "Obtain Additional Exception";
                case 0x80FA -> "Request Setup Name List";
                case 0x9000 -> "Retired item 114";
                case 0x9001 -> "Print Buffered Data";
                case 0x9002 -> "Deactivate Saved Page Group";
                case 0x9003 -> "Specify Group Operation";
                case 0x9004 -> "Define Group Boundary";
                case 0x9005 -> "Erase Residual Print Data";
                case 0x9007 -> "Erase Residual Font Data";
                case 0x9009 -> "Separate Continuous Forms";
                case 0x900A -> "Remove Saved Page Group";
                case 0x900B -> "Retired item 115";
                case 0x900D -> "Stack Received Pages";
                case 0x900E -> "Select Medium Modifications";
                case 0x9013 -> "Eject to Front Facing";
                case 0x9015 -> "Select Input Media Source";
                case 0x9016 -> "Set Media Origin";
                case 0x9017 -> "Set Media Size";
                case 0x90D0 -> "Retired item 126";
                case 0x90F2 -> "Trace";
                case 0x90F3 -> "Obtain Printer Characteristics";
                case 0x90F4 -> "Retired item 116";
                case 0x90F5 -> "Page Counters Control";
                case 0xE000 -> "CMRs can be captured";
                case 0xE001 -> "Host-activated link color-conversion (subset “LK”) CMRs supported";
                case 0xE002 -> "Host-activated, non-generic halftone CMRs supported";
                case 0xE003 -> "Host-activated, non-generic tone-transfer-curve CMRs supported";
                case 0xE004 -> "Host-activated indexed CMRs supported";
                case 0xE006 -> "Host-activated ICC DeviceLink CMRs supported";
                case 0xE100 -> "CMRs can be reliably applied to all EPS/PDF objects";
                case 0xE102 -> "Pass-through audit color-conversion CMRs supported";
                case 0xF001 -> "End Persistent NACK without leaving IPDS mode";
                case 0xF002 -> "Blank sheets are emitted when paper movement is stopped";
                case 0xF003 -> "Long ACK support, up to 65,535 byte long Acknowledge Replies";
                case 0xF004 -> "Grayscale simulation supported";
                case 0xF005 -> "Grayscale simulation supported for device-default-monochrome device appearance";
                case 0xF100 -> "An IPDS intermediate device is present";
                case 0xF101 -> "UP3I finishing supported";
                case 0xF102 -> "Media feed direction returned in the XOH-OPC reply Printable-Area self-defining field";
                case 0xF200 -> "Local Date and Time Stamp (X'62') triplets supported in AR commands";
                case 0xF201 -> "Activation-failed NACK support";
                case 0xF202 -> "Font Resolution and Metric Technology (X'84') triplets supported in AR commands";
                case 0xF203 -> "Metric Adjustment (X'79') triplets supported in AR commands";
                case 0xF204 -> "Data-object font support";
                case 0xF205 -> "Color Management triplet support in "
                        + "IDO, LPD, RPO, SPE, WBCC, WGC, WIC2, WOCC, and WTC commands";
                case 0xF206 -> "Device Appearance (X'97') triplet support";
                case 0xF209 -> "Extended copy set number format supported in the Group Information (X'6E') triplet";
                case 0xF211 -> "Character-encoded object names in AR commands";
                case 0xF212 -> "QR Code with Image tertiary resource support";
                case 0xF401 -> "XOA-RRL Multiple Entry Query Support";
                case 0xF402 -> "Retired";
                case 0xF403 -> "Detailed settings support in XOA RSNL";
                case 0xF601 -> "Position-Check Highlighting Support in XOA EHC";
                case 0xF602 -> "Independent Exception Page-Print in XOA EHC";
                case 0xF603 -> "Support for operator-directed recovery in XOA EHC";
                case 0xF604 -> "Support for Page-Continuation Actions";
                case 0xF605 -> "Support for Skip-and-Continue Actions";
                case 0xF701 -> "Simplex 1-up supported in the LCC command";
                case 0xF702 -> "Simplex 2-up supported in the LCC command";
                case 0xF703 -> "Simplex 3-up supported in the LCC command";
                case 0xF704 -> "Simplex 4-up supported in the LCC command";
                case 0xF801 -> "Simplex and duplex 1-up supported in the LCC command";
                case 0xF802 -> "Simplex and duplex 2-up supported in the LCC command";
                case 0xF803 -> "Simplex and duplex 3-up supported in the LCC command";
                case 0xF804 -> "Simplex and duplex 4-up supported in the LCC command";
                case 0xF902 -> "Basic cut-sheet emulation mode supported";
                case 0xFA00 -> "XOH PCC X'02' counter update support";
                case 0xFB00 -> "All architected units of measure supported";
                case 0xFC00 -> "All function listed for IS/3 is supported";
                case 0xFC01 -> "All function listed for MO:DCA GA is supported";
                case 0xFF01 -> "Positioning Exception Sense Format Supported";
                case 0xFF02 -> "Three-Byte Sense Data Support";
                case 0xFF03 -> "Internal rendering intent support in XOH Trace";

                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD7E3) { // Text Command Set
            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0x5000 && propertyPair <= 0x50FF) {
                return "Multiple text-orientation support for all supported media origins";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WTC-TAP object area orientation support";
            }

            return switch (propertyPair) {
                case 0x1000 -> "Optimum performance if text data is in an ordered page";
                case 0x1001 -> "Unordered text supported";
                case 0x2001 -> "Text object support; includes support for the WTC command";
                case 0x2002 -> "Full range of text suppression IDs supported in LCC and LE commands";
                case 0x4303 -> "Support for PTOCA glyph layout controls";
                case 0x4304 -> "Support of encrypted text string control sequences";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC9D4) { // IM-Image Command Set
            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "Orientation support";
            }

            return switch (propertyPair) {
                case 0x1000 -> "Optimum performance when IM Image is in an ordered page";
                case 0x1001 -> "IM-Image objects may be sent in any order";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC9D6) { // IO-Image Command Set
            if (propertyPair >= 0x2000 && propertyPair <= 0x20FF) {
                return "Retired item 15";
            }

            if (propertyPair >= 0x3000 && propertyPair <= 0x30FF) {
                return "Retired item 16";
            }

            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WIC2-IAP object area orientation support";
            }

            return switch (propertyPair) {
                case 0x1001 -> "IO-Image objects may be sent in any order";
                case 0x1202 -> "IO-Image objects can be downloaded in home state as resources";
                case 0x1206 -> "IO-Image support for LPD extents";
                case 0x1208 -> "Negative object-area positioning";
                case 0x4401 -> "Extended IOCA bilevel color support";
                case 0x4402 -> "Extended IOCA Tile-Set-Color support";
                case 0x4403 -> "Bilevel IO-Image color support on the RPO command";
                case 0x5001 -> "Modified ITU-TSS Modified READ Algorithm (IBM MMR)";
                case 0x5003 -> "Uncompressed image";
                case 0x5006 -> "Run-Length 4 Compression Algorithm";
                case 0x5008 -> "ABIC (bilevel Q-coder) Compression Algorithm (ABIC)";
                case 0x500A -> "Concatenated ABIC";
                case 0x500D -> "TIFF LZW";
                case 0x500E -> "TIFF LZW with Differencing Predictor";
                case 0x5020 -> "Solid Fill Rectangle";
                case 0x5080 -> "ITU-TSS T.4 Facsimile Coding Scheme";
                case 0x5081 -> "ITU-TSS T.4 Facsimile Coding Scheme";
                case 0x5082 -> "ITU-TSS T.6 Facsimile Coding Scheme (G4 MMR)";
                case 0x5083 -> "ISO/ITU-TSS JPEG algorithms";
                case 0x5084 -> "JBIG2 Compression Algorithm";
                case 0x5101 -> "Bit ordering supported in the IOCA Image Encoding Parameter";
                case 0x5204 -> "Unpadded RIDIC recording algorithm";
                case 0x5308 -> "IDE size = 8 supported";
                case 0x5501 -> "Transparency masks";
                case 0x5505 -> "Multiple image content support";
                case 0x5506 -> "nColor Names parameter supported";
                case 0xF300 -> "Replicate-and-trim mapping supported";
                case 0xF301 -> "Scale-to-fill mapping supported";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xE5C7) { // Graphics Command Set
            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WGC-GAP object area orientation support";
            }

            return switch (propertyPair) {
                case 0x1001 -> "Graphics objects may be sent in any order";
                case 0x1207 -> "Support for GOCA image resolution in the WGC-GDD";
                case 0x1208 -> "Negative object-area positioning";
                case 0x4100 -> "Set Process Color drawing order supported";
                case 0x4101 -> "Box drawing orders supported";
                case 0x4102 -> "Partial Arc drawing orders supported";
                case 0x4106 -> "Set Fractional Line Width drawing order supported";
                case 0x4107 -> "Cubic Bézier Curve drawing orders";
                case 0x4108 -> "Set default support in GDD for Normal Line Width";
                case 0x4109 -> "Set default support in GDD for Process Color";
                case 0x4110 -> "Set Line End drawing order supported";
                case 0x4111 -> "Set Line Join drawing order supported";
                case 0x4112 -> "Clockwise full and partial arcs supported";
                case 0x4113 -> "Nonzero Winding mode supported";
                case 0x4114 -> "Clockwise boxes supported";
                case 0x4115 -> "Custom line types supported";
                case 0x4116 -> "Font positioning method used for GOCA character positioning";
                case 0x4117 -> "Cell positioning method used for GOCA character positioning";
                case 0x4130 -> "Custom patterns supported";
                case 0x4131 -> "Gradients supported for area fill";
                case 0x4132 -> "Marker size supported";
                case 0xF300 -> "Retired item 135";
                case 0xF301 -> "Scale-to-fill mapping supported";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC2C3) { // Bar Code Command Set
            if (propertyPair >= 0x4000 && propertyPair <= 0x40FF) {
                return "Standard OCA color-support";
            }

            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WBCC-BCAP object area orientation support";
            }

            return switch (propertyPair) {
                case 0x1001 -> "Bar code objects may be sent in any order";
                case 0x1208 -> "Negative object-area positioning";
                case 0x1300 -> "Small-symbol support";
                case 0x1301 -> "Retired item 139";
                case 0x1302 -> "Desired-symbol-width parameter supported in the Bar Code Symbol Descriptor";
                case 0x1303 -> "Data Matrix encodation scheme support";
                case 0x1304 -> "Full range of font local IDs supported in WBCC-BCDD";
                case 0x1305 -> "Support for bar code suppression";
                case 0x1306 -> "Support for the too-much-data flag in the QR Code Special-Function Parameters";
                case 0x1307 -> "Support for the too-much-data flag in the Data Matrix Special-Function Parameters";
                case 0x4400 -> "Extended bar code color support";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD6C3) { // Object Container Command Set
            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "WOCC-OCAP and IDO-DOAP object area orientation support";
            }

            return switch (propertyPair) {
                case 0x1201 -> "Data-object-resource support";
                case 0x1203 -> "Object Container Presentation Space Size (X'9C') triplet supported for "
                        + "PDF objects in IDO, RPO, and WOCC commands";
                case 0x1204 -> "Remove Resident Resource (RRR) command support";
                case 0x1205 -> "Request Resident Resource List (RRRL) command support";
                case 0x1208 -> "Negative object-area positioning";
                case 0x1209 -> "Object Container Presentation Space Size (X'9C') triplet supported for "
                        + "SVG objects in IDO, RPO, and WOCC commands";
                case 0x120A -> "Extension entries supported in the DORE command";
                case 0x120B -> "Retired item 149";
                case 0x120D -> "TrueType/OpenType Fonts supported as secondary resources in the DORE2 command";
                case 0x120E -> "Data Object Resource Equivalence 2 (DORE2) command support";
                case 0x5800 -> "Image Resolution (X'9A') triplet supported in IDO, RPO, and WOCC commands";
                case 0x5801 -> "Bilevel and grayscale image color support for object containers";
                case 0xF301 -> "Scale-to-fill mapping supported";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD4C4) { // MO1 Subset of the Metadata Command Set
            return switch (propertyPair) {
                case 0xD001 -> "Support for the AFP Tagging format";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD6D3) { // Overlay Command Set
            if (propertyPair > 0x1501 && propertyPair < 0x15FF) {
                return String.format("Overlay nesting up to %d levels is supported", (propertyPair & 0xFF));
            }

            return switch (propertyPair) {
                case 0x1102 -> "Extended overlay support; up to 32511 overlays can be activated at one time";
                case 0x1501 -> "No overlay nesting is supported";
                case 0x15FF -> "255 or more levels of overlay nesting supported";
                case 0x1600 -> "Preprinted form overlay support in LCC and IO commands";
                case 0xA004 -> "Page-overlay-rotation support; all 4 orientations supported in the IO command";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xD7E2) { // Page Segment Command Set
            return switch (propertyPair) {
                case 0x1101 -> "Extended page segment support; up to 32511 page segments can be activated at one time";
                default -> "** Unknown **";
            };
        }

        if (vector.getSubsetIdOrCommandSetId() == 0xC3C6) { // Loaded Font Command Set
            if (propertyPair >= 0xA000 && propertyPair <= 0xA0FF) {
                return "Orientation support";
            }

            return switch (propertyPair) {
                case 0xB001 -> "Double-byte coded fonts and code pages";
                case 0xB002 -> "Underscore width and position parameters in the LFI command are used by printer";
                case 0xB003 -> "GRID-parts fields allowed in the LFC, LFCSC, and LCPC commands";
                case 0xB004 -> "Default character parameters supported in the LCPC command";
                case 0xB005 -> "Extended (Unicode mapping) code page support";
                case 0xC005 -> "Coded-font pattern-technology; Bounded-box raster-font technology";
                case 0xC01E -> "Coded-font pattern-technology; CID-keyed outline-font technology";
                case 0xC01F -> "Coded-font pattern-technology; Type 1 PFB outline-font technology";
                case 0xC100 -> "Coded-font metric-technology; Fixed metrics";
                case 0xC101 -> "Coded-font metric-technology; Relative metrics";
                default -> "** Unknown **";
            };
        }

        return "** Unknown **";
    }
}
