package mk.ipdsbox.ipds.commands;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link IpdsCommandFlags}.
 */
public final class IpdsCommandFlagsTest extends TestCase {

    /**
     * Checks the getters of the IPDS Command.
     */
    public void testIpdsCommandFlags() {
        this.performTest(0x80, true, false, false, false);
        this.performTest(0x40, false, false, false, true);
        this.performTest(0x20, false, true, false, false);
        this.performTest(0x10, false, false, true, false);

        this.performTest(0x50, false, false, true, true);
        this.performTest(0x90, true, false, true, false);
        this.performTest(0xA0, true, true, false, false);
        this.performTest(0xF0, true, true, true, true);
    }

    /**
     * Performs the check.
     */
    private void performTest(final int value, final boolean isAcknowledgmentRequired,
        final boolean isAcknowledgmentContinuationRequested, final boolean isLongAcknowledgeReplyAccepted,
        final boolean hasCorrelationID) {

        final IpdsCommandFlags f = new IpdsCommandFlags((byte) value);
        assertEquals(isAcknowledgmentRequired, f.isAcknowledgmentRequired());
        assertEquals(isAcknowledgmentContinuationRequested, f.isAcknowledgmentContinuationRequested());
        assertEquals(isLongAcknowledgeReplyAccepted, f.isLongAcknowledgeReplyAccepted());
        assertEquals(hasCorrelationID, f.hasCorrelationID());
    }
}
