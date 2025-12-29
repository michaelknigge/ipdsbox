package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;
import java.util.List;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.io.IpdsByteArrayOutputStream;
import de.textmode.ipdsbox.ipds.acknowledge.AcknowledgeReply;
import de.textmode.ipdsbox.ipds.xohorders.UnknownXohOrderCode;
import de.textmode.ipdsbox.ppd.PagePrinterRequest;

/**
 * A factory for all supported {@link IpdsCommand}s.
 */
public final class IpdsCommandFactory {

    private IpdsCommandFactory() {
    }

    /**
     * Creates a {@link IpdsCommand} from the given {@link PagePrinterRequest}.
     */
    public static IpdsCommand create(final PagePrinterRequest request) throws IOException, InvalidIpdsCommandException, UnknownXohOrderCode {

        if (request.getRequest() == 0x0E) {
            return create(new IpdsByteArrayInputStream(request.getData(), 8));
        }

        throw new IOException("Can not handle request type " + Integer.toHexString(request.getRequest()));
    }

    /**
     * Creates a {@link IpdsCommand} from the given List of {@link PagePrinterRequest}s. This method allows combining
     * multiple "Acknowledge Reply" commands to be combined to one Acknowledge Reply. The caller is responsible to
     * provide {@link PagePrinterRequest}s that "fit together" (i. e. that all {@link PagePrinterRequest}s have the
     * same correlation ID (if present).
     */
    public static IpdsCommand create(final List<PagePrinterRequest> requests) throws IOException, InvalidIpdsCommandException, UnknownXohOrderCode {

        final IpdsByteArrayOutputStream ipdsOutputStream = new IpdsByteArrayOutputStream();

        for (final PagePrinterRequest request : requests) {
            if (request.getRequest() != 0x0E) {
                throw new IOException("Can not handle request type " + Integer.toHexString(request.getRequest()));
            }

            final IpdsByteArrayInputStream ipds = new IpdsByteArrayInputStream(request.getData(), 8);
            ipds.skip(2); // Skip the 2 byte length field...

            final IpdsCommandId commandId = IpdsCommandId.getIfKnown(ipds.readUnsignedInteger16());

            if (commandId != IpdsCommandId.ACK) {
                throw new InvalidIpdsCommandException("Only `Acknowledge Replies` may be combined");
            }

            // TODO test cases..
            //   different commands (one no ACK)
            //   with and without correlation IDs

            if (ipdsOutputStream.getSize() == 0) {
                ipdsOutputStream.writeUnsignedInteger16(0x0000); // Dummy length...
                ipdsOutputStream.writeUnsignedInteger16(commandId.getValue());
            } else {
                // Read the Flag byte...
                final IpdsCommandFlags flags = new IpdsCommandFlags((byte) ipds.readUnsignedByte());

                // Skip the correlation ID if present...
                if (flags.hasCorrelationID()) {
                    ipds.skip(2);
                }

                AcknowledgeReply.skipCounters(ipds);
            }

            ipdsOutputStream.writeBytes(ipds.readRemainingBytes());
        }

        final byte[] ipdsData = ipdsOutputStream.toByteArray();
        final int len = ipdsData.length;

        ipdsData[0] = (byte) ((len >>> 8) & 0xFF);
        ipdsData[1] = (byte) (len & 0xFF);

        final IpdsCommand ipdsCommand = create(ipdsData);
        ipdsCommand.getCommandFlags().isAcknowledgmentContinuationRequested(false);

        return ipdsCommand;
    }

    /**
     * Creates a {@link IpdsCommand} from the given byte array.
     */
    public static IpdsCommand create(final byte[] data) throws IOException, InvalidIpdsCommandException, UnknownXohOrderCode {
        return create(new IpdsByteArrayInputStream(data));
    }

