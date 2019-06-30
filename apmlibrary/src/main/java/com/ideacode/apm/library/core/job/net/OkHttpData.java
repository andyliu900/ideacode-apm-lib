package com.ideacode.apm.library.core.job.net;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.net
 * @ClassName: OkHttpData
 * @Description:    网络数据
 * @Author: randysu
 * @CreateDate: 2019-05-28 11:20
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 11:20
 * @UpdateRemark:
 * @Version: 1.0
 */
public class OkHttpData {

    public String url;
    public String headers;
    public long startTime;
    public String method;
    public long requestSize;
    public long costTime;
    public int responseCode;
    public long responseSize;
    public String errorMessage;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OkHttpData[");
        sb.append("url = " + url + ",");
        sb.append("headers = " + headers + ",");
        sb.append("startTime = " + startTime + ",");
        sb.append("method = " + method + ",");
        sb.append("requestSize = " + requestSize + ",");
        sb.append("costTime = " + costTime + ",");
        sb.append("responseCode = " + responseCode + ",");
        sb.append("responseSize = " + responseSize + ",");
        sb.append("errorMessage = " + errorMessage + "]");
        return sb.toString();
    }
}
