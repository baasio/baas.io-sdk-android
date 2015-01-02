/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kth.baasio.test.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;
import com.kth.baasio.entity.push.BaasioPayload;
import com.kth.baasio.entity.push.BaasioPush;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.exception.BaasioRuntimeException;
import com.kth.baasio.test.BaasioConfig;
import com.kth.baasio.test.BuildConfig;
import com.kth.baasio.test.MainActivity;
import com.kth.baasio.test.R;
import com.kth.baasio.test.UnitTestConfig;
import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import java.util.Random;
import java.util.UUID;

import static com.kth.common.utils.LogUtils.LOGE;
import static com.kth.common.utils.LogUtils.LOGI;
import static com.kth.common.utils.LogUtils.LOGW;

/**
 * {@link android.app.IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = LogUtils.makeLogTag("GCM");

    private static final int TRIGGER_SYNC_MAX_JITTER_MILLIS = 3 * 60 * 1000; // 3
                                                                             // minutes

    private static final Random sRandom = new Random();

    public GCMIntentService() {
        super(BaasioConfig.GCM_SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String regId) {
        LogUtils.LOGI(TAG, "Device registered: regId=" + regId);

        try {
            BaasioPush.register(context, regId);
        } catch (BaasioException e) {
            LogUtils.LOGI(TAG, e.toString());
        }
    }

    @Override
    protected void onUnregistered(Context context, String regId) {
        LogUtils.LOGI(TAG, "Device unregistered");

        try {
            BaasioPush.unregister(context);
        } catch (BaasioException e) {
            LogUtils.LOGI(TAG, e.toString());
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String announcement = intent.getStringExtra("message");
        if (announcement != null) {
            generateNotification(context, announcement);
            return;
        }

        int jitterMillis = (int)(sRandom.nextFloat() * TRIGGER_SYNC_MAX_JITTER_MILLIS);
        final String debugMessage = "Received message to trigger sync; " + "jitter = "
                + jitterMillis + "ms";
        LOGI(TAG, debugMessage);

        if (BuildConfig.DEBUG) {
            displayNotification(context, debugMessage);
        }

        generateNotification(context, announcement);
    }

    private void displayNotification(Context context, String message) {
        LOGI(TAG, "displayNotification: " + message);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        String body;
        try {
            LogUtils.LOGE(TAG, "GCM Received: " + message);
            BaasioPayload msg = JsonUtils.parse(message, BaasioPayload.class);
            if (msg == null) {
                return;
            }

            if (!ObjectUtils.isEmpty(msg.getAlert())) {
                body = msg.getAlert().replace("\\r\\n", "\n");
                if (!ObjectUtils.isEmpty(msg.getProperty(UnitTestConfig.PUSH_CUSTOM_FILED_KEY))) {
                    body = body + "("
                            + msg.getProperty(UnitTestConfig.PUSH_CUSTOM_FILED_KEY).getTextValue()
                            + ")";
                }

            } else {
                return;
            }
        } catch (BaasioRuntimeException e) {
            if (!ObjectUtils.isEmpty(message)) {
                body = message;
            } else {
                body = "Error";
            }

        }

        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context).setWhen(when)
                .setSmallIcon(icon).setContentTitle(context.getString(R.string.app_name))
                .setContentText(body).setContentIntent(intent).setTicker(body).setAutoCancel(true)
                .getNotification();

        notificationManager.notify(UUID.randomUUID().hashCode(), notification);
    }

    @Override
    public void onError(Context context, String errorId) {
        LogUtils.LOGE(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        LogUtils.LOGW(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
}
