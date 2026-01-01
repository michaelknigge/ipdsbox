package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * A self-defining field describes characteristics of an IPDS  printer.
 */
public abstract class SelfDefiningField {

    private int selfDefiningFieldId;

    /**
     * Constructs the {@link SelfDefiningField}.
     */
    SelfDefiningField(final SelfDefiningFieldId selfDefiningFieldId) {
        this.selfDefiningFieldId = selfDefiningFieldId.getId();
    }

    /**
     * Constructs the {@link SelfDefiningField}.
     */
    SelfDefiningField(final int selfDefiningFieldId) {
        this.selfDefiningFieldId = selfDefiningFieldId;
    }

    /**
     * Writes this {@link SelfDefiningField} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;

    /**
     * Returns the self-defining field ID.
     */
    public int getSelfDefiningFieldId() {
        return this.selfDefiningFieldId;
    }

    /**
     * Sets the self-defining field ID.
     */
    public void setSelfDefiningFieldId(final int selfDefiningFieldId) {
        this.selfDefiningFieldId = selfDefiningFieldId;
    }

    /**
     * Accept method for the {@link SelfDefiningFieldVisitor}.
     */
    public abstract void accept(final SelfDefiningFieldVisitor visitor);
}
