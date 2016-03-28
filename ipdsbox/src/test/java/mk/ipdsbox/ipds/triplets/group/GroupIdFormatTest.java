package mk.ipdsbox.ipds.triplets.group;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link GroupIdFormat}.
 */
public final class GroupIdFormatTest extends TestCase {

    /**
     * Checks if the reverse mapping of all {@link GroupIdFormat}s works.
     */
    public void testAllCommandIds() {
        for (final GroupIdFormat f : GroupIdFormat.values()) {
            final GroupIdFormat toCheck = GroupIdFormat.getForIfExists(f.getId());

            assertEquals(f, toCheck);
            assertEquals(f.getId(), toCheck.getId());
            assertEquals(f.getMeaning(), toCheck.getMeaning());
        }
    }

    /**
     * Checks if the reverse mapping of all {@link GroupIdFormat}s works.
     */
    public void testUnknownGroupIdFormat() {
        assertNull(GroupIdFormat.getForIfExists(0x1234));
    }
}
