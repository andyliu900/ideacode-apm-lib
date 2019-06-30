package com.ideacode.apm.library.core.storage.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.entity
 * @ClassName: ProcessInfoEntity
 * @Description:    程序进程信息记录
 * @Author: randysu
 * @CreateDate: 2019-05-23 15:13
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 15:13
 * @UpdateRemark:
 * @Version: 1.0
 */

@Entity(tableName = "process_info")
public class ProcessInfoEntity extends BaseEntity {

    @ColumnInfo(name = "start_count", typeAffinity = ColumnInfo.INTEGER)
    private int startCount;

    public int getStartCount() {
        return startCount;
    }

    public void setStartCount(int startCount) {
        this.startCount = startCount;
    }
}
