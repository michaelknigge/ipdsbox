package mk.ipdsbox.ppd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;

/**
 * JUnit tests of the {@link PagePrinterRequestReader}.
 */
public final class PagePrinterRequestReaderTest extends TestCase {

    /**
     * Builds an {@link InputStream} from the given hex encoded data, reads / parses the
     * supplied request and returns it to the caller.
     *
     * @param data Hex encoded request.
     * @return Constructed {@link PagePrinterRequest}
     * @throws IOException If the data is invalid
     */
    private PagePrinterRequest getRequestFromStream(final String data) throws IOException {
        final byte[] input = DatatypeConverter.parseHexBinary(data);
        final ByteArrayInputStream stream = new ByteArrayInputStream(input);
        return PagePrinterRequestReader.read(stream);
    }

    /**
     * A valid request without data.
     */
    public void testValidCommandWithoutData() throws Exception {
        final PagePrinterRequest req = this.getRequestFromStream("0000000800000001");
        assertEquals(1, req.getRequest());
        assertEquals(0, req.getData().length);
    }

    /**
     * A valid request with data.
     */
    public void testValidCommandWithData() throws Exception {
        final PagePrinterRequest req = this.getRequestFromStream("0000000B000000AB010203");
        assertEquals(0xAB, req.getRequest());
        assertEquals(3, req.getData().length);
        assertTrue(Arrays.equals(new byte[] {0x01, 0x02, 0x03}, req.getData()));
    }

    /**
     * Empty stream.
     */
    public void testEmptyStream() throws Exception {
        assertNull(this.getRequestFromStream(""));
    }

    /**
     * Truncated after the second byte (truncated within the length field).
     */
    public void testTruncationInLengthField() throws Exception {
        try {
            this.getRequestFromStream("0000");
        } catch (final IOException e) {
            assertTrue(e.getMessage().contains(PagePrinterRequestReader.ERROR_UNEXPECTED_EOF));
        }
    }

    /**
     * Truncated after the 6th byte (truncated within the request field).
     */
    public void testTruncationInRequestField() throws Exception {
        try {
            this.getRequestFromStream("000000080000");
        } catch (final IOException e) {
            assertTrue(e.getMessage().contains(PagePrinterRequestReader.ERROR_UNEXPECTED_EOF));
        }
    }

    /**
     * Truncated after the 9th byte (truncated within the data field).
     */
    public void testTruncationInDataField() throws Exception {
        try {
            this.getRequestFromStream("0000000B000000AB01");
        } catch (final IOException e) {
            assertTrue(e.getMessage().contains(PagePrinterRequestReader.ERROR_UNEXPECTED_EOF));
        }
    }
}
