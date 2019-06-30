package com.ideacode.apm.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * <p>descript：程序包工具类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/9/28<p>
 * <p>update time：2018/9/28<p>
 * <p>version：1<p>
 */
public class PackageUtil {

    /**
     * 获取应用版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode;

        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = 0;
        }

        return versionCode;
    }

    /**
     * 获取应用版本名称
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName;

        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "";
        }

        return versionName;
    }

}
