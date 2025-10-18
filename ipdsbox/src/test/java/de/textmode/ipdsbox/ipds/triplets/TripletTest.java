package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HexFormat;

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
     */
    public static Triplet buildTriplet(final TripletId id, final String hex) throws IOException {
        final int len = (hex.length() / 2) + 2;
        final String withLen = String.format("%02X%02X", len, id.getId()) + hex;

        return TripletFactory.create(HexFormat.of().parseHex(withLen));
    }

    /**
     * Builds a {@link Triplet} of the given type with the supplied content.
     * @param id Id of the {@link Triplet} to be built.
     * @param raw the content of the {@link Triplet}.
     * @return a build {@link Triplet}.
     * @throws IOException if the content of the {@link Triplet} is invalid
     */
    public static Triplet buildTriplet(final TripletId id, final byte[] raw) throws IOException {
        final byte[] result = new byte[raw.length + 2];
        result[0] = (byte) (raw.length + 2);
        result[1] = (byte) (id.getId() & 0xFF);
        System.arraycopy(raw, 0, result, 2, raw.length);

        return TripletFactory.create(result);
    }

    /**
     * Handle a Triplet that has no "payload". We use the "Group ID" Triplet that may
     * contain grouping information, but also may not....
     */
    public void testWithNoData() throws Exception {

        final GroupIdTriplet testTriplet = (GroupIdTriplet) TripletFactory.create(HexFormat.of().parseHex("0200"));

        assertEquals(TripletId.GroupID, testTriplet.getTripletId());
        assertEquals(0x00, testTriplet.getFormat());
        assertEquals(null, testTriplet.getGroupIdData());

    }

    /**
     * Handle a Triplet that has just a few bytes of data.
     */
    public void testWithSomeBytesOfData() throws Exception {

        final GroupIdTriplet testTriplet = (GroupIdTriplet) TripletFactory.create(HexFormat.of().parseHex("060006007807"));

        assertEquals(TripletId.GroupID, testTriplet.getTripletId());
        assertEquals(0x00, testTriplet.getFormat());

        assertTrue(Arrays.equals(
                HexFormat.of().parseHex("06007807"),
                testTriplet.getGroupIdData().toByteArray()));
    }
}
