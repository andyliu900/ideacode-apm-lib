package com.ideacode.apm.library.api;

import android.content.Context;

import com.ideacode.apm.library.core.Config;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.utils.ApmLogX;

import static com.ideacode.apm.library.Env.APM_TAG;
import static com.ideacode.apm.library.Env.getVersionName;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.api
 * @ClassName: Client
 * @Description: 外部调用接口，提供配置、初始化
 * @Author: randysu
 * @CreateDate: 2019-05-17 14:28
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 14:28
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Client {

    private static final String SUB_TAG = Client.class.getName();
    private static volatile boolean isStart = false;
    private static volatile boolean isAttached = false;

    /**
     * APM初始化配置
     *
     * @param config
     */
    public static synchronized void attach(Config config) {
        if (isAttached) {
            ApmLogX.e(APM_TAG, SUB_TAG, "attach ideacode.apm version V" + getVersionName() + " already attached.");
            return;
        }

        isAttached = true;
        ApmLogX.i(APM_TAG, SUB_TAG, "attach ideacode.apm version V" + getVersionName());
        Manager.getInstance().setConfig(config);
        Manager.getInstance().init();
    }

    public static synchronized void detach() {
        if (!isAttached) {
            ApmLogX.e(APM_TAG, SUB_TAG, "attach ideacode.apm version V" + getVersionName() + " not ready to attach.");
            return;
        }

        isAttached = false;
        Manager.getInstance().terminate();
    }

    /**
     * 启动APM任务
     */
    public static synchronized void startWork() {
        if (isStart) {
            ApmLogX.e(APM_TAG, SUB_TAG, "attach ideacode.apm version V" + getVersionName() + " already start.");
            return;
        }

        isStart = true;
        ApmLogX.d(APM_TAG, SUB_TAG, "APM开始任务:startWork");

        Manager.getInstance().startWork();
    }

    /**
     * 是否打开调试悬浮窗
     * @param isOpen
     */
    public static void setDebugWindowOpen(boolean isOpen) {

    }

    /**
     * 是否在指定的应用进程打开调试悬浮窗
     * @param isOpen
     * @param floatwinProcessName
     */
    public static void setDebugWindowOpen(boolean isOpen, String floatwinProcessName) {

    }

    /**
     * 判断某个Task是否在工作
     * @param taskName
     * @return
     */
    public static boolean isTaskRunning(String taskName) {
        return Manager.getInstance().getTaskManager().taskIsCanWork(taskName);
    }

    public static Context getContext() {
        return Manager.getContext();
    }

}
