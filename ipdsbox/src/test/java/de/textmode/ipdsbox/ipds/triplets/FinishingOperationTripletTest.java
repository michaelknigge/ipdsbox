package de.textmode.ipdsbox.ipds.triplets;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link FinishingOperationTriplet}.
 */
public final class FinishingOperationTripletTest extends TestCase {

    /**
     * Test a FinishingOperationTriplet without positions.
     */
    public void testWithoutPositions() throws Exception {
        final String hex = "04000001001234";

        final FinishingOperationTriplet triplet =
            (FinishingOperationTriplet) TripletTest
                .buildTriplet(TripletId.FinishingOperation, hex);

        assertEquals(0x04, triplet.getOperationType());
        assertEquals(0x01, triplet.getReference());
        assertEquals(0x00, triplet.getCount());
        assertEquals(0x1234, triplet.getAxisOffset());
        assertTrue(triplet.getPositions().isEmpty());
    }

    /**
     * Test a FinishingOperationTriplet with a single position.
     */
    public void testWithOnePosition() throws Exception {
        final String hex = "040000010112342345";

        final FinishingOperationTriplet triplet =
            (FinishingOperationTriplet) TripletTest
                .buildTriplet(TripletId.FinishingOperation, hex);

        assertEquals(0x04, triplet.getOperationType());
        assertEquals(0x01, triplet.getReference());
        assertEquals(0x01, triplet.getCount());
        assertEquals(0x1234, triplet.getAxisOffset());
        assertEquals(1, triplet.getPositions().size());
        assertEquals(Integer.valueOf(0x2345), triplet.getPositions().get(0));
    }

    /**
     * Test a FinishingOperationTriplet with multiple positions.
     */
    public void testWithMultiplePositions() throws Exception {
        final String hex = "0400000102123423450007";

        final FinishingOperationTriplet triplet =
            (FinishingOperationTriplet) TripletTest
                .buildTriplet(TripletId.FinishingOperation, hex);

        assertEquals(0x04, triplet.getOperationType());
        assertEquals(0x01, triplet.getReference());
        assertEquals(0x02, triplet.getCount());
        assertEquals(0x1234, triplet.getAxisOffset());
        assertEquals(2, triplet.getPositions().size());
        assertEquals(Integer.valueOf(0x2345), triplet.getPositions().get(0));
        assertEquals(Integer.valueOf(0x0007), triplet.getPositions().get(1));
    }
}
