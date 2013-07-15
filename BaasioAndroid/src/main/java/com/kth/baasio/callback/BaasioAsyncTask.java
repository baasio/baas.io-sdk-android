
package com.kth.baasio.callback;

import com.kth.baasio.exception.BaasioException;

import org.springframework.web.client.HttpClientErrorException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public abstract class BaasioAsyncTask<T> extends AsyncTask<Void, Void, T> {

    BaasioCallback<T> mCallback;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BaasioAsyncTask(BaasioCallback<T> callback) {
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
    protected T doInBackground(Void... v) {
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

    public abstract T doTask() throws BaasioException;

    @Override
    protected void onPostExecute(T response) {
        if (mCallback != null && response != null) {
            mCallback.onResponse(response);
        }
    }
}
