package com.ideacode.apm.library.cloud.entity;

import com.ideacode.apm.library.cloud.CloudConfig;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.entity
 * @ClassName: ActivityUploadEntity
 * @Description:    AppStart数据上传字段
 * @Author: randysu
 * @CreateDate: 2019-06-13 18:21
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 18:21
 * @UpdateRemark:
 * @Version: 1.0
 */
public class AppStartUploadEntity extends BaseUploadEntity {

    private int start_time;

    public AppStartUploadEntity() {
        setEvent_key(CloudConfig.APPSTART_EVENT_KEY);
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }
}
