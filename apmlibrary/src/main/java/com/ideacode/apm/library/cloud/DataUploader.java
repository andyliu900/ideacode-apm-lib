package com.ideacode.apm.library.cloud;

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
 * @Package: com.ideacode.apm.library.cloud
 * @ClassName: DataUploader
 * @Description:    数据上传管理类
 * @Author: randysu
 * @CreateDate: 2019-06-13 11:46
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 11:46
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DataUploader {

    private static final String SUB_TAG = DataUploader.class.getCanonicalName();

    private Context mContext;

    public DataUploader(Context context) {
        mContext = context;
    }

    public void createUploader() {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "DataUploader  dataUploader");
        }

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            mContext.registerReceiver(receiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyUploader() {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "DataUploader  destroyUploader");
        }

        mContext.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "upload receive action_time_click");
                }

                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - getLastTime(); // 首次运行会执行一次清理数据操作  getLastTime() == 0
                long uploadInterval = TaskConfig.REPORT_DATA_INTERVAL;
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "upload deltaTime:" + deltaTime + "  currentTime:" + currentTime + "  uploadInterval:" + uploadInterval);
                }

                // 记录时间差大于设定的时间间隔，则进行清除数据操作
                if (deltaTime >= uploadInterval) {
                    if (Manager.isDebugLog()) {
                        ApmLogX.d(APM_TAG, SUB_TAG, "start to upload db");
                    }
                    uploadData();
                    setLastTime(currentTime);
                }
            }
        }
    };

    private void setLastTime(long time) {
        PreperenceUtils.setLong(mContext, PreperenceUtils.SP_KEY_LAST_UPLOADDATA_TIME, time);
    }

    private long getLastTime() {
        return PreperenceUtils.getLong(mContext, PreperenceUtils.SP_KEY_LAST_UPLOADDATA_TIME, 0L);
    }

    private void uploadData() {
        AsyncThreadTask.execute(() -> {
            ITask activityTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_ACTIVITY);
            if (activityTask != null) {
                activityTask.upload();
            }

            ITask appStartTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_APP_START);
            if (appStartTask != null) {
                appStartTask.upload();
            }

            ITask blockTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_BLOCK);
            if (blockTask != null) {
                blockTask.upload();
            }

            ITask fpsTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_FPS);
            if (fpsTask != null) {
                fpsTask.upload();
            }

            ITask memTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_MEM);
            if (memTask != null) {
                memTask.upload();
            }

            ITask netTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_NET);
            if (netTask != null) {
                netTask.upload();
            }

            ITask processTask = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_PROCESS_INFO);
            if (processTask != null) {
                processTask.upload();
            }
        });
    }

}
