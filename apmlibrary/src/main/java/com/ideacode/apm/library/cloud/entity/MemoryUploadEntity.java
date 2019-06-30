package com.ideacode.apm.library.cloud.entity;

import com.ideacode.apm.library.cloud.CloudConfig;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.entity
 * @ClassName: ActivityUploadEntity
 * @Description:    Mempry数据上传字段
 * @Author: randysu
 * @CreateDate: 2019-06-13 18:21
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 18:21
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MemoryUploadEntity extends BaseUploadEntity {

    private long current_used_mem;

    private long total_mem;

    private int dalvik_pss;

    private int native_pss;

    private int other_pss;

    private int total_pss;

    public MemoryUploadEntity() {
        setEvent_key(CloudConfig.MEMORY_EVENT_KEY);
    }

    public long getCurrent_used_mem() {
        return current_used_mem;
    }

    public void setCurrent_used_mem(long current_used_mem) {
        this.current_used_mem = current_used_mem;
    }

    public long getTotal_mem() {
        return total_mem;
    }

    public void setTotal_mem(long total_mem) {
        this.total_mem = total_mem;
    }

    public int getDalvik_pss() {
        return dalvik_pss;
    }

    public void setDalvik_pss(int dalvik_pss) {
        this.dalvik_pss = dalvik_pss;
    }

    public int getNative_pss() {
        return native_pss;
    }

    public void setNative_pss(int native_pss) {
        this.native_pss = native_pss;
    }

    public int getOther_pss() {
        return other_pss;
    }

    public void setOther_pss(int other_pss) {
        this.other_pss = other_pss;
    }

    public int getTotal_pss() {
        return total_pss;
    }

    public void setTotal_pss(int total_pss) {
        this.total_pss = total_pss;
    }
}
