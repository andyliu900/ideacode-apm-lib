package com.ideacode.apm.library.core.storage.db.converter;


import androidx.room.TypeConverter;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.converter
 * @ClassName: DateConverter
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-05-22 10:33
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 10:33
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BooleanConverter {

    @TypeConverter
    public static Boolean toBoolean(int flag) {
        return flag == 0 ? false : true;
    }

    @TypeConverter
    public static int toInt(Boolean flag) {
        return flag == false ? 0 : 1;
    }

}
