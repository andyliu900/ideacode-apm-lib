package com.ideacode.apm.library.cloud.entity;

import com.ideacode.apm.library.core.storage.db.entity.BaseEntity;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud
 * @ClassName: BaseUploadEntity
 * @Description:    数据上传基本字段
 * @Author: randysu
 * @CreateDate: 2019-06-13 17:43
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 17:43
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BaseUploadEntity {

    private String extend;
    private String sn;
    private String did;
    private String mac;
    private String app_name;
    private int app_version;
    private String apm_sdk_version;
    private String process_name;
    private long thread_id;
    private String thread_name;
    private long create_time;
    private String event_key;

    public void setBaseData(BaseEntity entity) {
        extend = entity.getExtend();
        sn = entity.getSn();
        did = entity.getDid();
        mac = entity.getMac();
        app_name = entity.getAppName();
        app_version = entity.getAppVersion();
        apm_sdk_version = entity.getApmSdkVersion();
        process_name = entity.getProcessName();
        thread_id = entity.getTheadId();
        thread_name = entity.getThreadName();
        create_time = entity.getCreateTime().getTime();
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public int getApp_version() {
        return app_version;
    }

    public void setApp_version(int app_version) {
        this.app_version = app_version;
    }

    public String getApm_sdk_version() {
        return apm_sdk_version;
    }

    public void setApm_sdk_version(String apm_sdk_version) {
        this.apm_sdk_version = apm_sdk_version;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }

    public long getThread_id() {
        return thread_id;
    }

    public void setThread_id(long thread_id) {
        this.thread_id = thread_id;
    }

    public String getThread_name() {
        return thread_name;
    }

    public void setThread_name(String thread_name) {
        this.thread_name = thread_name;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getEvent_key() {
        return event_key;
    }

    public void setEvent_key(String event_key) {
        this.event_key = event_key;
    }
}
