
package com.kth.baasio.entity.push;

import android.content.Context;
import android.text.TextUtils;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BaasioPush {

    enum REG_STATE {
        CREATE_DEVICE, UPDATE_DEVICE_BY_REGID, UPDATE_DEVICE_BY_UUID
    };

    private static final String TAG = LogUtils.makeLogTag(BaasioPush.class);

    private static final int MAX_ATTEMPTS = 5;

    private static final int BACKOFF_MILLIS = 4000;

    private static final int BACKOFF_MILLIS_DEFAULT = 3000;

    private static final Random sRandom = new Random();

    public static final String TAG_REGEXP = "^[a-zA-Z0-9-_]*$";



    public BaasioPush() {

    }

    private static boolean needRegister(Context context, String signedInUsername, String regId,
            String oldRegId, List<String> newTags) {
        boolean bResult = true;

        if (GCMRegistrar.isRegisteredOnServer(context)) {
            String registeredUsername = BaasioPreferences.getRegisteredUserName(context);

            if (registeredUsername.equals(signedInUsername)) {
                List<String> curTags = BaasioPreferences.getRegisteredTags(context);

                if (compareArrays(curTags, newTags)) {
                    if (oldRegId.equals(regId)) {
                        LogUtils.LOGV(TAG, "BaasioPush.register() called but already registered.");
                        bResult = false;
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

        return bResult;
    }

    public synchronized static BaasioDevice register(Context context, String regId) throws BaasioException {
        if (!Baas.io().isGcmEnabled()) {
            throw new BaasioException(BaasioError.ERROR_GCM_DISABLED);
        }

        BaasioPreferences.setRegisteredSenderId(context, Baas.io().getGcmSenderId());

        return register(context);
    }

    public synchronized static BaasioDevice register(Context context) throws BaasioException {
        if (!Baas.io().isGcmEnabled()) {
            throw new BaasioException(BaasioError.ERROR_GCM_DISABLED);
        }

        String regId = GCMRegistrar.getRegistrationId(context);

        if (needRegisterSenderId(context, regId)) {
            GCMRegistrar.register(context, Baas.io().getGcmSenderId());
            throw new BaasioException(BaasioError.ERROR_GCM_REGISTERING_SENDER_ID);
        }

        GCMRegistrar.checkDevice(context);
        if (BuildConfig.DEBUG) {
            GCMRegistrar.checkManifest(context);
        }

        String signedInUsername = "";

        if (!ObjectUtils.isEmpty(Baas.io().getSignedInUser())) {
            signedInUsername = Baas.io().getSignedInUser().getUsername();
        }

        List<String> newTags = BaasioPreferences.getNeedRegisteredTags(context);
        String oldRegId = BaasioPreferences.getRegisteredRegId(context);

        if (!needRegister(context, signedInUsername, regId, oldRegId, newTags)) {
            throw new BaasioException(BaasioError.ERROR_GCM_ALREADY_REGISTERED);
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
        device.setTags(newTags);

        long backoff = BACKOFF_MILLIS + sRandom.nextInt(1000);
        REG_STATE eREG_STATE = REG_STATE.CREATE_DEVICE;

        if (!ObjectUtils.isEmpty(oldRegId)) {
            if (!oldRegId.equals(regId)) {
                LogUtils.LOGV(TAG, "RegId changed!!!!");

                LogUtils.LOGV(TAG, "New RegId: " + regId);
                LogUtils.LOGV(TAG, "Old RegId: " + oldRegId);
            } else {
                LogUtils.LOGV(TAG, "New and old regId are same!!!");

                LogUtils.LOGV(TAG, "RegId: " + oldRegId);
            }

            eREG_STATE = REG_STATE.UPDATE_DEVICE_BY_REGID;
        }

        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            LogUtils.LOGV(TAG, "#" + i + " Attempt..");
            REG_STATE curState = eREG_STATE;
            try {
                BaasioResponse response = null;

                switch (eREG_STATE) {
                    case CREATE_DEVICE: {
                        LogUtils.LOGV(TAG, "POST /devices");
                        LogUtils.LOGV(TAG, "Request: " + device.toString());

                        response = Baas.io().apiRequest(HttpMethod.POST, null, device, "devices");

                        LogUtils.LOGV(TAG, "Response: " + response.toString());
                        break;
                    }
                    case UPDATE_DEVICE_BY_REGID: {
                        LogUtils.LOGV(TAG, "PUT /devices/" + oldRegId);
                        LogUtils.LOGV(TAG, "Request: " + device.toString());

                        response = Baas.io().apiRequest(HttpMethod.PUT, null, device, "devices",
                                oldRegId);

                        LogUtils.LOGV(TAG, "Response: " + response.toString());
                        break;
                    }
                    case UPDATE_DEVICE_BY_UUID: {
                        String deviceUuid = BaasioPreferences.getDeviceUuidForPush(context);

                        LogUtils.LOGV(TAG, "PUT /devices/" + deviceUuid);
                        LogUtils.LOGV(TAG, "Request: " + device.toString());

                        response = Baas.io().apiRequest(HttpMethod.PUT, null, device, "devices",
                                deviceUuid);

                        LogUtils.LOGV(TAG, "Response: " + response.toString());
                        break;
                    }
                }
                if (response != null) {
                    BaasioDevice entity = response.getFirstEntity().toType(BaasioDevice.class);
                    if (!ObjectUtils.isEmpty(entity)) {
                        GCMRegistrar.setRegisteredOnServer(context, true);

                        BaasioPreferences.setRegisteredUserName(context, signedInUsername);

                        BaasioPreferences.setRegisteredTags(context, entity.getTags());
                        BaasioPreferences.setDeviceUuidForPush(context, entity.getUuid().toString());
                        BaasioPreferences.setRegisteredRegId(context, entity.getToken());
                        return entity;
                    }

                    throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
                }
            } catch (BaasioException e) {
                LogUtils.LOGV(TAG, "Failed to register on attempt " + i, e);

                String statusCode = e.getStatusCode();
                if (!ObjectUtils.isEmpty(statusCode)) {

                    if (eREG_STATE == REG_STATE.CREATE_DEVICE) {
                        if (statusCode.equals("400") && e.getErrorCode() == 913) {
                            // 이미 regId가 등록되어 있음. 하지만 태그 정보가 업데이트되어 있는지 알 수 없으니
                            // Retry

                            LogUtils.LOGV(
                                    TAG,
                                    "Already registered on the GCM server. But, need to register again because other data could be changed.");

                            oldRegId = regId;
                            i--;

                            eREG_STATE = REG_STATE.UPDATE_DEVICE_BY_REGID;
                        }
                    } else if (eREG_STATE == REG_STATE.UPDATE_DEVICE_BY_REGID) {
                        if (statusCode.equals("400")
                                && (e.getErrorCode() == 620 || e.getErrorCode() == 103)) {
                            String deviceUuid = BaasioPreferences.getDeviceUuidForPush(context);

                            if (!ObjectUtils.isEmpty(deviceUuid)) {
                                eREG_STATE = REG_STATE.UPDATE_DEVICE_BY_UUID;
                                i--;
                            } else {
                                LogUtils.LOGE(TAG,
                                        "Failed to register. This should not happen. Give up registering.(400)");
                                break;
                            }
                        } else if (statusCode.equals("404")) {
                            LogUtils.LOGE(TAG,
                                    "Failed to register. This should not happen. Give up registering.(404)");
                            break;
                        }
                    } else if (eREG_STATE == REG_STATE.UPDATE_DEVICE_BY_UUID) {
                        if (statusCode.equals("404")) {
                            eREG_STATE = REG_STATE.CREATE_DEVICE;
                            i--;
                        } else if (statusCode.equals("400")
                                && (e.getErrorCode() == 620 || e.getErrorCode() == 103)) {
                            eREG_STATE = REG_STATE.CREATE_DEVICE;
                            i--;
                        }
                    }
                }

                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).

                if (i >= MAX_ATTEMPTS) {
                    LogUtils.LOGE(TAG,
                            "Failed all attempts to register. Next time application launched, will try again.");
                    break;
                }

                if (curState == eREG_STATE) {
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
                } else {
                    try {
                        LogUtils.LOGV(TAG, "Sleeping for " + BACKOFF_MILLIS_DEFAULT
                                + " ms before retry");
                        Thread.sleep(BACKOFF_MILLIS_DEFAULT);
                    } catch (InterruptedException e1) {
                        // Activity finished before we complete - exit.
                        LogUtils.LOGD(TAG, "Thread interrupted: abort remaining retries!");
                        Thread.currentThread().interrupt();
                        return null;
                    }
                }
            }
        }

        return null;
    }

    static boolean compareArrays(String[] arr1, String[] arr2) {
        if (ObjectUtils.isEmpty(arr1) && ObjectUtils.isEmpty(arr2)) {
            return true;
        }

        if (ObjectUtils.isEmpty(arr1) || ObjectUtils.isEmpty(arr2)) {
            return false;
        }

        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);
    }

    static boolean compareArrays(List<String> arr1, List<String> arr2) {
        if (ObjectUtils.isEmpty(arr1) && ObjectUtils.isEmpty(arr2)) {
            return true;
        }

        if (ObjectUtils.isEmpty(arr1) || ObjectUtils.isEmpty(arr2)) {
            return false;
        }

        Collections.sort(arr1);
        Collections.sort(arr2);
        return arr1.equals(arr2);
    }

    private static boolean needRegisterSenderId(Context context, String regId) {
        if (TextUtils.isEmpty(regId)) {
            LogUtils.LOGD(TAG, "RegId is empty. Need register Sender ID.");
            return true;
        }

        String[] oldSenderIds = BaasioPreferences.getRegisteredSenderId(context);
        String[] newSenderIds = Baas.io().getGcmSenderId();
        if (!compareArrays(oldSenderIds, newSenderIds)) {
            LogUtils.LOGD(TAG, "SenderID is different. Need register Sender ID.");
            return true;
        }
        return false;
    }

    public static BaasioDeviceAsyncTask registerInBackground(final Context context,
                                                             final BaasioDeviceCallback callback) {
        if (!Baas.io().isGcmEnabled()) {
            if (callback != null) {
                callback.onException(new BaasioException(BaasioError.ERROR_GCM_DISABLED));
            }
            return null;
        }

        BaasioDeviceAsyncTask task = new BaasioDeviceAsyncTask(callback) {
            @Override
            public BaasioDevice doTask() throws BaasioException {
                BaasioDevice device = register(context);
                if (ObjectUtils.isEmpty(device)) {
                    GCMRegistrar.unregister(context);
                }
                return device;
            }
        };
        task.execute();
        return task;
    }

    /**
     * Unregister device. If request failed, it will not retry.
     * 
     * @param context Context
     */
    public static BaasioResponse unregister(Context context) throws BaasioException {
        if (!GCMRegistrar.isRegisteredOnServer(context)) {
            throw new BaasioException(BaasioError.ERROR_GCM_ALREADY_UNREGISTERED);
        }

        String deviceUuid = BaasioPreferences.getDeviceUuidForPush(context);
        String oldRegId = BaasioPreferences.getRegisteredRegId(context);

        BaasioPreferences.setDeviceUuidForPush(context, "");
        BaasioPreferences.setNeedRegisteredTags(context, new ArrayList<String>());
        BaasioPreferences.setRegisteredUserName(context, "");
        BaasioPreferences.setRegisteredTags(context, new ArrayList<String>());
        BaasioPreferences.setRegisteredRegId(context, "");

        GCMRegistrar.setRegisteredOnServer(context, false);

        if (!ObjectUtils.isEmpty(deviceUuid)) {
            LogUtils.LOGV(TAG, "DELETE /devices/" + deviceUuid);
            BaasioResponse response = Baas.io().apiRequest(HttpMethod.DELETE, null, null,
                    "devices", deviceUuid);

            if (response != null) {
                LogUtils.LOGV(TAG, "Response: " + response.toString());
                return response;
            } else {
                throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
            }
        } else {
            if (!ObjectUtils.isEmpty(oldRegId)) {
                LogUtils.LOGV(TAG, "DELETE /devices/" + oldRegId);
                BaasioResponse response = Baas.io().apiRequest(HttpMethod.DELETE, null, null,
                        "devices", oldRegId);

                if (response != null) {
                    LogUtils.LOGV(TAG, "Response: " + response.toString());
                    return response;
                } else {
                    throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
                }
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
