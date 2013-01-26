
package com.kth.baasio.preferences;

import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.PlatformSpecificImplementationFactory;
import com.kth.common.preference.SharedPreferenceSaver;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class BaasioPreferences {
    private static final String DEFAULT_PREFERENCES_NAME = "BaasioPreferences";

    private static final String SHARED_PREFERENCE_NAME_SECRET_UUID = "baasio_user_secret";

    private static final String SHARED_PREFERENCE_NAME_USER_STRING = "baasio_user_data";

    private static final String SHARED_PREFERENCE_NAME_ACCESS_TOKEN = "baasio_access_token";

    private static final String SHARED_PREFERENCE_NAME_REGISTERED_DEVICE_UUID_FOR_PUSH = "baasio_registered_device_uuid_for_push";

    private static final String SHARED_PREFERENCE_NAME_REGISTERED_USERNAME_FOR_PUSH = "baasio_registered_username_for_push";

    private static final String SHARED_PREFERENCE_NAME_REGISTERED_TAGS_FOR_PUSH = "baasio_registered_tag_for_push";

    private static final String SHARED_PREFERENCE_NAME_NEED_REGISTER_TAGS_FOR_PUSH = "baasio_need_register_tag_for_push";

    private static SharedPreferences mPreferences;

    private static SharedPreferences getPreference(Context context) {
        if (mPreferences == null)
            mPreferences = context.getSharedPreferences(DEFAULT_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);

        return mPreferences;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException(BaasioError.ERROR_NOT_SUPPORT_CLONING);
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.clear();

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static void setUserString(Context context, String string) {
        String secret = UUID.randomUUID().toString();

        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(SHARED_PREFERENCE_NAME_SECRET_UUID, secret);
        editor.putString(SHARED_PREFERENCE_NAME_USER_STRING, encrypt(context, secret, string));

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static String getUserString(Context context) {
        SharedPreferences prefs = getPreference(context);
        String secret = prefs.getString(SHARED_PREFERENCE_NAME_SECRET_UUID, "");
        if (ObjectUtils.isEmpty(secret)) {
            return "";
        }

        String result = prefs.getString(SHARED_PREFERENCE_NAME_USER_STRING, "");

        if (result.length() <= 0) {
            return result;
        }

        return decrypt(context, secret, result);
    }

    public static void setAccessToken(Context context, String string) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(SHARED_PREFERENCE_NAME_ACCESS_TOKEN, string);

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = getPreference(context);
        String result = prefs.getString(SHARED_PREFERENCE_NAME_ACCESS_TOKEN, "");

        return result;
    }

    public static void setDeviceUuidForPush(Context context, String string) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(SHARED_PREFERENCE_NAME_REGISTERED_DEVICE_UUID_FOR_PUSH, string);

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static String getDeviceUuidForPush(Context context) {
        SharedPreferences prefs = getPreference(context);
        String result = prefs.getString(SHARED_PREFERENCE_NAME_REGISTERED_DEVICE_UUID_FOR_PUSH, "");

        return result;
    }

    public static void setRegisteredUserName(Context context, String string) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(SHARED_PREFERENCE_NAME_REGISTERED_USERNAME_FOR_PUSH, string);

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static String getRegisteredUserName(Context context) {
        SharedPreferences prefs = getPreference(context);
        String result = prefs.getString(SHARED_PREFERENCE_NAME_REGISTERED_USERNAME_FOR_PUSH, "");

        return result;
    }

    public static void setRegisteredTags(Context context, String string) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(SHARED_PREFERENCE_NAME_REGISTERED_TAGS_FOR_PUSH, string);

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static String getRegisteredTags(Context context) {
        SharedPreferences prefs = getPreference(context);
        String result = prefs.getString(SHARED_PREFERENCE_NAME_REGISTERED_TAGS_FOR_PUSH, "");

        return result;
    }

    public static void setNeedRegisteredTags(Context context, String string) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(SHARED_PREFERENCE_NAME_NEED_REGISTER_TAGS_FOR_PUSH, string);

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static String getNeedRegisteredTags(Context context) {
        SharedPreferences prefs = getPreference(context);
        String result = prefs.getString(SHARED_PREFERENCE_NAME_NEED_REGISTER_TAGS_FOR_PUSH, "");

        return result;
    }

    private static final char[] SEKRIT = new String("baasio_eoqkrqktm!@").toCharArray();

    protected static String encrypt(Context context, String uuid, String value) {

        try {
            final byte[] bytes = value != null ? value.getBytes("UTF-8") : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(uuid.getBytes("UTF-8"),
                    20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected static String decrypt(Context context, String uuid, String value) {
        try {
            final byte[] bytes = value != null ? Base64.decode(value, Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(uuid.getBytes("UTF-8"),
                    20));
            return new String(pbeCipher.doFinal(bytes), "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
