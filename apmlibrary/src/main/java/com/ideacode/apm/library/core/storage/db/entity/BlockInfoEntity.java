package com.ideacode.apm.library.core.storage.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.entity
 * @ClassName: BlockInfoEntity
 * @Description:   卡顿信息记录
 * @Author: randysu
 * @CreateDate: 2019-05-24 16:18
 * @UpdateUser:
 * @UpdateDate: 2019-05-24 16:18
 * @UpdateRemark:
 * @Version: 1.0
 */

@Entity(tableName = "block_info")
public class BlockInfoEntity extends BaseEntity {

    @ColumnInfo(name = "block_stack", typeAffinity = ColumnInfo.TEXT)
    private String blockStack;

    @ColumnInfo(name = "block_time", typeAffinity = ColumnInfo.INTEGER)
    private int blockTime;

    public String getBlockStack() {
        return blockStack;
    }

    public void setBlockStack(String blockStack) {
        this.blockStack = blockStack;
    }

    public int getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(int blockTime) {
        this.blockTime = blockTime;
    }
}
