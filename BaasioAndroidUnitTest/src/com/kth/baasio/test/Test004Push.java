
package com.kth.baasio.test;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioDeviceCallback;
import com.kth.baasio.callback.BaasioSignInCallback;
import com.kth.baasio.callback.BaasioSignUpCallback;
import com.kth.baasio.entity.push.BaasioDevice;
import com.kth.baasio.entity.push.BaasioMessage;
import com.kth.baasio.entity.push.BaasioPush;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.preferences.BaasioPreferences;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import android.os.AsyncTask;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;

public class Test004Push extends InstrumentationTestCase {
    private static final String TAG = LogUtils.makeLogTag(Test004Push.class);

    private static AsyncTask mGCMRegisterTask;

    public Test004Push() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test000Init() throws InterruptedException {
        Baas.io().init(getInstrumentation().getContext(), BaasioConfig.BAASIO_URL,
                BaasioConfig.BAASIO_ID, BaasioConfig.APPLICATION_ID);

        final CountDownLatch signal = new CountDownLatch(1);
        mGCMRegisterTask = Baas.io().setGcmEnabled(getInstrumentation().getContext(),
                UnitTestConfig.PUSH_SHOULD_RECEIVE_TAG, new BaasioDeviceCallback() {

                    @Override
                    public void onException(BaasioException e) {
                        if (!BaasioError.ERROR_GCM_ALREADY_REGISTERED.equals(e.getMessage())) {
                            LogUtils.LOGE(TAG, e.toString());
                            fail(e.toString());
                        } else {
                            LogUtils.LOGV(TAG, e.toString());
                        }

                        signal.countDown();
                    }

                    @Override
                    public void onResponse(BaasioDevice response) {
                        LogUtils.LOGV(TAG, response.toString());
                        signal.countDown();
                    }
                }, BaasioConfig.GCM_SENDER_ID);

        if (mGCMRegisterTask != null) {
            signal.await();
        }
    }

    public void test001Init_User1SignIn() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioUser.signOut(getInstrumentation().getContext());

        BaasioUser.signInInBackground(getInstrumentation().getContext(),
                UnitTestConfig.USER1_USERNAME, UnitTestConfig.COMMON_PASSWORD,
                new BaasioSignInCallback() {

                    @Override
                    public void onException(BaasioException e) {
                        LogUtils.LOGV(TAG, e.toString());

                        signal.countDown();
                    }

                    @Override
                    public void onResponse(BaasioUser response) {
                        LogUtils.LOGV(TAG, response.toString());

                        signal.countDown();
                    }
                });

