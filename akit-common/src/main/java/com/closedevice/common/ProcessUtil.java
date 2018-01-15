package com.closedevice.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * @author liudongdong
 * @date 2018/1/12
 */

public class ProcessUtil {

    /**
     * Gets whether current process is main process.
     *
     * @return Whether current process is main process.
     */
    public static boolean isInMainProcess(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            return false;
        }
        String mainProcess = packageInfo.applicationInfo.processName;
        String processName = getProcessName(context);
        return processName.equals(mainProcess);
    }

    /**
     * Gets current process name.
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        int myPid = android.os.Process.myPid();
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningAppProcessInfo myProcess = null;
        List<ActivityManager.RunningAppProcessInfo> runningProcesses =
                activityManager.getRunningAppProcesses();
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo process : runningProcesses) {
                if (process.pid == myPid) {
                    myProcess = process;
                    break;
                }
            }
        }
        return myProcess != null ? myProcess.processName : "";
    }
}
