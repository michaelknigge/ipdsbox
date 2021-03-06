package mk.ipdsbox.ipds.triplets;

import junit.framework.TestCase;
import mk.ipdsbox.core.InvalidIpdsCommandException;
import mk.ipdsbox.ipds.triplets.FinishingOperationTriplet.OperationType;
import mk.ipdsbox.ipds.triplets.FinishingOperationTriplet.ReferenceCorner;

/**
 * JUnit tests of the {@link FinishingOperationTriplet}.
 */
public final class FinishingOperationTripletTest extends TestCase {

    /**
     * Checks if the reverse mapping of all {@link OperationType}s works.
     */
    public void testAllOperationTypes() throws Exception {
        for (final OperationType f : OperationType.values()) {
            final OperationType toCheck = OperationType.getFor(f.getValue());

            assertEquals(f, toCheck);
            assertEquals(f.getValue(), toCheck.getValue());
            assertEquals(f.getDescription(), toCheck.getDescription());
        }
    }

    /**
     * Checks if mapping an invalid OperationType throws an exception.
     */
    public void testUnknownOperationType() {
        try {
            OperationType.getFor(0xD0);
            fail();
        } catch (final InvalidIpdsCommandException e) {
            assert true; // Just to make Checkstyle happy...
        }
    }

    /**
     * Checks if the reverse mapping of all {@link ReferenceCorner}s works.
     */
    public void testAllReferenceCorners() throws Exception {
        for (final ReferenceCorner f : ReferenceCorner.values()) {
            final ReferenceCorner toCheck = ReferenceCorner.getFor(f.getValue());

            assertEquals(f, toCheck);
            assertEquals(f.getValue(), toCheck.getValue());
            assertEquals(f.getDescription(), toCheck.getDescription());
        }
    }

    /**
     * Checks if mapping an invalid ReferenceCorner throws an exception.
     */
    public void testUnknownReferenceCorner() {
        try {
            ReferenceCorner.getFor(0xD0);
            fail();
        } catch (final InvalidIpdsCommandException e) {
            assert true; // Just to make Checkstyle happy...
        }
    }

    /**
     * Test a FinishingOperationTriplet without positions.
     */
    public void testWithoutPositions() throws Exception {
        final String hex = "04000001001234";

        final FinishingOperationTriplet triplet =
            (FinishingOperationTriplet) TripletTest
                .buildTriplet(TripletId.FinishingOperation, hex);

        assertEquals(OperationType.FoldIn, triplet.getOperationType());
        assertEquals(ReferenceCorner.TopRightCorner, triplet.getReferenceCorner());
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

        assertEquals(OperationType.FoldIn, triplet.getOperationType());
        assertEquals(ReferenceCorner.TopRightCorner, triplet.getReferenceCorner());
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

        assertEquals(OperationType.FoldIn, triplet.getOperationType());
        assertEquals(ReferenceCorner.TopRightCorner, triplet.getReferenceCorner());
        assertEquals(0x02, triplet.getCount());
        assertEquals(0x1234, triplet.getAxisOffset());
        assertEquals(2, triplet.getPositions().size());
        assertEquals(Integer.valueOf(0x2345), triplet.getPositions().get(0));
        assertEquals(Integer.valueOf(0x0007), triplet.getPositions().get(1));
    }
}
