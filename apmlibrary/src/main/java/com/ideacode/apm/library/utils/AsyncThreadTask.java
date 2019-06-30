package com.ideacode.apm.library.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.utils
 * @ClassName: AsyncThreadTask
 * @Description:    异步线程池
 * @Author: randysu
 * @CreateDate: 2019-05-23 14:28
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 14:28
 * @UpdateRemark:
 * @Version: 1.0
 */
public class AsyncThreadTask {

    private static final String SUB_TAG = AsyncThreadTask.class.getCanonicalName();

    private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private final int CORE_THREAD_COUNT = CPU_COUNT + 3; // 核心线程数
    private final int MAX_THREAD_COUNT = CPU_COUNT + 3 + 3; // 最大线程数
    private final int KEEP_ALIVE = 3; // 空线程alive时间，3秒
    private ExecutorService executorService; // 线程池

    private static final ThreadFactory threadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "AsyncThreadTask # " + mCount.getAndIncrement()); // 线程名称每次加1
            return thread;
        }
    };

    private static AsyncThreadTask instance;

    public static AsyncThreadTask getInstance() {
        if (instance == null) {
            synchronized (AsyncThreadTask.class) {
                if (instance == null) {
                    instance = new AsyncThreadTask();
                }
            }
        }

        return instance;
    }

    private AsyncThreadTask() {
        executorService = new ThreadPoolExecutor(CORE_THREAD_COUNT, MAX_THREAD_COUNT, KEEP_ALIVE, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10000),
                threadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    private void executeRunnable(Runnable runnable) {
        executorService.execute(runnable);
    }

    private void executeRunnableDelayed(Runnable runnable, long delayedTime) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                executorService.execute(runnable);
            }
        }, delayedTime);
    }

    private void executeRunnableDelayedToUI(Runnable runnable, long delayedTime) {
        getHandler().postDelayed(runnable, delayedTime);
    }

    /***********    对外公布的方法    ************/
    public static void execute(Runnable runnable) {
        AsyncThreadTask.getInstance().executeRunnable(runnable);
    }

    public static void executeDelayed(Runnable runnable, long delayedTime) {
        AsyncThreadTask.getInstance().executeRunnableDelayed(runnable, delayedTime);
    }

    public void executeToUI(Runnable runnable) {
        getHandler().post(runnable);
    }

    public void executeDelayedToUI(Runnable runnable, long delayedTime) {
        getHandler().postDelayed(runnable, delayedTime);
    }

    /*************   切换UI线程   ************/
    private InternalHandler handler;

    private Handler getHandler() {
        synchronized (this) {
            if (handler == null) {
                handler = new InternalHandler();
            }
        }
        return handler;
    }

    private static class InternalHandler extends Handler {

        public InternalHandler() {
            super(Looper.getMainLooper());
        }

    }

}
