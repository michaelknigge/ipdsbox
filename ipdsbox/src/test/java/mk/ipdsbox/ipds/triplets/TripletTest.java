package mk.ipdsbox.ipds.triplets;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link Triplet}.
 */
public final class TripletTest extends TestCase {

    /**
     * Handle a Triplet that has just a few bytes of data.
     */
    public void testWithSomeBytesOfData() {

        final Triplet testTriplet = new Triplet(DatatypeConverter.parseHexBinary("065000007807")) {
        };

        assertEquals(6, testTriplet.getLength());
        assertEquals(TripletId.EncodingSchemeID, testTriplet.getTripletId());
        assertEquals(4, testTriplet.getData().length);

        assertEquals(0x00, testTriplet.getData()[0]);
        assertEquals(0x00, testTriplet.getData()[1]);
        assertEquals(0x78, testTriplet.getData()[2]);
        assertEquals(0x07, testTriplet.getData()[3]);
    }

    /**
     * Handle a Triplet that has 253 bytes of data.
     */
    public void testWithMaxBytesOfData() {

        final byte[] raw = new byte[255];
        raw[0] = (byte) 0xFF;
        raw[1] = (byte) 0x8D;
        raw[2] = (byte) 0x00;
        raw[3] = (byte) 0x01;
        raw[4] = (byte) 0x02;

        for (int i = 5; i < 255; ++i) {
            raw[i] = (byte) i;
        }

        final Triplet testTriplet = new Triplet(raw) {
        };

        assertEquals(255, testTriplet.getLength());
        assertEquals(TripletId.LinkedFont, testTriplet.getTripletId());
        assertEquals(253, testTriplet.getData().length);

        assertEquals(0x00, testTriplet.getData()[0]);
        assertEquals(0x01, testTriplet.getData()[1]);
        assertEquals(0x02, testTriplet.getData()[2]);
        assertEquals(0x05, testTriplet.getData()[3]);
        // .... 0x06, 0x07, 0x08, .....
        assertEquals(254, testTriplet.getData()[252] & 0xFF);
    }
}
