
package com.kth.baasio.test;

import android.os.AsyncTask;
import android.test.InstrumentationTestCase;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioDeviceCallback;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.callback.BaasioSignInCallback;
import com.kth.baasio.callback.BaasioSignUpCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.push.BaasioDevice;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Test001Entity extends InstrumentationTestCase {
    private static final String TAG = LogUtils.makeLogTag(Test001Entity.class);

    private static BaasioEntity mEntity1;

    private static BaasioEntity mEntity2;

    private static AsyncTask mGCMRegisterTask;

    public Test001Entity() {
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

        List<String> tags = new ArrayList<String>();
        tags.add(UnitTestConfig.PUSH_SHOULD_RECEIVE_TAG);

        mGCMRegisterTask = Baas.io().setGcmEnabled(getInstrumentation().getContext(),
                tags, new BaasioDeviceCallback() {

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

    public void test300Entity1SaveByName() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final String name = String.valueOf(System.currentTimeMillis());

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity.setName(name);
        entity.setProperty(UnitTestConfig.ENTITY_PROPERTY_NAME,
                UnitTestConfig.ENTITY1_PROPERTY_VALUE);

        entity.saveInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!name.equals(response.getName())) {
                    fail("name miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.ENTITY1_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }

                mEntity1 = response;

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

    public void test301Entity1GetByName() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity.setName(mEntity1.getName());

        entity.getInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity1.getName().equals(response.getName())) {
                    fail("name miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.ENTITY1_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test302Entity1UpdateByName() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity.setName(mEntity1.getName());
        entity.setProperty(UnitTestConfig.ENTITY_PROPERTY_NAME,
                UnitTestConfig.ENTITY1_PROPERTY_CHANGED_VALUE);

        entity.updateInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity1.getName().equals(response.getName())) {
                    fail("name miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.ENTITY1_PROPERTY_CHANGED_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test303Entity1GetByName() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity.setName(mEntity1.getName());

        entity.getInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity1.getName().equals(response.getName())) {
                    fail("name miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.ENTITY1_PROPERTY_CHANGED_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test304Entity1DeleteByName() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity.setName(mEntity1.getName());

        entity.deleteInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity1.getName().equals(response.getName())) {
                    fail("name miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.ENTITY1_PROPERTY_CHANGED_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test305Entity1CheckDeletedByName000() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity.setName(mEntity1.getName());

        entity.getInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
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

    public void test306Entity2SaveByUuid() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final String name = String.valueOf(System.currentTimeMillis());

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity.setName(name);
        entity.setProperty(UnitTestConfig.ENTITY_PROPERTY_NAME,
                UnitTestConfig.ENTITY2_PROPERTY_VALUE);

        entity.saveInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }

                mEntity2 = response;

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

    public void test307Entity2GetByUuid() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity.setUuid(mEntity2.getUuid());

        entity.getInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity2.getUuid().equals(response.getUuid())) {
                    fail("uuid miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();
                if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test308Entity2UpdateByUuid() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity.setUuid(mEntity2.getUuid());
        entity.setProperty(UnitTestConfig.ENTITY_PROPERTY_NAME,
                UnitTestConfig.ENTITY2_PROPERTY_CHANGED_VALUE);

        entity.updateInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity2.getUuid().equals(response.getUuid())) {
                    fail("name miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.ENTITY2_PROPERTY_CHANGED_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test309Entity2DeleteByUuid() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity.setUuid(mEntity2.getUuid());

        entity.deleteInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity2.getUuid().equals(response.getUuid())) {
                    fail("uuid miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();
                if (!UnitTestConfig.ENTITY2_PROPERTY_CHANGED_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test310Entity2CheckDeletedByUuid() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity.setUuid(mEntity2.getUuid());

        entity.getInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
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

    public void test311Entity1Save() throws InterruptedException {
        test300Entity1SaveByName();
    }

    public void test312Entity2Save() throws InterruptedException {
        test306Entity2SaveByUuid();
    }

    public void test313ConnectEntities() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity1 = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity1.setName(mEntity1.getName());

        BaasioEntity entity2 = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity2.setUuid(mEntity2.getUuid());

        entity1.connectInBackground(UnitTestConfig.RELATIONSHIP_NAME, entity2,
                new BaasioCallback<BaasioBaseEntity>() {

                    @Override
                    public void onResponse(BaasioBaseEntity response) {
                        if (!response.getType().equals(UnitTestConfig.ENTITY2_TYPE)) {
                            fail("Type miss match");
                        }

                        if (!response.getUuid().equals(mEntity2.getUuid())) {
                            fail("Uuid miss match");
                        }

                        String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                                .getTextValue();
                        if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                            fail("Property miss match");
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

    public void test314CheckConnected() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity1 = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity1.setName(mEntity1.getName());

        BaasioQuery query = new BaasioQuery();
        query.setRawString(UnitTestConfig.ENTITY1_TYPE + "/" + entity1.getUniqueKey() + "/"
                + UnitTestConfig.RELATIONSHIP_NAME + "/" + UnitTestConfig.ENTITY2_TYPE + "/"
                + mEntity2.getUniqueKey());

        query.queryInBackground(new BaasioQueryCallback() {

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list,
                    BaasioQuery query, long timestamp) {
                BaasioEntity entity = entities.get(0).toType(BaasioEntity.class);
                if (!entity.getType().equals(UnitTestConfig.ENTITY2_TYPE)) {
                    fail("Type miss match");
                }

                if (!entity.getUuid().equals(mEntity2.getUuid())) {
                    fail("Uuid miss match");
                }

                String test = entity.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();
                if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test315DisconnectEntities() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity1 = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity1.setName(mEntity1.getName());

        BaasioEntity entity2 = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity2.setUuid(mEntity2.getUuid());

        entity1.disconnectInBackground(UnitTestConfig.RELATIONSHIP_NAME, entity2,
                new BaasioCallback<BaasioBaseEntity>() {

                    @Override
                    public void onResponse(BaasioBaseEntity response) {
                        if (!response.getType().equals(UnitTestConfig.ENTITY2_TYPE)) {
                            fail("Type miss match");
                        }

                        if (!response.getUuid().equals(mEntity2.getUuid())) {
                            fail("Uuid miss match");
                        }

                        String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                                .getTextValue();
                        if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                            fail("Property miss match");
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

    public void test316CheckDisconnected() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity1 = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity1.setName(mEntity1.getName());

        BaasioQuery query = new BaasioQuery();
        query.setRawString(UnitTestConfig.ENTITY1_TYPE + "/" + entity1.getUniqueKey() + "/"
                + UnitTestConfig.RELATIONSHIP_NAME + "/" + UnitTestConfig.ENTITY2_TYPE + "/"
                + mEntity2.getUniqueKey());

        query.queryInBackground(new BaasioQueryCallback() {

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list,
                    BaasioQuery query, long timestamp) {
                if (entities != null && entities.size() > 0) {
                    fail("Not disconnected");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                if (!e.getStatusCode().equals("404") || e.getErrorCode() != 101) {
                    fail(e.toString());
                }

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test317ConnectEntitiesWithClassType() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity1 = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity1.setName(mEntity1.getName());

        BaasioEntity entity2 = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity2.setUuid(mEntity2.getUuid());

        entity1.connectInBackground(UnitTestConfig.RELATIONSHIP_NAME, entity2, BaasioEntity.class,
                new BaasioCallback<BaasioEntity>() {

                    @Override
                    public void onResponse(BaasioEntity response) {
                        if (!response.getType().equals(UnitTestConfig.ENTITY2_TYPE)) {
                            fail("Type miss match");
                        }

                        if (!response.getUuid().equals(mEntity2.getUuid())) {
                            fail("Uuid miss match");
                        }

                        String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                                .getTextValue();
                        if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                            fail("Property miss match");
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

    public void test318CheckConnected() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity1 = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity1.setName(mEntity1.getName());

        BaasioQuery query = new BaasioQuery();
        query.setRawString(UnitTestConfig.ENTITY1_TYPE + "/" + entity1.getUniqueKey() + "/"
                + UnitTestConfig.RELATIONSHIP_NAME + "/" + UnitTestConfig.ENTITY2_TYPE + "/"
                + mEntity2.getUniqueKey());

        query.queryInBackground(new BaasioQueryCallback() {

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list,
                    BaasioQuery query, long timestamp) {
                BaasioEntity entity = entities.get(0).toType(BaasioEntity.class);
                if (!entity.getType().equals(UnitTestConfig.ENTITY2_TYPE)) {
                    fail("Type miss match");
                }

                if (!entity.getUuid().equals(mEntity2.getUuid())) {
                    fail("Uuid miss match");
                }

                String test = entity.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();
                if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test319DisconnectEntitiesByClassType() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity1 = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity1.setName(mEntity1.getName());

        BaasioEntity entity2 = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity2.setUuid(mEntity2.getUuid());

        entity1.disconnectInBackground(UnitTestConfig.RELATIONSHIP_NAME, entity2,
                BaasioEntity.class, new BaasioCallback<BaasioEntity>() {

                    @Override
                    public void onResponse(BaasioEntity response) {
                        if (!response.getType().equals(UnitTestConfig.ENTITY2_TYPE)) {
                            fail("Type miss match");
                        }

                        if (!response.getUuid().equals(mEntity2.getUuid())) {
                            fail("Uuid miss match");
                        }

                        String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                                .getTextValue();
                        if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                            fail("Property miss match");
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

    public void test320CheckDisconnected() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity1 = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity1.setName(mEntity1.getName());

        BaasioQuery query = new BaasioQuery();
        query.setRawString(UnitTestConfig.ENTITY1_TYPE + "/" + entity1.getUniqueKey() + "/"
                + UnitTestConfig.RELATIONSHIP_NAME + "/" + UnitTestConfig.ENTITY2_TYPE + "/"
                + mEntity2.getUniqueKey());

        query.queryInBackground(new BaasioQueryCallback() {

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list,
                    BaasioQuery query, long timestamp) {
                if (entities != null && entities.size() > 0) {
                    fail("Not disconnected");
                }

                signal.countDown();
            }

            @Override
            public void onException(BaasioException e) {
                LogUtils.LOGE(TAG, e.toString());
                if (!e.getStatusCode().equals("404") || e.getErrorCode() != 101) {
                    fail(e.toString());
                }

                signal.countDown();
            }
        });

        signal.await();
    }

    public void test321Entity1Delete() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY1_TYPE);
        entity.setName(mEntity1.getName());

        entity.deleteInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity1.getName().equals(response.getName())) {
                    fail("name miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.ENTITY1_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test322Entity2Delete() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioEntity entity = new BaasioEntity(UnitTestConfig.ENTITY2_TYPE);
        entity.setUuid(mEntity2.getUuid());

        entity.deleteInBackground(new BaasioCallback<BaasioEntity>() {

            @Override
            public void onResponse(BaasioEntity response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!mEntity2.getUuid().equals(response.getUuid())) {
                    fail("uuid miss match");
                }

                String test = response.getProperty(UnitTestConfig.ENTITY_PROPERTY_NAME)
                        .getTextValue();
                if (!UnitTestConfig.ENTITY2_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
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

    public void test323SaveEntities() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        List<BaasioEntity> entities = new ArrayList<BaasioEntity>();

        for (int i = 0; i < 10; i++) {
            BaasioEntity entity = new BaasioEntity(UnitTestConfig.BULK_ENTITY_TYPE);
            entity.setName(UnitTestConfig.BULK_ENTITY_NAME_PREFIX + i);
            entity.setProperty(UnitTestConfig.ENTITY_PROPERTY_NAME + i, i);

            entities.add(entity);
        }

        BaasioEntity.saveInBackground(UnitTestConfig.BULK_ENTITY_TYPE, entities,
                new BaasioCallback<List<BaasioEntity>>() {

                    @Override
                    public void onResponse(List<BaasioEntity> response) {
                        LogUtils.LOGV(TAG, response.toString());

                        if (response.size() != 10) {
                            fail("The number of entity is not 10");
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

    public void test324DeleteEntities() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            BaasioEntity entity = new BaasioEntity(UnitTestConfig.BULK_ENTITY_TYPE);
            entity.setName(UnitTestConfig.BULK_ENTITY_NAME_PREFIX + i);

            entity.deleteInBackground(new BaasioCallback<BaasioEntity>() {

                @Override
                public void onResponse(BaasioEntity response) {
                    LogUtils.LOGV(TAG, response.toString());

                    signal.countDown();
                }

                @Override
                public void onException(BaasioException e) {
                    LogUtils.LOGE(TAG, e.toString());
                    fail(e.toString());

                    signal.countDown();
                }
            });
        }
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
