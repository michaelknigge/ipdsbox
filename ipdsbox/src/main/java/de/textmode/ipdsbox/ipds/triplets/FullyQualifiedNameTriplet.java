package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class FullyQualifiedNameTriplet extends Triplet {

    private int fqnType;
    private int fqnFormat;
    private byte[] fqn;

    public FullyQualifiedNameTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.FullyQualifiedName);

        this.fqnType = ipds.readUnsignedByte();
        this.fqnFormat = ipds.readUnsignedByte();

        // In a future release we could parse it to a OID byte[] or a String. But be carefull... The
        // encoding might NOT be UTF16-BE if in the IPDS command a Coded Graphic Character Set Global Identifier
        // Triplet preceeded...
        this.fqn = ipds.readRemainingBytes();
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(4 + this.fqn.length);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedByte(this.fqnType);
        out.writeUnsignedByte(this.fqnFormat);
        out.writeBytes(this.fqn);
    }

    /**
     * Returns the FQN type.
     */
    public int getFqnType() {
        return this.fqnType;
    }

    /**
     * Sets the FQN type.
     */
    public void setFqnType(final int fqnType) {
        this.fqnType = fqnType;
    }

    /**
     * Returns the FQN format.
     */
    public int getFqnFormat() {
        return this.fqnFormat;
    }

    /**
     * Sets the FQN format.
     */
    public void setFqnFormat(final int fqnFormat) {
        this.fqnFormat = fqnFormat;
    }

    /**
     * Returns the FQN.
     */
    public byte[] getFqn() {
        return this.fqn;
    }

    /**
     * Sets the FQN.
     */
    public void setFqn(final byte[] fqn) {
        this.fqn = fqn;
    }

    @Override
    public String toString() {
        return "FQN{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", type=0x" + Integer.toHexString(this.fqnType) +
                ", format=0x" + Integer.toHexString(this.fqnFormat) +
                ", fqnBytes=" + (this.fqn == null ? 0 : this.fqn.length) +
                "}";
    }
}
