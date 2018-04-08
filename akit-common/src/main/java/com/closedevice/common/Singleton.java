package com.closedevice.common;

import android.content.Context;

/**
 * @author liudongdong-os
 * @date 2018/3/16
 */

public abstract class Singleton<T> {
    private T value;

    protected abstract T create(Context context);

    public final T get(Context context) {
        synchronized (this) {
            if (value == null) {
                value = create(context);
            }
            return value;
        }
    }
}
