package com.ideacode.apm.library.core.storage.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.entity
 * @ClassName: AppStartInfoEntity
 * @Description:    程序启动信息记录
 * @Author: randysu
 * @CreateDate: 2019-05-22 15:35
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 15:35
 * @UpdateRemark:
 * @Version: 1.0
 */

@Entity(tableName = "appstart_info")
public class AppStartInfoEntity extends BaseEntity {

    @ColumnInfo(name = "start_time", typeAffinity = ColumnInfo.INTEGER)
    private int startTime;

    public AppStartInfoEntity() {

    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
