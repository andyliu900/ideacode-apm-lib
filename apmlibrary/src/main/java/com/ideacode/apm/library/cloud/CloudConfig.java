package com.ideacode.apm.library.cloud;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud
 * @ClassName: CloudConfig
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-06-13 10:09
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 10:09
 * @UpdateRemark:
 * @Version: 1.0
 */
public class CloudConfig {

    public static final String API_SIGN_KEY = "1d50a7f5b275";

    public static final int DEFAULT_TIME_OUT = 30; // 网络超时时间，30秒
    public static final int DEFAULT_READ_WRITE_TIME_OUT = 10; // 读写超时时间，10秒

    public static final String IDEACODE_APM_BACKEND_BASEURL_RELEASE = "https://www.baidu.com";   // 正式环境
    public static final String IDEACODE_APM_BACKEND_BASEURL_TEST = "https://www.baidu.com"; // 测试环境
    public static final String IDEACODE_APM_DATA = "/acquisition/app/apm-data";

    // EVENT_KEY
    public static final String ACTIVITY_EVENT_KEY = "activity_info";
    public static final String APPSTART_EVENT_KEY = "appstart_info";
    public static final String BLOCK_EVENT_KEY = "block_info";
    public static final String FPS_EVENT_KEY = "fps_info";
    public static final String MEMORY_EVENT_KEY = "memory_info";
    public static final String NET_EVENT_KEY = "net_info";
    public static final String PROCESS_EVENT_KEY = "process_info";

}
