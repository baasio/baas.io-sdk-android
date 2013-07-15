
package com.kth.baasio.callback;

import com.kth.baasio.entity.file.BaasioFile;
import com.kth.baasio.exception.BaasioException;

import org.springframework.web.client.HttpClientErrorException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public abstract class BaasioUploadAsyncTask extends AsyncTask<Void, Long, BaasioFile> {

    BaasioUploadCallback mCallback;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BaasioUploadAsyncTask(BaasioUploadCallback callback) {
        this.mCallback = callback;
    }

    private void onException(final BaasioException e) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onException(e);
                }
            }

        });
    }

    @Override
    protected BaasioFile doInBackground(Void... v) {
        try {
            return doTask();
        } catch (HttpClientErrorException e) {
            onException(new BaasioException(e.getStatusCode(), e.getResponseBodyAsString()));
        } catch (BaasioException e) {
            onException(e);
        } catch (Exception e) {
            onException(new BaasioException(e));
        }
        return null;
    }

    public abstract BaasioFile doTask() throws Exception;

    @Override
    protected void onPostExecute(BaasioFile response) {
        if (mCallback != null && response != null) {
            mCallback.onResponse(response);
        }
    }

    @Override
    protected void onProgressUpdate(Long... size) {
        if ((mCallback != null) && (size != null) && (size.length == 2)) {
            if (size[0] != null && size[1] != null) {
                mCallback.onProgress(size[0], size[1]);
            }
        }
    }
}
