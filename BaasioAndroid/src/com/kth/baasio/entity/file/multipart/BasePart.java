
package com.kth.baasio.entity.file.multipart;

import com.kth.baasio.exception.BaasioError;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;

/**
 * Parent class for FilePart and StringPart.
 * 
 * @author <a href="mailto:vit at cleverua.com">Vitaliy Khudenko</a>
 */
/* package */abstract class BasePart implements Part {

    protected static final byte[] CRLF = EncodingUtils.getAsciiBytes(MultipartEntity.CRLF);

    protected interface IHeadersProvider {
        public String getContentDisposition();

        public String getContentType();

        public String getContentTransferEncoding();
    }

    protected IHeadersProvider headersProvider; // must be initialized in
                                                // descendant constructor

    private byte[] header;

    protected byte[] getHeader(Boundary boundary) {
        if (header == null) {
            header = generateHeader(boundary); // lazy init
        }
        return header;
    }

    private byte[] generateHeader(Boundary boundary) {
        if (headersProvider == null) {
            throw new RuntimeException(BaasioError.ERROR_FILE_MULTIPART_FORM_UNINIT_HEADERSPROVIDER); //$NON-NLS-1$
        }
        final ByteArrayBuffer buf = new ByteArrayBuffer(256);
        append(buf, boundary.getStartingBoundary());
        try {
            append(buf, headersProvider.getContentDisposition().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            append(buf, headersProvider.getContentDisposition());
        }
        append(buf, CRLF);
        append(buf, headersProvider.getContentType());
        append(buf, CRLF);
        append(buf, headersProvider.getContentTransferEncoding());
        append(buf, CRLF);
        append(buf, CRLF);
        return buf.toByteArray();
    }

    private static void append(ByteArrayBuffer buf, String data) {
        append(buf, EncodingUtils.getAsciiBytes(data));
    }

    private static void append(ByteArrayBuffer buf, byte[] data) {
        buf.append(data, 0, data.length);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("\n");
        builder.append(headersProvider.getContentDisposition());
        builder.append("\n");
        builder.append(headersProvider.getContentType());
        builder.append("\n");
        builder.append(headersProvider.getContentTransferEncoding());
        builder.append("\n");

        return builder.toString();
    }
}
