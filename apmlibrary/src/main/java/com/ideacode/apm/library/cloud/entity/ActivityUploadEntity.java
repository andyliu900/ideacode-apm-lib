package com.ideacode.apm.library.cloud.entity;

import com.ideacode.apm.library.cloud.CloudConfig;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.entity
 * @ClassName: ActivityUploadEntity
 * @Description:    Activity数据上传字段
 * @Author: randysu
 * @CreateDate: 2019-06-13 18:21
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 18:21
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ActivityUploadEntity extends BaseUploadEntity {

    private String activity_name;
    private int start_type;
    private long time;
    private int life_cycle;
    private String life_cycle_str;

    public ActivityUploadEntity() {
        setEvent_key(CloudConfig.ACTIVITY_EVENT_KEY);
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public int getStart_type() {
        return start_type;
    }

    public void setStart_type(int start_type) {
        this.start_type = start_type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLife_cycle() {
        return life_cycle;
    }

    public void setLife_cycle(int life_cycle) {
        this.life_cycle = life_cycle;
    }

    public String getLife_cycle_str() {
        return life_cycle_str;
    }

    public void setLife_cycle_str(String life_cycle_str) {
        this.life_cycle_str = life_cycle_str;
    }
}
