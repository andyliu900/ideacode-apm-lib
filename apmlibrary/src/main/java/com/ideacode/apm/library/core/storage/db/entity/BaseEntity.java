package com.ideacode.apm.library.core.storage.db.entity;


import android.os.Looper;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.ideacode.apm.library.BuildConfig;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.utils.PackageUtil;
import com.ideacode.apm.library.utils.ProcessUtils;
import com.ideacode.apm.library.utils.SystemUtils;

import java.util.Date;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.entity
 * @ClassName: BaseEntity
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-05-22 10:43
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 10:43
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BaseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "extend", typeAffinity = ColumnInfo.TEXT)
    private String extend;

    @ColumnInfo(name = "sn", typeAffinity = ColumnInfo.TEXT)
    private String sn;

    @ColumnInfo(name = "did", typeAffinity = ColumnInfo.TEXT)
    private String did;

    @ColumnInfo(name = "package_name_en", typeAffinity = ColumnInfo.TEXT)
    private String packageNameEn;

    @ColumnInfo(name = "package_name_cn", typeAffinity = ColumnInfo.TEXT)
    private String packageNameCn;

    @ColumnInfo(name = "mac", typeAffinity = ColumnInfo.TEXT)
    private String mac;

    @ColumnInfo(name = "app_name", typeAffinity = ColumnInfo.TEXT)
    private String appName;

    @ColumnInfo(name = "app_version", typeAffinity = ColumnInfo.INTEGER)
    private int appVersion;

    @ColumnInfo(name = "apm_sdk_version", typeAffinity = ColumnInfo.TEXT)
    private String apmSdkVersion;

    @ColumnInfo(name = "process_name", typeAffinity = ColumnInfo.TEXT)
    private String processName;

    @ColumnInfo(name = "thread_id", typeAffinity = ColumnInfo.INTEGER)
    private long theadId;

    @ColumnInfo(name = "thread_name", typeAffinity = ColumnInfo.TEXT)
    private String threadName;

    @ColumnInfo(name = "is_upload")
    private boolean isUploaded;

    @ColumnInfo(name = "create_time")
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return SystemUtils.getSn();
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDid() {
        return SystemUtils.getDid();
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getPackageNameEn() {
        return SystemUtils.getPackageNameEn(Manager.getContext());
    }

    public void setPackageNameEn(String packageNameEn) {
        this.packageNameEn = packageNameEn;
    }

    public String getPackageNameCn() {
        return SystemUtils.getPackageNameCn(Manager.getContext());
    }

    public void setPackageNameCn(String packageNameCn) {
        this.packageNameCn = packageNameCn;
    }

    public String getMac() {
        return SystemUtils.getMacAddress(Manager.getContext());
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getAppName() {
        return PackageUtil.getVersionName(Manager.getContext());
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppVersion() {
        return PackageUtil.getVersionCode(Manager.getContext());
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public String getApmSdkVersion() {
        return BuildConfig.VERSION;
    }

    public void setApmSdkVersion(String apmSdkVersion) {
        this.apmSdkVersion = apmSdkVersion;
    }

    public String getProcessName() {
        return ProcessUtils.getCurrentProcessName();
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public long getTheadId() {
        Looper looper = Looper.myLooper();
        if (null != looper) {
            Thread currentThread = looper.getThread();
            return currentThread.getId();
        }
        return Thread.currentThread().getId();
    }

    public void setTheadId(long theadId) {
        this.theadId = theadId;
    }

    public String getThreadName() {
        Looper looper = Looper.myLooper();
        if (null != looper) {
            Thread currentThread = looper.getThread();
            return currentThread.getName();
        }
        return Thread.currentThread().getName();
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public Date getCreateTime() {
        return new Date();
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
