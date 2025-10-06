package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class FullyQualifiedNameTriplet extends Triplet {

    /** FQN type (CODE, 1). */
    private int fqnType;

    /** FQN format (CODE, 1). */
    private int fqnFormat;

    /** FQN (CHAR/CODE/UNDF). */
    private byte[] fqn;

    public FullyQualifiedNameTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.FullyQualifiedName);
        this.readFrom(this.getStream());
    }

    private void readFrom(final IpdsByteArrayInputStream in) throws IOException {
        this.fqnType = in.readUnsignedByte();
        this.fqnFormat = in.readUnsignedByte();

        // In afuture release we could parse it to a OID byte[] or a String. But eb carefull... The
        // encoding might NOT be UTF16-BE if in the IPDS command a Coded Graphic Character Set Global Identifier
        // Triplet preceeded...
        this.fqn = in.readBytes(this.getLength() - 4);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(4 + this.fqn.length);
        out.writeUnsignedByte(this.getTripletId().getId());  // TID
        out.writeUnsignedByte(this.fqnType);
        out.writeUnsignedByte(this.fqnFormat);
        out.writeBytes(this.fqn);

        return out.toByteArray();
    }

    /** Returns the FQN type. */
    public int getFqnType() {
        return this.fqnType;
    }

    /** Sets the FQN type. */
    public void setFqnType(final int fqnType) {
        this.fqnType = fqnType;
    }

    /** Returns the FQN format. */
    public int getFqnFormat() { return this.fqnFormat; }

    /** Sets the FQN format. */
    public void setFqnFormat(final int fqnFormat) { this.fqnFormat = fqnFormat; }

    /** Returns the FQN. */
    public byte[] getFqn() { return this.fqn; }

    /** Sets the FQN. */
    public void setFqn(final byte[] fqn) { this.fqn = fqn; }

    @Override
    public String toString() {
        return "FQN{length=" + this.length +
                ", tid=0x" + Integer.toHexString(tid) +
                ", type=0x" + Integer.toHexString(this.fqnType) +
                ", format=0x" + Integer.toHexString(this.fqnFormat) +
                ", fqnBytes=" + (this.fqn ==null?0: this.fqn.length) + "}";
    }

}
