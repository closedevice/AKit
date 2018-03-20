package com.closedevice.gc.tracker;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author liudongdong-os
 * @date 2018/3/19
 */

public class GcTracker {
    private static WeakReference<InternalGcWatcher> sGcWatcher = new WeakReference<InternalGcWatcher>(new InternalGcWatcher());
    private static ArrayList<GcRun> sGcRunners = new ArrayList<>();
    private static GcRun[] sTmpGcRunners = new GcRun[1];
    private static long sLastGcTime;

    public static void addGcRunWatcher(GcRun gcRun) {
        synchronized (sGcRunners) {
            sGcRunners.add(gcRun);
        }
    }

    public static long getLastGcTime() {
        return sLastGcTime;
    }

    private static final class InternalGcWatcher {
        @Override
        protected void finalize() throws Throwable {
            sLastGcTime = System.currentTimeMillis();
            synchronized (sGcRunners) {
                sTmpGcRunners = sGcRunners.toArray(sTmpGcRunners);
            }
            for (GcRun sTmpWatcher : sTmpGcRunners) {
                sTmpWatcher.doGc();
            }
            sGcWatcher = new WeakReference<InternalGcWatcher>(new InternalGcWatcher());
        }

    }


}
