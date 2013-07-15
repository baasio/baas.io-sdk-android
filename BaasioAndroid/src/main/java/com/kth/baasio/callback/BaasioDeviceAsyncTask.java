
package com.kth.baasio.callback;

import com.kth.baasio.entity.push.BaasioDevice;
import com.kth.baasio.exception.BaasioException;

import org.springframework.web.client.HttpClientErrorException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public abstract class BaasioDeviceAsyncTask extends AsyncTask<Void, Void, BaasioDevice> {

    BaasioDeviceCallback mCallback;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BaasioDeviceAsyncTask(BaasioDeviceCallback callback) {
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
    protected BaasioDevice doInBackground(Void... v) {
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

    public abstract BaasioDevice doTask() throws BaasioException;

    @Override
    protected void onPostExecute(BaasioDevice response) {
        if (mCallback != null && response != null) {
            mCallback.onResponse(response);
        }
    }
}
