
package com.kth.baasio.help;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioAsyncTask;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.help.data.Faq;
import com.kth.baasio.help.data.FaqCategory;
import com.kth.baasio.help.data.HelpResult;
import com.kth.baasio.help.data.HelpsResult;
import com.kth.baasio.help.data.Question;
import com.kth.baasio.help.data.QuestionResult;
import com.kth.baasio.utils.ObjectUtils;

import org.springframework.http.HttpMethod;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import java.util.List;

public class BaasioHelp {

    /**
     * Get FAQ category list.
     * 
     * @return FAQ category list
     */
    public static List<FaqCategory> getHelps() throws BaasioException {

        HelpsResult root = Baas.io().customApiRequest(HttpMethod.GET, HelpsResult.class, null,
                null, "help", "helps");

        return root.getEntities();
    }

    /**
     * Get FAQ category list. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public static void getHelpsInBackground(final BaasioCallback<List<FaqCategory>> callback) {
        (new BaasioAsyncTask<List<FaqCategory>>(callback) {
            @Override
            public List<FaqCategory> doTask() throws BaasioException {
                return getHelps();
            }
        }).execute();
    }

    /**
     * Get FAQ item's detail information.
     * 
     * @param uuid FAQ item's uuid
     * @return FAQ item
     */
    public static Faq getHelpDetail(String uuid) throws BaasioException {

        HelpResult help = Baas.io().customApiRequest(HttpMethod.GET, HelpResult.class, null, null,
                "help", "helps", uuid);

        return help.getEntities().get(0);
    }

    /**
     * Get FAQ item's detail information. Executes asynchronously in background
     * and the callbacks are called in the UI thread.
     * 
     * @param uuid FAQ item's uuid
     * @param callback Result callback
     */
    public static void getHelpDetailInBackground(final String uuid,
            final BaasioCallback<Faq> callback) {
        (new BaasioAsyncTask<Faq>(callback) {
            @Override
            public Faq doTask() throws BaasioException {
                return getHelpDetail(uuid);
            }
        }).execute();
    }

    /**
     * Search FAQ items with keyword.
     * 
     * @param keyword Keyword to search
     * @return Searched FAQ category list
     */
    public static List<FaqCategory> searchHelps(String keyword) throws BaasioException {
        if (ObjectUtils.isEmpty(keyword)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_KEYWORD);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("?keyword=");
        builder.append(keyword);

        HelpsResult root = Baas.io().customApiRequest(HttpMethod.GET, HelpsResult.class, null,
                null, "help", "helps" + builder.toString());

        return root.getEntities();
    }

    /**
     * Search FAQ items with keyword. Executes asynchronously in background and
     * the callbacks are called in the UI thread.
     * 
     * @param keyword Keyword to search
     * @param callback Result callback
     */
    public static void searchHelpsInBackground(final String keyword,
            final BaasioCallback<List<FaqCategory>> callback) {
        (new BaasioAsyncTask<List<FaqCategory>>(callback) {
            @Override
            public List<FaqCategory> doTask() throws BaasioException {
                return searchHelps(keyword);
            }
        }).execute();
    }

    private static String getVersion(Context context) {
        String version = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            version = pInfo.versionName;
        } catch (NameNotFoundException e1) {

        }
        return version;
    }

    /**
     * Send a question.
     * 
     * @param context Context
     * @param email Email to receive answer.
     * @param content Question
     * @return Sent question
     */
    public static Question sendQuestion(Context context, String email, String content)
            throws BaasioException {

        if (content.length() > 1000) {
            throw new IllegalArgumentException(BaasioError.ERROR_HELP_EXCEED_STRING_LENGTH);
        }

        String appInfo = getVersion(context);

        Question question = new Question();
        question.setEmail(email);
        question.setContent(content);
        question.setAppInfo(appInfo);
        question.setDeviceInfo(Build.MODEL);
        question.setPlatform("android");
        question.setOsInfo(Build.VERSION.RELEASE);

        QuestionResult root = Baas.io().customApiRequest(HttpMethod.POST, QuestionResult.class,
                null, question, "help", "questions");

        return root.getEntities().get(0);
    }

    /**
     * Send a question. Executes asynchronously in background and the callbacks
     * are called in the UI thread.
     * 
     * @param context Context
     * @param email Email to receive answer.
     * @param content Question
     * @param callback Result callback
     */
    public static void sendQuestionInBackground(final Context context, final String email,
            final String content, final BaasioCallback<Question> callback) {
        (new BaasioAsyncTask<Question>(callback) {
            @Override
            public Question doTask() throws BaasioException {
                return sendQuestion(context, email, content);
            }
        }).execute();
    }
}
