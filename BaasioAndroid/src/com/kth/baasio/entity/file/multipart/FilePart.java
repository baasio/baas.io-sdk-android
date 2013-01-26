
package com.kth.baasio.entity.file.multipart;

import com.kth.baasio.Baas;
import com.kth.baasio.exception.BaasioError;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author <a href="mailto:vit at cleverua.com">Vitaliy Khudenko</a>
 */
public final class FilePart extends BasePart {

    private final File mFile;

    private final BaasioProgressListener mListener;

    private boolean mIsCancelled = false;

    private long mTransferredBytes;

    private int mUploadBuffSize = Baas.getUploadBuffSize();

    public interface BaasioProgressListener {
        public void updateTransferred(long transferedBytes);
    }

    /**
     * @param name String - name of parameter (may not be <code>null</code>).
     * @param file File (may not be <code>null</code>).
     * @param filename String. If <code>null</code> is passed, then
     *            <code>file.getName()</code> is used.
     * @param contentType String. If <code>null</code> is passed, then default
     *            "application/octet-stream" is used.
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException if either <code>file</code> or
     *             <code>name</code> is <code>null</code>.
     */
    public FilePart(String name, File file, String filename, String contentType,
            BaasioProgressListener listener) {
        if (file == null) {
            throw new IllegalArgumentException(BaasioError.ERROR_FILE_MULTIPART_FORM_FILE_IS_NULL); //$NON-NLS-1$
        }
        if (name == null) {
            throw new IllegalArgumentException(BaasioError.ERROR_FILE_MULTIPART_FORM_NAME_IS_NULL); //$NON-NLS-1$
        }

        this.mFile = file;
        this.mListener = listener;
        this.mTransferredBytes = 0;

        final String partName = name;
        final String partFilename = (filename == null) ? mFile.getName() : filename;

        final String partContentType = (contentType == null) ? HTTP.DEFAULT_CONTENT_TYPE
                : contentType;

        headersProvider = new IHeadersProvider() {
            public String getContentDisposition() {
                return "Content-Disposition: form-data; name=\"" + partName //$NON-NLS-1$
                        + "\"; filename=\"" + partFilename + '"'; //$NON-NLS-1$
            }

            public String getContentType() {
                return "Content-Type: " + partContentType; //$NON-NLS-1$
            }

            public String getContentTransferEncoding() {
                return "Content-Transfer-Encoding: binary"; //$NON-NLS-1$
            }
        };
    }

    public long getContentLength(Boundary boundary) {
        return getHeader(boundary).length + mFile.length() + CRLF.length;
    }

    public void cancel() {
        mIsCancelled = true;
    }

    public void writeTo(OutputStream out, Boundary boundary) throws IOException {
        out.write(getHeader(boundary));
        InputStream in = new FileInputStream(mFile);
        try {
            byte[] tmp = new byte[mUploadBuffSize];
            int l;
            while ((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
                mTransferredBytes += l;

                if (mListener != null) {
                    mListener.updateTransferred(mTransferredBytes);
                }

                if (mIsCancelled) {
                    throw new IOException(BaasioError.ERROR_FILE_TASK_CANCELLED);
                }
            }
        } finally {
            in.close();
        }
        out.write(CRLF);
    }
}
