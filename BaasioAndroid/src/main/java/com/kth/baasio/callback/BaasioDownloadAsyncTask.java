
package com.kth.baasio.callback;

import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.utils.ObjectUtils;

import org.springframework.web.client.HttpClientErrorException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public abstract class BaasioDownloadAsyncTask extends AsyncTask<Void, Long, String> {

    BaasioDownloadCallback mCallback;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BaasioDownloadAsyncTask(BaasioDownloadCallback callback) {
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
    protected String doInBackground(Void... v) {
        try {
            return doTask();
        } catch (final HttpClientErrorException e) {
            onException(new BaasioException(e.getStatusCode(), e.getResponseBodyAsString()));
        } catch (BaasioException e) {
            onException(e);
        } catch (Exception e) {
            onException(new BaasioException(e));
        }
        return null;
    }

    public abstract String doTask() throws Exception;

    @Override
    protected void onPostExecute(String response) {
        if (mCallback != null && !ObjectUtils.isEmpty(response)) {
            mCallback.onResponse(response);
        }
    }

    @Override
    protected void onProgressUpdate(Long... size) {
        if ((mCallback != null) && (size != null) && (size.length == 2)) {
            mCallback.onProgress(size[0], size[1]);
        }
    }
}
