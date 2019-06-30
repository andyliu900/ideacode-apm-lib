package com.ideacode.apm.library.core.job.activity;

import android.text.TextUtils;

import com.ideacode.apm.library.core.BaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.activity
 * @ClassName: ActivityInfo
 * @Description:    Activity信息类型
 * @Author: randysu
 * @CreateDate: 2019-05-17 18:30
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 18:30
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ActivityInfo extends BaseInfo {

    private static final String TAG = ActivityInfo.class.getName();

    /**
     * Activity生命周期类型枚举
     * @return
     */
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_FIRST_FRAME = 1;
    public static final int TYPE_CREATE = 2;
    public static final int TYPE_START = 3;
    public static final int TYPE_RESUME = 4;
    public static final int TYPE_PAUSE = 5;
    public static final int TYPE_STOP = 6;
    public static final int TYPE_DESTROY = 7;
    public static final int TYPE_RESTART = 8;
    public static final int TYPE_SAVEINSTANCESTATE = 9;
    public static final int TYPE_RESTOREINSTANCESTATE = 10;

    /**
     * Activity生命周期类型值对应名称
     * @return
     */
    public static final String TYPE_STR_UNKNOWN = "unKnown";
    public static final String TYPE_STR_FIRST_FRAME = "firsrFrame";
    public static final String TYPE_STR_CREATE = "onCreate";
    public static final String TYPE_STR_START = "onStart";
    public static final String TYPE_STR_RESUME = "onResume";
    public static final String TYPE_STR_PAUSE = "onPause";
    public static final String TYPE_STR_STOP = "onStop";
    public static final String TYPE_STR_DESTROY = "onDestroy";
    public static final String TYPE_STR_RESTART = "onRestart";
    public static final String TYPE_STR_SAVEINSTANCESTATE = "onSaveInstanceState";
    public static final String TYPE_STR_RESTOREINSTANCESTATE = "onRestoreInstanceState";

    /**
     * 启动枚举类型
     */
    public static final int COLD_START = 0;
    public static final int HOT_START = 1;

    /**
     * activity DB数据私有字段
     */
    public static final String KEY_NAME = "name"; // activityName 完整类名
    public static final String KEY_TIME = "time"; // 运行时间
    public static final String KEY_LIFE_CYCLE = "life_cycle"; // 生命周期类型
    public static final String KEY_START_TYPE = "start_type"; // 启动类型（冷、热启动）

    /**
     * activityInfo 对象属性
     * @return
     */
    public String activityName = "";
    public int startType = 0; // 启动的类型
    public long time = 0;     // 消耗的时间
    public int lifeCycle = 0; // 生命周期
    public String appName = ""; // app名称
    public String appVer = ""; // app版本号

    public ActivityInfo() {

    }

    public void resetData() {
        this.mId = -1;
        this.activityName = "";
        this.recordTime = 0;
        this.startType = 0;
        this.time = 0;
        this.lifeCycle = 0;
        this.appName = "";
        this.appVer = "";
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson()
                .put(KEY_NAME, activityName)
                .put(KEY_START_TYPE, startType)
                .put(KEY_TIME, time)
                .put(KEY_LIFE_CYCLE, lifeCycle)
                .put(KEY_APP_NAME, appName)
                .put(KEY_APP_VERSION, appVer);
        return jsonObject;
    }

    @Override
    public void parserJsonStr(String json) throws JSONException {
        parserJson(new JSONObject(json));
    }

    @Override
    public void parserJson(JSONObject jsonObject) throws JSONException {
        this.activityName = jsonObject.getString(KEY_NAME);
        this.startType = jsonObject.getInt(KEY_START_TYPE);
        this.time = jsonObject.getLong(KEY_TIME);
        this.lifeCycle = jsonObject.getInt(KEY_LIFE_CYCLE);
        this.appName = jsonObject.getString(KEY_APP_NAME);
        this.appVer = jsonObject.getString(KEY_APP_VERSION);
    }

    public static String getLifeCycleString(int lifeCycle) {
        String lifeStr;
        switch (lifeCycle) {
            case TYPE_FIRST_FRAME:
                lifeStr = TYPE_STR_FIRST_FRAME;
                break;
            case TYPE_CREATE:
                lifeStr = TYPE_STR_CREATE;
                break;
            case TYPE_START:
                lifeStr = TYPE_STR_START;
                break;
            case TYPE_RESUME:
                lifeStr = TYPE_STR_RESUME;
                break;
            case TYPE_PAUSE:
                lifeStr = TYPE_STR_PAUSE;
                break;
            case TYPE_STOP:
                lifeStr = TYPE_STR_STOP;
                break;
            case TYPE_DESTROY:
                lifeStr = TYPE_STR_DESTROY;
                break;
            case TYPE_RESTART:
                lifeStr = TYPE_STR_RESTART;
                break;
            case TYPE_SAVEINSTANCESTATE:
                lifeStr = TYPE_STR_SAVEINSTANCESTATE;
                break;
            case TYPE_RESTOREINSTANCESTATE:
                lifeStr = TYPE_STR_RESTOREINSTANCESTATE;
                break;
            default:
                lifeStr = TYPE_STR_UNKNOWN;
        }
        return lifeStr;
    }

    public static int ofLifeCycleString(String lcStr) {
        int lc = 0;
        if (TextUtils.equals(lcStr, TYPE_STR_FIRST_FRAME)) {
            lc = TYPE_FIRST_FRAME;
        } else if (TextUtils.equals(lcStr, TYPE_STR_CREATE)) {
            lc = TYPE_CREATE;
        } else if (TextUtils.equals(lcStr, TYPE_STR_START)) {
            lc = TYPE_START;
        } else if (TextUtils.equals(lcStr, TYPE_STR_RESUME)) {
            lc = TYPE_RESUME;
        } else if (TextUtils.equals(lcStr, TYPE_STR_PAUSE)) {
            lc = TYPE_PAUSE;
        } else if (TextUtils.equals(lcStr, TYPE_STR_STOP)) {
            lc = TYPE_STOP;
        } else if (TextUtils.equals(lcStr, TYPE_STR_DESTROY)) {
            lc = TYPE_DESTROY;
        } else if (TextUtils.equals(lcStr, TYPE_STR_RESTART)) {
            lc = TYPE_RESTART;
        } else if (TextUtils.equals(lcStr, TYPE_STR_SAVEINSTANCESTATE)) {
            lc = TYPE_SAVEINSTANCESTATE;
        } else if (TextUtils.equals(lcStr, TYPE_STR_RESTOREINSTANCESTATE)) {
            lc = TYPE_RESTOREINSTANCESTATE;
        }
        return lc;
    }
}
