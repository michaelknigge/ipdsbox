package mk.ipdsbox.ppd;

import java.util.ArrayList;
import java.util.List;

/**
 * This {@link PagePrinterRequestHandler} collects all {@link PagePrinterRequest} in
 * an ArrayList for later analysis by the JUnit test.
 */
public final class TestcasePagePrinterRequestHandler implements PagePrinterRequestHandler {

    private final List<PagePrinterRequest> requests = new ArrayList<>();

    /**
     * Returns a list with all handled {@link PagePrinterRequest}s.
     *
     * @return a list with all handled {@link PagePrinterRequest}s.
     */
    public List<PagePrinterRequest> getHandledRequests() {
        return this.requests;
    }

    @Override
    public void handle(final PagePrinterRequest request) {
        this.requests.add(request);
    }

}
