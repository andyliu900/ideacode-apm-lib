package com.ideacode.apm.library.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.ideacode.apm.library.core.Manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.ideacode.apm.library.Env.APM_TAG;


/**
 * @author ArgusAPM Team
 */
public class SystemUtils {
    private static final String SUB_TAG = "SystemUtils";

    /**
     * 获取包名英文名
     *
     * @param context
     * @return
     */
    public static String getPackageNameEn(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取包名中文名
     *
     * @param context
     * @return
     */
    public static String getPackageNameCn(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            int labelRes = applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getUidByPkgName(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return null;
        }
        final PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ApplicationInfo> installedApplications = processInstalledApp(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        for (ApplicationInfo appInfo : installedApplications) {
            if (!TextUtils.isEmpty(appInfo.packageName) && pkgName.equalsIgnoreCase(appInfo.packageName)) {
                return appInfo.uid;
            }
        }
        return null;
    }

    /**
     * 处理安装的app，主要合并共享uid的app
     */
    private static List<ApplicationInfo> processInstalledApp(List<ApplicationInfo> inputApps) {
        List<Integer> uidList = new ArrayList<Integer>();
        List<ApplicationInfo> result = new ArrayList<ApplicationInfo>();
        if (null == inputApps) {
            return result;
        }
        for (ApplicationInfo info : inputApps) {
            int uid = info.uid;
            if (!uidList.contains(uid)) {
                uidList.add(uid);
                result.add(info);
            }
        }
        inputApps.clear();
        uidList.clear();
        return result;
    }

    public static boolean isWifiConnected() {
        // 原有方法在android4.0上返回值总是为true，修改为以下方式
        boolean isWifiConnected = false;
        try {
            ConnectivityManager connecManager = (ConnectivityManager) Manager.getInstance().getConfig().appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = connecManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mNetworkInfo != null) {
                isWifiConnected = mNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, e.toString());
            }
        }
        return isWifiConnected;
    }

    public static boolean isMobileConnected() {
        boolean isCellConnected = false;
        try {
            ConnectivityManager connecManager = (ConnectivityManager) Manager.getInstance().getConfig().appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = connecManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mNetworkInfo != null) {
                isCellConnected = mNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, e.toString());
            }
        }
        return isCellConnected;
    }

    public static boolean isNetWorkConnect() {
        return isWifiConnected() || isMobileConnected();
    }

    /*
     * android 4.0
     */
    public static final boolean isIceCreamAboveVersion() {
        return android.os.Build.VERSION.SDK_INT >= 14;
    }

    /*
     * android 4.1
     */
    public static final boolean isJellyBeanAboveVersion() {
        return android.os.Build.VERSION.SDK_INT >= 16;
    }

    /*
     * android 5.0
     */
    public static final boolean isLollipopAboveVersion() {
        return android.os.Build.VERSION.SDK_INT >= 21;
    }

    /*
     * android 5.1
     */
    public static final boolean isLollipopOneAboveVersion() {
        return android.os.Build.VERSION.SDK_INT >= 22;
    }

    /*
     * android 6.0
     */
    public static final boolean isMarshmallowAboveVersion() {
        return android.os.Build.VERSION.SDK_INT >= 23;

    }

    /*
     * android 7.0
     */
    public static final boolean isNougatAboveVersion() {
        return android.os.Build.VERSION.SDK_INT >= 24;

    }

    /*
     * android 7.1
     */
    public static final boolean isUnderMaxSuportVersion() {
        return android.os.Build.VERSION.SDK_INT <= 24;

    }

    public static boolean isRooted() {

        File file = new File("/system/xbin/su");
        if (!file.exists()) {
            file = new File("/system/bin/su");
        }

        if (!file.exists()) {
            return false;
        }

        boolean isRooted = true;
        try {
            Runtime runtime = Runtime.getRuntime();

            runtime.exec("su");
        } catch (IOException e) {
            e.printStackTrace();
            isRooted = false;
        }

        return isRooted;
    }

    public static boolean isQiKuUI() {
        String value = getSystemProperty("ro.build.uiversion");
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        return value.contains("360UI");
    }

    public static String getSystemProperty(String propertyKey) {
        String propertyValue = null;
        try {
            Object obj = invokeStaticMethod("android.os.SystemProperties", "get", new Class[]{String.class}, new Object[]{propertyKey});
            if (obj != null && obj instanceof String) {
                propertyValue = (String) obj;
            }
        } catch (Exception e) {
            //ignore
        }

        return propertyValue;
    }

    public static Object invokeStaticMethod(String clzName, String methodName, Class<?>[] methodParamTypes, Object... methodParamValues) {
        try {
            Class clz = Class.forName(clzName);
            if (clz != null) {
                Method med = clz.getMethod(methodName, methodParamTypes);
                if (med != null) {
                    med.setAccessible(true);
                    Object retObj = med.invoke(null, methodParamValues);
                    return retObj;
                }
            }
        } catch (Exception e) {
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, e.getMessage());
            }
        }
        return null;
    }

    public static final String sdcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static boolean checkPermission(String permission) {
        if (TextUtils.isEmpty(permission)) return false;

        Context context = Manager.getContext();
        if (null == context) return false;

        String packageName = context.getPackageName();
        if (TextUtils.isEmpty(packageName)) return false;

        PackageManager pm = context.getPackageManager();
        if (null == pm) return false;

        int permissionState = PackageManager.PERMISSION_DENIED;

        try {
            permissionState = pm.checkPermission(permission, packageName);
        } catch (Exception e) {
            ApmLogX.e(APM_TAG, SUB_TAG, e.toString());
        }

        return PackageManager.PERMISSION_GRANTED == permissionState ? true : false;
    }

    public static String md5(String source) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(source.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getSn() {
        String serial = android.os.Build.SERIAL;
        return serial == null ? "" : serial;
    }

    public static String getDid() {
        String serial = android.os.Build.SERIAL;
        if (serial != null) {
            String[] serials = serial.split("\\|");
            if (serials != null && serials.length == 3) {
                try {
                    return serials[1];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String mac = "02:00:00:00:00:00";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        return mac;
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * @param context
     * @return
     */
    private static String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     * @return
     */
    private static String getMacAddress() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     * @return
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }
}
