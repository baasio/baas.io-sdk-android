
package com.kth.baasio.test;

import android.os.Environment;

import java.io.File;

public class UnitTestConfig {
    // Auth
    public static final String USER1_USERNAME = "android_unit_test1";

    public static final String USER1_EMAIL = USER1_USERNAME + "@test.com";

    public static final String USER2_USERNAME = "android_unit_test2";

    public static final String USER2_EMAIL = USER2_USERNAME + "@test.com";

    public static final String COMMON_PASSWORD = "test1234";

    public static final String CHANGE_PASSWORD = "test1231";

    // Entity
    public static final String ENTITY1_TYPE = "test1";

    public static final String ENTITY2_TYPE = "test2";

    public static final String ENTITY_PROPERTY_NAME = "test";

    public static final String ENTITY1_PROPERTY_VALUE = "1";

    public static final String ENTITY1_PROPERTY_CHANGED_VALUE = "1-2";

    public static final String ENTITY2_PROPERTY_VALUE = "2";

    public static final String ENTITY2_PROPERTY_CHANGED_VALUE = "2-2";

    public static final String RELATIONSHIP_NAME = "entity_relation";

    // Group
    public static final String GROUP_PATH = "AndroidUnitTestGroup";

    public static final String GROUP_PROPERTY_NAME = "test";

    public static final String GROUP_PROPERTY_VALUE = "1";

    // File
    public static final String FILE_TEST_ROOT_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "Download";

    public static final String FILE_TEST_FILENAME = "test_file1_파일이름.mp3";

    public static final String FILE_TEST_FILE_FULLPATH = FILE_TEST_ROOT_PATH + File.separator
            + FILE_TEST_FILENAME;

    public static final String FILE_TEST_UPDATED_FILENAME = "test_file2_파일이름.mp3";

    public static final String FILE_TEST_UPDATED_FILE_FULLPATH = FILE_TEST_ROOT_PATH
            + File.separator + FILE_TEST_UPDATED_FILENAME;

    public static final String FILE_TEST_EMPTY_FILENAME = "TestEmptyFile_파일이름.txt";

    public static final String FILE_PROPERTY_NAME = "test";

    public static final String FILE_PROPERTY_VALUE = "1";

    public static final String FILE_PROPERTY_CHANGED_VALUE = "2";

    // Push
    public static final String PUSH_SHOULD_RECEIVE_TAG = "AUT1";

    public static final String PUSH_SHOULD_NOT_RECEIVE_TAG = "AUT2";

    public static final String PUSH_TARGET_ALL_MSG = "전체발송";

    public static final String PUSH_TARGET_USER_MSG = "개별발송-USER";

    public static final String PUSH_TARGET_DEVICE_MSG = "개별발송-DEVICE";

    public static final String PUSH_TARGET_IOS_MSG = "iOS 전체발송";

    public static final String PUSH_TARGET_ANDROID_MSG = "Android 전체발송";

    public static final String PUSH_TARGET_TAG_SHOULD_RECEIVE = "Tag 발송: 받아야함";

    public static final String PUSH_TARGET_TAG_SHOULD_NOT_RECEIVE = "Tag 발송: 받지말아야함";

    public static final String PUSH_RESERVED_MSG_POSTFIX = " + 예약발송";

    // Help Center
    public static final boolean HELPCENTER_RECOMMENDED_HELP = true;

    public static final String HELPCENTER_QUESTION_BODY = "질문 보내기";

    public static final String HELPCENTER_NORMAL_TITLE = "일반_질문";

    public static final String HELPCENTER_NORMAL_CONTENT = "일반_질문_답변";

    public static final String HELPCENTER_RECOMMENDED_CATEGORY_TITLE = "추천 도움말";

    public static final String HELPCENTER_NORMAL_CATEGORY_TITLE = "일반";

    public static final String HELPCENTER_SEARCH_KEYWORD = "일반_질문";
}
