package com.ideacode.apm.library.core.storage.db.converter;


import androidx.room.TypeConverter;

import java.util.Date;

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
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestmap(Date date) {
        return date == null ? null : date.getTime();
    }

}
