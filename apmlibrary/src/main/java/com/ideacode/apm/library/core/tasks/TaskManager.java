package com.ideacode.apm.library.core.tasks;

import android.os.Build;
import android.text.TextUtils;

import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.job.activity.ActivityTask;
import com.ideacode.apm.library.core.job.activity.InstrumentationHooker;
import com.ideacode.apm.library.core.job.appstart.AppStartTask;
import com.ideacode.apm.library.core.job.block.BlockTask;
import com.ideacode.apm.library.core.job.fps.FpsTask;
import com.ideacode.apm.library.core.job.memory.MemoryTask;
import com.ideacode.apm.library.core.job.net.NetTask;
import com.ideacode.apm.library.core.job.processinfo.ProcessInfoTask;
import com.ideacode.apm.library.utils.ApmLogX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.tasks
 * @ClassName: TaskManager
 * @Description:  任务管理类
 * @Author: randysu
 * @CreateDate: 2019-05-17 15:32
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 15:32
 * @UpdateRemark:
 * @Version: 1.0
 */
public class TaskManager {

    private final String SUB_TAG = TaskManager.class.getName();

    private static TaskManager instance;
    private Map<String, ITask> taskMap;

    public static TaskManager getInstance() {
        if (null == instance) {
            synchronized (TaskManager.class) {
                if (null == instance) {
                    instance = new TaskManager();
                }
            }
        }
        return instance;
    }

    private TaskManager() {
        taskMap = new HashMap<>();
    }

    public void onDestroy() {
        if (null != taskMap) {
            taskMap.clear();
            taskMap = null;
        }
    }

    /**
     * 获取所有任务
     * @return
     */
    public List<ITask> getAllTask() {
        List<ITask> taskList = new ArrayList<>();
        if (null == taskMap) {
            return null;
        }

        for (Map.Entry<String, ITask> entry : taskMap.entrySet()) {
            taskList.add(entry.getValue());
        }

        return taskList;
    }

    /**
     * 通过任务名称获取任务
     *
     * @param taskName
     * @return
     */
    public ITask getTask(String taskName) {
        if (TextUtils.isEmpty(taskName)) {
            return null;
        }

        return null == taskMap ? null : taskMap.get(taskName);
    }

    /**
     * 通过任务名称更新任务开关
     * @param taskName
     * @param value
     */
    public void updateTaskSwitchByTaskName(String taskName, boolean value) {
        if (TextUtils.isEmpty(taskName) || (taskMap.get(taskName) == null)) {
            return;
        }

        if (Manager.getInstance().getConfig().isEnabled(ApmTask.getTaskMap().get(taskName)) && value) {
            taskMap.get(taskName).setCanWork(true);
        } else {
            taskMap.get(taskName).setCanWork(false);
        }
    }

    /**
     * 根据任务名称查询当前任务是否正在运行
     * @param taskName
     * @return
     */
    public boolean taskIsCanWork(String taskName) {
        synchronized (this) {
            if (TextUtils.isEmpty(taskName)) {
                return false;
            }
            if (taskMap.get(taskName) == null) {
                return false;
            }
            return taskMap.get(taskName).isCanWork();
        }
    }

    /**
     * APM是否正在运行
     * @return
     */
    public boolean isApmEnable() {
        if (taskMap == null) {
            ApmLogX.d(APM_TAG, SUB_TAG, "taskMap is null");
            return false;
        }

        for (ITask task : taskMap.values()) {
            if (task.isCanWork()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 开始任务
     */
    public void startWorkTasks() {
        if (taskMap == null) {
            ApmLogX.d(APM_TAG, SUB_TAG, "taskMap is null");
            return;
        }

        if (taskMap.get(ApmTask.TASK_ACTIVITY) != null &&
                taskMap.get(ApmTask.TASK_ACTIVITY).isCanWork()) {
            if (Manager.getInstance().getConfig().isEnabled(ApmTask.FLAG_COLLECT_ACTIVITY_INSTRUMENTATION)) {
                ApmLogX.d(APM_TAG, SUB_TAG, "activity local INSTRUMENTATION");
                InstrumentationHooker.doHook();
            } else {
                ApmLogX.d(APM_TAG, SUB_TAG, "activity local aop");
            }
        }

        List<ITask> taskList = getAllTask();
        for (ITask task : taskList) {
            if (!task.isCanWork()) {
                continue;
            }
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "start task " + task.getTaskName());
            }
            task.start();
        }
    }

    /**
     * 关闭所有任务
     */
    public void stopWorkTasks() {
        List<ITask> taskList = getAllTask();
        for (ITask task : taskList) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "stop task " + task.getTaskName());
            }
            task.stop();
        }
    }

    /**
     * 注册tasks
     */
    public void registerTask() {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "registerTask");
        }

        if (Build.VERSION.SDK_INT >= 16) {
            taskMap.put(ApmTask.TASK_FPS, new FpsTask());
        }
        taskMap.put(ApmTask.TASK_MEM, new MemoryTask());
        taskMap.put(ApmTask.TASK_ACTIVITY, new ActivityTask());
        taskMap.put(ApmTask.TASK_NET, new NetTask());
        taskMap.put(ApmTask.TASK_APP_START, new AppStartTask());
        taskMap.put(ApmTask.TASK_PROCESS_INFO, new ProcessInfoTask());
        taskMap.put(ApmTask.TASK_BLOCK, new BlockTask());
//        taskMap.put(ApmTask.TASK_ANR, new AnrLoopTask(Manager.getContext()));
//        taskMap.put(ApmTask.TASK_FILE, new FileInfoTask());
//        taskMap.put(ApmTask.TASK_WATCHDOG, new WatchDogTask());
    }

}
