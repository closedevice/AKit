package com.closedevice.common;

/**
 * @author liudongdong
 * @date 2018/1/12
 */

public class Preconditions {

    public static boolean checkNotNull(Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                return false;
            }
        }
        return true;
    }

    public static void assetNotNull(Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                throw new NullPointerException("args[" + i + "] = null");
            }
        }
    }

    public static void assetNotNull(String msg, Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new NullPointerException(msg);
            }
        }
    }
}