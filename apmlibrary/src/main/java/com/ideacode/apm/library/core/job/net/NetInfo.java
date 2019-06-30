package com.ideacode.apm.library.core.job.net;

import com.ideacode.apm.library.core.BaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.net
 * @ClassName: NetInfo
 * @Description:    网络请求信息
 * @Author: randysu
 * @CreateDate: 2019-05-28 13:58
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 13:58
 * @UpdateRemark:
 * @Version: 1.0
 */
public class NetInfo extends BaseInfo {

    public String url = "";
    public String headers = "";
    public String method = "";
    public long sentBytes = 0;
    public long receivedBytes = 0;
    public long startTime = 0;
    public long costTime = 0;
    public boolean isWifi = false;
    public int statusCode = 0;
    public String errorMessage = "";

    public static final String KEY_URL = "url";
    public static final String KEY_HEADERS = "headers";
    public static final String KEY_METHOD = "method";
    public static final String KEY_SENT_BYTES = "sent_bytes";
    public static final String KEY_RECEIVED_BYTES = "received_bytes";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_COST_TIME = "cost_time";
    public static final String KEY_IS_WIFI = "is_wifi";
    public static final String KEY_STATUS_CODE = "status_code";
    public static final String KEY_ERROR_MESSAGE = "error_message";

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson()
                .put(KEY_URL, url)
                .put(KEY_HEADERS, headers)
                .put(KEY_METHOD, method)
                .put(KEY_SENT_BYTES, sentBytes)
                .put(KEY_RECEIVED_BYTES, receivedBytes)
                .put(KEY_START_TIME, startTime)
                .put(KEY_COST_TIME, costTime)
                .put(KEY_IS_WIFI, isWifi)
                .put(KEY_STATUS_CODE, statusCode)
                .put(KEY_ERROR_MESSAGE, errorMessage);
        return jsonObject;
    }

    @Override
    public void parserJsonStr(String json) throws JSONException {
        parserJson(new JSONObject(json));
    }

    @Override
    public void parserJson(JSONObject jsonObject) throws JSONException {
        url = jsonObject.getString(KEY_URL);
        headers = jsonObject.getString(KEY_HEADERS);
        method = jsonObject.getString(KEY_METHOD);
        sentBytes = jsonObject.getLong(KEY_SENT_BYTES);
        receivedBytes = jsonObject.getLong(KEY_RECEIVED_BYTES);
        startTime = jsonObject.getLong(KEY_START_TIME);
        costTime = jsonObject.getLong(KEY_COST_TIME);
        isWifi = jsonObject.getBoolean(KEY_IS_WIFI);
        statusCode = jsonObject.getInt(KEY_STATUS_CODE);
        errorMessage = jsonObject.getString(KEY_ERROR_MESSAGE);
    }
}
