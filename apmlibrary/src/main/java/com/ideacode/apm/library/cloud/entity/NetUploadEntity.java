package com.ideacode.apm.library.cloud.entity;

import com.ideacode.apm.library.cloud.CloudConfig;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.entity
 * @ClassName: ActivityUploadEntity
 * @Description:    Net数据上传字段
 * @Author: randysu
 * @CreateDate: 2019-06-13 18:21
 * @UpdateUser:
 * @UpdateDate: 2019-06-13 18:21
 * @UpdateRemark:
 * @Version: 1.0
 */
public class NetUploadEntity extends BaseUploadEntity {

    private String url;

    private String headers;

    private String method;

    private long sent_bytes;

    private long received_bytes;

    private long start_time;

    private long cost_time;

    private int is_wifi;

    private int status_code;

    private String error_message;

    public NetUploadEntity() {
        setEvent_key(CloudConfig.NET_EVENT_KEY);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getSent_bytes() {
        return sent_bytes;
    }

    public void setSent_bytes(long sent_bytes) {
        this.sent_bytes = sent_bytes;
    }

    public long getReceived_bytes() {
        return received_bytes;
    }

    public void setReceived_bytes(long received_bytes) {
        this.received_bytes = received_bytes;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getCost_time() {
        return cost_time;
    }

    public void setCost_time(long cost_time) {
        this.cost_time = cost_time;
    }

    public int getIs_wifi() {
        return is_wifi;
    }

    public void setIs_wifi(int is_wifi) {
        this.is_wifi = is_wifi;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
