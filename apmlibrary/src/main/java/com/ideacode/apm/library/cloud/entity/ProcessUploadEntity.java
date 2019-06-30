package com.ideacode.apm.library.cloud.entity;

import com.ideacode.apm.library.cloud.CloudConfig;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.entity
 * @ClassName: ActivityUploadEntity
 * @Description:    Process数据上传字段
 * @Author: randysu
 * @CreateDate: 2019-06-13 18:21
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 18:21
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ProcessUploadEntity extends BaseUploadEntity {

    private int start_count;

    public ProcessUploadEntity() {
        setEvent_key(CloudConfig.PROCESS_EVENT_KEY);
    }

    public int getStart_count() {
        return start_count;
    }

    public void setStart_count(int start_count) {
        this.start_count = start_count;
    }
}
