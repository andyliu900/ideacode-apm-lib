package com.ideacode.apm.library.api;

import java.util.HashMap;
import java.util.Map;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.api
 * @ClassName: ApmTask
 * @Description:  模块、任务名称等相关配置
 * @Author: randysu
 * @CreateDate: 2019-05-17 15:59
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 15:59
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ApmTask {

    /******************  APM 任务名称  ********************/
    public static final String TASK_ACTIVITY = "activity";
    public static final String TASK_NET = "net";
    public static final String TASK_MEM = "memory";
    public static final String TASK_FPS = "fps";
    public static final String TASK_APP_START = "appstart";
    public static final String TASK_FILE = "file";
    public static final String TASK_ANR = "anr";
    public static final String TASK_PROCESS_INFO = "processinfo";
    public static final String TASK_BLOCK = "block";
    public static final String TASK_WATCHDOG = "watchdog";
    public static final String TASK_FUNC = "func";
    public static final String TASK_WEBVIEW = "webview";

    /*** 以下常量为进程启动时，各个模块是否生效的开关。多进程应用在初始化时需要进行配置 ***/
    // 本地数据清理开关
    public static final int FLAG_DATA_CLEAN = 0x001;
    // 数据上传开关
    public static final int FLAG_DATA_UPLOAD = 0x002;
    // 本地Debug调试开关
    public static final int FLAG_LOCAL_DEBUG = 0x003;
    //以下为任务采集模块开关
    public static final int FLAG_COLLECT_FPS = 0x00000020;
    public static final int FLAG_COLLECT_APPSTART = 0x00000100;
    public static final int FLAG_COLLECT_ACTIVITY = 0x00000200;
    public static final int FLAG_COLLECT_MEM = 0x00000400;
    public static final int FLAG_COLLECT_NET = 0x00001000;
    public static final int FLAG_COLLECT_FILE_INFO = 0x00002000;
    public static final int FLAG_COLLECT_ANR = 0x00004000;
    public static final int FLAG_COLLECT_ACTIVITY_AOP = 0x00008000;
    public static final int FLAG_COLLECT_ACTIVITY_INSTRUMENTATION = 0x00010000;
    public static final int FLAG_COLLECT_PROCESS_INFO = 0x00020000;
    public static final int FLAG_COLLECT_FUNC = 0x00080000;
    public static final int FLAG_COLLECT_BLOCK = 0x00100000;
    public static final int FLAG_COLLECT_WEBVIEW = 0x00400000;
    public static final int FLAG_COLLECT_WATCHDOG = 0x00800000;

    // 每个任务模块为key，模块开关为value
    private static Map<String, Integer> sTaskMap;

    public static Map<String, Integer> getTaskMap() {
        if (sTaskMap == null) {
            sTaskMap = new HashMap<>();
            sTaskMap.put(TASK_ACTIVITY, FLAG_COLLECT_ACTIVITY);
            sTaskMap.put(TASK_NET, FLAG_COLLECT_NET);
            sTaskMap.put(TASK_MEM, FLAG_COLLECT_MEM);
            sTaskMap.put(TASK_FPS, FLAG_COLLECT_FPS);
            sTaskMap.put(TASK_APP_START, FLAG_COLLECT_APPSTART);
            sTaskMap.put(TASK_FILE, FLAG_COLLECT_FILE_INFO);
            sTaskMap.put(TASK_ANR, FLAG_COLLECT_ANR);
            sTaskMap.put(TASK_PROCESS_INFO, FLAG_COLLECT_PROCESS_INFO);
            sTaskMap.put(TASK_BLOCK, FLAG_COLLECT_BLOCK);
            sTaskMap.put(TASK_WATCHDOG, FLAG_COLLECT_WATCHDOG);
            sTaskMap.put(TASK_FUNC, FLAG_COLLECT_FUNC);
            sTaskMap.put(TASK_WEBVIEW, FLAG_COLLECT_WEBVIEW);
        }
        return sTaskMap;
    }

}