    /**
     * Creates a {@link IpdsCommand} from the given {@link IpdsByteArrayInputStream}.
     */
    public static IpdsCommand create(final IpdsByteArrayInputStream ipds) throws IOException, InvalidIpdsCommandException, UnknownXohOrderCode {

        // The implementation requires that the IpdsByteArrayInputStream contains exactly as many
        // bytes as specified in the length field.
        final int availableLength = ipds.bytesAvailable();
        final int commandLength = ipds.readUnsignedInteger16();

        if (commandLength != availableLength) {
            throw new InvalidIpdsCommandException(String.format(
                "An IPDS command to be read seems to be %1$d bytes long but the IPDS data stream ends after %2$d bytes.",
                commandLength,
                availableLength));
        }

        final int commandIdValue = ipds.readUnsignedInteger16();
        final IpdsCommandId commandId = IpdsCommandId.getIfKnown(commandIdValue);

        if (commandId == null) {
            return new UnknownIpdsCommand(ipds, commandIdValue);
        }

        // TODO: Implement "toString()" in all IpdsCommands...

        return switch (commandId) {
            case ACK -> new AcknowledgeReply(ipds);
            //case AFO -> new ApplyFinishingOperationsCommand(ipds);
            case AR -> new ActivateResourceCommand(ipds);
            //case BO -> new BeginOverlayCommand(ipds);
            case BP -> new BeginPageCommand(ipds);
            //case BPS -> new BeginPageSegmentCommand(ipds);
            //case DDOFC -> new DeactivateDataObjectFontComponentCommand(ipds);
            //case DDOR -> new DeactivateDataObjectResourceCommand(ipds);
            case DF -> new DeactivateFontCommand(ipds);
            //case DO -> new DeactivateOverlayCommand(ipds);
            //case DORE -> new DataObjectResourceEquivalenceCommand(ipds);
            //case DPS -> new DeactivatePageSegmentCommand(ipds);
            case DUA -> new DefineUserAreaCommand(ipds);
            case END -> new EndCommand(ipds);
            case EP -> new EndPageCommand(ipds);
            case ICMR -> new InvokeCmrCommand(ipds);
            //case IDO -> new IncludeDataObjectCommand(ipds);
            //case IO -> new IncludeOverlayCommand(ipds);
            //case IPS -> new IncludePageSegmentCommand(ipds);
            case ISP -> new IncludeSavedPageCommand(ipds);
            case LCC -> new LoadCopyControlCommand(ipds);
            //case LCP -> new LoadCodePageCommand(ipds);
            //case LCPC -> new LoadCodePageControlCommand(ipds);
            //case LE -> new LoadEquivalenceCommand(ipds);
            //case LF -> new LoadFontCommand(ipds);
            //case LFC -> new LoadFontControlCommand(ipds);
            //case LFCSC -> new LoadFontCharacterSetControlCommand(ipds);
            case LFE -> new LoadFontEquivalenceCommand(ipds);
            //case LFI -> new LoadFontIndexCommand(ipds);
            case LPD -> new LogicalPageDescriptorCommand(ipds);
            case LPP -> new LogicalPagePositionCommand(ipds);
            //case LSS -> new LoadSymbolSetCommand(ipds);
            case MID -> new ManageIpdsDialogCommand(ipds);
            case NOP -> new NoOperationCommand(ipds);
            case PFC -> new PresentationFidelityControlCommand(ipds);
            //case RPO -> new RasterizePresentationObjectCommand(ipds);
            //case RRR -> new RemoveResidentResourceCommand(ipds);
            //case RRRL -> new RequestResidentResourceListCommand(ipds);
            case SHS -> new SetHomeStateCommand(ipds);
            case SPE -> new SetPresentationEnvironmentCommand(ipds);
            case STM -> new SenseTypeAndModelCommand(ipds);
            //case WBC -> new WriteBarCodeCommand(ipds);
            //case WBCC -> new WriteBarCodeControlCommand(ipds);
            //case WG -> new WriteGraphicsCommand(ipds);
            //case WGC -> new WriteGraphicsControlCommand(ipds);
            //case WI -> new WriteImageCommand(ipds);
            //case WI2 -> new WriteImage2Command(ipds);
            //case WIC -> new WriteImageControlCommand(ipds);
            //case WIC2 -> new WriteImageControl2Command(ipds);
            //case WOC -> new WriteObjectContainerCommand(ipds);
            //case WOCC -> new WriteObjectContainerControlCommand(ipds);
            case WT -> new WriteTextCommand(ipds);
            //case WTC -> new WriteTextControlCommand(ipds);
            case XOA -> new ExecuteOrderAnystateCommand(ipds);
            case XOH -> new ExecuteOrderHomeStateCommand(ipds);

            default -> new UnknownIpdsCommand(ipds, commandIdValue);
        };
    }
}
