package com.closedevice.common.io;

import android.support.annotation.NonNull;

import java.io.Closeable;

/**
 * @author liudongdong-os
 * @date 2018/3/19
 */

public class IoUtil {
    public static void closeQuietly(@NonNull Closeable... closeable) {
        for (Closeable close : closeable) {
            try {
                close.close();
            } catch (Exception e) {
                //ignore
            }
        }
    }
}
