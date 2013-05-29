
package com.kth.baasio.entity.push;

import com.google.android.gcm.GCMRegistrar;
import com.kth.baasio.Baas;
import com.kth.baasio.BuildConfig;
import com.kth.baasio.callback.BaasioAsyncTask;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioDeviceAsyncTask;
import com.kth.baasio.callback.BaasioDeviceCallback;
import com.kth.baasio.callback.BaasioResponseCallback;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.preferences.BaasioPreferences;
import com.kth.baasio.response.BaasioResponse;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import org.springframework.http.HttpMethod;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class BaasioPush {
    private static final String TAG = LogUtils.makeLogTag(BaasioPush.class);

    private static final int MAX_ATTEMPTS = 5;

    private static final int BACKOFF_MILLIS = 2000;

    private static final Random sRandom = new Random();

    private static final String TAG_REGEXP = "^[a-zA-Z0-9-_]*$";

    static List<String> getTagList(String tagString) {
        List<String> result = new ArrayList<String>();

        String[] tags = tagString.split("\\,");
        for (String tag : tags) {
            tag = tag.toLowerCase(Locale.getDefault()).trim();
            if (!ObjectUtils.isEmpty(tag)) {
                result.add(tag);
            }
        }

        return result;
    }

    public BaasioPush() {

    }

    public static BaasioDevice register(Context context, String regId) throws BaasioException {
        if (!Baas.io().isGcmEnabled()) {
            throw new BaasioException(BaasioError.ERROR_GCM_DISABLED);
        }

        GCMRegistrar.checkDevice(context);
        if (BuildConfig.DEBUG) {
            GCMRegistrar.checkManifest(context);
        }

        String signedInUsername = "";

        if (!ObjectUtils.isEmpty(Baas.io().getSignedInUser())) {
            signedInUsername = Baas.io().getSignedInUser().getUsername();
        }

        String newTags = BaasioPreferences.getNeedRegisteredTags(context);
        String oldRegId = BaasioPreferences.getRegisteredRegId(context);

        if (GCMRegistrar.isRegisteredOnServer(context)) {
            String registeredUsername = BaasioPreferences.getRegisteredUserName(context);

            if (registeredUsername.equals(signedInUsername)) {
                String curTags = BaasioPreferences.getRegisteredTags(context);

                if (curTags.equals(newTags)) {
                    if (oldRegId.equals(regId)) {
                        LogUtils.LOGV(TAG, "BaasioPush.register() called but already registered.");
                        throw new BaasioException(BaasioError.ERROR_GCM_ALREADY_REGISTERED);
                    } else {
                        LogUtils.LOGV(
                                TAG,
                                "BaasioPush.register() called. Already registered on the GCM server. But, need to register again because regId changed.");
                    }
                } else {
                    LogUtils.LOGV(
                            TAG,
                            "BaasioPush.register() called. Already registered on the GCM server. But, need to register again because tags changed.");
                }
            } else {
                LogUtils.LOGV(
                        TAG,
                        "BaasioPush.register() called. Already registered on the GCM server. But, need to register again because username changed.");
            }
        }

        BaasioDevice device = new BaasioDevice();

        if (ObjectUtils.isEmpty(device.getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(device.getPlatform())) {
            device.setPlatform("G");
        }

        if (ObjectUtils.isEmpty(regId)) {
            throw new IllegalArgumentException(BaasioError.ERROR_GCM_MISSING_REGID);
        }

        device.setToken(regId);

        String tagString = BaasioPreferences.getNeedRegisteredTags(context);
        List<String> tags = getTagList(tagString);

        device.setTags(tags);

        long backoff = BACKOFF_MILLIS + sRandom.nextInt(1000);

        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            LogUtils.LOGV(TAG, "#" + i + " Attempt..");
            try {
                BaasioResponse response = null;
                if (ObjectUtils.isEmpty(oldRegId)) {
                    LogUtils.LOGV(TAG, "POST /devices");
                    LogUtils.LOGV(TAG, "Request: " + device.toString());

                    response = Baas.io().apiRequest(HttpMethod.POST, null, device, "devices");

                    LogUtils.LOGV(TAG, "Response: " + response.toString());
                } else {
                    if (!oldRegId.equals(regId)) {
                        LogUtils.LOGV(TAG, "RegId changed!!!!");

                        LogUtils.LOGV(TAG, "New RegId: " + regId);
                        LogUtils.LOGV(TAG, "Old RegId: " + oldRegId);
                    } else {
                        LogUtils.LOGV(TAG, "New and old regId are same!!!");

                        LogUtils.LOGV(TAG, "RegId: " + oldRegId);
                    }

                    LogUtils.LOGV(TAG, "PUT /devices/" + oldRegId);
                    LogUtils.LOGV(TAG, "Request: " + device.toString());

                    response = Baas.io().apiRequest(HttpMethod.PUT, null, device, "devices",
                            oldRegId);

                    LogUtils.LOGV(TAG, "Response: " + response.toString());
                }

                if (response != null) {
                    BaasioDevice entity = response.getFirstEntity().toType(BaasioDevice.class);
                    if (!ObjectUtils.isEmpty(entity)) {
                        GCMRegistrar.setRegisteredOnServer(context, true);

                        BaasioPreferences.setRegisteredTags(context, tagString);
                        BaasioPreferences.setRegisteredUserName(context, signedInUsername);

                        String newDeviceUuid = entity.getUuid().toString();

                        BaasioPreferences.setDeviceUuidForPush(context, newDeviceUuid);

                        BaasioPreferences.setRegisteredRegId(context, regId);
                        return entity;
                    }

                    throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
                }

                throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
            } catch (BaasioException e) {
                LogUtils.LOGV(TAG, e.toString());

                String statusCode = e.getStatusCode();
                if (!ObjectUtils.isEmpty(statusCode)) {
                    if (statusCode.equals("400") && e.getErrorCode() == 913) {
                        // 이미 regId가 등록되어 있음. 하지만 태그 정보가 업데이트되어 있는지 알 수 없으니
                        // Retry

                        LogUtils.LOGV(
                                TAG,
                                "Already registered on the GCM server. But, need to register again because other data could be changed.");

                        oldRegId = regId;
                        i = 1;
                    } else {
                        if (!statusCode.startsWith("5")) {
                            break;
                        }
                    }
                }

                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                LogUtils.LOGE(TAG, "Failed to register on attempt " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    LogUtils.LOGV(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    LogUtils.LOGD(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return null;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }

        return null;
    }

    /**
     * Register device. If server is not available(HTTP status 5xx), it will
     * retry 5 times. Executes asynchronously in background and the callbacks
     * are called in the UI thread.
     * 
     * @param context Context
     * @param callback GCM registration result callback
     * @return registration task
     */
    public static BaasioDeviceAsyncTask registerInBackground(final Context context,
            final BaasioDeviceCallback callback) {
        if (!Baas.io().isGcmEnabled()) {
            if (callback != null) {
                callback.onException(new BaasioException(BaasioError.ERROR_GCM_DISABLED));
            }
            return null;
        }

        final String regId = GCMRegistrar.getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {
            GCMRegistrar.register(context, Baas.io().getGcmSenderId());
        } else {
            BaasioDeviceAsyncTask task = new BaasioDeviceAsyncTask(callback) {
                @Override
                public BaasioDevice doTask() throws BaasioException {
                    BaasioDevice device = register(context, regId);
                    if (ObjectUtils.isEmpty(device)) {
                        GCMRegistrar.unregister(context);
                    }
                    return device;
                }
            };
            task.execute();
            return task;
        }

        return null;
    }

    /**
     * Register device with tags. If server is not available(HTTP status 5xx),
     * it will retry 5 times. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param context Context
     * @param tags tags
     * @param callback GCM registration result callback
     * @return registration task
     */
    public static BaasioDeviceAsyncTask registerWithTagsInBackground(final Context context,
            String tags, final BaasioDeviceCallback callback) {
        if (!Baas.io().isGcmEnabled()) {
            if (callback != null) {
                callback.onException(new BaasioException(BaasioError.ERROR_GCM_DISABLED));
            }
            return null;
        }

        List<String> tagList = getTagList(tags);
        for (String tag : tagList) {
            if (tag.length() > 12) {
                throw new IllegalArgumentException(BaasioError.ERROR_GCM_TAG_LENGTH_EXCEED);
            }

            Pattern pattern = Pattern.compile(TAG_REGEXP);
            if (!pattern.matcher(tag).matches()) {
                throw new IllegalArgumentException(BaasioError.ERROR_GCM_TAG_PATTERN_MISS_MATCHED);
            }
        }

        BaasioPreferences.setNeedRegisteredTags(context, tags);

        return registerInBackground(context, callback);
    }

    /**
     * Unregister device. However, server is not available(HTTP status 5xx), it
     * will not retry.
     * 
     * @param context Context
     */
    public static BaasioResponse unregister(Context context) throws BaasioException {
        if (!Baas.io().isGcmEnabled()) {
            throw new BaasioException(BaasioError.ERROR_GCM_DISABLED);
        }

        if (!GCMRegistrar.isRegisteredOnServer(context)) {
            throw new BaasioException(BaasioError.ERROR_GCM_ALREADY_UNREGISTERED);
        }

        String regId = BaasioPreferences.getRegisteredRegId(context);

        if (!ObjectUtils.isEmpty(regId)) {
            LogUtils.LOGV(TAG, "DELETE /devices/" + regId);

            BaasioResponse response = Baas.io().apiRequest(HttpMethod.DELETE, null, null,
                    "devices", regId);

            BaasioPreferences.setDeviceUuidForPush(context, "");
            BaasioPreferences.setNeedRegisteredTags(context, "");
            BaasioPreferences.setRegisteredUserName(context, "");
            BaasioPreferences.setRegisteredTags(context, "");
            BaasioPreferences.setRegisteredRegId(context, "");

            GCMRegistrar.setRegisteredOnServer(context, false);

            if (response != null) {
                LogUtils.LOGV(TAG, "Response: " + response.toString());
                return response;
            } else {
                throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
            }
        }

        throw new BaasioException(BaasioError.ERROR_GCM_MISSING_REGID);
    }

    /**
     * Unregister device. However, server is not available(HTTP status 5xx), it
     * will not retry. Executes asynchronously in background and the callbacks
     * are called in the UI thread.
     * 
     * @param context Context
     * @param callback GCM unregistration result callback
     */
    public static void unregisterInBackground(final Context context,
            final BaasioResponseCallback callback) {
        if (!Baas.io().isGcmEnabled()) {
            if (callback != null) {
                callback.onException(new BaasioException(BaasioError.ERROR_GCM_DISABLED));
            }
            return;
        }

        (new BaasioAsyncTask<BaasioResponse>(callback) {
            @Override
            public BaasioResponse doTask() throws BaasioException {
                return unregister(context);
            }
        }).execute();
    }

    /**
     * Send a push message.
     * 
     * @param message push message
     */
    public static BaasioMessage sendPush(BaasioMessage message) throws BaasioException {
        if (ObjectUtils.isEmpty(message)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_MESSAGE);
        }

        if (ObjectUtils.isEmpty(message.getTarget())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TARGET);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.POST, null, message, "pushes");

        if (response != null) {
            BaasioMessage entity = response.getFirstEntity().toType(BaasioMessage.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Send a push message. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param message Push message
     * @param callback Result callback
     */
    public static void sendPushInBackground(final BaasioMessage message,
            final BaasioCallback<BaasioMessage> callback) {
        (new BaasioAsyncTask<BaasioMessage>(callback) {
            @Override
            public BaasioMessage doTask() throws BaasioException {
                return sendPush(message);
            }
        }).execute();
    }
}
