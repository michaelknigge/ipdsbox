package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.*;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * Page count format.
 */
public final class PageCountFormat extends GroupInformationData {

    private final long pageCount;

    /**
     * Constructs the {@link PageCountFormat}.
     */
    public PageCountFormat(final IpdsByteArrayInputStream ipds) throws IOException {
        final byte[] longBigEndian = ipds.readBytes(8);
        final DataInputStream dis = new DataInputStream(new ByteArrayInputStream(longBigEndian));

        this.pageCount = dis.readLong();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream(baos);

        dos.writeLong(this.pageCount);

        return baos.toByteArray();
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
