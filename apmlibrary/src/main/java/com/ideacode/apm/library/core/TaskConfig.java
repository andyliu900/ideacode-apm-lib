package com.ideacode.apm.library.core;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core
 * @ClassName: TaskConfig
 * @Description:    任务配置信息
 * @Author: randysu
 * @CreateDate: 2019-05-20 14:48
 * @UpdateUser:
 * @UpdateDate: 2019-05-20 14:48
 * @UpdateRemark:
 * @Version: 1.0
 */
public class TaskConfig {

    public static final String BASE_DIR_PATH = "/ideacode/Apm";

    public static final int CPU_INTERVAL = 30 * 1000; // CPU采样间隔
    public static final int FPS_INTERVAL = 1000; // FPS采样间隔
    public static final int ANR_INTERVAL = 2 * 60 * 60 * 1000; // ANR采样间隔（每隔2小时采样一次）
    public static final int ANR_VALID_TIME = 2 * 24 * 60 * 60 * 1000; // ANR文件有效期（2天）
    public static final int BATTERY_INTERVAL = 2 * 60 * 60 * 1000; // 电池采样率
    public static final int FILE_INFO_INTERVAL = 12 * 60 * 60 * 1000; // 文件采样率
    public static final int MEMORY_TRACE_INTERVAL = 1 * 60 * 60 * 1000; // 内存采样率（每隔1小时）
    public static final int TASK_DELAY_RANDOM_INTERVAL = 2 * 1000; // 定时任务启动随机延时时间

    public static final int IO_INTERVAL = 30 * 1000; // IO 轮询检查文件是否关闭
    public static final int IO_TIMEOUT_INTERVAL = 30 * 1000; // IO timeout

    public static final int DEFAULT_PROCESS_LIVE_INTERVAL = 2 * 60 * 60 * 1000; // 默认高频任务采样暂停间隔
    public static final int DEFAULT_ONCE_MAX_COUNT = 130; // 默认高频任务采样，一次最大采样数量

    public static final int FILE_MIN_SIZE = 50 * 1024; // 文件大小大于FILE_MIN_SIZE的才收集
    public static final int DEFAULT_ACTIVITY_FIRST_MIN_TIME = 300; // Activity生命周期第一帧最小数据收集时间间隔 单位：ms
    public static final int DEFAULT_ACTIVITY_LIFECYCLE_MIN_TIME = 100; // Activity生命周期最小数据收集时间间隔单位：ms
    public static final int DEFAULE_BLOCK_TIME = 4500; // block超时时间
    public static final int DEFAULT_FPS_MIN_COUNT = 30; // 最小上报帧率
    public static final int MIN_MEMORY_DELAY_TIME = 10 * 1000; // 最小内存采样延时时间
    public static final int DEFAULT_MEMORY_DELAY_TIME = 10 * 1000; // 默认内存收集延时时间（进程启动后多久开始收集内存数据）
    public static final int DEFAULT_MEMORY_INTERVAL = 30 * 60 * 1000; // 默认内存采样间隔
    public static final boolean RUN_TEST_CASE = false; // 是否进行CPU测试
    public static final int DEFAULT_FILE_DEPTH = 3; // file的默认采样文件夹层级
    public static final int MIN_THREAD_CNT_DELAYTIME = 10 * 1000; // 最小线程采样延时时间
    public static final int DEFAULT_THREAD_CNT_DELAY_TIME = 10 * 1000; // 线程采样的默认延时时间
    public static final int DEFAULT_THREAD_CNT_INTERVAL_TIME = 30 * 60 * 1000; // 线程数采集的默认间隔时间

    public static final long RANDOM_CONTROL_TIME = 10 * 60 * 1000; // 请求云控的随机时间
    public static final long MAX_READ_FILE_SIZE = 50 * 1024 * 1024; // 文件读取最大限制阀值
    public static final int DEFAULT_WATCH_DOG_DELAY_TIME = 10 * 1000; //watchDog采集默认延迟时间
    public static final int DEFAULT_WATCH_DOG_INTERVAL_TIME = 5 * 1000; //watchDog采集默认间隔时间
    public static final int DEFAULT_WATCH_DOG_MIN_TIME = 4500; //最小上报单帧耗时

    public static final long REPORT_DATA_INTERVAL = 30 * 60 * 1000; // 上报数据间隔  30分钟上报一次
    public static final long CLEAN_DATA_INTERVAL = 24 * 60 * 60 * 1000; // 清理数据间隔24小时
    public static final int CLEAN_DATA_NUM = 10; // 每次清除时间最靠前的10条记录

}
