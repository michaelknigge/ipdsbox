package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class InvokeTertiaryResourceTriplet extends Triplet {

    private int tertiaryResourceType;
    private int hostAssignedId;
    private int idType;
    private int internalResourceId;

    /**
     * Constructs a {@link InvokeTertiaryResourceTriplet} from the given {@link IpdsByteArrayInputStream}.
     */
    public InvokeTertiaryResourceTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.InvokeTertiaryResource);

        this.tertiaryResourceType = ipds.readUnsignedByte();
        this.hostAssignedId = ipds.readUnsignedInteger16();

        ipds.skip(4);

        this.idType = ipds.readUnsignedByte();
        this.internalResourceId = ipds.readUnsignedInteger16();

        // TODO Shall we throw an exception if idType is not 0x01 ???
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(12);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedByte(this.tertiaryResourceType);
        out.writeUnsignedInteger16(this.hostAssignedId);
        out.writeUnsignedInteger32(0);
        out.writeUnsignedByte(this.idType);
        out.writeUnsignedInteger16(this.internalResourceId);
    }

    /**
     * Returns the host assigned ID.
     *
     * @return the host assigned ID.
     */
    public int getHostAssignedId() {
        return this.hostAssignedId;
    }

    /**
     * Sets the host assigned ID.
     */
    public void setHostAssignedId(final int hostAssignedId) {
        this.hostAssignedId = hostAssignedId;
    }

    /**
     * Returns the tertiary resource type.
     *
     * @return the tertiary resource type.
     */
    public int getTertiaryResourceType() {
        return this.tertiaryResourceType;
    }

    /**
     * Sets the tertiary resource type.
     */
    public void setTertiaryResourceType(final int tertiaryResourceType) {
        this.tertiaryResourceType = tertiaryResourceType;
    }

    /**
     * Returns the ID type.
     *
     * @return the ID Type.
     */
    public int getIdType() {
        return this.idType;
    }

    /**
     * Sets the ID type.
     */
    public void setIdType(final int idType) {
        this.idType = idType;
    }

    /**
     * Returns the internal resource ID.
     *
     * @return the internal resource ID.
     */
    public int getInternalResourceId() {
        return this.internalResourceId;
    }

    /**
     * Sets the internal resource ID.
     */
    public void setInternalResourceId(final int internalResourceId) {
        this.internalResourceId = internalResourceId;
    }

    @Override
    public String toString() {
        return "InvokeTertiaryResource{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", tertiaryResourceType=" + Integer.toHexString(this.tertiaryResourceType) +
                ", haid=" + Integer.toHexString(this.hostAssignedId) +
                ", idType=" + this.idType +
                ", internalResourceId=" + this.internalResourceId +
                "}";
    }
}
