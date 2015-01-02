package com.kth.baasio.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMRegistrar;
import com.kth.baasio.Baas;
import com.kth.common.utils.LogUtils;

/**
 * Created by brad on 14. 12. 24..
 */
public class GCMBootReceiver extends BroadcastReceiver{
    private static final String TAG = LogUtils.makeLogTag("GCM");

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Baas.io().isGcmEnabled()) {
            LogUtils.LOGD(TAG, "GCMBootReceiver");
            GCMRegistrar.register(context.getApplicationContext(), Baas.io().getGcmSenderId());
        }
    }
}
