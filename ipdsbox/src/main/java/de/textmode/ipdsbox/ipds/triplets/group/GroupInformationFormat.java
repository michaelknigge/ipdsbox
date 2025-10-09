package de.textmode.ipdsbox.ipds.triplets.group;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all known formats of the Group Information Triplet.
 */
// TOOD: Do we really need this enum in ipdsbox? Guess this shoul better be implemented in a conrete app...
public enum GroupInformationFormat {
    /**
     * Microfilm save/restore format.
     */
    MICROFILM_SAVE_RESTORE(0x01, "Microfilm save/restore format"),

    /**
     * Copy set number format.
     */
    COPY_SET_NUMBER(0x02, "Copy set number format"),

    /**
     * Group name format.
     */
    GROUP_NAME(0x03, "Group name format"),

    /**
     * Additional information format.
     */
    ADDITIONAL_INFORMATION(0x04, "Additional information format"),

    /**
     * Page count format.
     */
    PAGE_COUNT(0x05, "Page count format"),

    /**
     * Extended copy set number format.
     */
    EXTENDED_COPY_SET_NUMBER(0x82, "Extended copy set number format");

    private static final Map<Integer, GroupInformationFormat> REVERSE_MAP = new HashMap<>();

    static {
        for (final GroupInformationFormat tripletId : values()) {
            REVERSE_MAP.put(tripletId.getId(), tripletId);
        }
    }

    private final int id;
    private final String meaning;

    /**
     * Constructor of the enum value.
     */
    GroupInformationFormat(final int id, final String meaning) {
        this.id = id;
        this.meaning = meaning;
    }

    /**
     * Gets the enum value for the given integer.
     * @return the enum value for the given integer or <code>null</code> if the value is unknown.
     */
    public static GroupInformationFormat getForIfExists(final int value) {
        return REVERSE_MAP.get(value);
    }

    /**
     * Gets the integer value of the Group Information Format.
     * @return the integer value of the Group Information Format.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the meaning of the Group Information Format.
     * @return The meaning of the Group Information Format (i. e. "Group name format").
     */
    public String getMeaning() {
        return this.meaning;
    }
}
