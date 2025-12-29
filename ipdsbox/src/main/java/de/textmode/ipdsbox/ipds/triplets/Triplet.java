package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * Triplets are variable-length substructures that can be used within one or more IPDS commands to provide
 * additional information for the command. A triplet is a three-part self-defining variable-length parameter
 * consisting of a length byte, an identifier byte, and parameter-value bytes.
 */
public abstract class Triplet {

    private final int tripletId;

    /**
     * Constructs a new {@link Triplet}.
     */
    public Triplet(final int tripletId) {
        this.tripletId = tripletId;
    }

    /**
     * Constructs a new {@link Triplet}.
     */
    public Triplet(final TripletId tripletId) {
        this.tripletId = tripletId.getId();
    }

    /**
     * Returns the triplet ID.
     */
    public final int getTripletId() {
        return this.tripletId;
    }

    /**
     * Writes this {@link Triplet} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;
}
