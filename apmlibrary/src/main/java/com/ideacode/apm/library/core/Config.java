package com.ideacode.apm.library.core;

import android.content.Context;

import com.ideacode.apm.library.Env;
import com.ideacode.apm.library.cloud.CloudConfig;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.ProcessUtils;

import java.util.List;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core
 * @ClassName: Config
 * @Description: 启动配置项
 * @Author: randysu
 * @CreateDate: 2019-05-17 11:33
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 11:33
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Config {

    private static final String SUB_TAG = Config.class.getName();

    public Context appContext;
    public String appName = "";
    public String appVersion = "";
    public boolean isMainProcess = true;
    public boolean debugLog = false;
    public String uploadBaseUrl = CloudConfig.IDEACODE_APM_BACKEND_BASEURL_RELEASE;
    public int localFlags = 0xffffffff;
    public List<String> workTasks;

    public Config() {

    }

    public boolean isEnabled(int flag) {
        return (localFlags & flag) == flag;
    }

    @Override
    public String toString() {
        return "apm config : appContext:" + appContext.toString() +
                " appName:" + appName.toString() +
                " appVersion:" + appVersion.toString() +
                " proc: " + ProcessUtils.getCurrentProcessName();
    }

    public static class ConfigBuilder {
        private Config config = new Config();

        public ConfigBuilder setAppContext(Context context) {
            this.config.appContext = context;
            return this;
        }

        public ConfigBuilder setAppName(String appName) {
            this.config.appName = appName;
            return this;
        }

        public ConfigBuilder setAppVersion(String appVersion) {
            this.config.appVersion = appVersion;
            return this;
        }

        public ConfigBuilder setEnabled(int flag) {
            this.config.localFlags |= flag;  // 按位或
            return this;
        }

        public ConfigBuilder setDisabled(int flag) {
            this.config.localFlags &= (~flag);  // 按位与   ~取反
            return this;
        }

        public ConfigBuilder setWorkTasks(List<String> workTaskNames) {
            this.config.workTasks = workTaskNames;
            return this;
        }

        public ConfigBuilder setIsMainProcess(boolean isMainProcess) {
            this.config.isMainProcess = isMainProcess;
            return this;
        }

        public ConfigBuilder setDebugLog(boolean debugLog) {
            this.config.debugLog = debugLog;
            return this;
        }

        public ConfigBuilder setUploadBaseUrl(String uploadBaseUrl) {
            this.config.uploadBaseUrl = uploadBaseUrl;
            return this;
        }

        public Config build() {
            ApmLogX.d(Env.APM_TAG, SUB_TAG, config.toString());

            return config;
        }

    }
}
