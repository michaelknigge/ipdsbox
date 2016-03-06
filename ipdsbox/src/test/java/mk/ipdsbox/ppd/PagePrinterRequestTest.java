package mk.ipdsbox.ppd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link PagePrinterRequest}.
 */
public final class PagePrinterRequestTest extends TestCase {

    /**
     * Builds a {@link PagePrinterRequest} without a data portion.
     */
    public void testPagePrinterRequestWithoutData() throws IOException {
        final PagePrinterRequest req = new PagePrinterRequest(0x12345678);

        assertEquals(0x12345678, req.getRequest());
        assertEquals(0, req.getData().length);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        req.writeTo(baos);

        final byte[] expected = new byte[] {0x00, 0x00, 0x00, 0x08, 0x12, 0x34, 0x56, 0x78};
        assertTrue(Arrays.equals(expected, baos.toByteArray()));
    }

    /**
     * Builds a {@link PagePrinterRequest} with a data portion.
     */
    public void testPagePrinterRequestWithData() throws IOException {
        final byte[] data = new byte[] {0x11, 0x22, 0x33, 0x44};
        final PagePrinterRequest req = new PagePrinterRequest(0x56781234, data);

        assertEquals(0x56781234, req.getRequest());
        assertTrue(Arrays.equals(data, req.getData()));

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        req.writeTo(baos);

        final byte[] expected = new byte[] {0x00, 0x00, 0x00, 0x0C, 0x56, 0x78, 0x12, 0x34, 0x11, 0x22, 0x33, 0x44};
        assertTrue(Arrays.equals(expected, baos.toByteArray()));
    }
}
