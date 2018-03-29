package com.closedevice.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;

/**
 * @author closedevice
 * @date 2018/1/12
 */

public class AppUtil {

    private AppUtil() {
    }

    public static int getVersionCode(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getVersionName(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isAppEnable(PackageManager packageManager, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            return ai.enabled;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAppWorking(PackageManager packageManager, String packageName) {
        if (packageName == null || "".equals(packageName) || packageManager == null) {
            return false;
        }
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            if (ai.enabled) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return false;
    }

    public static String getPackageName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("PackageManagerGetSignatures")
    public static Signature[] getSystemSignatures(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo androidPackageInfo = pm.getPackageInfo("android", PackageManager.GET_SIGNATURES);
            return androidPackageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            //never happen,ignore
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("PackageManagerGetSignatures")
    public static Signature[] getAppSignatures(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo appPackageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            return appPackageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            //never happen,ignore
        }
        return null;
    }

    public static boolean isAppDebug(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(getPackageName(context), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private static void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build());
    }
}
