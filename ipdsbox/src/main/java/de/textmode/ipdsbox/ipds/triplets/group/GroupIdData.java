package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupIdTriplet;
import de.textmode.ipdsbox.ipds.triplets.TripletId;

/**
 * Superclass of all classes representing the group data of the {@link GroupIdTriplet}.
 */
public abstract class GroupIdData {

    /**
     * Returns the raw bytes of this {@link GroupIdData}.
     */
    public abstract byte[] toByteArray() throws IOException;
}
