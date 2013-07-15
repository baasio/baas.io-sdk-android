
package com.kth.baasio.callback;

import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.response.BaasioResponse;

import org.springframework.web.client.HttpClientErrorException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public abstract class BaasioQueryAsyncTask extends AsyncTask<Void, Void, BaasioResponse> {

    BaasioQueryCallback mCallback;

    BaasioQuery mQuery;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BaasioQueryAsyncTask(BaasioQuery query, BaasioQueryCallback callback) {
        this.mCallback = callback;
        this.mQuery = query;
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
    protected BaasioResponse doInBackground(Void... v) {
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

    public abstract BaasioResponse doTask() throws Exception;

    @Override
    protected void onPostExecute(BaasioResponse response) {
        if (mCallback != null && response != null) {
            mCallback.onResponse(response.getEntities(), response.getList(),
                    (BaasioQuery)mQuery.clone(), response.getTimestamp());
        }
    }
}
