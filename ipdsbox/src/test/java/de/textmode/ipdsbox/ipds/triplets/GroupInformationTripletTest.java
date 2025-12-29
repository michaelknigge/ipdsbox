package de.textmode.ipdsbox.ipds.triplets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import de.textmode.ipdsbox.ipds.triplets.group.AdditionalInformationFormat;
import de.textmode.ipdsbox.ipds.triplets.group.CopySetNumberFormat;
import de.textmode.ipdsbox.ipds.triplets.group.GroupNameFormat;
import de.textmode.ipdsbox.ipds.triplets.group.MicrofilmSaveRestoreFormat;
import de.textmode.ipdsbox.ipds.triplets.group.PageCountFormat;
import junit.framework.TestCase;

/**
 * JUnit tests of the {@link GroupInformationTriplet}.
 */
public final class GroupInformationTripletTest extends TestCase {

    /**
     * Tests the {@link GroupInformationTriplet} with {@link MicrofilmSaveRestoreFormat}.
     */
    public void testFormatX01() throws Exception {
        this.testFormatX01(0x80, "Save microfilm information");
        this.testFormatX01(0x40, "Restore microfilm information");
    }

    /**
     * Tests the {@link GroupInformationTriplet} with {@link MicrofilmSaveRestoreFormat}.
     */
    private void testFormatX01(final int value, final String expected) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x01);
        baos.write(value);

        final GroupInformationTriplet triplet =
                (GroupInformationTriplet) TripletTest.buildTriplet(TripletId.GroupInformation, baos.toByteArray());
        assertEquals(0x01, triplet.getFormat());

        assertEquals(expected, triplet.getGroupInformationDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupInformationTriplet} with {@link CopySetNumberFormat}.
     */
    public void testFormatX02() throws Exception {
        this.testFormatX02(0x00, "No copy set number provided");
        this.testFormatX02(0x1234, "Copy set number 4660");
        this.testFormatX02(0xFFFF, "Copy set number is larger than 65534");
    }

    /**
     * Tests the {@link GroupInformationTriplet} with {@link CopySetNumberFormat}.
     */
    private void testFormatX02(final int value, final String expected) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x02);
        baos.write((value & 0xFF00) >> 8);
        baos.write(value & 0x00FF);

        final GroupInformationTriplet triplet =
            (GroupInformationTriplet) TripletTest.buildTriplet(TripletId.GroupInformation, baos.toByteArray());
        assertEquals(0x02, triplet.getFormat());

        assertEquals(expected, triplet.getGroupInformationDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupInformationTriplet} with {@link GroupNameFormat}.
     */
    public void testFormatX03() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x03);
        baos.write(0x01);
        baos.write(0x02);
        baos.write(0x03);

        final GroupInformationTriplet triplet =
            (GroupInformationTriplet) TripletTest.buildTriplet(TripletId.GroupInformation, baos.toByteArray());

        assertEquals("Group name = 010203 (hex)", triplet.getGroupInformationDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupInformationTriplet} with {@link AdditionalInformationFormat}.
     */
    public void testFormatX04() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x04);
        baos.write(0x01);
        baos.write(0x02);
        baos.write(0x03);

        final GroupInformationTriplet triplet =
            (GroupInformationTriplet) TripletTest.buildTriplet(TripletId.GroupInformation, baos.toByteArray());

        assertEquals("Group information = 010203 (hex)", triplet.getGroupInformationDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupInformationTriplet} with {@link PageCountFormat}.
     */
    public void testFormatX05() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream(baos);
        dos.write(0x05);
        dos.writeLong(0x0102030405060708L);

        final GroupInformationTriplet triplet =
            (GroupInformationTriplet) TripletTest.buildTriplet(TripletId.GroupInformation, baos.toByteArray());

        assertEquals("72623859790382856 pages", triplet.getGroupInformationDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupInformationTriplet} with {@link CopySetNumberFormat}.
     */
    public void testFormatX82() throws Exception {
        this.testFormatX82(0x00, 0x00, "No copy set number provided, no number of copies provided");
        this.testFormatX82(0x00, 0x1234, "No copy set number provided, number of copies 4660");
        this.testFormatX82(0x00, 0xFFFFFFFFL, "No copy set number provided, number of copies 4294967295");

        this.testFormatX82(0x11, 0x00, "Copy set number 17, no number of copies provided");
        this.testFormatX82(0x11, 0x1234, "Copy set number 17, number of copies 4660");
        this.testFormatX82(0x11, 0xFFFFFFFFL, "Copy set number 17, number of copies 4294967295");

        this.testFormatX82(0xFFFFFFFFL, 0x00, "Copy set number 4294967295, no number of copies provided");
        this.testFormatX82(0xFFFFFFFFL, 0x1234, "Copy set number 4294967295, number of copies 4660");
        this.testFormatX82(0xFFFFFFFFL, 0xFFFFFFFFL, "Copy set number 4294967295, number of copies 4294967295");
    }

    /**
     * Tests the {@link GroupInformationTriplet} with {@link CopySetNumberFormat}.
     */
    public void testFormatX82(final long copySet, final long total, final String expected) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream(baos);
        dos.write(0x82);
        dos.writeInt((int) (copySet & 0xFFFFFFFF));
        dos.writeInt((int) (total & 0xFFFFFFFF));

        final GroupInformationTriplet triplet =
            (GroupInformationTriplet) TripletTest.buildTriplet(TripletId.GroupInformation, baos.toByteArray());
        assertEquals(0x82, triplet.getFormat());

        assertEquals(expected, triplet.getGroupInformationDataIfExist().toString());
    }
}
