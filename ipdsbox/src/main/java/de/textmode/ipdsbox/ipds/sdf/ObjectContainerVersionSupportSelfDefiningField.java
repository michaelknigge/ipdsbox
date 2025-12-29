package de.textmode.ipdsbox.ipds.sdf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.ByteUtils;
import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This self-defining field lists the object container versions supported by the printer.
 */
public final class ObjectContainerVersionSupportSelfDefiningField extends SelfDefiningField {

    public class VersionRecord {

        private byte[] regId;
        private int flags;
        private int majorVersion;
        private int minorVersion;
        private int subminorVersion;
        private String versionName;

        /**
         * Returns the MO:DCA-registered object ID.
         */
        public byte[] getRegId() {
            return this.regId;
        }

        /**
         * Sets the MO:DCA-registered object ID.
         */
        public void setRegId(final byte[] regId) {
            this.regId = regId;
        }

        /**
         * Returns the flags.
         */
        public int getFlags() {
            return this.flags;
        }

        /**
         * Sets the flags.
         */
        public void setFlags(final int flags) {
            this.flags = flags;
        }

        /**
         * Returns the major version.
         */
        public int getMajorVersion() {
            return this.majorVersion;
        }

        /**
         * Sets the major version.
         */
        public void setMajorVersion(final int majorVersion) {
            this.majorVersion = majorVersion;
        }

        /**
         * Returns the minor version.
         */
        public int getMinorVersion() {
            return this.minorVersion;
        }

        /**
         * Sets the minor version.
         */
        public void setMinorVersion(final int minorVersion) {
            this.minorVersion = minorVersion;
        }

        /**
         * Returns the subminor version.
         */
        public int getSubminorVersion() {
            return this.subminorVersion;
        }

        /**
         * Sets the subminor version.
         */
        public void setSubminorVersion(final int subminorVersion) {
            this.subminorVersion = subminorVersion;
        }

        /**
         * Returns the (optional) version name.
         */
        public String getVersionName() {
            return this.versionName;
        }

        /**
         * Sets the (optional) version name.
         */
        public void setVersionName(final String versionName) {
            this.versionName = versionName;
        }

        @Override
        public String toString() {
            return "VersionRecord{"	
                    + "regId=" + StringUtils.toHexString(this.regId)	
                    + ", flags=0x" + Integer.toHexString(this.flags)	
                    + ", majorVersion=" + this.majorVersion	
                    + ", minorVersion=" + this.minorVersion	
                    + ", subminorVersion=" + this.subminorVersion	
                    + ", versionName='" + this.versionName + '\''	
                    + '}';
        }
    }

    private static final Charset UTF16BE = Charset.forName("utf-16be");

    private List<VersionRecord> versionRecords = new ArrayList<>();

    /**
     * Creates a new {@link ObjectContainerVersionSupportSelfDefiningField}.
     */
    public ObjectContainerVersionSupportSelfDefiningField() throws IOException {
        super(SelfDefiningFieldId.ObjectContainerVersionSupport);
    }

    /**
     * Creates a {@link ObjectContainerVersionSupportSelfDefiningField} from the stream.
     */
    ObjectContainerVersionSupportSelfDefiningField(final IpdsByteArrayInputStream ipds) throws IOException {
        super(SelfDefiningFieldId.ObjectContainerVersionSupport);

        while (ipds.bytesAvailable() > 0) {
            final int entryLength = ipds.readUnsignedByte();

            final VersionRecord record = new VersionRecord();
            record.setRegId(ipds.readBytes(16));
            record.setFlags(ipds.readUnsignedByte());
            record.setMajorVersion(ipds.readUnsignedInteger16());
            record.setMinorVersion(ipds.readUnsignedInteger16());
            record.setSubminorVersion(ipds.readUnsignedInteger16());

            if (entryLength - 24 > 0) {
                record.setVersionName(
                        UTF16BE.decode(ByteBuffer.wrap(ipds.readBytes(entryLength - 24))).toString());
            }
        }
    }

    /**
     * Writes all data fields to the given {@code IpdsByteArrayOutputStream} in table order.
     */
    @Override
    public void writeTo(final IpdsByteArrayOutputStream ipds) throws IOException {
        final IpdsByteArrayOutputStream versionStream = new IpdsByteArrayOutputStream();

        for (final VersionRecord version : this.versionRecords) {

            final byte[] encodedVersionName = version.getVersionName() == null
                    ? ByteUtils.EMPTY_BYTE_ARRAY
                    : UTF16BE.encode(CharBuffer.wrap(version.getVersionName())).array();

            final int len = 24 + encodedVersionName.length;

            versionStream.writeUnsignedByte(len);
            versionStream.writeBytes(version.regId);
            versionStream.writeUnsignedByte(version.flags);
            versionStream.writeUnsignedInteger16(version.getMajorVersion());
            versionStream.writeUnsignedInteger16(version.getMinorVersion());
            versionStream.writeUnsignedInteger16(version.getSubminorVersion());
            versionStream.writeBytes(encodedVersionName);
        }

        final byte[] versionBytes = versionStream.toByteArray();

        ipds.writeUnsignedInteger16(4 + versionBytes.length);
        ipds.writeUnsignedInteger16(this.getSelfDefiningFieldId());
        ipds.writeBytes(versionBytes);
    }

    /**
     * Returns the version records.
     */
    public List<VersionRecord> getVersionRecords() {
        return this.versionRecords;
    }

    /**
     * Sets the version records.
     */
    public void setVersionRecords(final List<VersionRecord> versionRecords) {
        this.versionRecords = versionRecords;
    }

    @Override
    public String toString() {
        return "ObjectContainerVersionSupportSelfDefiningField{"	
                + "versionRecords=" + this.versionRecords	
                + '}';
    }
}
