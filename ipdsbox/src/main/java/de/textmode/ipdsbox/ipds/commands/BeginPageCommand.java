package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * The Begin Page (BP) command is valid only in home state and causes the printer to enter page state. This
 * command identifies the beginning of a page. The End Page command ends the page.
 */
public final class BeginPageCommand extends IpdsCommand {

    private long pageId;

    /**
     * Constructs the {@link BeginPageCommand}.
     */
    public BeginPageCommand() {
        this(0);
    }

    /**
     * Constructs the {@link BeginPageCommand}.
     */
    public BeginPageCommand(final long pageId) {
        super(IpdsCommandId.BP);

        this.pageId = pageId;
    }

    /**
     * Constructs the {@link BeginPageCommand}.
     */
    public BeginPageCommand(final IpdsByteArrayInputStream ipds) throws InvalidIpdsCommandException, IOException {
        super(ipds, IpdsCommandId.BP);

        this.pageId = ipds.readUnsignedInteger32();
    }

    /**
     * Returns the page ID.
     */
    public long getPageId() {
        return this.pageId;
    }

    /**
     * Sets the page ID.
     */
    public void setPageId(final long pageId) {
        this.pageId = pageId;
    }

    @Override
    protected void writeDataTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        ipds.writeUnsignedInteger32(this.pageId);
    }
}
