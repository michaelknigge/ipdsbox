package de.textmode.ipdsbox.ipds.commands;

import java.io.IOException;

import de.textmode.ipdsbox.core.InvalidIpdsCommandException;
import de.textmode.ipdsbox.io.IpdsByteArrayInputStream;
import de.textmode.ipdsbox.ipds.acknowledge.AcknowledgeReply;
import de.textmode.ipdsbox.ipds.xoaorders.UnknownXoaOrderCode;
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
    public static IpdsCommand create(final PagePrinterRequest request) throws IOException, InvalidIpdsCommandException, UnknownXoaOrderCode,UnknownXohOrderCode {

        if (request.getRequest() == 0x0E) {
            return create(new IpdsByteArrayInputStream(request.getData(), 8));
        }

        throw new IOException("Can not handle request type " + Integer.toHexString(request.getRequest()));
    }

    /**
     * Creates a {@link IpdsCommand} from the given byte array.
     */
    public static IpdsCommand create(final byte[] data) throws IOException, InvalidIpdsCommandException, UnknownXoaOrderCode,UnknownXohOrderCode {
        return create(new IpdsByteArrayInputStream(data));
    }

    /**
     * Creates a {@link IpdsCommand} from the given {@link IpdsByteArrayInputStream}.
     */
    public static IpdsCommand create(final IpdsByteArrayInputStream ipds) throws IOException, InvalidIpdsCommandException,UnknownXoaOrderCode, UnknownXohOrderCode {

        // TODO:
        // get rid of all the "UnknownFooExceptions"... create "RawFoo" objects instead so a implemented IPDS server (printer)
        // or IPDS client (spooler) don't abort due to a faulty IPDS command...

        // TODO: if we create the concreete IpdsCommand objects *ONLY* with this factoty, we could:
        //   make constructores package scoped
        //   pass the lengthe
        //   pass the commandID
        //   do not "rewind"
        //
        //   make things a little bit easier....
        //
         //   then do the same for all other objects we create with a factory (triplets, xoa, etc etc etc)
        ipds.skip(2); // Skip the 2 byte length field...

        // TODO really "inExists"??
        // TODO handle null...
        final IpdsCommandId commandId = IpdsCommandId.getForIfExists(ipds.readUnsignedInteger16());
        ipds.rewind(4); // Rewind so the SelfDefiningField implementation reads the whole SelfDefiningField

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

            default -> new RawIpdsCommand(ipds, commandId);
        };
    }
}
