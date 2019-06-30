package com.ideacode.apm.library.core.job.activity;

import android.app.Activity;

import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.job.appstart.AppStartInfo;
import com.ideacode.apm.library.core.tasks.ITask;
import com.ideacode.apm.library.core.tasks.TaskManager;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.TimeUtils;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.activity
 * @ClassName: ActivityCore
 * @Description:  Activity数据监控操作类
 * @Author: randysu
 * @CreateDate: 2019-05-17 17:39
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 17:39
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ActivityCore {

    private static final String SUB_TAG = ActivityCore.class.getName();

    public static boolean isFirst = true; // 是否第一次启动
    public static long appAttachTime = 0;
    public static int startType; // 启动类型
    public static String currentActivityName;

    public static ActivityInfo activityInfo = new ActivityInfo();

    public static void onCreateInfo(Activity activity, long startTime) {
        startType = isFirst ? ActivityInfo.COLD_START : ActivityInfo.HOT_START;
        activity.getWindow().getDecorView().post(new FirstFrameRunnable(activity, startType, startTime));
        // onCreate 时间
        long curTime = System.currentTimeMillis();
        saveActivityInfo(activity, startType, curTime - startTime, ActivityInfo.TYPE_CREATE);
    }

    public static void saveActivityInfo(Activity activity, int startType, long time, int lifeCycle) {
        if (activity == null) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "saveActivityInfo activity == null");
            }
            return;
        }

        currentActivityName = activity.getClass().getCanonicalName();

        // 生命周期时间大于100毫秒才会记录
        if (time < TaskConfig.DEFAULT_ACTIVITY_LIFECYCLE_MIN_TIME) {
            return;
        }

        String activityName = activity.getClass().getCanonicalName();
        activityInfo.resetData();
        activityInfo.activityName = activityName;
        activityInfo.startType = startType;
        activityInfo.time = time;
        activityInfo.lifeCycle = lifeCycle;
        activityInfo.appName = "";
        activityInfo.appVer = "";

        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "apmins saveActivityInfo activity:" + activity.getClass().getCanonicalName() +
                    " | lifecycle:" + ActivityInfo.getLifeCycleString(lifeCycle) + " | time:" + time + "ms");
        }

        ITask task = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_ACTIVITY);
        if (task != null) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "apmins saveActivityInfo");
            }
            task.save(activityInfo);
        } else {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "apmins saveActivityInfo task == null");
            }
        }

    }

    static class FirstFrameRunnable implements Runnable {

        private Activity activity;
        private int startType;
        private long startTime;

        public FirstFrameRunnable(Activity activity, int startType, long startTime) {
            this.activity = activity;
            this.startType = startType;
            this.startTime = startTime;
        }

        @Override
        public void run() {
            long firstFrameTime = System.currentTimeMillis() - startTime;
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "FirstFrameRunnable time:" + firstFrameTime);
            }
            if (firstFrameTime >= TaskConfig.DEFAULT_ACTIVITY_FIRST_MIN_TIME) {
                saveActivityInfo(activity, startType, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_FIRST_FRAME);
            }

            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "FirstFrameRunnable time:" + String.format("[%s, %s]", ActivityCore.isFirst,
                        TimeUtils.getFormatTime(ActivityCore.appAttachTime, TimeUtils.DATETIMESECOND_SPLIT)));
            }
            // 保存应用冷启动时间
            if (ActivityCore.isFirst) {
                ActivityCore.isFirst = false;
                if (ActivityCore.appAttachTime <= 0) {
                    return;
                }
                int t = (int)(System.currentTimeMillis() - ActivityCore.appAttachTime);
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "AppStartTime time: " + t);
                }
                AppStartInfo info = new AppStartInfo(t);
                ITask task = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_APP_START);
                if (task != null) {
                    if (Manager.isDebugLog()) {
                        ApmLogX.d(APM_TAG, SUB_TAG, "AppStartInfo saveAppStartInfo");
                    }
                    task.save(info);
                } else {
                    if (Manager.isDebugLog()) {
                        ApmLogX.d(APM_TAG, SUB_TAG, "AppStartInfo task == null");
                    }
                }
            }
        }
    }

    /**
     * activity任务是否正在工作
     *
     * @return
     */
    public static boolean isActivityTaskRunning() {
        TaskManager taskManager = Manager.getInstance().getTaskManager();
        if (taskManager == null) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "taskManager == null");
            }
            return false;
        }

        ITask task = taskManager.getTask(ApmTask.TASK_ACTIVITY);
        if (Manager.isDebugLog() && null != task) {
            ApmLogX.d(APM_TAG, SUB_TAG, "task.isRunning():" + task.isCanWork());
        }

        return null != task && task.isCanWork();
    }

}
