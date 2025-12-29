package de.textmode.ipdsbox.ipds.xoaorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.textmode.ipdsbox.core.StringUtils;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;

/**
 * This class carries all parameters of the Request Resource List order.
 */
public final class RequestResourceListOrder extends XoaOrder {

    private int queryType;
    private int continuationIndicator;

    private final List<ResourceQuery> resourceQueries = new ArrayList<ResourceQuery>();


    /**
     * Constructs the {@link RequestResourceListOrder} from the given {@link IpdsByteArrayInputStream}.
     */
    RequestResourceListOrder(final IpdsByteArrayInputStream ipds) throws IOException {
        super(XoaOrderCode.ActivatePrinterAlarm);

        this.queryType = ipds.readUnsignedByte();
        this.continuationIndicator = ipds.readUnsignedInteger16();

        while (ipds.bytesAvailable() > 0) {
            this.resourceQueries.add(new ResourceQuery(ipds));
        }
    }

    @Override
    public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
        out.writeUnsignedInteger16(XoaOrderCode.ActivatePrinterAlarm.getValue());
        out.writeUnsignedByte(this.queryType);
        out.writeUnsignedInteger16(this.continuationIndicator);

        for (final ResourceQuery query : this.resourceQueries) {
            query.writeTo(out);
        }
    }

    /**
     * Returns the query type.
     */
    public int getQueryType() {
        return this.queryType;
    }

    /**
     * Sets the query type.
     */
    public void setQueryType(final int queryType) {
        this.queryType = queryType;
    }

    /**
     * Returns the continuation indicator.
     */
    public int getContinuationIndicator() {
        return this.continuationIndicator;
    }

    /**
     * Sets the continuation indicator.
     */
    public void setContinuationIndicator(final int continuationIndicator) {
        this.continuationIndicator = continuationIndicator;
    }

    /**
     * Accept method for the {@link XoaOrderVisitor}.
     */
    @Override
    public void accept(final XoaOrderVisitor visitor) {
        visitor.handle(this);
    }

    public class ResourceQuery {

        private int resourceType;
        private int resourceIdFormat;
        private byte[] resourceId;

        public ResourceQuery(final int resourceType, final int resourceIdFormat, final byte[] resourceId) {
            this.resourceType = resourceType;
            this.resourceIdFormat = resourceIdFormat;
            this.resourceId = resourceId;
        }

        public ResourceQuery(final IpdsByteArrayInputStream ipds) throws IOException {
            final int length = ipds.readUnsignedByte();
            this.resourceType = ipds.readUnsignedByte();
            this.resourceIdFormat = ipds.readUnsignedByte();
            this.resourceId = ipds.readBytes(length - 3);
        }

        public void writeTo(final IpdsByteArrayOutputStream out) throws IOException {
            out.writeUnsignedByte(3 + this.resourceId.length);
            out.writeUnsignedByte(this.resourceType);
            out.writeUnsignedByte(this.resourceIdFormat);
            out.writeBytes(this.resourceId);
        }

        /**
         * Returns the resource type.
         */
        public int getResourceType() {
            return this.resourceType;
        }

        /**
         * Sets the resource type.
         */
        public void setResourceType(final int resourceType) {
            this.resourceType = resourceType;
        }

        /**
         * Returns the resource ID format.
         */
        public int getResourceIdFormat() {
            return this.resourceIdFormat;
        }

        /**
         * Sets the resource ID format.
         */
        public void setResourceIdFormat(final int resourceIdFormat) {
            this.resourceIdFormat = resourceIdFormat;
        }

        /**
         * Returns the resource ID.
         */
        public byte[] getResourceId() {
            return this.resourceId;
        }

        /**
         * Sets the resource ID.
         */
        public void setResourceId(final byte[] resourceId) {
            this.resourceId = resourceId;
        }

        @Override
        public String toString() {
            return "ResourceQuery{"
                    + "resourceType=0x" + Integer.toHexString(this.resourceType)
                    + ", resourceIdFormat=0x" + Integer.toHexString(this.resourceIdFormat)
                    + ", resourceId=" + StringUtils.toHexString(this.resourceId)
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "RequestResourceListOrder{"
                + "queryType=" + this.queryType
                + ", continuationIndicator=" + this.continuationIndicator
                + ", resourceQueries=" + this.resourceQueries
                + '}';
    }
}
