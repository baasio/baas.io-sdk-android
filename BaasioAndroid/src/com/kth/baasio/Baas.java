
package com.kth.baasio;

import com.google.android.gcm.GCMRegistrar;
import com.kth.baasio.callback.BaasioDeviceAsyncTask;
import com.kth.baasio.callback.BaasioDeviceCallback;
import com.kth.baasio.entity.file.multipart.MultipartEntity;
import com.kth.baasio.entity.push.BaasioPush;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.preferences.BaasioPreferences;
import com.kth.baasio.response.BaasioResponse;
import com.kth.baasio.ssl.HttpUtils;
import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.baasio.utils.UrlUtils;
import com.kth.common.utils.LogUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Baas {
    private static final String TAG = LogUtils.makeLogTag(Baas.class);

    protected static RestTemplate restTemplate = new RestTemplate();

    private boolean isInited = false;

    private String accessToken;

    private String baasioUrl;

    private String baasioId;

    private String applicationId;

    private String[] gcmSenderId;

    private BaasioUser mLoggedInUser;

    private boolean gcmEnabled;

    public static final int MIN_BUFFER_SIZE = 1024 * 4;

    private static int mUploadBuffSize = MIN_BUFFER_SIZE;

    private static int mDownloadBuffSize = MIN_BUFFER_SIZE;

    public static final String ACTION_UNAUTHORIZED = "com.kth.baasio.ACTION_UNAUTHORIZED";

    private Context applicationContext;

    private Baas() {
        HttpURLConnection.setFollowRedirects(true);
        HttpsURLConnection.setFollowRedirects(true);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new ResourceHttpMessageConverter());

        MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
        ObjectMapper mapper = converter.getObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        messageConverters.add(converter);

        restTemplate.setMessageConverters(messageConverters);

        if (Build.VERSION.SDK_INT < 9) {
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpUtils
                    .getNewHttpClient()));
        }
    }

    // References:
    // "http://stackoverflow.com/questions/11165852/java-singleton-and-synchronization"
    private static class InstanceHolder {
        private static final Baas mSingleton = new Baas();
    }

    /**
     * Get baas.io singleton object.
     * 
     * @return baas.io singleton object
     */
    public static Baas io() {
        return InstanceHolder.mSingleton;
    }

    /**
     * Set read timeout and connect timeout
     * 
     * @param readTimeout milliseconds
     * @param connectTimeout milliseconds
     */
    public static void setTimeout(int readTimeout, int connectTimeout) {
        if (restTemplate.getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
            LogUtils.LOGD(TAG, "HttpUrlConnection is used");
            ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory())
                    .setConnectTimeout(connectTimeout);
            ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory())
                    .setReadTimeout(readTimeout);
        } else if (restTemplate.getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
            LogUtils.LOGD(TAG, "HttpClient is used");
            ((HttpComponentsClientHttpRequestFactory)restTemplate.getRequestFactory())
                    .setReadTimeout(readTimeout);
            ((HttpComponentsClientHttpRequestFactory)restTemplate.getRequestFactory())
                    .setConnectTimeout(connectTimeout);
        }
    }

    /**
     * Get baas.io URL.
     * 
     * @return baas.io singleton object
     */
    public String getBaasioUrl() {
        return baasioUrl;
    }

    /**
     * Get baas.io member id.
     * 
     * @return baas.io member id
     */
    public String getBaasioId() {
        return baasioId;
    }

    /**
     * Get baas.io application id.
     * 
     * @return baas.io application id
     */
    public String getApplicationId() {
        return applicationId;
    }

    protected void assertInited() {
        if (!isInited) {
            throw new IllegalArgumentException(BaasioError.ERROR_NEED_INIT);
        }
    }

    protected void assertValidBaasioUrl() {
        if (ObjectUtils.isEmpty(baasioUrl)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_BAASIO_URL);
        }
    }

    protected void assertValidBaasioId() {
        if (ObjectUtils.isEmpty(baasioId)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_BAASIO_ID);
        }
    }

    protected void assertValidApplicationId() {
        if (ObjectUtils.isEmpty(applicationId)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_APPLICATION_ID);
        }
    }

    /**
     * Initialize baas.io module.
     * 
     * @param context
     * @param baasioUrl baas.io API server URL
     * @param baasioId baas.io member id
     * @param applicationId baas.io application id
     */
    public void init(Context context, String baasioUrl, String baasioId, String applicationId) {
        this.applicationContext = context;

        this.baasioUrl = baasioUrl;
        this.baasioId = baasioId;
        this.applicationId = applicationId;

        assertValidBaasioUrl();
        assertValidBaasioId();
        assertValidApplicationId();

        if (!baasioUrl.startsWith("http://") && !baasioUrl.startsWith("https://")) {
            throw new IllegalArgumentException(BaasioError.ERROR_WRONG_BAASIO_URL);
        }

        String accessToken = BaasioPreferences.getAccessToken(context);
        if (!ObjectUtils.isEmpty(accessToken)) {
            setAccessToken(accessToken);
        }

        String userInfo = BaasioPreferences.getUserString(context);
        if (!ObjectUtils.isEmpty(userInfo)) {
            BaasioUser user = JsonUtils.parse(userInfo, BaasioUser.class);

            setSignedInUser(user);
        }

        isInited = true;
    }

    /**
     * Set to enable GCM.
     * 
     * @param context
     * @param tags Tags
     * @param callback GCM registration result callback
     * @param gcmSenderId GCM sender ID
     * @return GCM registration task
     */
    public BaasioDeviceAsyncTask setGcmEnabled(Context context, String tags,
            BaasioDeviceCallback callback, String... gcmSenderId) {
        if (!ObjectUtils.isEmpty(gcmSenderId) && !ObjectUtils.isEmpty(gcmSenderId[0])) {
            this.gcmSenderId = gcmSenderId;

            gcmEnabled = true;

            if (!ObjectUtils.isEmpty(tags)) {
                return BaasioPush.registerWithTagsInBackground(context, tags, callback);
            } else {
                return BaasioPush.registerInBackground(context, callback);
            }
        }

        return null;
    }

    /**
     * This method must be placed in Application.onDestroy()
     * 
     * @param context
     */
    public void uninit(Context context) {
        if (Baas.io().isGcmEnabled()) {
            try {
                GCMRegistrar.onDestroy(context);
            } catch (Exception e) {
                LogUtils.LOGV(TAG, "GCMRegistrar.onDestroy failed", e);
            }
        }
    }

    /**
     * "com.kth.baasio.ACTION_UNAUTHORIZED" action fire when access token is
     * expired or access denied.
     */
    public void fireUnauthorized() {
        Intent intent = new Intent();
        intent.setAction(ACTION_UNAUTHORIZED);
        applicationContext.sendBroadcast(intent);
    }

    /**
     * Send baas.io API request
     * 
     * @param method HTTP method
     * @param params URL parameters
     * @param data HTTP entity
     * @param segments URL segments(paths)
     * @return Response for API request
     */
    public BaasioResponse apiRequest(HttpMethod method, Map<String, Object> params, Object data,
            String... segments) throws BaasioException {
        assertInited();

        assertValidBaasioUrl();
        assertValidBaasioId();
        assertValidApplicationId();

        ArrayList<String> list = new ArrayList<String>();
        list.add(baasioId);
        list.add(applicationId);
        list.addAll(Arrays.asList(segments));

        String[] newSegments = list.toArray(new String[list.size()]);

        BaasioResponse response = null;
        try {
            response = httpRequest(baasioUrl, method, BaasioResponse.class, params, data,
                    newSegments);
        } catch (ResourceAccessException e) {
            String message = e.getMessage();
            if (message != null && message.contains("No authentication challenges found")) {
                LogUtils.LOGV(TAG, "Need Login");

                fireUnauthorized();
            }
            throw new BaasioException(e);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                LogUtils.LOGV(TAG, "Need Login");

                fireUnauthorized();
            }
            throw new BaasioException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            throw new BaasioException(e.getStatusCode(), e.getResponseBodyAsString());
        }
        return response;
    }

    /**
     * Send baas.io custom API request
     * 
     * @param method HTTP method
     * @param cls Result object class
     * @param params URL parameters
     * @param data HTTP entity
     * @param segments URL segments(paths)
     * @return Response for API request with class type
     */
    public <T> T customApiRequest(HttpMethod method, Class<T> cls, Map<String, Object> params,
            Object data, String... segments) throws BaasioException {
        assertInited();

        assertValidBaasioUrl();
        assertValidBaasioId();
        assertValidApplicationId();

        ArrayList<String> list = new ArrayList<String>();
        list.add(baasioId);
        list.add(applicationId);
        list.addAll(Arrays.asList(segments));

        String[] newSegments = list.toArray(new String[list.size()]);

        T response = null;
        try {
            response = httpRequest(baasioUrl, method, cls, params, data, newSegments);
        } catch (ResourceAccessException e) {
            String message = e.getMessage();
            if (message != null && message.contains("No authentication challenges found")) {
                LogUtils.LOGV(TAG, "Need Login");

                fireUnauthorized();
            }
            throw new BaasioException(e);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                LogUtils.LOGV(TAG, "Need Login");

                fireUnauthorized();
            }
            throw new BaasioException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            throw new BaasioException(e.getStatusCode(), e.getResponseBodyAsString());
        }
        return response;
    }

    /**
     * Send baas.io custom API request
     * 
     * @param method HTTP method
     * @param cls Result object class
     * @param params URL parameters
     * @param data HTTP entity
     * @param segments URL segments(paths)
     * @return Response for API request with class type
     */
    public <T> T pastaApiRequest(HttpMethod method, Class<T> cls, Map<String, Object> params,
            Object data, String... segments) throws BaasioException {
        assertInited();

        assertValidBaasioUrl();
        assertValidBaasioId();
        assertValidApplicationId();

        ArrayList<String> list = new ArrayList<String>();
        list.add(baasioId);
        list.add(applicationId);
        list.add("pasta");
        list.addAll(Arrays.asList(segments));

        String[] newSegments = list.toArray(new String[list.size()]);

        URL url = UrlUtils.url(baasioUrl);
        String host = url.getHost();

        String pastaUrl = url.getProtocol() + "://pasta-" + host;

        T response = null;
        try {
            response = httpRequest(pastaUrl, method, cls, params, data, newSegments);
        } catch (ResourceAccessException e) {
            String message = e.getMessage();
            if (message != null && message.contains("No authentication challenges found")) {
                LogUtils.LOGV(TAG, "Need Login");

                fireUnauthorized();
            }
            throw new BaasioException(e);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                LogUtils.LOGV(TAG, "Need Login");

                fireUnauthorized();
            }
            throw new BaasioException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            throw new BaasioException(e.getStatusCode(), e.getResponseBodyAsString());
        }
        return response;
    }

    /**
     * Send upload file API request
     * 
     * @param method HTTP method
     * @param entity Multipart-form entity
     * @param segments Paths
     * @return HTTP response for upload API request
     */
    public HttpResponse uploadFileRequest(HttpMethod method, MultipartEntity entity,
            String... segments) throws BaasioException {
        assertValidBaasioUrl();
        assertValidBaasioId();
        assertValidApplicationId();

        ArrayList<String> list = new ArrayList<String>();
        list.add(baasioId);
        list.add(applicationId);
        list.addAll(Arrays.asList(segments));

        String[] newSegments = list.toArray(new String[list.size()]);

        String url = UrlUtils.path(baasioUrl, newSegments);

        LogUtils.LOGV(TAG, "Client.httpRequest(): " + method + " url: " + url);

        HttpClient client = HttpUtils.getNewHttpClient();

        if (method == HttpMethod.POST) {
            HttpPost post = new HttpPost(url);
            if (!ObjectUtils.isEmpty(entity)) {
                post.setEntity(entity);
            }

            if (!ObjectUtils.isEmpty(getAccessToken())) {
                post.setHeader("Authorization", "Bearer " + getAccessToken());
            }

            HttpResponse httpResponse = null;
            try {
                httpResponse = client.execute(post);
            } catch (ClientProtocolException e) {
                throw new BaasioException(e);
            } catch (IOException e) {
                throw new BaasioException(e);
            }

            return httpResponse;
        } else if (method == HttpMethod.PUT) {
            HttpPut put = new HttpPut(url);
            if (!ObjectUtils.isEmpty(entity)) {
                put.setEntity(entity);
            }

            if (!ObjectUtils.isEmpty(getAccessToken())) {
                put.setHeader("Authorization", "Bearer " + getAccessToken());
            }

            HttpResponse httpResponse = null;
            try {
                httpResponse = client.execute(put);
            } catch (ClientProtocolException e) {
                throw new BaasioException(e);
            } catch (IOException e) {
                throw new BaasioException(e);
            }

            return httpResponse;
        }

        throw new BaasioException(BaasioError.ERROR_NOT_IMPLEMENTED);
    }

    /**
     * Send download file API request
     * 
     * @param method HTTP method
     * @param segments Paths
     * @return HTTP response for download API request
     */
    public HttpResponse downloadFileRequest(HttpMethod method, String... segments)
            throws BaasioException {
        assertValidBaasioUrl();
        assertValidBaasioId();
        assertValidApplicationId();

        ArrayList<String> list = new ArrayList<String>();
        list.add(baasioId);
        list.add(applicationId);
        list.addAll(Arrays.asList(segments));

        String[] newSegments = list.toArray(new String[list.size()]);

        String fileDownloadUrl;
        if (baasioUrl.contains("api.")) {
            fileDownloadUrl = baasioUrl.replace("api", "blob");
        } else {
            throw new IllegalArgumentException(BaasioError.ERROR_WRONG_BAASIO_URL);
        }

        String url = UrlUtils.path(fileDownloadUrl, newSegments);

        LogUtils.LOGV(TAG, "Client.httpRequest(): " + method + " url: " + url);

        HttpClient client = HttpUtils.getNewHttpClient();

        HttpGet get = new HttpGet(url);

        if (!ObjectUtils.isEmpty(getAccessToken())) {
            get.setHeader("Authorization", "Bearer " + getAccessToken());
        }

        HttpResponse httpResponse = null;
        try {
            httpResponse = client.execute(get);
        } catch (ClientProtocolException e) {
            throw new BaasioException(e);
        } catch (IOException e) {
            throw new BaasioException(e);
        }

        return httpResponse;
    }

    /**
     * Send HTTP request.
     * 
     * @param method HTTP method
     * @param cls Result object class
     * @param params URL parameters
     * @param data HTTP entity
     * @param segments Paths
     * @return Response for API request with class type
     */
    public <T> T httpRequest(String baseUrl, HttpMethod method, Class<T> cls,
            Map<String, Object> params, Object data, String... segments) throws BaasioException {
        // assertValidBaasioUrl();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (accessToken != null) {
            String auth = "Bearer " + accessToken;
            requestHeaders.set("Authorization", auth);
            LogUtils.LOGV(TAG, "Authorization: " + auth);
        }
        String url = UrlUtils.path(baseUrl, segments);

        MediaType contentType = MediaType.APPLICATION_JSON;
        if (method.equals(HttpMethod.POST) && ObjectUtils.isEmpty(data)
                && !ObjectUtils.isEmpty(params)) {
            data = UrlUtils.encodeParams(params);
            contentType = MediaType.APPLICATION_FORM_URLENCODED;
        } else {
            url = UrlUtils.addQueryParams(url, params);
        }
        requestHeaders.setContentType(contentType);
        List<Charset> accept = new ArrayList<Charset>();
        accept.add(Charset.forName("UTF-8"));

        requestHeaders.setAcceptCharset(accept);
        HttpEntity<?> requestEntity = null;

        if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)) {
            LogUtils.LOGV(TAG, "Client.httpRequest(): header: " + requestHeaders.toString());
            if (ObjectUtils.isEmpty(data)) {
                requestEntity = new HttpEntity<Object>(requestHeaders);
            } else {
                requestEntity = new HttpEntity<Object>(data, requestHeaders);
                LogUtils.LOGV(TAG, "Client.httpRequest(): request: " + data.toString());
            }

        } else {
            requestEntity = new HttpEntity<Object>(requestHeaders);
        }
        LogUtils.LOGV(TAG, "Client.httpRequest(): " + method + " url: " + url);

        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, cls);

        if (responseEntity != null) {
            if (responseEntity.getBody() != null) {
                LogUtils.LOGV(TAG, "Client.httpRequest(): reponse body: "
                        + responseEntity.getBody().toString());
            } else {
                LogUtils.LOGV(TAG,
                        "Client.httpRequest(): reponse body is null: " + responseEntity.toString());
            }
        }

        return responseEntity.getBody();
    }

    /**
     * Set signed in user.
     * 
     * @param user user to set signed-in user
     */
    public void setSignedInUser(BaasioUser user) {
        mLoggedInUser = user;
    }

    /**
     * Get signed-in user.
     * 
     * @return signed-in user
     */
    public BaasioUser getSignedInUser() {
        return mLoggedInUser;
    }

    /**
     * Set access token.
     * 
     * @param accessToken access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Get access token.
     * 
     * @return access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * To check GCM enabled.
     * 
     * @return true is enabled.
     */
    public boolean isGcmEnabled() {
        return gcmEnabled;
    }

    /**
     * Get GCM sender id.
     * 
     * @return GCM sender id
     */
    public String[] getGcmSenderId() {
        return gcmSenderId;
    }

    /**
     * Get file upload buffer size(byte).
     * 
     * @return upload buffer size(byte)
     */
    public static int getUploadBuffSize() {
        return mUploadBuffSize;
    }

    /**
     * Set file upload buffer size(byte).
     * 
     * @param uploadBuffSize upload buffer size
     */
    public static boolean setUploadBuffSize(int uploadBuffSize) {
        if (uploadBuffSize > MIN_BUFFER_SIZE) {
            Baas.mUploadBuffSize = uploadBuffSize;
            return true;
        }

        return false;
    }

    /**
     * Get file download buffer size(byte).
     * 
     * @return file download buffer size(byte)
     */
    public static int getDownloadBuffSize() {
        return mDownloadBuffSize;
    }

    /**
     * Set download buffer size(byte)
     * 
     * @param downloadBuffSize download buffer size
     */
    public static boolean setDownloadBuffSize(int downloadBuffSize) {
        if (downloadBuffSize > MIN_BUFFER_SIZE) {
            Baas.mDownloadBuffSize = downloadBuffSize;
            return true;
        }

        return false;
    }

}
