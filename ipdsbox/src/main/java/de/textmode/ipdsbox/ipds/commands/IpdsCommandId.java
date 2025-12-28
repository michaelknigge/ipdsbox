package de.textmode.ipdsbox.ipds.commands;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all known IPDS command id's.
 */
public enum IpdsCommandId {
    /**
     * Acknowledge Reply.
     **/
    ACK(0xD6FF, "Acknowledge Reply"),

    /**
     * Activate Resource.
     **/
    AR(0xD62E, "Activate Resource"),

    /**
     * Apply Finishing Operations.
     */
    AFO(0xD602, "Apply Finishing Operations"),

    /**
     * Begin Page.
     */
    BP(0xD6AF, "Begin Page"),

    /**
     * Deactivate Font.
     */
    DF(0xD64F, "Deactivate Font"),

    /**
     * Define User Area.
     */
    DUA(0xD6CE, "Define User Area"),

    /**
     * End.
     */
    END(0xD65D, "End"),

    /**
     * End Page.
     */
    EP(0xD6BF, "End Page"),

    /**
     * Execute Order Anystate.
     */
    XOA(0xD633, "Execute Order Anystate"),

    /**
     * Execute Order Home State.
     */
    XOH(0xD68F, "Execute Order Home State"),

    /**
     * Include Saved Page.
     */
    ISP(0xD67E, "Include Saved Page"),

    /**
     * Invoke CMR.
     */
    ICMR(0xD66B, "Invoke CMR"),

    /**
     * Load Copy Control.
     */
    LCC(0xD69F, "Load Copy Control"),

    /**
     * Load Font Equivalence.
     */
    LFE(0xD63F, "Load Font Equivalence"),

    /**
     * Logical Page Descriptor.
     */
    LPD(0xD6CF, "Logical Page Descriptor"),

    /**
     * Logical Page Position.
     */
    LPP(0xD66D, "Logical Page Position"),

    /**
     * Manage IPDS Dialog.
     */
    MID(0xD601, "Manage IPDS Dialog"),

    /**
     * No Operation.
     */
    NOP(0xD603, "No Operation"),

    /**
     * Presentation Fidelity Control.
     */
    PFC(0xD634, "Presentation Fidelity Control"),

    /**
     * Rasterize Presentation Object.
     */
    RPO(0xD67B, "Rasterize Presentation Object"),

    /**
     * Sense Type and Model.
     */
    STM(0xD6E4, "Sense Type and Model"),

    /**
     * Set Home State.
     */
    SHS(0xD697, "Set Home State"),

    /**
     * Set Presentation Environment.
     */
    SPE(0xD608, "Set Presentation Environment"),

    /**
     * Load Equivalence.
     */
    LE(0xD61D, "Load Equivalence"),

    /**
     * Write Text Control.
     */
    WTC(0xD688, "Write Text Control"),

    /**
     * Write Text.
     */
    WT(0xD62D, "Write Text"),

    /**
     * Write Image Control.
     */
    WIC(0xD63D, "Write Image Control"),

    /**
     * Write Image.
     */
    WI(0xD64D, "Write Image"),

    /**
     * Write Image Control 2.
     */
    WIC2(0xD63E, "Write Image Control 2"),

    /**
     * Write Image 2.
     */
    WI2(0xD64E, "Write Image 2"),

    /**
     * Write Graphics Control.
     */
    WGC(0xD684, "Write Graphics Control"),

    /**
     * Write Graphics.
     */
    WG(0xD685, "Write Graphics"),

    /**
     * Write Bar Code Control.
     */
    WBCC(0xD680, "Write Bar Code Control"),

    /**
     * Write Bar Code.
     */
    WBC(0xD681, "Write Bar Code"),

    /**
     * Data Object Resource Equivalence.
     */
    DORE(0xD66C, "Data Object Resource Equivalence"),

    /**
     * Deactivate Data-Object-Font Component.
     */
    DDOFC(0xD65B, "Deactivate Data-Object-Font Component"),

    /**
     * Deactivate Data Object Resource.
     */
    DDOR(0xD65C, "Deactivate Data Object Resource"),

    /**
     * Include Data Object.
     */
    IDO(0xD67C, "Include Data Object"),

    /**
     * Remove Resident Resource.
     */
    RRR(0xD65A, "Remove Resident Resource"),

    /**
     * Request Resident Resource List.
     */
    RRRL(0xD659, "Request Resident Resource List"),

    /**
     * Write Object Container Control.
     */
    WOCC(0xD63C, "Write Object Container Control"),

    /**
     * Write Object Container.
     */
    WOC(0xD64C, "Write Object Container"),

    /**
     * Begin Overlay.
     */
    BO(0xD6DF, "Begin Overlay"),

    /**
     * Deactivate Overlay.
     */
    DO(0xD6EF, "Deactivate Overlay"),

    /**
     * Include Overlay.
     */
    IO(0xD67D, "Include Overlay"),

    /**
     * Begin Page Segment.
     */
    BPS(0xD65F, "Begin Page Segment"),

    /**
     * Deactivate Page Segment.
     */
    DPS(0xD66F, "Deactivate Page Segment"),

    /**
     * Include Page Segment.
     */
    IPS(0xD67F, "Include Page Segment"),

    /**
     * Load Code Page Control.
     */
    LCPC(0xD61A, "Load Code Page Control"),

    /**
     * Load Code Page.
     */
    LCP(0xD61B, "Load Code Page"),

    /**
     * Load Font Character Set Control.
     */
    LFCSC(0xD619, "Load Font Character Set Control"),

    /**
     * Load Font Control.
     */
    LFC(0xD61F, "Load Font Control"),

    /**
     * Load Font Index.
     */
    LFI(0xD60F, "Load Font Index"),

    /**
     * Load Font.
     */
    LF(0xD62F, "Load Font"),

    /**
     * Load Symbol Set.
     */
    LSS(0xD61E, "Load Symbol Set");

    private static final Map<Integer, IpdsCommandId> REVERSE_MAP = new HashMap<>();

    static {
        for (final IpdsCommandId code : values()) {
            REVERSE_MAP.put(code.getValue(), code);
        }
    }

    private final int code;
    private final String description;

    /**
     * Constructor of the enum value.
     */
    IpdsCommandId(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Gets the enum value for the given integer.
     * @return the enum value for the given integer or <code>null</code> if the IPDS command code is unknown.
     */
    public static IpdsCommandId getIfKnown(final int value) {
        return REVERSE_MAP.get(value);
    }

    /**
     * Gets the integer value of the IPDS command code.
     * @return the integer value of the command code (X'D600' - X'D6FF')
     */
    public int getValue() {
        return this.code;
    }

    /**
     * Gets a textual description of the IPDS command.
     * @return A textual description of the IPDS command (i. e. "Begin Page" or "Load Symbol Set").
     */
    public String getDescription() {
        return this.description;
    }
}
