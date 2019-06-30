package com.ideacode.apm.library.core.storage.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.entity
 * @ClassName: FpsInfoEntity
 * @Description:    刷新帧率信息记录
 * @Author: randysu
 * @CreateDate: 2019-05-28 09:28
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 09:28
 * @UpdateRemark:
 * @Version: 1.0
 */

@Entity(tableName = "fps_info")
public class FpsInfoEntity extends BaseEntity {

    @ColumnInfo(name = "activity_name", typeAffinity = ColumnInfo.TEXT)
    private String activityName;

    @ColumnInfo(name = "fps", typeAffinity = ColumnInfo.INTEGER)
    private int fps;

    @ColumnInfo(name = "stack", typeAffinity = ColumnInfo.TEXT)
    private String stack;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
