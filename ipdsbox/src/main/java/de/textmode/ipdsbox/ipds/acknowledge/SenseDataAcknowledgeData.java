package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

public final class SenseDataAcknowledgeData implements AcknowledgeData {

    // Valid no matter if 3 byte or 24 byte sense data..
    private int exceptionId;

    // From here, only valid if 24 byte sense data...
    private int actionCode;
    private int formatId;
    private int senseDetail;

    private int count;
    private int overlayId;
    private int pageSegmentId;
    private int commandInProcess;
    private int objectId;
    private int exceptionSpecificInformation;
    private int objectType;
    private long pageIdentifier;

    private int textPositionExceptionCount;
    private int imagePositionExceptionCount;
    private int rulePositionExceptionCount;
    private int graphicsPositionExceptionCount;

    private int up3iErrorCode;
    private int up3iPaperSequenceId;
    private byte[] up3iErrorInformation;


    // This field contains the raw data from formats that are unsupported or retired (i. e.
    // formats 3, 4 or 5)...
    private byte[] rawData;

    public SenseDataAcknowledgeData(final int exceptionId) {
        this.exceptionId = exceptionId;
    }

    SenseDataAcknowledgeData(final IpdsByteArrayInputStream ipds) throws IOException {

        if (ipds.bytesAvailable() == 3) {
            this.exceptionId = ipds.readUnsignedInteger24();
            this.formatId = -1; // Marker for 3 byte data
        } else {
            assert ipds.bytesAvailable() == 24;

            this.exceptionId = ipds.readUnsignedInteger16();
            this.actionCode = ipds.readUnsignedByte();

            ipds.skip(1); // Skip retired item

            this.senseDetail = ipds.readUnsignedByte(); // in most cases 0xDE, but see format 2...
            this.formatId = ipds.readUnsignedByte();

            if (this.formatId == 0x00) {
                this.count = ipds.readUnsignedInteger16();
                this.overlayId = ipds.readUnsignedInteger16();
                this.pageSegmentId = ipds.readUnsignedInteger16();
                this.commandInProcess = ipds.readUnsignedInteger16();
                this.objectId = ipds.readUnsignedInteger16();
                this.exceptionSpecificInformation = ipds.readUnsignedInteger16();
                this.objectType = ipds.readUnsignedByte();
                this.exceptionId = this.exceptionId * 256 + ipds.readUnsignedByte();
                this.pageIdentifier = ipds.readUnsignedInteger32();

                assert ipds.bytesAvailable() == 0;
                return;
            }

            if (this.formatId == 0x01) {
                this.count = ipds.readUnsignedInteger16();
                this.overlayId = ipds.readUnsignedInteger16();
                this.pageSegmentId = ipds.readUnsignedInteger16();
                this.commandInProcess = ipds.readUnsignedInteger16();
                this.textPositionExceptionCount = ipds.readUnsignedByte();
                this.imagePositionExceptionCount = ipds.readUnsignedByte();
                this.rulePositionExceptionCount = ipds.readUnsignedByte();
                this.graphicsPositionExceptionCount = ipds.readUnsignedByte();
                this.objectType = ipds.readUnsignedByte();
                this.exceptionId = this.exceptionId * 256 + ipds.readUnsignedByte();
                this.pageIdentifier = ipds.readUnsignedInteger32();

                assert ipds.bytesAvailable() == 0;
                return;
            }

            if (this.formatId == 0x02) {
                this.rawData = ipds.readRemainingBytes();
                this.exceptionId = this.exceptionId * 256 + (this.rawData[13] & 0xFF);
                return;
            }

            if (this.formatId == 0x07) {
                this.count = ipds.readUnsignedInteger16();
                ipds.skip(10);
                this.objectType = ipds.readUnsignedByte();
                this.pageIdentifier = ipds.readUnsignedInteger32();

                assert ipds.bytesAvailable() == 0;
                return;
            }

            if (this.formatId == 0x08) {
                this.commandInProcess = ipds.readUnsignedInteger16();
                this.up3iErrorCode = ipds.readUnsignedInteger16();
                this.up3iPaperSequenceId = ipds.readUnsignedByte();
                this.up3iErrorInformation = ipds.readBytes(8);
                this.exceptionId = this.exceptionId * 256 + ipds.readUnsignedByte();
                this.pageIdentifier = ipds.readUnsignedInteger32();

                assert ipds.bytesAvailable() == 0;
                return;
            }

            this.rawData = ipds.readRemainingBytes();
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        if (this.formatId == -1) {
            ipds.writeUnsignedInteger24(this.exceptionId);
            return;
        }

        ipds.writeUnsignedInteger16(this.exceptionId >>> 16); // Only first two bytes
        ipds.writeUnsignedByte(this.actionCode);
        ipds.writeUnsignedByte(0x00);
        ipds.writeUnsignedByte(this.senseDetail);
        ipds.writeUnsignedByte(this.formatId);

        if (this.formatId == 0x00) {
            ipds.writeUnsignedInteger16(this.count);
            ipds.writeUnsignedInteger16(this.overlayId);
            ipds.writeUnsignedInteger16(this.pageSegmentId);
            ipds.writeUnsignedInteger16(this.commandInProcess);
            ipds.writeUnsignedInteger16(this.objectId);
            ipds.writeUnsignedInteger16(this.exceptionSpecificInformation);
            ipds.writeUnsignedByte(this.objectType);
            ipds.writeUnsignedByte(this.exceptionId & 0xFF); // Only the last byte
            ipds.writeUnsignedInteger32(this.pageIdentifier);

            return;
        }

        if (this.formatId == 0x01) {
            ipds.writeUnsignedInteger16(this.count);
            ipds.writeUnsignedInteger16(this.overlayId);
            ipds.writeUnsignedInteger16(this.pageSegmentId);
            ipds.writeUnsignedInteger16(this.commandInProcess);
            ipds.writeUnsignedByte(this.textPositionExceptionCount);
            ipds.writeUnsignedByte(this.imagePositionExceptionCount);
            ipds.writeUnsignedByte(this.rulePositionExceptionCount);
            ipds.writeUnsignedByte(this.graphicsPositionExceptionCount);
            ipds.writeUnsignedByte(this.objectType);
            ipds.writeUnsignedByte(this.exceptionId & 0xFF); // Only the last byte
            ipds.writeUnsignedInteger32(this.pageIdentifier);

            return;
        }

        if (this.formatId == 0x02) {
            ipds.writeBytes(this.rawData);
            return;
        }

        if (this.formatId == 0x07) {
            ipds.writeUnsignedInteger16(this.count);

            ipds.writeUnsignedInteger32(0x00); // 4 bytes
            ipds.writeUnsignedInteger32(0x00); // 4 bytes
            ipds.writeUnsignedInteger16(0x00); // 2 bytes

            ipds.writeUnsignedByte(this.objectType);
            ipds.writeUnsignedInteger32(this.pageIdentifier);

            return;
        }

        if (this.formatId == 0x08) {
            ipds.writeUnsignedInteger16(this.commandInProcess);
            ipds.writeUnsignedInteger16(this.up3iErrorCode);
            ipds.writeUnsignedByte(this.up3iPaperSequenceId);
            ipds.writeBytes(this.up3iErrorInformation);

            ipds.writeUnsignedByte(this.exceptionId & 0xFF); // Only the last byte
            ipds.writeUnsignedInteger32(this.pageIdentifier);

            return;
        }

        ipds.writeBytes(this.rawData);
    }

    /**
     * Returns the 3 byte exception code.
     */
    public int getExceptionId() {
        return this.exceptionId;
    }

    /**
     * Sets the 3 byte exception code.
     */
    public void setExceptionId(final int exceptionId) {
        this.exceptionId = exceptionId;
    }

    /**
     * Returns the action code.
     */
    public int getActionCode() {
        return this.actionCode;
    }

    /**
     * Sets the action code.
     */
    public void setActionCode(final int actionCode) {
        this.actionCode = actionCode;
    }

    /**
     * Returns the format ID.
     */
    public int getFormatId() {
        return this.formatId;
    }

    /**
     * Sets the format ID. Can be set to -1 to create 3 byte sense data.
     */
    public void setFormatId(final int formatId) {
        this.formatId = formatId;
    }

    /**
     * Returns the sense detail.
     */
    public int getSenseDetail() {
        return this.senseDetail;
    }

    /**
     * Sets the sense detail.
     */
    public void setSenseDetail(final int senseDetail) {
        this.senseDetail = senseDetail;
    }

    /**
     * Returns the count.
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Sets the count.
     */
    public void setCount(final int count) {
        this.count = count;
    }

    /**
     * Returns the overlay ID.
     */
    public int getOverlayId() {
        return this.overlayId;
    }

    /**
     * Sets the overlay ID.
     */
    public void setOverlayId(final int overlayId) {
        this.overlayId = overlayId;
    }

    /**
     * Returns the page segment ID.
     */
    public int getPageSegmentId() {
        return this.pageSegmentId;
    }

    /**
     * Sets the page segment ID.
     */
    public void setPageSegmentId(final int pageSegmentId) {
        this.pageSegmentId = pageSegmentId;
    }

    /**
     * Returns the value of the IPDS command that was in process.
     */
    public int getCommandInProcess() {
        return this.commandInProcess;
    }

    /**
     * Sets the value of the IPDS command that was in process.
     */
    public void setCommandInProcess(final int commandInProcess) {
        this.commandInProcess = commandInProcess;
    }

    /**
     * Returns the ID of the object.
     */
    public int getObjectId() {
        return this.objectId;
    }

    /**
     * Sets the ID of the object.
     */
    public void setObjectId(final int objectId) {
        this.objectId = objectId;
    }

    /**
     * Returns exception specific information.
     */
    public int getExceptionSpecificInformation() {
        return this.exceptionSpecificInformation;
    }

    /**
     * Sets exception specific information.
     */
    public void setExceptionSpecificInformation(final int exceptionSpecificInformation) {
        this.exceptionSpecificInformation = exceptionSpecificInformation;
    }

    /**
     * Returns the object type.
     */
    public int getObjectType() {
        return this.objectType;
    }

    /**
     * Sets the object type.
     */
    public void setObjectType(final int objectType) {
        this.objectType = objectType;
    }

    /**
     * Returns the page identifier.
     */
    public long getPageIdentifier() {
        return this.pageIdentifier;
    }

    /**
     * Sets the page identifier.
     */
    public void setPageIdentifier(final long pageIdentifier) {
        this.pageIdentifier = pageIdentifier;
    }

    /**
     * Returns the text position exception count.
     */
    public int getTextPositionExceptionCount() {
        return this.textPositionExceptionCount;
    }

    /**
     * Sets the text position exception count.
     */
    public void setTextPositionExceptionCount(final int textPositionExceptionCount) {
        this.textPositionExceptionCount = textPositionExceptionCount;
    }

    /**
     * Returns the image position exception count.
     */
    public int getImagePositionExceptionCount() {
        return this.imagePositionExceptionCount;
    }

    /**
     * Sets the image position exception count.
     */
    public void setImagePositionExceptionCount(final int imagePositionExceptionCount) {
        this.imagePositionExceptionCount = imagePositionExceptionCount;
    }

    /**
     * Returns the rule (bar-code-symbol bar) position exception count.
     */
    public int getRulePositionExceptionCount() {
        return this.rulePositionExceptionCount;
    }

    /**
     * Sets the rule (bar-code-symbol bar) position exception count.
     */
    public void setRulePositionExceptionCount(final int rulePositionExceptionCount) {
        this.rulePositionExceptionCount = rulePositionExceptionCount;
    }

    /**
     * Returns the graphics position exception count.
     */
    public int getGraphicsPositionExceptionCount() {
        return this.graphicsPositionExceptionCount;
    }

    /**
     * Sets the graphics position exception count.
     */
    public void setGraphicsPositionExceptionCount(final int graphicsPositionExceptionCount) {
        this.graphicsPositionExceptionCount = graphicsPositionExceptionCount;
    }

    /**
     * Returns the UP3i specific error code.
     */
    public int getUp3iErrorCode() {
        return this.up3iErrorCode;
    }

    /**
     * Sets the UP3i specific error code.
     */
    public void setUp3iErrorCode(final int up3iErrorCode) {
        this.up3iErrorCode = up3iErrorCode;
    }

    /**
     * Returns the UP3i specific paper sequence ID.
     */
    public int getUp3iPaperSequenceId() {
        return this.up3iPaperSequenceId;
    }

    /**
     * Sets the UP3i specific paper sequence ID.
     */
    public void setUp3iPaperSequenceId(final int up3iPaperSequenceId) {
        this.up3iPaperSequenceId = up3iPaperSequenceId;
    }

    /**
     * Returns the UP3i specific error information.
     */
    public byte[] getUp3iErrorInformation() {
        return this.up3iErrorInformation;
    }

    /**
     * Sets the UP3i specific error information.
     */
    public void setUp3iErrorInformation(final byte[] up3iErrorInformation) {
        this.up3iErrorInformation = up3iErrorInformation;
    }

    /**
     * Returns the raw data (only valid for unknown or retired formats).
     */
    public byte[] getRawData() {
        return this.rawData;
    }

    /**
     * Sets the raw data (only valid for unknown or retired formats).
     */
    public void setRawData(final byte[] rawData) {
        this.rawData = rawData;
    }
}
