package de.textmode.ipdsbox.ipds.triplets;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
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
     * Constructs a {@link CodedGraphicCharacterSetGlobalIdentifierTriplet}.
     */
    public CodedGraphicCharacterSetGlobalIdentifierTriplet(
            final int graphicCharacterSetGlobalIdentifier,
            final int codePageGlobalIdentifier) {

        super(TripletId.CodedGraphicCharacterSetGlobalIdentifier);

        this.graphicCharacterSetGlobalIdentifier = graphicCharacterSetGlobalIdentifier;
        this.codePageGlobalIdentifier = codePageGlobalIdentifier;
        this.codedCharacterSetIdentifier = 0;

    }

    /**
     * Constructs a {@link CodedGraphicCharacterSetGlobalIdentifierTriplet}.
     */
    public CodedGraphicCharacterSetGlobalIdentifierTriplet(final int codedCharacterSetIdentifier) throws IOException {
        super(TripletId.CodedGraphicCharacterSetGlobalIdentifier);

        this.codedCharacterSetIdentifier = codedCharacterSetIdentifier;
        this.graphicCharacterSetGlobalIdentifier = 0;
        this.codePageGlobalIdentifier = 0;
    }

    /**
     * Constructs a {@link CodedGraphicCharacterSetGlobalIdentifierTriplet} from
     * the given {@link IpdsByteArrayInputStream}.
     */
    CodedGraphicCharacterSetGlobalIdentifierTriplet(final IpdsByteArrayInputStream ipds) throws IOException {
        super(TripletId.CodedGraphicCharacterSetGlobalIdentifier);

        this.graphicCharacterSetGlobalIdentifier = ipds.readUnsignedInteger16();

        if (this.graphicCharacterSetGlobalIdentifier == 0) {
            this.codedCharacterSetIdentifier = ipds.readUnsignedInteger16();
            this.codePageGlobalIdentifier = 0;
        } else {
            this.codedCharacterSetIdentifier = 0;
            this.codePageGlobalIdentifier = ipds.readUnsignedInteger16();
        }
    }

    /**
     * Returns <code>true</code> if and only this {@link Triplet} is in CCSID form rather than GCSGID/CPGID form.
     */
    public boolean hasCodedCharacterSetIdentifier() {
        return this.graphicCharacterSetGlobalIdentifier == 0;
    }

    /**
     * Returns the CCSID (Coded Character Set Identifier) that is specified in this {@link Triplet}.
     */
    public int getCodedCharacterSetIdentifier() {
        return this.codedCharacterSetIdentifier;
    }

    /**
     * Returns the CPGID (Code Page Global Identifier) that is specified in this {@link Triplet}.
     */
    public int getCodePageGlobalIdentifier() {
        return this.codePageGlobalIdentifier;
    }

    /**
     * Returns the GCSGID (Graphic Character Set Global Identifier) that is specified in this {@link Triplet}.
     */
    public int getGraphicCharacterSetGlobalIdentifier() {
        return this.graphicCharacterSetGlobalIdentifier;
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedByte(6);
        out.writeUnsignedByte(this.getTripletId());
        out.writeUnsignedInteger16(this.graphicCharacterSetGlobalIdentifier);

        if (this.graphicCharacterSetGlobalIdentifier == 0) {
            out.writeUnsignedInteger16(this.codedCharacterSetIdentifier);
        } else {
            out.writeUnsignedInteger16(this.codePageGlobalIdentifier);
        }
    }

    @Override
    public String toString() {
        return "CGCSGID{" +
                "tid=0x" + Integer.toHexString(this.getTripletId()) +
                ", gcsgid=" + Integer.toHexString(this.graphicCharacterSetGlobalIdentifier) +
                ", cpgid=" + Integer.toHexString(this.codePageGlobalIdentifier) +
                ", ccsid=" + Integer.toHexString(this.codedCharacterSetIdentifier) +
                "}";
    }
}
