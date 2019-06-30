package com.ideacode.apm.library;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db
 * @ClassName: ApmExecutor
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-05-21 16:11
 * @UpdateUser:
 * @UpdateDate: 2019-05-21 16:11
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ApmExecutor {

    private static final String SUB_TAG = ApmExecutor.class.getCanonicalName();

    private final ExecutorService mDiskIO;

    private final ExecutorService mNetworkIO;

    private final Executor mMainThread;

    public ApmExecutor() {
        mDiskIO = Executors.newSingleThreadExecutor();
        mNetworkIO = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        mMainThread = new MainThreadExecutor();
    }

    public ExecutorService diskIO() {
        return mDiskIO;
    }

    public ExecutorService netWorkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
