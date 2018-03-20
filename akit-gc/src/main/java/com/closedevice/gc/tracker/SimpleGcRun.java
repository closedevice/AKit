package com.closedevice.gc.tracker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.closedevice.gc.GcTrigger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * @author liudongdong-os
 * @date 2018/3/19
 */

public class SimpleGcRun implements GcRun {
    public final String TAG = "GcTracker";
    private ExecutorService gcExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r, "GC-Run");
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    });

    @Override
    public void doGc() {
        if (!forceGc()) {
            return;
        }
        runOnGcExecutor(new Runnable() {
            @Override
            public void run() {
                beforeGc();
                try {
                    GcTrigger.DEFAULT.runGc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                afterGc();
            }
        });

    }

    public void runOnGcExecutor(Runnable runnable) {
        gcExecutor.submit(runnable);
    }

    public boolean forceGc() {
        Runtime runtime = Runtime.getRuntime();
        long dalvikMax = runtime.maxMemory();
        long dalvikTotal = runtime.totalMemory();
        long dalvikUsed = dalvikTotal - runtime.freeMemory();
        long rate = Math.round((double) dalvikUsed / (double) dalvikMax * 100);
        String gcLog = formatGcLog(dalvikMax, dalvikTotal, dalvikUsed, GcTracker.getLastGcTime());
        if (dalvikUsed > (4 * dalvikMax) / 5) {
            Log.e(TAG, String.format("GC occurred (%s%%) : %s", rate, gcLog));
            return true;
        }

        if (dalvikUsed > (3 * dalvikMax) / 5) {
            Log.w(TAG, String.format("GC occurred (%s%%) : %s", rate, gcLog));
            return true;
        }

        Log.e(TAG, String.format("GC occurred (%s%%) : %s", rate, gcLog));
        return false;
    }

    protected void beforeGc() {
        Runtime runtime = Runtime.getRuntime();
        long dalvikMax = runtime.maxMemory();
        long dalvikTotal = runtime.totalMemory();
        long dalvikUsed = dalvikTotal - runtime.freeMemory();
        Log.d(TAG, "before force-gc " + formatGcLog(dalvikMax, dalvikTotal, dalvikUsed, GcTracker.getLastGcTime()));
    }

    protected void afterGc() {
        Runtime runtime = Runtime.getRuntime();
        long dalvikMax = runtime.maxMemory();
        long dalvikTotal = runtime.totalMemory();
        long dalvikUsed = dalvikTotal - runtime.freeMemory();
        Log.d(TAG, "after force-gc " + formatGcLog(dalvikMax, dalvikTotal, dalvikUsed, GcTracker.getLastGcTime()));
    }

    public String formatGcLog(long dalvikMax, long total, long dalvikUsed, long lastGcTime) {
        int unit = 1024;
        StringBuilder builder = new StringBuilder()
                .append("max=").append(dalvikMax / unit).append("KB ")
                .append("total=").append(total / unit).append("KB ")
                .append("used=").append(dalvikUsed / unit).append("KB ")
                .append("gap=").append((System.currentTimeMillis() - lastGcTime)).append("ms");
        return builder.toString();
    }


}
