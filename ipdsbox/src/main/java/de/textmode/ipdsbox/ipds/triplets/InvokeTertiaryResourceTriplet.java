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
     * Constructs a {@link InvokeTertiaryResourceTriplet} from the given byte array.
     *
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public InvokeTertiaryResourceTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.InvokeTertiaryResource);
        this.readFrom(this.getStream());
    }

    private void readFrom(final IpdsByteArrayInputStream in) throws IOException {
        this.tertiaryResourceType = in.readUnsignedByte();
        this.hostAssignedId = in.readUnsignedInteger16();

        in.skip(4);

        this.idType = in.readUnsignedByte();
        this.internalResourceId = in.readUnsignedInteger16();

        // TODO Shall we throw an exception if idType is not 0x01 ???
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(12);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedByte(this.tertiaryResourceType);
        out.writeUnsignedInteger16(this.hostAssignedId);
        out.writeUnsignedInteger32(0);
        out.writeUnsignedByte(this.idType);
        out.writeUnsignedInteger16(this.internalResourceId);

        return out.toByteArray();
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
        return "InvokeTertiaryResource{length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", tertiaryResourceType=" + Integer.toHexString(this.tertiaryResourceType) +
                ", haid=" + Integer.toHexString(this.hostAssignedId) +
                ", idType=" + this.idType +
                ", internalResourceId=" + this.internalResourceId + "}";
    }

}
