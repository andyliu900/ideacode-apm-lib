package com.ideacode.apm.library.core.storage.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.entity
 * @ClassName: MemoryInfoEntity
 * @Description:    程序内存信息记录
 * @Author: randysu
 * @CreateDate: 2019-05-23 17:57
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 17:57
 * @UpdateRemark:
 * @Version: 1.0
 */

@Entity(tableName = "memory_info")
public class MemoryInfoEntity extends BaseEntity {

    @ColumnInfo(name = "current_used_mem", typeAffinity = ColumnInfo.INTEGER)
    private long currentUsedMem;

    @ColumnInfo(name = "total_mem", typeAffinity = ColumnInfo.INTEGER)
    private long totalMem;

    @ColumnInfo(name = "dalvik_pss", typeAffinity = ColumnInfo.INTEGER)
    private int dalvikPss;

    @ColumnInfo(name = "native_pss", typeAffinity = ColumnInfo.INTEGER)
    private int nativePss;

    @ColumnInfo(name = "other_pss", typeAffinity = ColumnInfo.INTEGER)
    private int otherPss;

    @ColumnInfo(name = "total_pss", typeAffinity = ColumnInfo.INTEGER)
    private int totalPss;

    public long getCurrentUsedMem() {
        return currentUsedMem;
    }

    public void setCurrentUsedMem(long currentUsedMem) {
        this.currentUsedMem = currentUsedMem;
    }

    public long getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(long totalMem) {
        this.totalMem = totalMem;
    }

    public int getDalvikPss() {
        return dalvikPss;
    }

    public void setDalvikPss(int dalvikPss) {
        this.dalvikPss = dalvikPss;
    }

    public int getNativePss() {
        return nativePss;
    }

    public void setNativePss(int nativePss) {
        this.nativePss = nativePss;
    }

    public int getOtherPss() {
        return otherPss;
    }

    public void setOtherPss(int otherPss) {
        this.otherPss = otherPss;
    }

    public int getTotalPss() {
        return totalPss;
    }

    public void setTotalPss(int totalPss) {
        this.totalPss = totalPss;
    }
}
