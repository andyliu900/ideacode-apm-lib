package com.ideacode.apm;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.api.Client;
import com.ideacode.apm.library.cloud.CloudConfig;
import com.ideacode.apm.library.core.Config;
import com.ideacode.apm.library.utils.PackageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ApmLib
 * @Package: com.ideacode.apm
 * @ClassName: MyApplication
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-06-30 22:50
 * @UpdateUser:
 * @UpdateDate: 2019-06-30 22:50
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        initApm();
    }

    private void initApm() {
        Config.ConfigBuilder builder = new Config.ConfigBuilder()
                .setAppContext(this)
                .setAppName(PackageUtil.getVersionName(this))
                .setAppVersion(String.valueOf(PackageUtil.getVersionCode(this)))
                .setIsMainProcess(isMainProcess());

        // 指定使用hook形式进行Activity生命周期拦截
        builder.setEnabled(ApmTask.FLAG_COLLECT_ACTIVITY_INSTRUMENTATION);

        // 配置需要监控的系统组件
        List<String> workTaskNames = new ArrayList();
        if (isMainProcess()) {
            workTaskNames.add(ApmTask.TASK_ACTIVITY);
            workTaskNames.add(ApmTask.TASK_NET);
            workTaskNames.add(ApmTask.TASK_MEM);
            workTaskNames.add(ApmTask.TASK_FPS);
            workTaskNames.add(ApmTask.TASK_APP_START);
            workTaskNames.add(ApmTask.TASK_BLOCK);
        }
        workTaskNames.add(ApmTask.TASK_PROCESS_INFO);

        builder.setWorkTasks(workTaskNames);
//        builder.setUploadBaseUrl(CloudConfig.VIOMI_APM_BACKEND_BASEURL_RELEASE);
        builder.setDebugLog(true);

        Client.attach(builder.build());
        Client.setDebugWindowOpen(true);
        Client.startWork();
    }

    private boolean isMainProcess() {
        String mainProcessName = getPackageName();
        String processName = getCurrentProcessName();
        return TextUtils.equals(processName, mainProcessName);
    }

    private String getCurrentProcessName() {
        int pid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

}
