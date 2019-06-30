package com.ideacode.apm.library.cleaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.tasks.ITask;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.AsyncThreadTask;
import com.ideacode.apm.library.utils.PreperenceUtils;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cleaner
 * @ClassName: DataCleaner
 * @Description:  数据清理类
 * @Author: randysu
 * @CreateDate: 2019-06-03 10:28
 * @UpdateUser:
 * @UpdateDate: 2019-06-03 10:28
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DataCleaner {

    private static final String SUB_TAG = DataCleaner.class.getCanonicalName();

    private Context mContext;

    public DataCleaner(Context context) {
        mContext = context;
    }

    public void createCleaner() {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "DataCleaner  createCleaner");
        }

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            mContext.registerReceiver(receiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyCleaner() {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "DataCleaner  destroyCleaner");
        }

        mContext.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "clean receive action_time_click");
                }

                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - getLastTime(); // 首次运行会执行一次清理数据操作  getLastTime() == 0
                long cleanInterval = TaskConfig.CLEAN_DATA_INTERVAL;
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "clean deltaTime:" + deltaTime + "  currentTime:" + currentTime + "  cleanInterval:" + cleanInterval);
                }

                // 记录时间差大于设定的时间间隔，则进行清除数据操作
                if (deltaTime >= cleanInterval) {
                    if (Manager.isDebugLog()) {
                        ApmLogX.d(APM_TAG, SUB_TAG, "start to clean db");
                    }
                    cleanData();
                    setLastTime(currentTime);
                }
            }
        }
    };

    private void setLastTime(long time) {
        PreperenceUtils.setLong(mContext, PreperenceUtils.SP_KEY_LAST_CLEANDATA_TIME, time);
    }

    private long getLastTime() {
        return PreperenceUtils.getLong(mContext, PreperenceUtils.SP_KEY_LAST_CLEANDATA_TIME, 0L);
    }

    private void cleanData() {
        AsyncThreadTask.execute(() -> {
            ITask activityTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_ACTIVITY);
            if (activityTask != null) {
                activityTask.clean();
            }

            ITask appStartTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_APP_START);
            if (appStartTask != null) {
                appStartTask.clean();
            }

            ITask blockTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_BLOCK);
            if (blockTask != null) {
                blockTask.clean();
            }

            ITask fpsTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_FPS);
            if (fpsTask != null) {
                fpsTask.clean();
            }

            ITask netTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_NET);
            if (netTask != null) {
                netTask.clean();
            }

            ITask processTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_PROCESS_INFO);
            if (processTask != null) {
                processTask.clean();
            }
        });
    }

}
