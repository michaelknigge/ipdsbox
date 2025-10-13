package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletBuilder;
import de.textmode.ipdsbox.ipds.triplets.UnknownTripletException;

/**
 * Set Presentation Environment (SPE) is a home state command used to set specific presentation attributes for
 * use on the pages that follow. For each specified triplet, the specified presentation attributes completely replace
 * those presentation attributes; all other previously specified presentation attributes (from other triplets) stay in
 * effect.
 */
public final class SetPresentationEnvironmentCommand extends IpdsCommand {

    private List<Triplet> triplets = new ArrayList<>();

    /**
     * Constructs the {@link SetPresentationEnvironmentCommand}.
     */
    public SetPresentationEnvironmentCommand() {
        super(IpdsCommandId.SPE);
    }

    /**
     * Constructs the {@link SetPresentationEnvironmentCommand}.
     */
    public SetPresentationEnvironmentCommand(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException, UnknownTripletException {
        super(ipds, IpdsCommandId.SPE);

        ipds.skip(2);

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletBuilder.build(buffer));
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
        ipds.writeUnsignedInteger16(0x0000);

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(ipds);
        }
    }
}
