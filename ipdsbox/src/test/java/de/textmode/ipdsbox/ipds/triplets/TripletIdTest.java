package de.textmode.ipdsbox.ipds.triplets;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link TripletId}.
 */
public final class TripletIdTest extends TestCase {

    /**
     * Checks if the reverse mapping of all {@link TripletId}s works.
     */
    public void testAllCommandIds() throws UnknownTripletException {
        for (final TripletId tripletId : TripletId.values()) {
            final TripletId toCheck = TripletId.getFor(tripletId.getId());

            assertEquals(tripletId, toCheck);
            assertEquals(tripletId.getId(), toCheck.getId());
            assertEquals(tripletId.getName(), toCheck.getName());
        }
    }

    /**
     * Checks if the reverse mapping of all {@link TripletId}s works.
     */
    public void testUnknownTripletId() {
        try {
            TripletId.getFor(0x1234);
            fail();
        } catch (final UnknownTripletException e) {
            assertEquals("The Triplet with ID X'1234' is unknown.", e.getMessage());
        }
    }
}
