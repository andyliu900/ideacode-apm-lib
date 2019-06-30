package com.ideacode.apm.library.core.storage.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Date;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.entity
 * @ClassName: NetInfoEntity
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-05-28 17:58
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 17:58
 * @UpdateRemark:
 * @Version: 1.0
 */

@Entity(tableName = "net_info")
public class NetInfoEntity extends BaseEntity {

    @ColumnInfo(name = "url", typeAffinity = ColumnInfo.TEXT)
    private String url;

    @ColumnInfo(name = "headers", typeAffinity = ColumnInfo.TEXT)
    private String headers;

    @ColumnInfo(name = "method", typeAffinity = ColumnInfo.TEXT)
    private String method;

    @ColumnInfo(name = "sent_bytes", typeAffinity = ColumnInfo.INTEGER)
    private long sentBytes;

    @ColumnInfo(name = "received_bytes", typeAffinity = ColumnInfo.INTEGER)
    private long receivedBytes;

    @ColumnInfo(name = "start_time")
    private Date startTime;

    @ColumnInfo(name = "cost_time", typeAffinity = ColumnInfo.INTEGER)
    private long costTime;

    @ColumnInfo(name = "is_wifi")
    private boolean isWifi;

    @ColumnInfo(name = "status_code", typeAffinity = ColumnInfo.INTEGER)
    private int statusCode;

    @ColumnInfo(name = "error_message", typeAffinity = ColumnInfo.TEXT)
    private String errorMessage;

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

    public long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public boolean isWifi() {
        return isWifi;
    }

    public void setWifi(boolean wifi) {
        isWifi = wifi;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
