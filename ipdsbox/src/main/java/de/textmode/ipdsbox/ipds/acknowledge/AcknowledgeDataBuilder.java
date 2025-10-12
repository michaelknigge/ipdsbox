package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * A builder for all supported {@link AcknowledgeData}.
 */
public final class AcknowledgeDataBuilder {

    /**
     * Private constructor to make checkstyle happy.
     */
    private AcknowledgeDataBuilder() {
    }

    /**
     * Builds a {@link AcknowledgeData} from the given byte array.
     */
    public static AcknowledgeData build(final int ackType, final IpdsByteArrayInputStream ipds) throws IOException {

        return switch (ackType) {
            case 0x00 -> new NoAcknowledgeData();
            case 0x40 -> new NoAcknowledgeData();


            default -> new RawAcknowledgeData(ipds);
        };
    }
}
