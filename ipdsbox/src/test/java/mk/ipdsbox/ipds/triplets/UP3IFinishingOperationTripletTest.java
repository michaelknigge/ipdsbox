package mk.ipdsbox.ipds.triplets;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link UP3IFinishingOperationTriplet}.
 */
public final class UP3IFinishingOperationTripletTest extends TestCase {

    /**
     * Tests the Triplet with just one byte of data.
     */
    public void testWithSingleByte() throws Exception {
        final UP3IFinishingOperationTriplet triplet =
            (UP3IFinishingOperationTriplet) TripletTest.buildTriplet(TripletId.UP3IFinishingOperation, "010002");

        assertEquals(0x01, triplet.getSequenceNumber());
        assertEquals(0x02, triplet.getFinishingOperationData()[0]);
        assertEquals(0x01, triplet.getFinishingOperationData().length);
    }

    /**
     * Tests the Triplet with just multiple bytey of data.
     */
    public void testWithMultipleBytes() throws Exception {
        final UP3IFinishingOperationTriplet triplet =
            (UP3IFinishingOperationTriplet) TripletTest.buildTriplet(TripletId.UP3IFinishingOperation, "FF0001020304");

        assertEquals(0xFF, triplet.getSequenceNumber());
        assertEquals(0x01, triplet.getFinishingOperationData()[0]);
        assertEquals(0x02, triplet.getFinishingOperationData()[1]);
        assertEquals(0x03, triplet.getFinishingOperationData()[2]);
        assertEquals(0x04, triplet.getFinishingOperationData()[3]);
        assertEquals(0x04, triplet.getFinishingOperationData().length);
    }
}
