package de.textmode.ipdsbox.ppd;

/**
 * This is the handler interface for every constructed {@link PagePrinterRequest}.
 */
public interface PagePrinterRequestHandler {

    /**
     * Handles the {@link PagePrinterRequest}.
     */
    void handle(final PagePrinterRequest request);
}
