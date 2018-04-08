package com.closedevice.common;

import java.io.File;

/**
 * @author liudongdong-os
 * @date 2018/3/16
 */

public class RootUtil {
    private static final String[] sPaths = {
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/system/sbin/su",
            "/data/local/bin/su",
            "/data/local/xbin/su",
            "/data/local/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/vendor/bin/su",
            "/su/bin/su"
    };

    public static boolean isRoot() {
        for (String path : sPaths) {
            try {
                if (new File(path).exists()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
