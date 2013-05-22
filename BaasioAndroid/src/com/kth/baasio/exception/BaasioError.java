
package com.kth.baasio.exception;

public final class BaasioError {
    public static final String ERROR_NEED_INIT = "Did you call init() on Application.onCreate()? You may miss to setting name of application tag from androidmanifest.xml.";

    public static final String ERROR_MISSING_BAASIO_URL = "Missing baas.io url";

    public static final String ERROR_MISSING_BAASIO_ID = "Missing baas.io member id";

    public static final String ERROR_MISSING_APPLICATION_ID = "Missing application id";

    public static final String ERROR_MISSING_TYPE = "Missing type";

    public static final String ERROR_MISSING_GROUP_UUID_OR_PATH = "Missing group uuid or path";

    public static final String ERROR_MISSING_UUID = "Missing uuid";

    public static final String ERROR_MISSING_UUID_OR_NAME = "Missing uuid or name";

    public static final String ERROR_MISSING_USER_UUID_OR_USERNAME = "Missing uuid or username";

    public static final String ERROR_MISSING_MESSAGE = "Missing message";

    public static final String ERROR_MISSING_TARGET = "Missing target";

    public static final String ERROR_MISSING_USERNAME = "Missing username";

    public static final String ERROR_MISSING_PASSWORD = "Missing password";

    public static final String ERROR_MISSING_RELATIONSHIP = "Missing relationship";

    public static final String ERROR_MISSING_KEYWORD = "Missing keyword to query";

    public static final String ERROR_MISSING_TARGET_ENTITY = "Missing target entity";

    public static final String ERROR_MISSING_TARGET_GROUP_ENTITY = "Missing target group entity";

    public static final String ERROR_MISSING_FACEBOOK_TOKEN = "Missing facebook access token";

    public static final String ERROR_WRONG_BAASIO_URL = "baas.io URL must start with http:// or https://";

    public static final String ERROR_MISSING_FILE_NAME = "Missing filename";

    public static final String ERROR_MISSING_LOCAL_FILE_PATH = "Missing local file path";

    public static final String ERROR_MISSING_LOCAL_FILE_NAME = "Missing local file name";

    public static final String ERROR_NEED_SIGNIN = "Need signin";

    public static final String ERROR_NOT_SUPPORT_CLONING = "Clone is not allowed.";

    public static final String ERROR_ENTITY_TYPE_MISMATCHED = "Entity type is mismatched";

    public static final String ERROR_GCM_DISABLED = "GCM is disabled.";

    public static final String ERROR_GCM_ALREADY_REGISTERED = "GCM is already registered";

    public static final String ERROR_GCM_ALREADY_UNREGISTERED = "GCM is already unregistered";

    public static final String ERROR_GCM_MISSING_REGID = "Missing device token(regId)";

    public static final String ERROR_GCM_TAG_LENGTH_EXCEED = "Max tag length is 12";

    public static final String ERROR_GCM_TAG_PATTERN_MISS_MATCHED = "Tags only allowed a-z, A-Z, 0-9, hyphen, underscore characters";// "^[a-zA-Z0-9-_]*$"

    public static final String ERROR_FILE_IS_NOT_EXIST = "File is not exist.";

    public static final String ERROR_FILE_MULTIPART_FORM_UNINIT_HEADERSPROVIDER = "Uninitialized headersProvider";

    public static final String ERROR_FILE_MULTIPART_FORM_NOT_SUPPORT_CLONING = "MultipartEntity does not support cloning";

    public static final String ERROR_FILE_MULTIPART_FORM_FILE_IS_NULL = "File may not be null";

    public static final String ERROR_FILE_MULTIPART_FORM_NAME_IS_NULL = "Name may not be null";

    public static final String ERROR_FILE_MULTIPART_FORM_VALUE_IS_NULL = "Value may not be null";

    public static final String ERROR_FILE_OUTPUTSTREAM_IS_NULL = "Output stream may not be null";

    public static final String ERROR_FILE_SIZE_DIFFERENT = "File download size is diffrent with remote file size";

    public static final String ERROR_FILE_TASK_CANCELLED = "File task cancelled";

    public static final String ERROR_HELP_EXCEED_STRING_LENGTH = "Question is exceeded max length. Max length is 1000.";

    public static final String ERROR_QUERY_NO_MORE_NEXT = "No more next entities";

    public static final String ERROR_QUERY_NO_MORE_PREV = "No more prev entities";

    public static final String ERROR_UNKNOWN_NO_STATUSLINE = "Unknown error(No status line)";

    public static final String ERROR_UNKNOWN_NO_HTTP_RESPONSE_DATA = "Unknown error(No http response data)";

    public static final String ERROR_UNKNOWN_NO_RESPONSE_DATA = "Unknown error(No response data)";

    public static final String ERROR_UNKNOWN_NORESULT_ENTITY = "Unknown error(no result entity)";

    public static final String ERROR_NOT_IMPLEMENTED = "Not implemented";

    public static final String ERROR_UNKNOWN = "Unknown Error";

}
