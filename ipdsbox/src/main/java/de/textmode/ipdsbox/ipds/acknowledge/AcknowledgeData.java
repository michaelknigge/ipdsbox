package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public interface AcknowledgeData {

    /**
     * Writes this {@link AcknowledgeData} to the given {@link IpdsByteArrayOutputStream}.
     */
    public abstract void writeTo(IpdsByteArrayOutputStream out) throws IOException;
}
