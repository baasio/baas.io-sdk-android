
package com.kth.baasio.callback;

import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

import org.springframework.web.client.HttpClientErrorException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public abstract class BaasioSignUpAsyncTask extends AsyncTask<Void, BaasioException, BaasioUser> {

    BaasioSignUpCallback mCallback;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BaasioSignUpAsyncTask(BaasioSignUpCallback callback) {
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
    protected BaasioUser doInBackground(Void... v) {
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

    public abstract BaasioUser doTask() throws BaasioException;

    @Override
    protected void onPostExecute(BaasioUser response) {
        if (mCallback != null && response != null) {
            mCallback.onResponse(response);
        }
    }

    @Override
    protected void onProgressUpdate(BaasioException... e) {
        if ((mCallback != null) && (e != null) && (e.length > 0)) {
            mCallback.onException(e[0]);
        }
    }
}
