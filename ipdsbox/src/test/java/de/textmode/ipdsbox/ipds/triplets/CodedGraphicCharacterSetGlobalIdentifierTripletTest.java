package de.textmode.ipdsbox.ipds.triplets;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link CodedGraphicCharacterSetGlobalIdentifierTriplet}.
 */
public final class CodedGraphicCharacterSetGlobalIdentifierTripletTest extends TestCase {

    /**
     * Test GCSGID/CPGID form of the Triplet.
     */
    public void testWithGCSGID() throws Exception {
        final String hex = "00020004";

        final CodedGraphicCharacterSetGlobalIdentifierTriplet triplet =
            (CodedGraphicCharacterSetGlobalIdentifierTriplet) TripletTest
                .buildTriplet(TripletId.CodedGraphicCharacterSetGlobalIdentifier, hex);

        assertFalse(triplet.hasCodedCharacterSetIdentifier());
        assertEquals(0x0002, triplet.getGraphicCharacterSetGlobalIdentifier());
        assertEquals(0x0004, triplet.getCodePageGlobalIdentifier());
    }

    /**
     * Test CCSID form of the Triplet.
     */
    public void testWithCCSID() throws Exception {
        final String hex = "00000006";

        final CodedGraphicCharacterSetGlobalIdentifierTriplet triplet =
            (CodedGraphicCharacterSetGlobalIdentifierTriplet) TripletTest
                .buildTriplet(TripletId.CodedGraphicCharacterSetGlobalIdentifier, hex);

        assertTrue(triplet.hasCodedCharacterSetIdentifier());
        assertEquals(0x0006, triplet.getCodedCharacterSetIdentifier());
    }
}
