package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;
import java.util.HexFormat;

import de.textmode.ipdsbox.core.StringUtils;
import junit.framework.TestCase;

/**
 * JUnit tests of the {@link Triplet}.
 */
public final class TripletTest extends TestCase {

    /**
     * Builds a {@link Triplet} of the given type with the supplied content.
     */
    public static Triplet buildTriplet(final TripletId id, final String hex) throws IOException {
        final int len = (hex.length() / 2) + 2;
        final String withLen = String.format("%02X%02X", len, id.getId()) + hex;

        return TripletFactory.create(HexFormat.of().parseHex(withLen));
    }

    /**
     * Builds a {@link Triplet} of the given type with the supplied content.
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

        assertEquals(TripletId.GroupID.getId(), testTriplet.getTripletId());
        assertEquals(-1, testTriplet.getFormat());
        assertEquals(null, testTriplet.getGroupIdData());
    }

    /**
     * Handle a Triplet that has just a few bytes of data.
     */
    public void testWithSomeBytesOfData() throws Exception {

        final GroupIdTriplet testTriplet = (GroupIdTriplet) TripletFactory.create(HexFormat.of().parseHex("060006313233"));

        assertEquals(TripletId.GroupID.getId(), testTriplet.getTripletId());
        assertEquals(0x06, testTriplet.getFormat());

        assertEquals("FILE NAME=123", testTriplet.getGroupIdData().toString());
        assertEquals("313233", StringUtils.toHexString(testTriplet.getGroupIdData().toByteArray()));
    }
}
