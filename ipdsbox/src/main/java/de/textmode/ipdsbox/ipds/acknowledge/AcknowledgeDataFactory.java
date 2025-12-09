package de.textmode.ipdsbox.ipds.acknowledge;

import java.io.IOException;

import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;

/**
 * A factory for all supported {@link AcknowledgeData}.
 */
public final class AcknowledgeDataFactory {

    /**
     * Private constructor to make checkstyle happy.
     */
    private AcknowledgeDataFactory() {
    }

    /**
     * Creates a {@link AcknowledgeData} from the given {@link IpdsByteArrayInputStream}.
     */
    public static AcknowledgeData create(final int ackType, final IpdsByteArrayInputStream ipds) throws IOException {

        return switch (ackType) {
            case 0x00 -> new NoAcknowledgeData();
            case 0x40 -> new NoAcknowledgeData();

            case 0x80 -> new SenseDataAcknowledgeData(ipds);
            case 0xC0 -> new SenseDataAcknowledgeData(ipds);

            case 0x01 -> new SenseTypeAndModelAcknowledgeData(ipds);
            case 0x41 -> new SenseTypeAndModelAcknowledgeData(ipds);

            case 0x06 -> new ObtainPrinterCharacteristicsAcknowledgeData(ipds);
            case 0x46 -> new ObtainPrinterCharacteristicsAcknowledgeData(ipds);

            default -> new RawAcknowledgeData(ipds);
        };
    }
}
