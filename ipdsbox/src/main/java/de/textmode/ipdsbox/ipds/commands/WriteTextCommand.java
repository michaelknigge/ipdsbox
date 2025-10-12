package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Write Text (WT) command sends from 0 to 32,762 bytes of character data and controls to the printer. This
 * data is part of a text object, page, page segment, or overlay, depending on the IPDS state of the printer. The
 * Write Text command carries PTOCA data, as defined by the PT1, PT2, PT3, and PT4 subsets; refer to
 * Presentation Text Object Content Architecture Reference for information about these subsets.
 */
public final class WriteTextCommand extends IpdsCommand {

    private byte[] ptocaData;

    /**
     * Constructs the {@link WriteTextCommand}.
     */
    public WriteTextCommand() {
        this(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * Constructs the {@link WriteTextCommand}.
     */
    public WriteTextCommand(final byte[] ptocaData) {
        super(IpdsCommandId.WT);

        this.ptocaData = ptocaData;
    }
    /**
     * Constructs the {@link WriteTextCommand}.
     */
    public WriteTextCommand(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException {
        super(ipds, IpdsCommandId.WT);

        this.ptocaData = ipds.readRemainingBytes();
    }

    /**
     * Returns the PTOCA data bytes of the IPDS Command.
     */
    public byte[] getPtocaData() {
        return this.ptocaData;
    }

    /**
     * Sets the PTOCA data bytes of the IPDS Command.
     */
    public void setPtocaData(final byte[] ptocaData) {
        this.ptocaData = ptocaData;
    }

    @Override
    void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        if (this.ptocaData != null) {
            ipds.writeBytes(this.ptocaData);
        }
    }
}
