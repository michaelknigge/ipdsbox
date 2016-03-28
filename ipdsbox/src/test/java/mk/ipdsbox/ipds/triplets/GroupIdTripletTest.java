package mk.ipdsbox.ipds.triplets;

import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;
import mk.ipdsbox.core.StringUtils;
import mk.ipdsbox.core.TestHelper;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX01;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX02;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX03;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX04;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX05;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX06;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX08;
import mk.ipdsbox.ipds.triplets.group.GroupIdDataFormatX13;
import mk.ipdsbox.ipds.triplets.group.GroupIdFormat;

/**
 * JUnit tests of the {@link GroupIdTriplet}.
 */
public final class GroupIdTripletTest extends TestCase {

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX01}.
     */
    public void testFormatX01() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x01);

        final String jobClass = "P";
        TestHelper.writeEbcdicString(baos, jobClass);

        final String jobName = "MKTEST";
        TestHelper.writeEbcdicString(baos, jobName, 8);

        final String jobId = "J0123";
        TestHelper.writeEbcdicString(baos, jobId, 8);

        final String forms = "FORMABCD";
        TestHelper.writeEbcdicString(baos, forms, 8);

        final GroupIdTriplet triplet = (GroupIdTriplet) TripletTest.buildTriplet(TripletId.GroupID, baos.toByteArray());
        assertEquals(GroupIdFormat.MVS_AND_VSE, triplet.getGroupIdFormatIfExist());

        final StringBuilder sb = new StringBuilder();
        sb.append("JOBCLASS=");
        sb.append(jobClass);
        sb.append(", JOBNAME=");
        sb.append(jobName);
        sb.append(", JOBID=");
        sb.append(jobId);
        sb.append(", FORMS=");
        sb.append(forms);
        assertEquals(sb.toString(), triplet.getGroupIdDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX02}.
     */
    public void testFormatX02() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x02);

        final String spoolClass = "P";
        TestHelper.writeEbcdicString(baos, spoolClass);

        final String fileName = "FILE";
        TestHelper.writeEbcdicString(baos, fileName, 8);

        final String userId = "MKUID";
        TestHelper.writeEbcdicString(baos, userId, 8);

        final String formName = "FORM01";
        TestHelper.writeEbcdicString(baos, formName, 8);

        final String spoolId = "SPL1";
        TestHelper.writeEbcdicString(baos, spoolId, 4);

        final GroupIdTriplet triplet = (GroupIdTriplet) TripletTest.buildTriplet(TripletId.GroupID, baos.toByteArray());
        assertEquals(GroupIdFormat.VM, triplet.getGroupIdFormatIfExist());

        final StringBuilder sb = new StringBuilder();
        sb.append("SPOOL CLASS=");
        sb.append(spoolClass);
        sb.append(", FILE NAME=");
        sb.append(fileName);
        sb.append(", USER ID=");
        sb.append(userId);
        sb.append(", FORM NAME=");
        sb.append(formName);
        sb.append(", SPOOL ID=");
        sb.append(spoolId);
        assertEquals(sb.toString(), triplet.getGroupIdDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX03}.
     */
    public void testFormatX03() throws Exception {
        testFormatOS400(GroupIdFormat.OS400);
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX13}.
     */
    public void testFormatX13() throws Exception {
        testFormatOS400(GroupIdFormat.EXTENDED_OS400);
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX03} or {@link GroupIdDataFormatX13}.
     */
    private static void testFormatOS400(final GroupIdFormat format) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(format.getId() & 0xFF);

        final String libraryName = "LIB10";
        TestHelper.writeEbcdicString(baos, libraryName, 10);

        final String queueName = "QUEUE1";
        TestHelper.writeEbcdicString(baos, queueName, 10);

        final String fileName = "SPLFILE1";
        TestHelper.writeEbcdicString(baos, fileName, 10);

        final String fileNumber = "01";
        TestHelper.writeEbcdicString(baos, fileNumber, format == GroupIdFormat.OS400 ? 4 : 6);

        final String jobName = "PRNJOB1";
        TestHelper.writeEbcdicString(baos, jobName, 10);

        final String userName = "USERXYZ";
        TestHelper.writeEbcdicString(baos, userName, 10);

        final String jobNumber = "J123";
        TestHelper.writeEbcdicString(baos, jobNumber, 6);

        final String formsName = "FORMULARX";
        TestHelper.writeEbcdicString(baos, formsName, 10);

        final GroupIdTriplet triplet = (GroupIdTriplet) TripletTest.buildTriplet(TripletId.GroupID, baos.toByteArray());
        assertEquals(format, triplet.getGroupIdFormatIfExist());

        final StringBuilder sb = new StringBuilder();
        sb.append("LIBRARY NAME=");
        sb.append(libraryName);
        sb.append(", QUEUE NAME=");
        sb.append(queueName);
        sb.append(", FILE NAME=");
        sb.append(fileName);
        sb.append(", FILE NUMBER=");
        sb.append(fileNumber);
        sb.append(", JOB NAME=");
        sb.append(jobName);
        sb.append(", USER NAME=");
        sb.append(userName);
        sb.append(", JOB NUMBER=");
        sb.append(jobNumber);
        sb.append(", FORMS NAME=");
        sb.append(formsName);
        assertEquals(sb.toString(), triplet.getGroupIdDataIfExist().toString());

    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX04}.
     */
    public void testFormatX04() throws Exception {
        testFormatX04((byte) 0x80, " (Job header)");
        testFormatX04((byte) 0x40, " (Data set header)");
        testFormatX04((byte) 0x20, " (User data set)");
        testFormatX04((byte) 0x10, " (Message data set)");
        testFormatX04((byte) 0x04, " (Job trailer)");
        testFormatX04((byte) 0x00, " (Not specified)");
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX04}.
     */
    private static void testFormatX04(final byte type, final String typeString) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x04);
        baos.write(type);

        final String jobClass = "P";
        TestHelper.writeEbcdicString(baos, jobClass);

        final String jobName = "MKTEST";
        TestHelper.writeEbcdicString(baos, jobName, 8);

        final String jobId = "J0123";
        TestHelper.writeEbcdicString(baos, jobId, 8);

        final String forms = "FORMABCD";
        TestHelper.writeEbcdicString(baos, forms, 8);

        final String programmerName = "PROGRAMMERS NAME HERE";
        TestHelper.writeEbcdicString(baos, programmerName, 60);

        final String roomNumber = "ROOM NUMBER HERE";
        TestHelper.writeEbcdicString(baos, roomNumber, 60);

        final String submissionDate = "2016-12-12";
        TestHelper.writeEbcdicString(baos, submissionDate, 11);

        final String submissionTime = "12:12:12";
        TestHelper.writeEbcdicString(baos, submissionTime, 11);

        final GroupIdTriplet triplet = (GroupIdTriplet) TripletTest.buildTriplet(TripletId.GroupID, baos.toByteArray());
        assertEquals(GroupIdFormat.MVS_AND_VSE_COM, triplet.getGroupIdFormatIfExist());

        final StringBuilder sb = new StringBuilder();
        sb.append("FILE TYPE=");
        sb.append(Integer.toHexString(type & 0xFF));
        sb.append(typeString);
        sb.append(", JOBCLASS=");
        sb.append(jobClass);
        sb.append(", JOBNAME=");
        sb.append(jobName);
        sb.append(", JOBID=");
        sb.append(jobId);
        sb.append(", FORMS=");
        sb.append(forms);
        sb.append(", PROGRAMMER=");
        sb.append(programmerName);
        sb.append(", ROOM=");
        sb.append(roomNumber);
        sb.append(", DATE=");
        sb.append(submissionDate);
        sb.append(", TIME=");
        sb.append(submissionTime);
        assertEquals(sb.toString(), triplet.getGroupIdDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX05}.
     */
    public void testFormatX05() throws Exception {
        testFormatX05((byte) 0x80, " (Job header)");
        testFormatX05((byte) 0x40, " (Copy separator)");
        testFormatX05((byte) 0x20, " (User print file)");
        testFormatX05((byte) 0x10, " (Message file)");
        testFormatX05((byte) 0x08, " (User exit page)");
        testFormatX05((byte) 0x04, " (Job trailer)");
        testFormatX05((byte) 0x00, " (Not specified)");
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX05}.
     */
    private static void testFormatX05(final byte type, final String typeString) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x05);
        baos.write(type & 0xFF);

        final String fileName = "/path/to/my/print/file/on/aix/or/os2";
        TestHelper.writeAsciiString(baos, fileName);

        final GroupIdTriplet triplet = (GroupIdTriplet) TripletTest.buildTriplet(TripletId.GroupID, baos.toByteArray());
        assertEquals(GroupIdFormat.AIX_AND_OS2, triplet.getGroupIdFormatIfExist());

        final StringBuilder sb = new StringBuilder();
        sb.append("FILE TYPE=");
        sb.append(Integer.toHexString(type & 0xFF));
        sb.append(typeString);
        sb.append(", FILE NAME=");
        sb.append(fileName);
        assertEquals(sb.toString(), triplet.getGroupIdDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX06}.
     */
    public void testFormatX06() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x06);

        final String fileName = "/path/to/my/print/file/on/aix/or/windows";
        TestHelper.writeAsciiString(baos, fileName);

        final GroupIdTriplet triplet = (GroupIdTriplet) TripletTest.buildTriplet(TripletId.GroupID, baos.toByteArray());
        assertEquals(GroupIdFormat.AIX_AND_WINDOWS, triplet.getGroupIdFormatIfExist());

        final StringBuilder sb = new StringBuilder();
        sb.append("FILE NAME=");
        sb.append(fileName);
        assertEquals(sb.toString(), triplet.getGroupIdDataIfExist().toString());
    }

    /**
     * Tests the {@link GroupIdTriplet} with {@link GroupIdDataFormatX08}.
     */
    public void testFormatX08() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x08);

        final String data = "qwertzuiop-asdfghjkl-yxcvbnm";
        TestHelper.writeAsciiString(baos, data);

        final GroupIdTriplet triplet = (GroupIdTriplet) TripletTest.buildTriplet(TripletId.GroupID, baos.toByteArray());
        assertEquals(GroupIdFormat.VARIABLE_LENGTH_GROUP_ID, triplet.getGroupIdFormatIfExist());

        final StringBuilder sb = new StringBuilder();
        sb.append("DATA=");
        sb.append(StringUtils.toHexString(data.getBytes("IBM850")));
        assertEquals(sb.toString(), triplet.getGroupIdDataIfExist().toString());
    }
}
