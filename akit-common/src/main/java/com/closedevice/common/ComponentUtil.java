package com.closedevice.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED;

/**
 * @author liudongdong-os
 * @date 2018/3/20
 */

public class ComponentUtil {
    private static final Executor ioExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "Component-IO");
        }
    });

    public static void setEnableAsync(final Context context, final Class<?> componentClass, final boolean enabled) {
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                setEnableBlocking(context, componentClass, enabled);
            }
        });
    }

    public static void setEnableBlocking(Context context, Class<?> componetClass, boolean enabled) {
        ComponentName component = new ComponentName(context, componetClass);
        PackageManager pm = context.getPackageManager();
        int newState = enabled ? COMPONENT_ENABLED_STATE_ENABLED : COMPONENT_ENABLED_STATE_DISABLED;
        try {
            pm.setComponentEnabledSetting(component, newState, PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
