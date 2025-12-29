package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.triplets.Triplet;
import de.textmode.ipdsbox.ipds.triplets.TripletFactory;

/**
 * The Presentation Fidelity Control (PFC) command is valid only in home state and specifies the fidelity
 * requirements for certain presentation functions. The desired fidelity for each supported presentation function
 * can be specified with a triplet on the PFC command. The activate flag can be used to reset all fidelity controls
 * to their default settings before activating the settings specified in the PFC triplets. A PFC command with no
 * triplets and with the activate flag set to B'0' resets all fidelity controls to their default settings.
 */
public final class PresentationFidelityControlCommand extends IpdsCommand {

    private int fidelityControlFlags;
    private List<Triplet> triplets;

    /**
     * Constructs a {@link PresentationFidelityControlCommand} with default values.
     */
    public PresentationFidelityControlCommand() {
        this(0);
    }

    /**
     * Constructs the {@link PresentationFidelityControlCommand} with the given fidelity control flags.
     */
    public PresentationFidelityControlCommand(final int fidelityControlFlags) {
        super(IpdsCommandId.PFC);

        this.fidelityControlFlags = fidelityControlFlags;
        this.triplets = new ArrayList<>();
    }

    /**
     * Constructs the {@link PresentationFidelityControlCommand} from the given {@link IpdsByteArrayInputStream}.
     */
    PresentationFidelityControlCommand(final IpdsByteArrayInputStream ipds) throws IOException {
        super(ipds, IpdsCommandId.PFC);

        ipds.skip(1);
        this.fidelityControlFlags = ipds.readUnsignedByte();
        ipds.skip(2);

        byte[] buffer;
        while ((buffer = ipds.readTripletIfExists()) != null) {
            this.triplets.add(TripletFactory.create(buffer));
        }
    }

    /**
     * Returns the fidelity control flags.
     */
    public long getFidelityControlFlags() {
        return this.fidelityControlFlags;
    }

    /**
     * Sets the fidelity control flags.
     */
    public void setFidelityControlFlags(final int fidelityControlFlags) {
        this.fidelityControlFlags = fidelityControlFlags;
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
        ipds.writeUnsignedByte(0x00);
        ipds.writeUnsignedByte(this.fidelityControlFlags);
        ipds.writeUnsignedInteger16(0x00);

        for (final Triplet triplet : this.triplets) {
            triplet.writeTo(ipds);
        }
    }

    @Override
    public String toString() {
        return "PresentationFidelityControlCommand{"
                + "fidelityControlFlags=0x" + Integer.toHexString(this.fidelityControlFlags)
                + ", triplets=" + this.triplets
                + '}';
    }
}
