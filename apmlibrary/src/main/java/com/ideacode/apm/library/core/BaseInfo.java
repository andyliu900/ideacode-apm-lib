package com.ideacode.apm.library.core;

import android.util.Log;

import com.ideacode.apm.library.utils.ApmLogX;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core
 * @ClassName: BaseInfo
 * @Description:    基本数据实体类
 * @Author: randysu
 * @CreateDate: 2019-05-17 17:56
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 17:56
 * @UpdateRemark:
 * @Version: 1.0
 */
public abstract class BaseInfo implements IInfo {

    private static final String SUB_TAG = BaseInfo.class.getName();

    public static final String KEY_ID_RECORD = "id"; // 数据变自增长主键
    public static final String KEY_TIME_RECORD = "time_record"; // 写入的时间
    public static final String KEY_EXTEND = "extend"; // 扩展字段，保存扩展json信息
    public static final String KEY_APP_NAME = "app_name";
    public static final String KEY_APP_VERSION = "app_version";

    // 公用字段
    public static final String KEY_PROCESS_NAME = "process_name";
    public static final String KEY_THREAD_NAME = "thread_name";
    public static final String KEY_THREAD_ID = "thread_id";
    public static final String KEY_STACK_NAME = "stack_name";

    // 从数据库里读出的id有效，其他途径复制无效
    protected int mId = -1;

    protected String params;

    protected long recordTime;

    @Override
    public JSONObject toJson() throws JSONException {
        return new JSONObject().put(KEY_TIME_RECORD, recordTime);
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public void parserJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public void parserJsonStr(String json) throws JSONException {

    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return params;
    }

    @Override
    public String toString() {
        String value;
        try {
            value = toJson().toString();
        } catch (Exception e) {
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, Log.getStackTraceString(e.fillInStackTrace()));
            }
            value = super.toString();
        }
        return value;
    }
}
