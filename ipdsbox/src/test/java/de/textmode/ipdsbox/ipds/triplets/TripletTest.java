package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.util.HexFormat;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import junit.framework.TestCase;

/**
 * JUnit tests of the {@link Triplet}.
 */
public final class TripletTest extends TestCase {

    /**
     * Builds a {@link Triplet} of the given type with the supplied content.
     * @param id Id of the {@link Triplet} to be built.
     * @param hex the content of the {@link Triplet} in hex.
     * @return a build {@link Triplet}.
     * @throws IOException if the content of the {@link Triplet} is invalid
     * @throws UnknownTripletException if the {@link TripletId} is unknown.
     * @throws InvalidIpdsCommandException if the IPDS data of the {@link Triplet} is invalid.
     */
    public static Triplet buildTriplet(final TripletId id, final String hex)
        throws UnknownTripletException, IOException, InvalidIpdsCommandException {
        final int len = (hex.length() / 2) + 2;
        final String withLen = String.format("%02X%02X", len, id.getId()) + hex;

        return TripletBuilder.build(HexFormat.of().parseHex(withLen));
    }

    /**
     * Builds a {@link Triplet} of the given type with the supplied content.
     * @param id Id of the {@link Triplet} to be built.
     * @param raw the content of the {@link Triplet}.
     * @return a build {@link Triplet}.
     * @throws IOException if the content of the {@link Triplet} is invalid
     * @throws UnknownTripletException if the {@link TripletId} is unknown.
     * @throws InvalidIpdsCommandException ths the {@link Triplet} data is invalid.
     */
    public static Triplet buildTriplet(final TripletId id, final byte[] raw)
        throws UnknownTripletException, IOException, InvalidIpdsCommandException {
        final byte[] result = new byte[raw.length + 2];
        result[0] = (byte) (raw.length + 2);
        result[1] = (byte) (id.getId() & 0xFF);
        System.arraycopy(raw, 0, result, 2, raw.length);

        return TripletBuilder.build(result);
    }

    /**
     * Handle a Triplet that has no "payload". We use the "Group ID" Triplet that may
     * contain grouping information, but also may not....
     */
    public void testWithNoData() throws Exception {

        final Triplet testTriplet = TripletBuilder.build(HexFormat.of().parseHex("0200"));

        assertEquals(2, testTriplet.getLength());
        assertEquals(TripletId.GroupID, testTriplet.getTripletId());
        assertEquals(0, testTriplet.getData().length);
    }

    /**
     * Handle a Triplet that has just a few bytes of data.
     */
    public void testWithSomeBytesOfData() throws Exception {

        final Triplet testTriplet = TripletBuilder.build(HexFormat.of().parseHex("060006007807"));

        assertEquals(6, testTriplet.getLength());
        assertEquals(TripletId.GroupID, testTriplet.getTripletId());
        assertEquals(4, testTriplet.getData().length);

        assertEquals(0x06, testTriplet.getData()[0]);
        assertEquals(0x00, testTriplet.getData()[1]);
        assertEquals(0x78, testTriplet.getData()[2]);
        assertEquals(0x07, testTriplet.getData()[3]);
    }

    /**
     * Handle a Triplet that has 253 bytes of data.
     */
    public void testWithMaxBytesOfData() throws Exception {

        final byte[] raw = new byte[255];
        raw[0] = (byte) 0xFF;
        raw[1] = (byte) 0x00;
        raw[2] = (byte) 0x06;

        for (int i = 3; i < 255; ++i) {
            raw[i] = (byte) i;
        }

        final Triplet testTriplet = TripletBuilder.build(raw);

        assertEquals(255, testTriplet.getLength());
        assertEquals(TripletId.GroupID, testTriplet.getTripletId());
        assertEquals(253, testTriplet.getData().length);

        assertEquals(0x06, testTriplet.getData()[0]);
        assertEquals(0x03, testTriplet.getData()[1]);
        // .... 0x04, 0x05, 0x06, .....
        assertEquals(254, testTriplet.getData()[252] & 0xFF);
    }
}
