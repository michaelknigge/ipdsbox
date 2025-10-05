package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import de.textmode.ipdsbox.ipds.triplets.GroupInformationTriplet;

/**
 * Page count format.
 */
public final class GroupInformationDataFormatX05 extends GroupInformationData {

    private final long pageCount;

    /**
     * Constructs the {@link GroupInformationDataFormatX05}.
     * @param raw the raw IPDS data of the {@link GroupInformationTriplet}.
     * @throws IOException if the {@link GroupInformationTriplet} is broken.
     */
    public GroupInformationDataFormatX05(final byte[] raw) throws IOException {
        super(raw, GroupInformationFormat.PAGE_COUNT);


        final byte[] longBigEndian = this.getStream().readBytes(8);
        final DataInputStream dis = new DataInputStream(new ByteArrayInputStream(longBigEndian));

        this.pageCount = dis.readLong();
    }

    /**
     * Returns the page count. The page count is informational only. Typically, the page count
     * is a count of the number of pages in a print file (or other group) that is specified before
     * the group is printed; for example, the value might represent the number of BPG/EPG-pairs
     * within a MO:DCA print file. It is intended to be a ball-park figure that can help operators
     * estimate time to completion or job progress; there is no requirement that the page count be
     * completely accurate in all circumstances.
     * @return the page count.
     */
    public long getPageCount() {
        return this.pageCount;
    }

    @Override
    public String toString() {
        return this.pageCount + " pages";
    }
}
