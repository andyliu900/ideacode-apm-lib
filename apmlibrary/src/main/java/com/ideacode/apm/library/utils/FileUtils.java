package com.ideacode.apm.library.utils;

import android.os.Environment;

import java.io.File;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.utils
 * @ClassName: FileUtils
 * @Description:   文件、sd卡相关工具类
 * @Author: randysu
 * @CreateDate: 2019-05-21 10:39
 * @UpdateUser:
 * @UpdateDate: 2019-05-21 10:39
 * @UpdateRemark:
 * @Version: 1.0
 */
public class FileUtils {

    private static final String SUB_TAG = FileUtils.class.getName();

    /**
     * 获取sd卡根目录
     *
     * @return
     */
    public static String getSDpath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            return "";
        }

        File sdDir = Environment.getExternalStorageDirectory();
        return sdDir.getAbsolutePath();
    }

}
