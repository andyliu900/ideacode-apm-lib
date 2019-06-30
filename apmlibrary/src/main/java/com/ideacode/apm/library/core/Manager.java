package com.ideacode.apm.library.core;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.cleaner.DataCleaner;
import com.ideacode.apm.library.cloud.CloudConfig;
import com.ideacode.apm.library.cloud.DataUploader;
import com.ideacode.apm.library.core.tasks.ITask;
import com.ideacode.apm.library.core.tasks.TaskManager;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.FileUtils;

import java.util.List;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core
 * @ClassName: Manager
 * @Description:  APM管理类
 * @Author: randysu
 * @CreateDate: 2019-05-17 14:38
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 14:38
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Manager {

    private static final String SUB_TAG = Manager.class.getName();

    private static Manager instance;
    private boolean mWorkFlag = false; // 判断APM是否在工作
    private Config mConfig;
    private ApmExecutor apmExecutor;
    private DataCleaner cleaner;
    private DataUploader uploader;

    public static Manager getInstance() {
        if (null == instance) {
            synchronized (Manager.class) {
                if (null == instance) {
                    instance = new Manager();
                }
            }
        }
        return instance;
    }

    private Manager() {

    }

    public TaskManager getTaskManager() {
        return TaskManager.getInstance();
    }

    public void setConfig(Config config) {
        this.mConfig = config;
    }

    public Config getConfig() {
        return mConfig;
    }

    public void init() {
        TaskManager.getInstance().registerTask();

        List<String> workTaskNames = mConfig.workTasks;
        TaskManager taskManager = getTaskManager();
        for (String workTaskName : workTaskNames) {
            taskManager.updateTaskSwitchByTaskName(workTaskName, true);
        }

        if (mConfig.isMainProcess) {
            // 启动数据清理定时任务
            cleaner = new DataCleaner(mConfig.appContext);
            cleaner.createCleaner();

            // 启动数据上报定时任务
            uploader = new DataUploader(mConfig.appContext);
            uploader.createUploader();
        }

    }

    public void terminate() {
        stopWork();
        TaskManager.getInstance().onDestroy();

        if (cleaner != null) {
            cleaner.destroyCleaner();
        }

        if (uploader != null) {
            uploader.destroyUploader();
        }
    }

    /**
     * apm开始工作
     * 要在主线程调用
     */
    public void startWork() {
        if (mWorkFlag) {
            return;
        }
        mWorkFlag = true;
        if (isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "startWork");
        }
        if (mConfig == null) {
            throw new NullPointerException("mConfig == null, please call method of Client.attach(Config config)");
        }
        if (!TaskManager.getInstance().isApmEnable()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "startWork: apm.disable");
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("startWork is must run in MainThread");
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "是否是主线程: " + (Looper.myLooper() == Looper.getMainLooper()));
        }

        // 开始任务
        apmExecutor = new ApmExecutor();
        TaskManager.getInstance().startWorkTasks();
    }

    public void stopWork() {
        TaskManager.getInstance().stopWorkTasks();
    }

    public void setTaskCanNotWork() {
        if (!TaskManager.getInstance().isApmEnable()) {
            return;
        }
        List<ITask> tasks = TaskManager.getInstance().getAllTask();
        for (ITask task : tasks) {
            task.setCanWork(false);
        }
    }

    public static boolean isDebugLog() {
        Config config = getInstance().getConfig();
        return config != null ? config.debugLog : false;
    }

    public static String getBaseUrl() {
        Config config = getInstance().getConfig();
        return config != null ? config.uploadBaseUrl : CloudConfig.IDEACODE_APM_BACKEND_BASEURL_RELEASE;
    }

    public static Context getContext() {
        Config config = getInstance().getConfig();
        return config != null ? config.appContext : null;
    }

    public static ApmExecutor getApmExecutor() {
        ApmExecutor executor = getInstance().apmExecutor;
        return executor;
    }

    public String getBaseDirPath() {
        if (TextUtils.isEmpty(FileUtils.getSDpath())) {
            return "";
        }
        return FileUtils.getSDpath() + TaskConfig.BASE_DIR_PATH;
    }

}
