
package com.kth.baasio.test;

import android.os.AsyncTask;
import android.test.InstrumentationTestCase;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioDeviceCallback;
import com.kth.baasio.callback.BaasioSignInCallback;
import com.kth.baasio.callback.BaasioSignUpCallback;
import com.kth.baasio.entity.push.BaasioDevice;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.help.BaasioHelp;
import com.kth.baasio.help.data.Faq;
import com.kth.baasio.help.data.FaqCategory;
import com.kth.baasio.help.data.Question;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Test005HelpCenter extends InstrumentationTestCase {
    private static final String TAG = LogUtils.makeLogTag(Test005HelpCenter.class);

    private static List<FaqCategory> mHelps;

    private static AsyncTask mGCMRegisterTask;

    public Test005HelpCenter() {
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

    public void test300GetHelps() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        BaasioHelp.getHelpsInBackground(new BaasioCallback<List<FaqCategory>>() {

            @Override
            public void onResponse(List<FaqCategory> response) {
                LogUtils.LOGV(TAG, response.toString());

                mHelps = response;

                FaqCategory faqCategory = mHelps.get(0);
                if (!faqCategory.getName().equals(
                        UnitTestConfig.HELPCENTER_RECOMMENDED_CATEGORY_TITLE)) {
                    fail("Recommended help category miss matched: name is " + faqCategory.getName());
                }

                if (faqCategory.getFaqs().size() > 0) {
                    if (!UnitTestConfig.HELPCENTER_RECOMMENDED_HELP) {
                        fail("Recommended help should not response");
                    }
                } else {
                    if (UnitTestConfig.HELPCENTER_RECOMMENDED_HELP) {
                        fail("Recommended help should response");
                    }
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

    public void test301GetHelpDetail() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        Faq faq = mHelps.get(1).getFaqs().get(0);
        BaasioHelp.getHelpDetailInBackground(faq.getUuid(), new BaasioCallback<Faq>() {

            @Override
            public void onResponse(Faq response) {
                LogUtils.LOGV(TAG, response.toString());

                if (!response.getTitle().equals(UnitTestConfig.HELPCENTER_NORMAL_TITLE)) {
                    fail("Title miss matched");
                }

                if (!response.getContent().equals(UnitTestConfig.HELPCENTER_NORMAL_CONTENT)) {
                    fail("Content miss matched");
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

    public void test302SearchHelp() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioHelp.searchHelpsInBackground(UnitTestConfig.HELPCENTER_SEARCH_KEYWORD,
                new BaasioCallback<List<FaqCategory>>() {

                    @Override
                    public void onResponse(List<FaqCategory> response) {
                        LogUtils.LOGV(TAG, response.toString());

                        for (FaqCategory faqCategory : response) {
                            for (Faq faq : faqCategory.getFaqs()) {
                                if (!faq.getTitle().contains(
                                        UnitTestConfig.HELPCENTER_SEARCH_KEYWORD)) {
                                    fail("Keyword is not contained");
                                }
                            }
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

    public void test303SendQuestion() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        BaasioUser user = Baas.io().getSignedInUser();

        BaasioHelp.sendQuestionInBackground(getInstrumentation().getContext(), user.getEmail(),
                UnitTestConfig.HELPCENTER_QUESTION_BODY, new BaasioCallback<Question>() {

                    @Override
                    public void onResponse(Question response) {
                        if (!response.getContent().equals(UnitTestConfig.HELPCENTER_QUESTION_BODY)) {
                            fail("Question miss matched");
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
