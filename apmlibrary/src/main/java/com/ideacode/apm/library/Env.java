package com.ideacode.apm.library;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library
 * @ClassName: Env
 * @Description:  APM全局配置文件
 * @Author: randysu
 * @CreateDate: 2019-05-17 11:21
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 11:21
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Env {

    public static final String VERSION = BuildConfig.VERSION;

    public static final String APM_DATABASE_NAME = BuildConfig.APM_DATABASE_NAME;

    public static final String getVersionName() {
        return VERSION;
    }

    /**
     * apm日志输出TAG
     */
    public static final String APM_TAG = "< ideacode-apm V" + getVersionName() + " >";

    /**
     * 是否将数据库生成到sdcard，方便调试
     */
    public static final boolean DB_IN_SDCARD = BuildConfig.DB_IN_SDCARD;

}
