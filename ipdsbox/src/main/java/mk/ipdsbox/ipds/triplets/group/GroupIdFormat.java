package mk.ipdsbox.ipds.triplets.group;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all known formats of the Group ID Triplet.
 */
public enum GroupIdFormat {
    /**
     * MVS and VSE print-data format.
     */
    MVS_AND_VSE(0x01, "MVS and VSE print-data format"),

    /**
     * VM print-data format.
     */
    VM(0x02, "VM print-data format"),

    /**
     * OS/400 print-data format.
     */
    OS400(0x03, "OS/400 print-data format"),

    /**
     * MVS and VSE COM-data format.
     */
    MVS_AND_VSE_COM(0x04, "MVS and VSE COM-data format"),

    /**
     * AIX and OS/2 COM-data format.
     */
    AIX_AND_OS2(0x05, "AIX and OS/2 COM-data format"),

    /**
     * AIX and Windows print-data.
     */
    AIX_AND_WINDOWS(0x06, "AIX and Windows print-data format"),

    /**
     *Variable-length Group ID format.
     */
    VARIABLE_LENGTH_GROUP_ID(0x08, "Variable-length Group ID format"),

    /**
     * Extended OS/400 print-data format.
     */
    EXTENDED_OS400(0x13, "Extended OS/400 print-data format");

    private static final Map<Integer, GroupIdFormat> REVERSE_MAP = new HashMap<>();

    static {
        for (final GroupIdFormat tripletId : values()) {
            REVERSE_MAP.put(tripletId.getId(), tripletId);
        }
    }

    private final int id;
    private final String meaning;

    /**
     * Constructor of the enum value.
     */
    GroupIdFormat(final int id, final String meaning) {
        this.id = id;
        this.meaning = meaning;
    }

    /**
     * Gets the enum value for the given integer.
     * @return the enum value for the given integer or <code>null</code> if the value is unknown.
     */
    public static GroupIdFormat getForIfExists(final int value) {
        return REVERSE_MAP.get(value);
    }

    /**
     * Gets the integer value of the Group ID Format.
     * @return the integer value of the Group ID Format.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the meaning of the Group ID Format.
     * @return The meaning of the Group ID Format (i. e. "AIX and Windows print-data").
     */
    public String getMeaning() {
        return this.meaning;
    }
}
