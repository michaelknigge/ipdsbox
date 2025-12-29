package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;

/**
 * The Include Saved Page (ISP) command is a page state command that causes a previously saved page to be
 * presented at the origin of the current page presentation space. If page overlays were also saved with the
 * saved page, the overlays are also presented.
 */
public final class IncludeSavedPageCommand extends IpdsCommand {

    private long pageSequenceNumber;
    private List<Triplet> triplets;

    /**
     * Constructs the {@link IncludeSavedPageCommand}.
     */
    public IncludeSavedPageCommand() {
        this(0);
    }

    /**
     * Constructs the {@link IncludeSavedPageCommand}.
     */
    public IncludeSavedPageCommand(final long pageSequenceNumber) {
        super(IpdsCommandId.ISP);

        this.pageSequenceNumber = pageSequenceNumber;
        this.triplets = new ArrayList<>();
    }

    /**
     * Constructs the {@link IncludeSavedPageCommand}.
     */
    IncludeSavedPageCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.ISP);

        this.pageSequenceNumber = ipds.readUnsignedInteger32();

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletFactory.create(buffer));
        }
    }

    /**
     * Returns the page sequence number.
     */
    public long getPageSequenceNumber() {
        return this.pageSequenceNumber;
    }

    /**
     * Sets the page sequence number.
     */
    public void setPageSequenceNumber(final int pageSequenceNumber) {
        this.pageSequenceNumber = pageSequenceNumber;
    }

    /**
     * Returns the Triplets of this IPDS command.
     */
    public List<Triplet> getTriplets() {
        return this.triplets;
    }

    /**
     * Sets the Triplets of this IPDS command.
     */
    public void setTriplets(final List<Triplet> triplets) {
        this.triplets = triplets;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger32(this.pageSequenceNumber);

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(ipds);
        }
    }

    @Override
    public String toString() {
        return "IncludeSavedPageCommand{" +
                "pageSequenceNumber=" + this.pageSequenceNumber +
                ", triplets=" + this.triplets +
                + '}';
    }
}
