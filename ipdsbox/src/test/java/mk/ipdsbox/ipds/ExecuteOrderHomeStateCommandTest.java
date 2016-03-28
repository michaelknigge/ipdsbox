package mk.ipdsbox.ipds;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;
import mk.ipdsbox.core.InvalidIpdsCommandException;
import mk.ipdsbox.ipds.triplets.GroupIdTriplet;
import mk.ipdsbox.ipds.triplets.Triplet;
import mk.ipdsbox.ipds.triplets.UnknownTripletException;
import mk.ipdsbox.ipds.triplets.group.GroupIdFormat;
import mk.ipdsbox.ipds.xohorders.DeactivateSavedPageGroupOrder;
import mk.ipdsbox.ipds.xohorders.UnknownXohOrderCode;
import mk.ipdsbox.ipds.xohorders.XohOrder;
import mk.ipdsbox.ipds.xohorders.XohOrderCode;

/**
 * JUnit tests of the {@link ExecuteOrderHomeStateCommand}.
 */
public final class ExecuteOrderHomeStateCommandTest extends TestCase {

    private static XohOrder getOrder(final String hex)
        throws InvalidIpdsCommandException, UnknownXohOrderCode, IOException, UnknownTripletException {

        final String withoutLen = "D68F00" + hex;
        final int len = (withoutLen.length() / 2) + 2;
        final String withLen = String.format("%04X", len) + withoutLen;

        return new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(withLen)).getOrder();
    }

    /**
     * Test of a {@link ExecuteOrderHomeStateCommand} with order code DSPG (Deactivate
     * Saved Page Group Order) that contains an "empty" X'00' Triplet. Completely nonsense,
     * just to check if {@link DeactivateSavedPageGroupOrder} can handle an empty {@link Triplet}.
     */
    public void testDeactivateSavedPageGroupOrderWithEmptyTriplet() throws Exception {
        final DeactivateSavedPageGroupOrder order = (DeactivateSavedPageGroupOrder) getOrder("02000200");
        assertEquals(XohOrderCode.DeactivateSavedPageGroup, order.getOrderCode());
        assertEquals(1, order.getTriplets().size());

        final GroupIdTriplet triplet = (GroupIdTriplet) order.getTriplets().get(0);
        assertEquals(null, triplet.getGroupIdFormatIfExist());
        assertEquals(null, triplet.getGroupIdDataIfExist());
    }

    /**
     * Test of a {@link ExecuteOrderHomeStateCommand} with order code DSPG (Deactivate
     * Saved Page Group Order) that contains a X'00' Triplet with format X'06' ("AIX and Windows print-data").
     */
    public void testDeactivateSavedPageGroupOrderWithTripletFormat06() throws Exception {
        final DeactivateSavedPageGroupOrder order = (DeactivateSavedPageGroupOrder) getOrder("0200060006313233");
        assertEquals(XohOrderCode.DeactivateSavedPageGroup, order.getOrderCode());
        assertEquals(1, order.getTriplets().size());

        final GroupIdTriplet triplet = (GroupIdTriplet) order.getTriplets().get(0);
        assertEquals(GroupIdFormat.AIX_AND_WINDOWS, triplet.getGroupIdFormatIfExist());
        assertEquals("FILE NAME=123", triplet.getGroupIdDataIfExist().toString());
    }

    /**
     * Test of a {@link ExecuteOrderHomeStateCommand} with order code DSPG (Deactivate
     * Saved Page Group Order) that contains multiple a X'00' Triplets.
     */
    public void testDeactivateSavedPageGroupOrderWithMultipleTriplets() throws Exception {
        final DeactivateSavedPageGroupOrder order =
            (DeactivateSavedPageGroupOrder) getOrder("0200060006313233060006363738");

        assertEquals(XohOrderCode.DeactivateSavedPageGroup, order.getOrderCode());
        assertEquals(2, order.getTriplets().size());

        final GroupIdTriplet triplet1 = (GroupIdTriplet) order.getTriplets().get(0);
        assertEquals(GroupIdFormat.AIX_AND_WINDOWS, triplet1.getGroupIdFormatIfExist());
        assertEquals("FILE NAME=123", triplet1.getGroupIdDataIfExist().toString());

        final GroupIdTriplet triplet2 = (GroupIdTriplet) order.getTriplets().get(1);
        assertEquals(GroupIdFormat.AIX_AND_WINDOWS, triplet2.getGroupIdFormatIfExist());
        assertEquals("FILE NAME=678", triplet2.getGroupIdDataIfExist().toString());
    }

    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code DGB).
    //     */
    //    public void testDGB() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code EFF).
    //     */
    //    public void testEFF() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code ERFD).
    //     */
    //    public void testERFD() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code ERPD).
    //     */
    //    public void testERPD() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code OPC).
    //     */
    //    public void testOPC() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code PCC).
    //     */
    //    public void testPCC() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code PBD).
    //     */
    //    public void testPBD() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code RSPG).
    //     */
    //    public void testRSPG() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code SIMS).
    //     */
    //    public void testSIMS() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code SMM).
    //     */
    //    public void testSMM() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code SCF).
    //     */
    //    public void testSCF() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code SMO).
    //     */
    //    public void testSMO() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code SMS).
    //     */
    //    public void testSMS() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code SGO).
    //     */
    //    public void testSGO() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code SRP).
    //     */
    //    public void testSRP() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
    //
    //    /**
    //     * Test of a {@link ExecuteOrderHomeStateCommand} with order code TRC).
    //     */
    //    public void testTRC() throws Exception {
    //        new ExecuteOrderHomeStateCommand(DatatypeConverter.parseHexBinary(""));
    //    }
}
