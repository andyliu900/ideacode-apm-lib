package com.ideacode.apm.library.cloud.entity;

import com.ideacode.apm.library.cloud.CloudConfig;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.entity
 * @ClassName: ActivityUploadEntity
 * @Description:    Block数据上传字段
 * @Author: randysu
 * @CreateDate: 2019-06-13 18:21
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 18:21
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BlockUploadEntity extends BaseUploadEntity {

    private String block_stack;

    private int block_time;

    public BlockUploadEntity() {
        setEvent_key(CloudConfig.BLOCK_EVENT_KEY);
    }

    public String getBlock_stack() {
        return block_stack;
    }

    public void setBlock_stack(String block_stack) {
        this.block_stack = block_stack;
    }

    public int getBlock_time() {
        return block_time;
    }

    public void setBlock_time(int block_time) {
        this.block_time = block_time;
    }
}
