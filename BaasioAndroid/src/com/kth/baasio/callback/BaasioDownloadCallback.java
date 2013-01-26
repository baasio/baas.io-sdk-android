
package com.kth.baasio.callback;

import com.kth.baasio.exception.BaasioException;

public interface BaasioDownloadCallback {
    public void onResponse(String localFilePath);

    public void onException(BaasioException e);

    public void onProgress(long total, long current);
}
