package com.closedevice.http.worker;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liudongdong
 * @date 12.12
 */

public class NetWorkExecutor implements Executor {
  private static final int CPU_CORE = 2;
  private static final int ALIVE_TIME = 5;
  private static ThreadPoolExecutor poolExecutor;
  private ThreadFactory theadFactory = new ThreadFactory() {
    AtomicInteger id = new AtomicInteger(1);

    @Override public Thread newThread(@NonNull Runnable r) {
      return new Thread(r, String.format("akit-thread : %s", id.getAndIncrement()));
    }
  };

  public NetWorkExecutor() {
    initExecutor();
  }

  private void initExecutor() {
    if (poolExecutor == null) {
      new ThreadPoolExecutor(CPU_CORE, ALIVE_TIME, Integer.MAX_VALUE, TimeUnit.SECONDS,
          new SynchronousQueue<Runnable>(), theadFactory, new ThreadPoolExecutor.DiscardPolicy());
    }
  }

  @Override public void execute(@NonNull final Runnable command) {

  }
}
