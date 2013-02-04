
package com.kth.baasio.entity.file;

import static com.kth.common.utils.LogUtils.makeLogTag;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.Baas;
import com.kth.baasio.BuildConfig;
import com.kth.baasio.callback.BaasioAsyncTask;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioDownloadAsyncTask;
import com.kth.baasio.callback.BaasioDownloadCallback;
import com.kth.baasio.callback.BaasioUploadAsyncTask;
import com.kth.baasio.callback.BaasioUploadCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.file.multipart.FilePart;
import com.kth.baasio.entity.file.multipart.FilePart.BaasioProgressListener;
import com.kth.baasio.entity.file.multipart.MultipartEntity;
import com.kth.baasio.entity.file.multipart.StringPart;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.response.BaasioResponse;
import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BaasioFile extends BaasioBaseEntity {
    private static final String TAG = makeLogTag(BaasioFile.class);

    public final static String ENTITY_TYPE = "file";

    public final static String PROPERTY_FILENAME = "filename";

    public final static String PROPERTY_CONTENT_LENGTH = "content-length";

    public final static String PROPERTY_CONTENT_TYPE = "content-type";

    public final static String PROPERTY_CHECKSUM = "checksum";

    public final static String PROPERTY_ETAG = "etag";

    public final static String PROPERTY_SIZE = "size";

    public BaasioFile() {
        super();
        setType(ENTITY_TYPE);
    }

    public BaasioFile(BaasioBaseEntity entity) {
        super(entity);
    }

    /**
     * Get predefined property names.
     * 
     * @return List of predefined property names
     */
    @Override
    @JsonIgnore
    public List<String> getPropertyNames() {
        List<String> properties = super.getPropertyNames();
        properties.add(PROPERTY_FILENAME);
        properties.add(PROPERTY_CONTENT_LENGTH);
        properties.add(PROPERTY_CONTENT_TYPE);
        properties.add(PROPERTY_CHECKSUM);
        properties.add(PROPERTY_ETAG);
        properties.add(PROPERTY_SIZE);
        return properties;
    }

    /**
     * Get file name.
     * 
     * @return File name
     */
    @JsonSerialize(include = NON_NULL)
    public String getFilename() {
        return JsonUtils.getStringProperty(properties, PROPERTY_FILENAME);
    }

    /**
     * Set file name.
     * 
     * @param filename File name
     */
    public void setFilename(String filename) {
        JsonUtils.setStringProperty(properties, PROPERTY_FILENAME, filename);
    }

    /**
     * Get content length.
     * 
     * @return Content length
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_CONTENT_LENGTH)
    public Long getContentLength() {
        return JsonUtils.getLongProperty(properties, PROPERTY_CONTENT_LENGTH);
    }

    /**
     * Set content length.
     * 
     * @param contentLength content-length
     */
    public void setContentLength(long contentLength) {
        JsonUtils.setLongProperty(properties, PROPERTY_CONTENT_LENGTH, contentLength);
    }

    /**
     * Get content type.
     * 
     * @return Content type
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_CONTENT_TYPE)
    public String getContentType() {
        return JsonUtils.getStringProperty(properties, PROPERTY_CONTENT_TYPE);
    }

    /**
     * Set content type.
     * 
     * @param contentType content-type
     */
    public void setContentType(String contentType) {
        JsonUtils.setStringProperty(properties, PROPERTY_CONTENT_TYPE, contentType);
    }

    /**
     * Get checksum.
     * 
     * @return Checksum
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_CHECKSUM)
    public String getChecksum() {
        return JsonUtils.getStringProperty(properties, PROPERTY_CHECKSUM);
    }

    /**
     * Set checksum.
     * 
     * @param checksum Checksum
     */
    public void setChecksum(String checksum) {
        JsonUtils.setStringProperty(properties, PROPERTY_CHECKSUM, checksum);
    }

    /**
     * Get etag.
     * 
     * @return etag
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_ETAG)
    public String getEtag() {
        return JsonUtils.getStringProperty(properties, PROPERTY_ETAG);
    }

    /**
     * Set etag.
     * 
     * @param etag ETAG
     */
    public void setEtag(String etag) {
        JsonUtils.setStringProperty(properties, PROPERTY_ETAG, etag);
    }

    /**
     * Get etag.
     * 
     * @return etag
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_SIZE)
    public Long getSize() {
        return JsonUtils.getLongProperty(properties, PROPERTY_SIZE);
    }

    /**
     * Set etag.
     * 
     * @param size Size
     */
    public void setSize(long size) {
        JsonUtils.setLongProperty(properties, PROPERTY_SIZE, size);
    }

    private static String getMimeType(File file) {
        String url = file.getAbsolutePath();

        String type = null;

        int dotPos = url.lastIndexOf(".") + 1;
        String extension = url.substring(dotPos);

        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * Get file entity.
     * 
     * @return Entity
     */
    public BaasioFile get() throws BaasioException {
        BaasioBaseEntity entity = BaasioBaseEntity.get(getType(), getUniqueKey());
        return entity.toType(BaasioFile.class);
    }

    /**
     * Get file entity. Executes asynchronously in background and the callbacks
     * are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void getInBackground(final BaasioCallback<BaasioFile> callback) {
        (new BaasioAsyncTask<BaasioFile>(callback) {
            @Override
            public BaasioFile doTask() throws BaasioException {
                return BaasioFile.this.get();
            }
        }).execute();
    }

    /**
     * Save file entity to baas.io.
     * 
     * @return Saved entity
     */
    public BaasioFile save() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(getFilename())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_FILE_NAME);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.POST, null, this, getType());

        if (response != null) {
            BaasioFile entity = response.getFirstEntity().toType(BaasioFile.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Save file entity to baas.io. Executes asynchronously in background and
     * the callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void saveInBackground(final BaasioCallback<BaasioFile> callback) {
        (new BaasioAsyncTask<BaasioFile>(callback) {
            @Override
            public BaasioFile doTask() throws BaasioException {
                return save();
            }
        }).execute();
    }

    /**
     * Update file entity from baas.io.
     * 
     * @return Updated entity
     */
    public BaasioFile update() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(getFilename())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_FILE_NAME);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.PUT, null, this, getType(),
                getUniqueKey());

        if (response != null) {
            BaasioFile entity = response.getFirstEntity().toType(BaasioFile.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Update file entity from baas.io. Executes asynchronously in background
     * and the callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void updateInBackground(final BaasioCallback<BaasioFile> callback) {
        (new BaasioAsyncTask<BaasioFile>(callback) {
            @Override
            public BaasioFile doTask() throws BaasioException {
                return update();
            }
        }).execute();
    }

    /**
     * Delete file entity from baas.io.
     * 
     * @return Deleted entity
     */
    public BaasioFile delete() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.DELETE, null, this, getType(),
                getUniqueKey());

        if (response != null) {
            BaasioFile entity = response.getFirstEntity().toType(BaasioFile.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Delete file entity from baas.io. Executes asynchronously in background
     * and the callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void deleteInBackground(final BaasioCallback<BaasioFile> callback) {
        (new BaasioAsyncTask<BaasioFile>(callback) {
            @Override
            public BaasioFile doTask() throws BaasioException {
                return delete();
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Connected file entity with class type
     */
    public BaasioFile connect(String relationship, String targetType, String targetUuid)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.connect(getType(), getUniqueKey(), relationship,
                targetType, targetUuid);
        return entity.toType(BaasioFile.class);
    }

    /**
     * Connect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param callback Result callback
     */
    public void connectInBackground(final String relationship, final String targetType,
            final String targetUuid, final BaasioCallback<BaasioFile> callback) {
        (new BaasioAsyncTask<BaasioFile>(callback) {
            @Override
            public BaasioFile doTask() throws BaasioException {
                return connect(relationship, targetType, targetUuid);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @return Connected file entity with class type
     */
    public <T extends BaasioBaseEntity> BaasioFile connect(String relationship, T target)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.connect(getType(), getUniqueKey(), relationship,
                target.getType(), target.getUniqueKey());
        return entity.toType(BaasioFile.class);
    }

    /**
     * Connect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void connectInBackground(final String relationship,
            final T target, final BaasioCallback<BaasioFile> callback) {
        (new BaasioAsyncTask<BaasioFile>(callback) {
            @Override
            public BaasioFile doTask() throws BaasioException {
                return connect(relationship, target);
            }
        }).execute();
    }

    /**
     * Disconnect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Disconnected file entity with class type
     */
    public BaasioFile disconnect(String relationship, String targetType, String targetUuid)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.disconnect(getType(), getUniqueKey(),
                relationship, targetType, targetUuid);
        return entity.toType(BaasioFile.class);
    }

    /**
     * Disconnect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param callback Result callback
     */
    public void disconnectInBackground(final String relationship, final String targetType,
            final String targetUuid, final BaasioCallback<BaasioFile> callback) {
        (new BaasioAsyncTask<BaasioFile>(callback) {
            @Override
            public BaasioFile doTask() throws BaasioException {
                return disconnect(relationship, targetType, targetUuid);
            }
        }).execute();
    }

    /**
     * Disconnect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @return Disconnected file entity with class type
     */
    public <T extends BaasioBaseEntity> BaasioFile disconnect(String relationship, T target)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.disconnect(getType(), getUniqueKey(),
                relationship, target.getType(), target.getUniqueKey());
        return entity.toType(BaasioFile.class);
    }

    /**
     * Disconnect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void disconnectInBackground(final String relationship,
            final T target, final BaasioCallback<BaasioFile> callback) {
        (new BaasioAsyncTask<BaasioFile>(callback) {
            @Override
            public BaasioFile doTask() throws BaasioException {
                return disconnect(relationship, target);
            }
        }).execute();
    }

    /**
     * Create a file entity and upload file content to baas.io. Executes
     * asynchronously in background and the callbacks are called in the UI
     * thread.
     * 
     * @param localFilePath Source local file path. This must not be null.
     * @return The uploading task to cancel.
     */
    public BaasioUploadAsyncTask fileUploadInBackground(final String localFilePath,
            final String destFileName, final BaasioUploadCallback callback) {

        BaasioUploadAsyncTask task = new BaasioUploadAsyncTask(callback) {
            private FilePart filePart = null;

            @Override
            public BaasioFile doTask() throws BaasioException {
                if (ObjectUtils.isEmpty(localFilePath)) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_LOCAL_FILE_PATH);
                }

                File file = new File(localFilePath);
                if (!file.exists()) {
                    throw new IllegalArgumentException(BaasioError.ERROR_FILE_IS_NOT_EXIST);
                }

                StringPart stringPart = new StringPart("entity",
                        JsonUtils.toJsonString(BaasioFile.this), "UTF-8");

                if (BuildConfig.DEBUG) {
                    LogUtils.LOGV(TAG, "StringPart:\n" + stringPart.toString());
                }

                final long fileSize = file.length();
                filePart = new FilePart("file", file, destFileName, getMimeType(file),
                        new BaasioProgressListener() {

                            @Override
                            public void updateTransferred(long transferedBytes) {
                                publishProgress(fileSize, transferedBytes);
                                if (isCancelled()) {
                                    filePart.cancel();
                                    Log.e("upload", "cancel");
                                }
                            }
                        });

                if (BuildConfig.DEBUG) {
                    LogUtils.LOGV(TAG, "FilePart:\n" + filePart.toString());
                }

                MultipartEntity multipartEntity = new MultipartEntity();
                multipartEntity.addPart(stringPart);
                multipartEntity.addPart(filePart);

                HttpResponse httpResponse = Baas.io().uploadFileRequest(HttpMethod.POST,
                        multipartEntity, "files");

                if (!ObjectUtils.isEmpty(httpResponse)) {
                    if (!ObjectUtils.isEmpty(httpResponse.getStatusLine())) {
                        if (httpResponse.getStatusLine().getStatusCode() >= 200
                                && httpResponse.getStatusLine().getStatusCode() < 300) {

                            String body = null;

                            try {
                                body = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                            } catch (ParseException e) {
                                throw new BaasioException(e);
                            } catch (IOException e) {
                                throw new BaasioException(e);
                            }

                            if (!ObjectUtils.isEmpty(body)) {
                                LogUtils.LOGV(TAG, "Client.httpRequest(): reponse body: " + body);

                                BaasioResponse response = JsonUtils.parse(body,
                                        BaasioResponse.class);
                                if (response != null) {
                                    if (response.getFirstEntity() != null) {
                                        BaasioBaseEntity entity = response.getFirstEntity();
                                        return entity.toType(BaasioFile.class);
                                    }

                                    throw new BaasioException(
                                            BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
                                }
                            }

                            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
                        }

                        HttpStatus status = HttpStatus.valueOf(httpResponse.getStatusLine()
                                .getStatusCode());

                        String body = null;

                        try {
                            body = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                        } catch (ParseException e) {
                            throw new BaasioException(e);
                        } catch (IOException e) {
                            throw new BaasioException(e);
                        }

                        if (!ObjectUtils.isEmpty(body)) {
                            BaasioException baasioError = new BaasioException(status, body);

                            if (HttpStatus.UNAUTHORIZED.toString().equals(
                                    baasioError.getStatusCode())) {
                                LogUtils.LOGV(TAG, "Need Login");
                                Baas.io().fireUnauthorized();
                            }
                            throw baasioError;
                        }
                        throw new BaasioException(status, "");
                    }

                    throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_STATUSLINE);
                }

                throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_HTTP_RESPONSE_DATA);
            }
        };

        task.execute();
        return task;
    }

    /**
     * Update file entity and change file content to baas.io. UUID must not be
     * null. Executes asynchronously in background and the callbacks are called
     * in the UI thread.
     * 
     * @param localFilePath Source local file path. This must not be null.
     * @return The uploading task to cancel.
     */
    public BaasioUploadAsyncTask fileUpdateInBackground(final String localFilePath,
            final String destFileName, final BaasioUploadCallback callback) {

        BaasioUploadAsyncTask task = new BaasioUploadAsyncTask(callback) {
            private FilePart filePart = null;

            @Override
            public BaasioFile doTask() throws BaasioException {
                if (ObjectUtils.isEmpty(getUuid())) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_UUID);
                }

                if (ObjectUtils.isEmpty(localFilePath)) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_LOCAL_FILE_PATH);
                }

                File file = new File(localFilePath);
                if (!file.exists()) {
                    throw new IllegalArgumentException(BaasioError.ERROR_FILE_IS_NOT_EXIST);
                }

                StringPart stringPart = new StringPart("entity",
                        JsonUtils.toJsonString(BaasioFile.this), "UTF-8");

                if (BuildConfig.DEBUG) {
                    LogUtils.LOGV(TAG, "StringPart:\n" + stringPart.toString());
                }

                final long fileSize = file.length();
                filePart = new FilePart("file", file, destFileName, getMimeType(file),
                        new BaasioProgressListener() {

                            @Override
                            public void updateTransferred(long transferedBytes) {
                                publishProgress(fileSize, transferedBytes);
                                if (isCancelled()) {
                                    filePart.cancel();
                                }
                            }
                        });

                if (BuildConfig.DEBUG) {
                    LogUtils.LOGV(TAG, "FilePart:\n" + filePart.toString());
                }

                MultipartEntity multipartEntity = new MultipartEntity();
                multipartEntity.addPart(stringPart);
                multipartEntity.addPart(filePart);

                HttpResponse httpResponse = Baas.io().uploadFileRequest(HttpMethod.PUT,
                        multipartEntity, "files", getUuid().toString());

                if (!ObjectUtils.isEmpty(httpResponse)) {
                    if (!ObjectUtils.isEmpty(httpResponse.getStatusLine())) {
                        if (httpResponse.getStatusLine().getStatusCode() >= 200
                                && httpResponse.getStatusLine().getStatusCode() < 300) {

                            String body = null;

                            try {
                                body = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                            } catch (ParseException e) {
                                throw new BaasioException(e);
                            } catch (IOException e) {
                                throw new BaasioException(e);
                            }

                            if (!ObjectUtils.isEmpty(body)) {
                                LogUtils.LOGV(TAG, "Client.httpRequest(): reponse body: " + body);

                                BaasioResponse response = JsonUtils.parse(body,
                                        BaasioResponse.class);
                                if (response != null) {
                                    if (response.getFirstEntity() != null) {
                                        BaasioBaseEntity entity = response.getFirstEntity();
                                        return entity.toType(BaasioFile.class);
                                    }

                                    throw new BaasioException(
                                            BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
                                }
                            }

                            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
                        }

                        HttpStatus status = HttpStatus.valueOf(httpResponse.getStatusLine()
                                .getStatusCode());

                        String body = null;

                        try {
                            body = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                        } catch (ParseException e) {
                            throw new BaasioException(e);
                        } catch (IOException e) {
                            throw new BaasioException(e);
                        }

                        if (!ObjectUtils.isEmpty(body)) {
                            BaasioException baasioError = new BaasioException(status, body);

                            if (HttpStatus.UNAUTHORIZED.toString().equals(
                                    baasioError.getStatusCode())) {
                                LogUtils.LOGV(TAG, "Need Login");
                                Baas.io().fireUnauthorized();
                            }
                            throw baasioError;
                        }
                        throw new BaasioException(status, "");
                    }

                    throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_STATUSLINE);
                }

                throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_HTTP_RESPONSE_DATA);
            }
        };

        task.execute();
        return task;
    }

    /**
     * Download file to the local file path from baas.io. Executes
     * asynchronously in background and the callbacks are called in the UI
     * thread.
     * 
     * @param localFilePath Destination local file path
     * @return The downloading task to cancel.
     */
    public BaasioDownloadAsyncTask fileDownloadInBackground(final String localFilePath,
            final BaasioDownloadCallback callback) {

        BaasioDownloadAsyncTask task = new BaasioDownloadAsyncTask(callback) {

            @Override
            public String doTask() throws Exception {
                if (ObjectUtils.isEmpty(localFilePath)) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_LOCAL_FILE_PATH);
                }

                String destFilePath = localFilePath.replace("\\", "/");

                String fileName = "";
                String filePath;

                if (destFilePath.endsWith("/")) {
                    filePath = destFilePath.substring(0, destFilePath.length() - 1);
                    fileName = getFilename();
                } else {
                    Uri uri = Uri.parse(destFilePath);
                    fileName = uri.getLastPathSegment();
                    filePath = destFilePath.substring(0, destFilePath.length() - fileName.length()
                            - 1);
                }

                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                } else {
                    if (!dir.isDirectory()) {
                        dir.mkdirs();
                    }
                }

                if (ObjectUtils.isEmpty(fileName)) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_LOCAL_FILE_NAME);
                }

                String localResultFilePath = filePath + File.separator + fileName;

                File file = new File(localResultFilePath);
                file.createNewFile();

                HttpResponse httpResponse = null;

                httpResponse = Baas.io().downloadFileRequest(HttpMethod.GET, "files",
                        getUuid().toString());

                if (httpResponse != null) {
                    if (httpResponse.getStatusLine() != null) {
                        if (httpResponse.getStatusLine().getStatusCode() >= 200
                                && httpResponse.getStatusLine().getStatusCode() < 300) {
                            Header[] clHeaders = httpResponse.getHeaders("Content-Length");
                            if (clHeaders.length > 0) {
                                Header header = clHeaders[0];

                                long totalSize = Long.parseLong(header.getValue());
                                long downloadedSize = 0;

                                byte buf[] = new byte[Baas.getDownloadBuffSize()];
                                int numBytesRead;

                                if (httpResponse.getEntity() != null) {
                                    InputStream stream = httpResponse.getEntity().getContent();
                                    BufferedOutputStream fos = new BufferedOutputStream(
                                            new FileOutputStream(file));
                                    do {
                                        numBytesRead = stream.read(buf);
                                        if (numBytesRead > 0) {
                                            fos.write(buf, 0, numBytesRead);
                                            downloadedSize += numBytesRead;

                                            if (callback != null) {
                                                callback.onProgress(totalSize, downloadedSize);
                                            }
                                        }

                                        if (isCancelled()) {
                                            if (fos != null) {
                                                fos.flush();
                                                fos.close();
                                            }

                                            if (stream != null) {
                                                stream.close();
                                            }

                                            throw new IOException(
                                                    BaasioError.ERROR_FILE_TASK_CANCELLED);
                                        }
                                    } while (numBytesRead > 0);

                                    if (fos != null) {
                                        fos.flush();
                                        fos.close();
                                    }

                                    if (stream != null) {
                                        stream.close();
                                    }

                                    if (totalSize != downloadedSize) {
                                        throw new BaasioException(
                                                BaasioError.ERROR_FILE_SIZE_DIFFERENT);
                                    } else {
                                        return localResultFilePath;
                                    }
                                } else {
                                    throw new BaasioException(
                                            BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
                                }
                            }
                        } else {
                            HttpStatus status = HttpStatus.valueOf(httpResponse.getStatusLine()
                                    .getStatusCode());

                            String body = null;
                            try {
                                body = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                            } catch (ParseException e) {
                                throw new BaasioException(e);
                            } catch (IOException e) {
                                throw new BaasioException(e);
                            }

                            BaasioException baasioError = new BaasioException(status, body);

                            if (HttpStatus.UNAUTHORIZED.toString().equals(
                                    baasioError.getStatusCode())) {
                                LogUtils.LOGV(TAG, "Need Login");
                                Baas.io().fireUnauthorized();
                            }
                            throw baasioError;
                        }
                    } else {
                        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_STATUSLINE);
                    }
                }
                throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_HTTP_RESPONSE_DATA);
            }
        };

        task.execute();
        return task;
    }
}
