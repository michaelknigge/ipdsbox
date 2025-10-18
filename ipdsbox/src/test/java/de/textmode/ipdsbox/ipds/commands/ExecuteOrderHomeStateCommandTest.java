package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.HexFormat;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.triplets.CodedGraphicCharacterSetGlobalIdentifierTriplet;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.UP3IFinishingOperationTriplet;
import de.textmode.ipdsbox.ipds.xohorders.*;
import junit.framework.TestCase;

/**
 * JUnit tests of the {@link ExecuteOrderHomeStateCommand}.
 */
public final class ExecuteOrderHomeStateCommandTest extends TestCase {

    private static XohOrder getOrder(final String hex)
        throws InvalidIpdsCommandException, UnknownXohOrderCode, IOException {

        final String withoutLen = "D68F00" + hex;
        final int len = (withoutLen.length() / 2) + 2;
        final String withLen = String.format("%04X", len) + withoutLen;

        final byte[] rawData = HexFormat.of().parseHex(withLen);
        final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(rawData);

        return new ExecuteOrderHomeStateCommand(ipds).getOrder();
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
        assertEquals(0x00, triplet.getFormat());
        assertEquals(null, triplet.getGroupIdData());
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
        assertEquals(0x06, triplet.getFormat());
        assertEquals("FILE NAME=123", triplet.getGroupIdData().toString());
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
        assertEquals(0x06, triplet1.getFormat());
        assertEquals("FILE NAME=123", triplet1.getGroupIdData().toString());

        final GroupIdTriplet triplet2 = (GroupIdTriplet) order.getTriplets().get(1);
        assertEquals(0x06, triplet2.getFormat());
        assertEquals("FILE NAME=678", triplet2.getGroupIdData().toString());
    }

    /**
     * Test of a {@link ExecuteOrderHomeStateCommand} with order code DSPG (Define Group
     * Boundary Order) that initializes a group and contains just one Triplet.
     */
    public void testDefineGroupBoundaryOrderWithSingleTriplets() throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("0400");
        sb.append("0003");
        sb.append("060100020004");
        final DefineGroupBoundaryOrder order = (DefineGroupBoundaryOrder) getOrder(sb.toString());

        assertEquals(XohOrderCode.DefineGroupBoundary, order.getOrderCode());
        assertEquals(0x00, order.getOrderType());
        assertEquals(0x03, order.getGroupLevel());
        assertEquals(1, order.getTriplets().size());

        final CodedGraphicCharacterSetGlobalIdentifierTriplet triplet1 =
            (CodedGraphicCharacterSetGlobalIdentifierTriplet) order.getTriplets().get(0);

        assertFalse(triplet1.hasCodedCharacterSetIdentifier());
        assertEquals(0x0002, triplet1.getGraphicCharacterSetGlobalIdentifier());
        assertEquals(0x0004, triplet1.getCodePageGlobalIdentifier());

    }

    /**
     * Test of a {@link ExecuteOrderHomeStateCommand} with order code DSPG (Define Group
     * Boundary Order) that terminates a group and contains multiple Triplets.
     */
    public void testDefineGroupBoundaryOrderWithMultipleTriplets() throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("0400");
        sb.append("0002");
        sb.append("060100020004");
        sb.append("058E010002");
        final DefineGroupBoundaryOrder order = (DefineGroupBoundaryOrder) getOrder(sb.toString());

        assertEquals(XohOrderCode.DefineGroupBoundary, order.getOrderCode());
        assertEquals(0x00, order.getOrderType());
        assertEquals(0x02, order.getGroupLevel());
        assertEquals(2, order.getTriplets().size());

        final CodedGraphicCharacterSetGlobalIdentifierTriplet triplet1 =
            (CodedGraphicCharacterSetGlobalIdentifierTriplet) order.getTriplets().get(0);
        assertFalse(triplet1.hasCodedCharacterSetIdentifier());
        assertEquals(0x0002, triplet1.getGraphicCharacterSetGlobalIdentifier());
        assertEquals(0x0004, triplet1.getCodePageGlobalIdentifier());

        final UP3IFinishingOperationTriplet triplet2 =
            (UP3IFinishingOperationTriplet) order.getTriplets().get(1);
        assertEquals(1, triplet2.getSequenceNumber());
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