        signal.await();
    }

    public void test002Init_User1Unsubscribe() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioUser user = Baas.io().getSignedInUser();
        if (!ObjectUtils.isEmpty(user)) {
            user.unsubscribeInBackground(getInstrumentation().getContext(),
                    new BaasioCallback<BaasioUser>() {

                        @Override
                        public void onResponse(BaasioUser response) {
                            LogUtils.LOGV(TAG, response.toString());

                            signal.countDown();
                        }

                        @Override
                        public void onException(BaasioException e) {
                            LogUtils.LOGV(TAG, e.toString());

                            signal.countDown();
                        }
                    });

            signal.await();
        }
    }

    public void test003Init_User1SignUp() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioUser.signUpInBackground(UnitTestConfig.USER1_USERNAME, UnitTestConfig.USER1_USERNAME,
                UnitTestConfig.USER1_EMAIL, UnitTestConfig.COMMON_PASSWORD,
                new BaasioSignUpCallback() {

                    @Override
                    public void onException(BaasioException e) {
                        LogUtils.LOGE(TAG, e.toString());
                        fail(e.toString());

                        signal.countDown();
                    }

                    @Override
                    public void onResponse(BaasioUser response) {
                        LogUtils.LOGV(TAG, response.toString());
                        signal.countDown();
                    }
                });

        signal.await();
    }

    public void test004Init_User1SignIn() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioUser.signInInBackground(getInstrumentation().getContext(),
                UnitTestConfig.USER1_USERNAME, UnitTestConfig.COMMON_PASSWORD,
                new BaasioSignInCallback() {

                    @Override
                    public void onException(BaasioException e) {
                        LogUtils.LOGE(TAG, e.toString());
                        fail(e.toString());

                        signal.countDown();
                    }

                    @Override
                    public void onResponse(BaasioUser response) {
                        LogUtils.LOGV(TAG, response.toString());
                        signal.countDown();
                    }
                });

        signal.await();
    }

    public void test300SendTargetAllMsg() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioMessage msg = new BaasioMessage();
        msg.setMessage(UnitTestConfig.PUSH_TARGET_ALL_MSG, null, null);

        BaasioPush.sendPushInBackground(msg, new BaasioCallback<BaasioMessage>() {

            @Override
            public void onResponse(BaasioMessage response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!response.getPlatform().contains(BaasioMessage.PLATFORM_TYPE_GCM)
                        || !response.getPlatform().contains(BaasioMessage.PLATFORM_TYPE_IOS)) {
                    fail("Not target all msg");
                }

                if (!UnitTestConfig.PUSH_TARGET_ALL_MSG.equals(response.getPayload().getAlert())) {
                    fail("Alert miss matched");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                fail(e.toString());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test301SendTargetUserMsg() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioUser user = Baas.io().getSignedInUser();

        final BaasioMessage msg = new BaasioMessage();
        msg.setTarget(BaasioMessage.TARGET_TYPE_USER);
        msg.setTo(user.getUuid().toString());
        msg.setMessage(UnitTestConfig.PUSH_TARGET_USER_MSG, null, null);

        BaasioPush.sendPushInBackground(msg, new BaasioCallback<BaasioMessage>() {

            @Override
            public void onResponse(BaasioMessage response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!BaasioMessage.TARGET_TYPE_USER.equalsIgnoreCase(response.getTarget())) {
                    fail("Not target user");
                }

                if (!UnitTestConfig.PUSH_TARGET_USER_MSG.equals(response.getPayload().getAlert())) {
                    fail("Alert miss matched");
                }

                if (!response.getTo().contains(msg.getTo())) {
                    fail("'To' value miss matched");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                fail(e.toString());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test302SendTargetDeviceMsg() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        String deviceUuid = BaasioPreferences.getDeviceUuidForPush(getInstrumentation()
                .getContext());

        final BaasioMessage msg = new BaasioMessage();
        msg.setTarget(BaasioMessage.TARGET_TYPE_DEVICE);
        msg.setTo(deviceUuid);
        msg.setMessage(UnitTestConfig.PUSH_TARGET_DEVICE_MSG, null, null);

        BaasioPush.sendPushInBackground(msg, new BaasioCallback<BaasioMessage>() {

            @Override
            public void onResponse(BaasioMessage response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!BaasioMessage.TARGET_TYPE_DEVICE.equalsIgnoreCase(response.getTarget())) {
                    fail("Not target device");
                }

                if (!UnitTestConfig.PUSH_TARGET_DEVICE_MSG.equals(response.getPayload().getAlert())) {
                    fail("Alert miss matched");
                }

                if (!response.getTo().contains(msg.getTo())) {
                    fail("'To' value miss matched");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                fail(e.toString());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test303SendTargetAndroidPlatformMsg() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final BaasioMessage msg = new BaasioMessage();
        msg.setTarget(BaasioMessage.TARGET_TYPE_ALL);
        msg.setPlatform(BaasioMessage.PLATFORM_FLAG_TYPE_GCM);
        msg.setMessage(UnitTestConfig.PUSH_TARGET_ANDROID_MSG, null, null);

        BaasioPush.sendPushInBackground(msg, new BaasioCallback<BaasioMessage>() {

            @Override
            public void onResponse(BaasioMessage response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!BaasioMessage.TARGET_TYPE_ALL.equalsIgnoreCase(response.getTarget())) {
                    fail("Not target all");
                }

                if (!BaasioMessage.PLATFORM_TYPE_GCM.equalsIgnoreCase(response.getPlatform())) {
                    fail("Not platform gcm");
                }

                if (!UnitTestConfig.PUSH_TARGET_ANDROID_MSG
                        .equals(response.getPayload().getAlert())) {
                    fail("Alert miss matched");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                fail(e.toString());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test304SendTargetIosPlatformMsg() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final BaasioMessage msg = new BaasioMessage();
        msg.setTarget(BaasioMessage.TARGET_TYPE_ALL);
        msg.setPlatform(BaasioMessage.PLATFORM_FLAG_TYPE_IOS);
        msg.setMessage(UnitTestConfig.PUSH_TARGET_IOS_MSG, null, null);

        BaasioPush.sendPushInBackground(msg, new BaasioCallback<BaasioMessage>() {

            @Override
            public void onResponse(BaasioMessage response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!BaasioMessage.TARGET_TYPE_ALL.equalsIgnoreCase(response.getTarget())) {
                    fail("Not target all");
                }

                if (!BaasioMessage.PLATFORM_TYPE_IOS.equalsIgnoreCase(response.getPlatform())) {
                    fail("Not platform ios");
                }

                if (!UnitTestConfig.PUSH_TARGET_IOS_MSG.equals(response.getPayload().getAlert())) {
                    fail("Alert miss matched");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                fail(e.toString());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test305SendReservedMsg() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        long reserved = System.currentTimeMillis() + 6 * 60 * 1000;

        final BaasioMessage msg = new BaasioMessage();
        msg.setTarget(BaasioMessage.TARGET_TYPE_ALL);
        msg.setReserve(reserved);
        msg.setMessage(UnitTestConfig.PUSH_TARGET_ALL_MSG
                + UnitTestConfig.PUSH_RESERVED_MSG_POSTFIX, null, null);

        BaasioPush.sendPushInBackground(msg, new BaasioCallback<BaasioMessage>() {

            @Override
            public void onResponse(BaasioMessage response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!BaasioMessage.TARGET_TYPE_ALL.equalsIgnoreCase(response.getTarget())) {
                    fail("Not target all");
                }

                if (!(UnitTestConfig.PUSH_TARGET_ALL_MSG + UnitTestConfig.PUSH_RESERVED_MSG_POSTFIX)
                        .equals(response.getPayload().getAlert())) {
                    fail("Alert miss matched");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                fail(e.toString());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test306SendTargetTagMsg() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final BaasioMessage msg = new BaasioMessage();
        msg.setTarget(BaasioMessage.TARGET_TYPE_TAG);
        msg.setTo(UnitTestConfig.PUSH_SHOULD_RECEIVE_TAG);
        msg.setMessage(UnitTestConfig.PUSH_TARGET_TAG_SHOULD_RECEIVE, null, null);

        BaasioPush.sendPushInBackground(msg, new BaasioCallback<BaasioMessage>() {

            @Override
            public void onResponse(BaasioMessage response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!BaasioMessage.TARGET_TYPE_TAG.equalsIgnoreCase(response.getTarget())) {
                    fail("Not target all");
                }

                if (!UnitTestConfig.PUSH_TARGET_TAG_SHOULD_RECEIVE.equals(response.getPayload()
                        .getAlert())) {
                    fail("Alert miss matched");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                fail(e.toString());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test307SendTargetTagMsg() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final BaasioMessage msg = new BaasioMessage();
        msg.setTarget(BaasioMessage.TARGET_TYPE_TAG);
        msg.setTo(UnitTestConfig.PUSH_SHOULD_NOT_RECEIVE_TAG);
        msg.setMessage(UnitTestConfig.PUSH_TARGET_TAG_SHOULD_NOT_RECEIVE, null, null);

        BaasioPush.sendPushInBackground(msg, new BaasioCallback<BaasioMessage>() {

            @Override
            public void onResponse(BaasioMessage response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!BaasioMessage.TARGET_TYPE_TAG.equalsIgnoreCase(response.getTarget())) {
                    fail("Not target all");
                }

                if (!UnitTestConfig.PUSH_TARGET_TAG_SHOULD_NOT_RECEIVE.equals(response.getPayload()
                        .getAlert())) {
                    fail("Alert miss matched");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                fail(e.toString());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test996Uninit_UserSignOut() {
        BaasioUser.signOut(getInstrumentation().getContext());
    }

    public void test997Uninit_User1SignIn() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioUser.signInInBackground(getInstrumentation().getContext(),
                UnitTestConfig.USER1_USERNAME, UnitTestConfig.COMMON_PASSWORD,
                new BaasioSignInCallback() {

                    @Override
                    public void onException(BaasioException e) {
                        LogUtils.LOGE(TAG, e.toString());
                        fail(e.toString());

                        signal.countDown();
                    }

                    @Override
                    public void onResponse(BaasioUser response) {
                        LogUtils.LOGV(TAG, response.toString());

                        signal.countDown();
                    }
                });

        signal.await();
    }

    public void test998Uninit_User1Unsubscribe() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioUser user = Baas.io().getSignedInUser();
        if (!ObjectUtils.isEmpty(user)) {
            user.unsubscribeInBackground(getInstrumentation().getContext(),
                    new BaasioCallback<BaasioUser>() {

                        @Override
                        public void onResponse(BaasioUser response) {
                            LogUtils.LOGV(TAG, response.toString());

                            signal.countDown();
                        }

                        @Override
                        public void onException(BaasioException e) {
                            LogUtils.LOGV(TAG, e.toString());

                            signal.countDown();
                        }
                    });

            signal.await();
        }
    }

    public void test999Uninit() throws InterruptedException {
        if (mGCMRegisterTask != null) {
            mGCMRegisterTask.cancel(true);
        }

        Baas.io().uninit(getInstrumentation().getContext());
    }
}
