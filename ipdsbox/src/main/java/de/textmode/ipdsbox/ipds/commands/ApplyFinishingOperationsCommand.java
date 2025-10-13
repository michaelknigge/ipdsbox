package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

/**
 * The Apply Finishing Operations (AFO) command is valid only in home state and directs the printer to apply
 * zero or more finishing operations to the current sheet and each copy of that sheet. The current sheet is the
 * sheet on which the first copy of the next received page is printed. The operations are not applied to sheets
 * after the copies of the current sheet.
 */
public final class ApplyFinishingOperationsCommand extends IpdsCommand {

    private List<Triplet> triplets = new ArrayList<>();

    /**
     * Constructs the {@link ApplyFinishingOperationsCommand}.
     */
    public ApplyFinishingOperationsCommand() {
        super(IpdsCommandId.AFO);
    }

    /**
     * Constructs the {@link ApplyFinishingOperationsCommand}.
     */
    public ApplyFinishingOperationsCommand(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException, UnknownTripletException {
        super(ipds, IpdsCommandId.AFO);

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletFactory.create(buffer));
        }
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
        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(ipds);
        }
    }
}
