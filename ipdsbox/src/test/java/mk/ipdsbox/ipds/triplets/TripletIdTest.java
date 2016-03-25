package mk.ipdsbox.ipds.triplets;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link TripletId}.
 */
public final class TripletIdTest extends TestCase {

    /**
     * Checks if the reverse mapping of all {@link TripletId}s works.
     */
    public void testAllCommandIds() {
        for (final TripletId tripletId : TripletId.values()) {
            final TripletId toCheck = TripletId.getForIfExists(tripletId.getId());

            assertEquals(tripletId, toCheck);
            assertEquals(tripletId.getId(), toCheck.getId());
            assertEquals(tripletId.getName(), toCheck.getName());
        }
    }

    /**
     * Checks if the reverse mapping of all {@link TripletId}s works.
     */
    public void testUnknownCommandId() {
        assertNull(TripletId.getForIfExists(0x1234));
    }
}