#  APP客户端APM性能监控组件

## 目前能监控的功能包括
    1、程序冷启动启动进入首帧耗时
    2、监控程序启动的进程信息
    3、Activity生命周期耗时统计，只记录耗时大于300毫秒的数据
    4、每隔1小时监控内存使用情况
    5、监控基于OKHttp框架的网络过程信息，包括上行数据、下行数据、过程耗时等
    6、监控刷新帧率
    7、监控卡顿信息，界面冻结超过4.5秒后触发

以上监控功能可按需配置开放，目前只开放控制台日志展示监控信息功能

# 使用方法：

**1、app的gradle中配置脚本：**
```
implementation project(path: ':apmlibrary')
```
**2、自定义Application中「attachBaseContext」方法**
```
        Config.ConfigBuilder builder = new Config.ConfigBuilder()
                .setAppContext(this)
                .setAppName(PackageUtil.getVersionName(this))
                .setAppVersion(String.valueOf(PackageUtil.getVersionCode(this)));

        // 指定使用hook形式进行Activity生命周期拦截
        builder.setEnabled(ApmTask.FLAG_COLLECT_ACTIVITY_INSTRUMENTATION);

        // 配置需要监控的系统组件
        List<String> workTaskNames = new ArrayList();
        if (isMainProcess()) { 
           workTaskNames.add(ApmTask.TASK_ACTIVITY);
            workTaskNames.add(ApmTask.TASK_NET);
            workTaskNames.add(ApmTask.TASK_MEM);
           // workTaskNames.add(ApmTask.TASK_FPS);  // 按需开启或关闭特定监控功能
            workTaskNames.add(ApmTask.TASK_APP_START);
           // workTaskNames.add(ApmTask.TASK_BLOCK);  // 按需开启或关闭特定监控功能
        }
		// 进程监控要独立设置
        workTaskNames.add(ApmTask.TASK_PROCESS_INFO);
        builder.setWorkTasks(workTaskNames);

        Client.attach(builder.build());
        Client.startWork();
```

**3、配置OKHttp拦截器**
```
OkHttpClient  client = new OkHttpClient.Builder()
.addInterceptor(new ViomiApmNetInterceptor())
.build();
```

**4、在控制台查看日志输出**

过滤关键字  <abbr title="Hyper Text Markup Language">ideacode-apm</abbr>，查看具体输出日志内容
```
2019-06-30 23:30:09.975 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.Config  ❖  apm config : appContext:com.ideacode.apm.MyApplication@4a45dbc appName:1.0 appVersion:1 proc: com.ideacode.apm
2019-06-30 23:30:09.976 21687-21687/com.ideacode.apm I/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.api.Client  ❖  attach ideacode.apm version V1.0.1.5
2019-06-30 23:30:09.977 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.tasks.TaskManager  ❖  registerTask
2019-06-30 23:30:09.987 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.tasks.BaseTask  ❖  setCanWork task :activity :true
2019-06-30 23:30:09.987 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.tasks.BaseTask  ❖  setCanWork task :net :true
2019-06-30 23:30:09.988 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.tasks.BaseTask  ❖  setCanWork task :memory :true
2019-06-30 23:30:09.988 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.tasks.BaseTask  ❖  setCanWork task :fps :true
2019-06-30 23:30:09.989 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.tasks.BaseTask  ❖  setCanWork task :appstart :true
2019-06-30 23:30:09.990 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.tasks.BaseTask  ❖  setCanWork task :block :true
2019-06-30 23:30:09.990 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.core.tasks.BaseTask  ❖  setCanWork task :processinfo :true
2019-06-30 23:30:09.991 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.cleaner.DataCleaner  ❖  DataCleaner  createCleaner
2019-06-30 23:30:09.993 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.cloud.DataUploader  ❖  DataUploader  dataUploader
2019-06-30 23:30:09.995 21687-21687/com.ideacode.apm D/< ideacode-apm V1.0.1.5 >: ❖ com.ideacode.apm/com.ideacode.apm.library.api.Client  ❖  APM开始任务:startWork
......

apmins saveActivityInfo:com.ideacode.apm.MainActivity activity | lifecycle:onCreate | time:2252ms

```

**2019/6/10 更新**

1、更新数据清理逻辑，默认每隔24小时清理已经上传并且生成时间前10的记录

2、自定义Application中「onTerminate」方法
```
@Override
public void onTerminate() {
    super.onTerminate();
    Client.detach(); // 增加退出程序清理资源逻辑
}
```

**2019/6/14 更新**

1、增加数据上报逻辑，每隔半小时上报一次已经保存并且未上传的记录

2、自定义Application中Builder调整
```
Config.ConfigBuilder builder = new Config.ConfigBuilder()
                ...
                ...
                .setIsMainProcess(isMainProcess());
```

**2019/6/28 更新**

builder增加代码设置上报baseUrl方法，默认是正式环境

builder增加代码控制是否打开debug模式，默认是关闭(false)
```
// CloudConfig.IDEACODE_APM_BACKEND_BASEURL_RELEASE  正式环境
// CloudConfig.IDEACODE_APM_BACKEND_BASEURL_TEST     测试环境
builder.setUploadBaseUrl(CloudConfig.IDEACODE_APM_BACKEND_BASEURL_RELEASE);  
builder.setDebugLog(true);
```

