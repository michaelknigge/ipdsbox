package de.textmode.ipdsbox.ipds.triplets.group;

import java.io.IOException;

import de.textmode.ipdsbox.core.IpdsboxRuntimeException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.triplets.GroupInformationTriplet;
import de.textmode.ipdsbox.ipds.triplets.TripletId;

/**
 * Superclass of all classes representing the group data of the {@link GroupInformationTriplet}.
 */
public abstract class GroupInformationData {
    /**
     * Returns the raw bytes of this {@link GroupInformationData}.
     */
    public abstract byte[] toByteArray() throws IOException;
}
