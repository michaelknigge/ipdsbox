package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class NoAcknowledgeData implements AcknowledgeData {

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        // Nothing to do...
    }
}
