package com.ideacode.apm.library.core.tasks;

import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.SystemUtils;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.tasks
 * @ClassName: BaseTask
 * @Description: APM任务基类
 * @Author: randysu
 * @CreateDate: 2019-05-17 15:19
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 15:19
 * @UpdateRemark:
 * @Version: 1.0
 */
public abstract class BaseTask<T> implements ITask<T> {

    public static final String SUB_TAG = BaseTask.class.getName();

    protected boolean mIsCanWork = false;

    public BaseTask() {

    }

    @Override
    public void start() {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "start task:" + getTaskName());
        }
    }

    @Override
    public boolean isCanWork() {
        return mIsCanWork;
    }

    @Override
    public void setCanWork(boolean isCanWork) {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "setCanWork task :" + getTaskName() + " :" + isCanWork);
        }
        mIsCanWork = isCanWork;
    }

    @Override
    public void save(T t) {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "save task : " + getTaskName());
        }
    }

    @Override
    public void stop() {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "stop task:" + getTaskName());
        }
    }

    @Override
    public void clean() {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "clean task:" + getTaskName());
        }
    }

    @Override
    public void upload() {
        if (!SystemUtils.isNetWorkConnect()) {
            return;
        }

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "upload task:" + getTaskName());
        }
    }
}
