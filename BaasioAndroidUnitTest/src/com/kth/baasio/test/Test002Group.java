
package com.kth.baasio.test;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioDeviceCallback;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.callback.BaasioSignInCallback;
import com.kth.baasio.callback.BaasioSignUpCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.push.BaasioDevice;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import android.os.AsyncTask;
import android.test.InstrumentationTestCase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Test002Group extends InstrumentationTestCase {
    private static final String TAG = LogUtils.makeLogTag(Test002Group.class);

    private static BaasioGroup mGroup;

    private static AsyncTask mGCMRegisterTask;

    public Test002Group() {
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

    public void test005Init_DeleteGroup() throws InterruptedException {
        BaasioGroup entity = new BaasioGroup();
        entity.setPath(UnitTestConfig.GROUP_PATH);

        final CountDownLatch signal = new CountDownLatch(1);
        entity.deleteInBackground(new BaasioCallback<BaasioGroup>() {

            @Override
            public void onResponse(BaasioGroup response) {
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

    public void test300SaveGroup000() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioGroup entity = new BaasioGroup();
        entity.setPath(UnitTestConfig.GROUP_PATH);
        entity.setProperty(UnitTestConfig.GROUP_PROPERTY_NAME, UnitTestConfig.GROUP_PROPERTY_VALUE);

        entity.saveInBackground(new BaasioCallback<BaasioGroup>() {

            @Override
            public void onResponse(BaasioGroup response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!UnitTestConfig.GROUP_PATH.equals(response.getPath())) {
                    fail("Path miss match");
                }

                String test = response.getProperty(UnitTestConfig.GROUP_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.GROUP_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }

                mGroup = response;

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

    public void test301GetGroupByPath000() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioGroup entity = new BaasioGroup();
        entity.setPath(UnitTestConfig.GROUP_PATH);

        entity.getInBackground(new BaasioCallback<BaasioGroup>() {

            @Override
            public void onResponse(BaasioGroup response) {
                LogUtils.LOGV(TAG, response.toString());
                signal.countDown();

                if (!UnitTestConfig.GROUP_PATH.equals(response.getPath())) {
                    fail("Path miss match");
                }

                String test = response.getProperty(UnitTestConfig.GROUP_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.GROUP_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }
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

    public void test302DeleteGroupByPath000() throws InterruptedException {
        BaasioGroup entity = new BaasioGroup();
        entity.setPath(UnitTestConfig.GROUP_PATH);

        final CountDownLatch signal = new CountDownLatch(1);
        entity.deleteInBackground(new BaasioCallback<BaasioGroup>() {

            @Override
            public void onResponse(BaasioGroup response) {
                LogUtils.LOGV(TAG, response.toString());
                signal.countDown();

                if (!UnitTestConfig.GROUP_PATH.equals(response.getPath())) {
                    fail("Path miss match");
                }

                String test = response.getProperty(UnitTestConfig.GROUP_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.GROUP_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }
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

    public void test303CheckDeletedByPath000() throws InterruptedException {
        BaasioGroup entity = new BaasioGroup();
        entity.setPath(UnitTestConfig.GROUP_PATH);

        final CountDownLatch signal = new CountDownLatch(1);
        entity.getInBackground(new BaasioCallback<BaasioGroup>() {

            @Override
            public void onResponse(BaasioGroup response) {
                LogUtils.LOGV(TAG, response.toString());
                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                if (e.getErrorCode() != 101) {
                    LogUtils.LOGE(TAG, e.toString());
                    fail(e.toString());
                }
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test304SaveGroup() throws InterruptedException {
        test300SaveGroup000();
    }

    public void test305GetGroupByUuid() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioGroup entity = new BaasioGroup();
        entity.setUuid(mGroup.getUuid());

        entity.getInBackground(new BaasioCallback<BaasioGroup>() {

            @Override
            public void onResponse(BaasioGroup response) {
                LogUtils.LOGV(TAG, response.toString());
                signal.countDown();

                if (!mGroup.getPath().equals(response.getPath())) {
                    fail("Path miss match");
                }

                String test = response.getProperty(UnitTestConfig.GROUP_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.GROUP_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }
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

    public void test306DeleteGroupByUuid() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioGroup entity = new BaasioGroup();
        entity.setUuid(mGroup.getUuid());

        entity.deleteInBackground(new BaasioCallback<BaasioGroup>() {

            @Override
            public void onResponse(BaasioGroup response) {
                LogUtils.LOGV(TAG, response.toString());
                signal.countDown();

                if (!mGroup.getPath().equals(response.getPath())) {
                    fail("Path miss match");
                }

                String test = response.getProperty(UnitTestConfig.GROUP_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.GROUP_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }
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

    public void test307SaveGroup() throws InterruptedException {
        test300SaveGroup000();
    }

    public void test308AddUser() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioGroup group = new BaasioGroup();
        group.setPath(UnitTestConfig.GROUP_PATH);

        final BaasioUser user = Baas.io().getSignedInUser();
        group.addInBackground(user, new BaasioCallback<BaasioUser>() {

            @Override
            public void onResponse(BaasioUser response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!user.getUsername().equals(response.getUsername())) {
                    fail("Username miss match");
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

    public void test309CheckAdded() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final BaasioUser user = Baas.io().getSignedInUser();

        BaasioQuery query = new BaasioQuery();
        query.setGroup(mGroup);
        query.setWheres(BaasioUser.PROPERTY_USERNAME + "='" + user.getUsername() + "'");

        query.queryInBackground(new BaasioQueryCallback() {

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list,
                    BaasioQuery query, long timestamp) {
                BaasioUser entity = entities.get(0).toType(BaasioUser.class);
                if (!entity.getType().equals(BaasioUser.ENTITY_TYPE)) {
                    fail("Type miss match");
                }

                if (!entity.getUuid().equals(user.getUuid())) {
                    fail("Uuid miss match");
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

    public void test310RemoveUser() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioGroup group = new BaasioGroup();
        group.setPath(UnitTestConfig.GROUP_PATH);

        final BaasioUser user = Baas.io().getSignedInUser();
        group.removeInBackground(user, new BaasioCallback<BaasioUser>() {

            @Override
            public void onResponse(BaasioUser response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!user.getUsername().equals(response.getUsername())) {
                    fail("Username miss match");
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

    public void test311CheckRemoved() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final BaasioUser user = Baas.io().getSignedInUser();

        BaasioQuery query = new BaasioQuery();
        query.setGroup(mGroup);
        query.setWheres(BaasioUser.PROPERTY_USERNAME + "='" + user.getUsername() + "'");

        query.queryInBackground(new BaasioQueryCallback() {

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list,
                    BaasioQuery query, long timestamp) {
                if (entities != null && entities.size() > 0) {
                    fail("Not removed");
                }

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

    public void test312Uninit_DeleteGroup() throws InterruptedException {
        BaasioGroup entity = new BaasioGroup();
        entity.setPath(UnitTestConfig.GROUP_PATH);

        final CountDownLatch signal = new CountDownLatch(1);
        entity.deleteInBackground(new BaasioCallback<BaasioGroup>() {

            @Override
            public void onResponse(BaasioGroup response) {
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
