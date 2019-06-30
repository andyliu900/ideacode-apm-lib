package com.ideacode.apm.library.cloud.entity;

import com.ideacode.apm.library.cloud.CloudConfig;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.entity
 * @ClassName: ActivityUploadEntity
 * @Description:    Fps数据上传字段
 * @Author: randysu
 * @CreateDate: 2019-06-13 18:21
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 18:21
 * @UpdateRemark:
 * @Version: 1.0
 */
public class FpsUploadEntity extends BaseUploadEntity {

    private String activity_name;

    private int fps;

    private String stack;

    public FpsUploadEntity() {
        setEvent_key(CloudConfig.FPS_EVENT_KEY);
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
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
