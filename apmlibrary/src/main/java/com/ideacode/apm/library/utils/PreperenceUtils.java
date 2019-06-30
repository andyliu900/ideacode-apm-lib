package com.ideacode.apm.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.utils
 * @ClassName: PreperenceUtils
 * @Description:    Preperence工具类
 * @Author: randysu
 * @CreateDate: 2019-05-23 17:34
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 17:34
 * @UpdateRemark:
 * @Version: 1.0
 */
public class PreperenceUtils {
    
    public static final String SP_NAME = "ideacode_sp_apm_sdk";
    public static final String SP_KEY_LAST_MEMORY_TIME = "sp_key_last_memory_time"; //memory save time
    public static final String SP_KEY_LAST_CLEANDATA_TIME = "sp_key_last_cleandata_time"; //clean data time
    public static final String SP_KEY_LAST_UPLOADDATA_TIME = "sp_key_last_uploaddata_time"; //clean data time

    public static void setLong(final Context context, final String key, final Long value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    public static long getLong(final Context context, String key, long defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    public static void setString(final Context context, final String key, final String value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(final Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(defaultValue)) {
            return sp.getString(key, "");
        } else {
            return sp.getString(key, "");
        }
    }

    public static void setInt(final Context context, final String key, final int value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(final Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static boolean getBoolean(final Context context, String key, boolean defaultV) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultV);
    }

    public static void setBoolean(final Context context, final String key, final boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void setFloat(final Context context, final String key, final float value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).apply();
    }

    public static float getFloat(final Context context, String key, float defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }
    
}
