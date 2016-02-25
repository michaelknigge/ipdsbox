package mk.ipdsbox.ppd;

/**
 * This is the handler interface for every contructed {@link PagePrinterRequest}.
 */
public interface PagePrinterRequestHandler {

    /**
     * Handles the {@link PagePrinterRequest}.
     */
    void handle(final PagePrinterRequest request);
}
