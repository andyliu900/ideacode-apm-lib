package com.ideacode.apm.library.core.job.activity;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.os.Bundle;

import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.utils.ApmLogX;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.activity
 * @ClassName: ApmInstrumentation
 * @Description:  自定义Instrumentation，监听Activity各个阶段耗时信息
 * @Author: randysu
 * @CreateDate: 2019-05-17 17:29
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 17:29
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ApmInstrumentation extends Instrumentation {

    private static final String SUB_TAG = ApmInstrumentation.class.getName();

    private Instrumentation mOldInstrumentation = null;

    public ApmInstrumentation(Instrumentation oldInstrumentation) {
        if (oldInstrumentation instanceof Instrumentation) {
            mOldInstrumentation = oldInstrumentation;
        }
    }

    @Override
    public void callApplicationOnCreate(Application app) {
        ActivityCore.appAttachTime = System.currentTimeMillis();
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callApplicationOnCreate time:" + ActivityCore.appAttachTime);
        }

        if (mOldInstrumentation != null) {
            mOldInstrumentation.callApplicationOnCreate(app);
        } else {
            super.callApplicationOnCreate(app);
        }
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        // activity任务没有运行，执行默认逻辑
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnCreate(activity, icicle);
            } else {
                super.callActivityOnCreate(activity, icicle);
            }
            return;
        }

        // activity任务正在运行，执行Activity信息记录
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnCreate");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnCreate(activity, icicle);
        } else {
            super.callActivityOnCreate(activity, icicle);
        }

        ActivityCore.startType = ActivityCore.isFirst ? ActivityInfo.COLD_START : ActivityInfo.HOT_START;
        ActivityCore.onCreateInfo(activity, startTime);
    }

    @Override
    public void callActivityOnStart(Activity activity) {
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnStart(activity);
            } else {
                super.callActivityOnStart(activity);
            }
            return;
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnStart");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnStart(activity);
        } else {
            super.callActivityOnStart(activity);
        }
        ActivityCore.saveActivityInfo(activity, ActivityInfo.HOT_START, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_START);
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnResume(activity);
            } else {
                super.callActivityOnResume(activity);
            }
            return;
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnResume");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnResume(activity);
        } else {
            super.callActivityOnResume(activity);
        }
        ActivityCore.saveActivityInfo(activity, ActivityInfo.HOT_START, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_RESUME);
    }

    @Override
    public void callActivityOnPause(Activity activity) {
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnPause(activity);
            } else {
                super.callActivityOnPause(activity);
            }
            return;
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnPause");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnPause(activity);
        } else {
            super.callActivityOnPause(activity);
        }
        ActivityCore.saveActivityInfo(activity, ActivityInfo.HOT_START, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_PAUSE);
    }

    @Override
    public void callActivityOnStop(Activity activity) {
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnStop(activity);
            } else {
                super.callActivityOnStop(activity);
            }
            return;
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnStop");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnStop(activity);
        } else {
            super.callActivityOnStop(activity);
        }
        ActivityCore.saveActivityInfo(activity, ActivityInfo.HOT_START, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_STOP);
    }

    @Override
    public void callActivityOnDestroy(Activity activity) {
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnDestroy(activity);
            } else {
                super.callActivityOnDestroy(activity);
            }
            return;
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnDestroy");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnDestroy(activity);
        } else {
            super.callActivityOnDestroy(activity);
        }
        ActivityCore.saveActivityInfo(activity, ActivityInfo.HOT_START, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_DESTROY);
    }

    @Override
    public void callActivityOnRestart(Activity activity) {
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnRestart(activity);
            } else {
                super.callActivityOnRestart(activity);
            }
            return;
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnRestart");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnRestart(activity);
        } else {
            super.callActivityOnRestart(activity);
        }
        ActivityCore.saveActivityInfo(activity, ActivityInfo.HOT_START, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_RESTART);
    }

    @Override
    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnSaveInstanceState(activity, outState);
            } else {
                super.callActivityOnSaveInstanceState(activity, outState);
            }
            return;
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnSaveInstanceState");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnSaveInstanceState(activity, outState);
        } else {
            super.callActivityOnSaveInstanceState(activity, outState);
        }
        ActivityCore.saveActivityInfo(activity, ActivityInfo.HOT_START, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_SAVEINSTANCESTATE);
    }

    @Override
    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        if (!isActivityTaskRunning()) {
            if (mOldInstrumentation != null) {
                mOldInstrumentation.callActivityOnRestoreInstanceState(activity, savedInstanceState);
            } else {
                super.callActivityOnRestoreInstanceState(activity, savedInstanceState);
            }
            return;
        }
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "callActivityOnRestoreInstanceState");
        }
        long startTime = System.currentTimeMillis();
        if (mOldInstrumentation != null) {
            mOldInstrumentation.callActivityOnRestoreInstanceState(activity, savedInstanceState);
        } else {
            super.callActivityOnRestoreInstanceState(activity, savedInstanceState);
        }
        ActivityCore.saveActivityInfo(activity, ActivityInfo.HOT_START, System.currentTimeMillis() - startTime, ActivityInfo.TYPE_RESTOREINSTANCESTATE);
    }

    private boolean isActivityTaskRunning() {
        boolean useInstrumation = Manager.getInstance().getConfig().isEnabled(ApmTask.FLAG_COLLECT_ACTIVITY_INSTRUMENTATION);
        return useInstrumation && ActivityCore.isActivityTaskRunning();
    }
}
