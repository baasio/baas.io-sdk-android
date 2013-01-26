
package com.kth.baasio.entity.file.multipart;

import com.kth.baasio.exception.BaasioError;

import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author <a href="mailto:vit at cleverua.com">Vitaliy Khudenko</a>
 */
public final class StringPart extends BasePart {

    private final byte[] valueBytes;

    /**
     * @param name String - name of parameter (may not be <code>null</code>).
     * @param value String - value of parameter (may not be <code>null</code>).
     * @param charset String, if null is passed then default "UTF-8" charset is
     *            used.
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException if either <code>value</code> or
     *             <code>name</code> is <code>null</code>.
     * @throws RuntimeException if <code>charset</code> is unsupported by OS.
     */
    public StringPart(String name, String value, String charset) {
        if (name == null) {
            throw new IllegalArgumentException(BaasioError.ERROR_FILE_MULTIPART_FORM_NAME_IS_NULL); //$NON-NLS-1$
        }
        if (value == null) {
            throw new IllegalArgumentException(BaasioError.ERROR_FILE_MULTIPART_FORM_VALUE_IS_NULL); //$NON-NLS-1$
        }

        final String partName = UrlEncodingHelper.encode(name, HTTP.DEFAULT_PROTOCOL_CHARSET);

        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        final String partCharset = charset;

        try {
            this.valueBytes = value.getBytes(partCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        headersProvider = new IHeadersProvider() {
            public String getContentDisposition() {
                return "Content-Disposition: form-data; name=\"" + partName + '"'; //$NON-NLS-1$
            }

            public String getContentType() {
                return "Content-Type: application/json";
            }

            public String getContentTransferEncoding() {
                return "Content-Transfer-Encoding: 8bit"; //$NON-NLS-1$
            }
        };
    }

    /**
     * Default "ISO-8859-1" charset is used.
     * 
     * @param name String - name of parameter (may not be <code>null</code>).
     * @param value String - value of parameter (may not be <code>null</code>).
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException if either <code>value</code> or
     *             <code>name</code> is <code>null</code>.
     */
    public StringPart(String name, String value) {
        this(name, value, null);
    }

    public long getContentLength(Boundary boundary) {
        return getHeader(boundary).length + valueBytes.length + CRLF.length;
    }

    public void writeTo(final OutputStream out, Boundary boundary) throws IOException {
        out.write(getHeader(boundary));
        out.write(valueBytes);
        out.write(CRLF);
    }
}
