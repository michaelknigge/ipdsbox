package de.textmode.ipdsbox.ipds.triplets;

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
            (UP3IFinishingOperationTriplet) TripletTest.buildTriplet(TripletId.UP3IFinishingOperation, "0100");

        assertEquals(0x01, triplet.getSequenceNumber());
        assertEquals(0x00, triplet.getData()[0]);
        assertEquals(0x01, triplet.getData().length);
    }

    /**
     * Tests the Triplet with just multiple bytey of data.
     */
    public void testWithMultipleBytes() throws Exception {
        final UP3IFinishingOperationTriplet triplet =
            (UP3IFinishingOperationTriplet) TripletTest.buildTriplet(TripletId.UP3IFinishingOperation, "FF0001020304");

        assertEquals(0xFF, triplet.getSequenceNumber());
        assertEquals(0x00, triplet.getData()[0]);
        assertEquals(0x01, triplet.getData()[1]);
        assertEquals(0x02, triplet.getData()[2]);
        assertEquals(0x03, triplet.getData()[3]);
        assertEquals(0x04, triplet.getData()[4]);
        assertEquals(0x05, triplet.getData().length);
    }
}
