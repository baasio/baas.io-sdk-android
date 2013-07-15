
package com.kth.baasio.callback;

import com.kth.baasio.entity.file.BaasioFile;
import com.kth.baasio.exception.BaasioException;

public interface BaasioUploadCallback {
    public void onResponse(BaasioFile response);

    public void onException(BaasioException e);

    public void onProgress(long total, long current);
}
