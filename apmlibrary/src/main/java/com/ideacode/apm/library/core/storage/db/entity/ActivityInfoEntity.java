package com.ideacode.apm.library.core.storage.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.entity
 * @ClassName: ActivityInfoEntity
 * @Description:    Activity数据监控记录
 * @Author: randysu
 * @CreateDate: 2019-05-22 10:40
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 10:40
 * @UpdateRemark:
 * @Version: 1.0
 */

@Entity(tableName = "activity_info")
public class ActivityInfoEntity extends BaseEntity {

    @ColumnInfo(name = "activity_name", typeAffinity = ColumnInfo.TEXT)
    private String activityName;

    @ColumnInfo(name = "start_type", typeAffinity = ColumnInfo.INTEGER)
    private int startType;

    @ColumnInfo(name = "time", typeAffinity = ColumnInfo.INTEGER)
    private long time;

    @ColumnInfo(name = "life_cycle", typeAffinity = ColumnInfo.INTEGER)
    private int lifeCycle;

    @ColumnInfo(name = "life_cycle_str", typeAffinity = ColumnInfo.TEXT)
    private String lifeCycleStr;

    public ActivityInfoEntity() {

    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getStartType() {
        return startType;
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(int lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getLifeCycleStr() {
        return lifeCycleStr;
    }

    public void setLifeCycleStr(String lifeCycleStr) {
        this.lifeCycleStr = lifeCycleStr;
    }
}
