
package com.kth.baasio.test;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioDeviceCallback;
import com.kth.baasio.callback.BaasioDownloadCallback;
import com.kth.baasio.callback.BaasioSignInCallback;
import com.kth.baasio.callback.BaasioSignUpCallback;
import com.kth.baasio.callback.BaasioUploadCallback;
import com.kth.baasio.entity.file.BaasioFile;
import com.kth.baasio.entity.push.BaasioDevice;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import android.os.AsyncTask;
import android.test.InstrumentationTestCase;

import java.io.File;
import java.util.concurrent.CountDownLatch;

public class Test003File extends InstrumentationTestCase {
    private static final String TAG = LogUtils.makeLogTag(Test003File.class);

    private static BaasioFile mFile;

    private static AsyncTask mGCMRegisterTask;

    public Test003File() {
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

    public void test300FileUpload() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setProperty(UnitTestConfig.FILE_PROPERTY_NAME, UnitTestConfig.FILE_PROPERTY_VALUE);

        file.fileUploadInBackground(UnitTestConfig.FILE_TEST_FILE_FULLPATH, null,
                new BaasioUploadCallback() {
                    private long total;

                    private long written;

                    @Override
                    public void onResponse(BaasioFile response) {
                        LogUtils.LOGV(TAG, response.toString());

                        String test = response.getProperty(UnitTestConfig.FILE_PROPERTY_NAME)
                                .getTextValue();

                        if (!UnitTestConfig.FILE_TEST_FILENAME.equals(response.getFilename())) {
                            fail("Filename miss match");
                        }

                        if (!UnitTestConfig.FILE_PROPERTY_VALUE.equals(test)) {
                            fail("Property miss match");
                        }

                        if (total != written) {
                            fail("Total size are different with witten size");
                        }

                        mFile = response;

                        signal.countDown();
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        this.total = total;
                        this.written = current;
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

    public void test301FileGet() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setUuid(mFile.getUuid());

        file.getInBackground(new BaasioCallback<BaasioFile>() {

            @Override
            public void onResponse(BaasioFile response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!UnitTestConfig.FILE_TEST_FILENAME.equals(response.getFilename())) {
                    fail("Filename miss match");
                }

                String test = response.getProperty(UnitTestConfig.FILE_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.FILE_PROPERTY_VALUE.equals(test)) {
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

    public void test302FileUpdate() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setUuid(mFile.getUuid());
        file.setProperty(UnitTestConfig.FILE_PROPERTY_NAME,
                UnitTestConfig.FILE_PROPERTY_CHANGED_VALUE);

        file.fileUpdateInBackground(UnitTestConfig.FILE_TEST_UPDATED_FILE_FULLPATH, null,
                new BaasioUploadCallback() {
                    private long total;

                    private long written;

                    @Override
                    public void onResponse(BaasioFile response) {
                        LogUtils.LOGV(TAG, response.toString());

                        if (!UnitTestConfig.FILE_TEST_UPDATED_FILENAME.equals(response
                                .getFilename())) {
                            fail("Filename miss match");
                        }

                        String test = response.getProperty(UnitTestConfig.FILE_PROPERTY_NAME)
                                .getTextValue();

                        if (!UnitTestConfig.FILE_PROPERTY_CHANGED_VALUE.equals(test)) {
                            fail("Property miss match");
                        }

                        if (total != written) {
                            fail("Total size are different with witten size");
                        }

                        mFile = response;

                        signal.countDown();
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        this.total = total;
                        this.written = current;
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

    public void test303CheckFileUpdated() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setUuid(mFile.getUuid());

        file.getInBackground(new BaasioCallback<BaasioFile>() {

            @Override
            public void onResponse(BaasioFile response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!UnitTestConfig.FILE_TEST_UPDATED_FILENAME.equals(response.getFilename())) {
                    fail("Filename miss match");
                }

                String test = response.getProperty(UnitTestConfig.FILE_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.FILE_PROPERTY_CHANGED_VALUE.equals(test)) {
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

    public void test304FileDownload() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setUuid(mFile.getUuid());
        file.setFilename(mFile.getFilename());

        file.fileDownloadInBackground(UnitTestConfig.FILE_TEST_ROOT_PATH + File.separator,
                new BaasioDownloadCallback() {
                    private long total;

                    private long written;

                    @Override
                    public void onResponse(String localFilePath) {
                        LogUtils.LOGV(TAG, "Downloaded Path: " + localFilePath);

                        if (!UnitTestConfig.FILE_TEST_UPDATED_FILE_FULLPATH.equals(localFilePath)) {
                            fail("Downloaded file path miss match");
                        }

                        File file = new File(localFilePath);
                        if (!file.exists()) {
                            fail("File not exist");
                        }

                        if (total != written) {
                            fail("Total size are different with witten size");
                        }

                        signal.countDown();
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        this.total = total;
                        this.written = current;
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

    public void test305FileDelete() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setUuid(mFile.getUuid());

        file.deleteInBackground(new BaasioCallback<BaasioFile>() {

            @Override
            public void onResponse(BaasioFile response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!UnitTestConfig.FILE_TEST_UPDATED_FILENAME.equals(response.getFilename())) {
                    fail("name miss match");
                }

                String test = response.getProperty(UnitTestConfig.FILE_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.FILE_PROPERTY_CHANGED_VALUE.equals(test)) {
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

    public void test306CheckFileDeleted() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setUuid(mFile.getUuid());

        file.getInBackground(new BaasioCallback<BaasioFile>() {

            @Override
            public void onResponse(BaasioFile response) {
                LogUtils.LOGE(TAG, response.toString());
                fail("File is not deleted");

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

    public void test307FileSave() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setProperty(UnitTestConfig.FILE_PROPERTY_NAME, UnitTestConfig.FILE_PROPERTY_VALUE);
        file.setFilename(UnitTestConfig.FILE_TEST_EMPTY_FILENAME);

        file.saveInBackground(new BaasioCallback<BaasioFile>() {

            @Override
            public void onResponse(BaasioFile response) {
                LogUtils.LOGV(TAG, response.toString());

                String test = response.getProperty(UnitTestConfig.FILE_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.FILE_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }

                if (!UnitTestConfig.FILE_TEST_EMPTY_FILENAME.equals(response.getFilename())) {
                    fail("Filename miss match");
                }

                mFile = response;

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

    public void test308FileGet() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setUuid(mFile.getUuid());

        file.getInBackground(new BaasioCallback<BaasioFile>() {

            @Override
            public void onResponse(BaasioFile response) {
                LogUtils.LOGV(TAG, response.toString());

                String test = response.getProperty(UnitTestConfig.FILE_PROPERTY_NAME)
                        .getTextValue();

                if (!UnitTestConfig.FILE_PROPERTY_VALUE.equals(test)) {
                    fail("Property miss match");
                }

                if (!UnitTestConfig.FILE_TEST_EMPTY_FILENAME.equals(response.getFilename())) {
                    fail("Filename miss match");
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

    public void test309FileUpdateToFileEntity() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioFile file = new BaasioFile();
        file.setUuid(mFile.getUuid());
        file.setProperty(UnitTestConfig.FILE_PROPERTY_NAME,
                UnitTestConfig.FILE_PROPERTY_CHANGED_VALUE);

        file.fileUpdateInBackground(UnitTestConfig.FILE_TEST_UPDATED_FILE_FULLPATH, null,
                new BaasioUploadCallback() {
                    private long total;

                    private long written;

                    @Override
                    public void onResponse(BaasioFile response) {
                        LogUtils.LOGV(TAG, response.toString());

                        if (!UnitTestConfig.FILE_TEST_UPDATED_FILENAME.equals(response
                                .getFilename())) {
                            fail("Filename miss match");
                        }

                        String test = response.getProperty(UnitTestConfig.FILE_PROPERTY_NAME)
                                .getTextValue();

                        if (!UnitTestConfig.FILE_PROPERTY_CHANGED_VALUE.equals(test)) {
                            fail("Property miss match");
                        }

                        if (total != written) {
                            fail("Total size are different with witten size");
                        }

                        mFile = response;

                        signal.countDown();
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        this.total = total;
                        this.written = current;
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

    public void test310CheckFileUpdated() throws InterruptedException {
        test303CheckFileUpdated();
    }

    public void test311FileDownload() throws InterruptedException {
        test304FileDownload();
    }

    public void test312FileDelete() throws InterruptedException {
        test305FileDelete();
    }

    public void test313CheckFileDelete() throws InterruptedException {
        test306CheckFileDeleted();
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
