package com.ideacode.apm.library.utils;

import android.os.Looper;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.utils
 * @ClassName: CommonUtils
 * @Description:    公共工具类
 * @Author: randysu
 * @CreateDate: 2019-05-28 10:06
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 10:06
 * @UpdateRemark:
 * @Version: 1.0
 */
public class CommonUtils {

    public static String getStack() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] traceElements = Looper.getMainLooper().getThread().getStackTrace();
        for (StackTraceElement element : traceElements) {
            sb.append(element.toString() + "\n");
        }

        return sb.toString();
    }

}
