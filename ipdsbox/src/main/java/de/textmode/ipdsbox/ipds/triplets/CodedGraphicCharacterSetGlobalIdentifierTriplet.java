package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Coded Graphic Character Set Global Identifier triplet (X'01') specifies the code page and
 * character set used to interpret character data.
 */
public final class CodedGraphicCharacterSetGlobalIdentifierTriplet extends Triplet {

    private final int graphicCharacterSetGlobalIdentifier;
    private final int codePageGlobalIdentifier;
    private final int codedCharacterSetIdentifier;

    /**
     * Constructs a {@link CodedGraphicCharacterSetGlobalIdentifierTriplet} from the given byte array.
     * @param raw raw IPDS data of the {@link Triplet}.
     * @throws IOException if the given IPDS data is incomplete
     */
    public CodedGraphicCharacterSetGlobalIdentifierTriplet(final byte[] raw) throws IOException {
        super(raw, TripletId.CodedGraphicCharacterSetGlobalIdentifier);

        this.graphicCharacterSetGlobalIdentifier = this.getStream().readUnsignedInteger16();

        if (this.graphicCharacterSetGlobalIdentifier == 0) {
            this.codedCharacterSetIdentifier = this.getStream().readUnsignedInteger16();
            this.codePageGlobalIdentifier = 0;
        } else {
            this.codedCharacterSetIdentifier = 0;
            this.codePageGlobalIdentifier = this.getStream().readUnsignedInteger16();
        }
    }

    /**
     * Returns <code>true</code> if and only this {@link Triplet} is in CCSID form rather than GCSGID/CPGID form.
     * @return <code>true</code> if and only this {@link Triplet} carries an CCSID.
     */
    public boolean hasCodedCharacterSetIdentifier() {
        return this.graphicCharacterSetGlobalIdentifier == 0;
    }

    /**
     * Returns the CCSID (Coded Character Set Identifier) that is specified in this {@link Triplet}.
     * @return The CCSID. Note that the returned value is valid only if {@link #hasCodedCharacterSetIdentifier()}
     * returns <code>true</code>.
     */
    public int getCodedCharacterSetIdentifier() {
        return this.codedCharacterSetIdentifier;
    }

    /**
     * Returns the CPGID (Code Page Global Identifier) that is specified in this {@link Triplet}.
     * @return The CPGID. Note that the returned value is valid only if {@link #hasCodedCharacterSetIdentifier()}
     * returns <code>false</code>.
     */
    public int getCodePageGlobalIdentifier() {
        return this.codePageGlobalIdentifier;
    }

    /**
     * Returns the GCSGID (Graphic Character Set Global Identifier) that is specified in this {@link Triplet}.
     * @return The GCSGID. Note that the returned value is valid only if {@link #hasCodedCharacterSetIdentifier()}
     * returns <code>false</code>.
     */
    public int getGraphicCharacterSetGlobalIdentifier() {
        return this.graphicCharacterSetGlobalIdentifier;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final IpdsByteArrayOutputStream out = new IpdsByteArrayOutputStream();

        out.writeUnsignedByte(6);
        out.writeUnsignedByte(this.getTripletId().getId());
        out.writeUnsignedInteger16(this.graphicCharacterSetGlobalIdentifier);

        if (this.graphicCharacterSetGlobalIdentifier == 0) {
            out.writeUnsignedInteger16(this.codedCharacterSetIdentifier);
        } else {
            out.writeUnsignedInteger16(this.codePageGlobalIdentifier);
        }

        return out.toByteArray();
    }

    @Override
    public String toString() {
        return "CGCSGID{length=" + this.getLength() +
                ", tid=0x" + Integer.toHexString(this.getTripletId().getId()) +
                ", gcsgid=" + Integer.toHexString(this.graphicCharacterSetGlobalIdentifier) +
                ", cpgid=" + Integer.toHexString(this.codePageGlobalIdentifier) +
                ", ccsid=" + Integer.toHexString(this.codedCharacterSetIdentifier) + "}";
    }

}
