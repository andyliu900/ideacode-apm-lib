package com.ideacode.apm.library.core.job.appstart;

import com.ideacode.apm.library.core.BaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.appstart
 * @ClassName: AppStartInfo
 * @Description:    app启动信息
 * @Author: randysu
 * @CreateDate: 2019-05-20 18:51
 * @UpdateUser:
 * @UpdateDate: 2019-05-20 18:51
 * @UpdateRemark:
 * @Version: 1.0
 */
public class AppStartInfo extends BaseInfo {

    public static String KET_START_TIME = "start_time";

    private int mStartTime;

    public AppStartInfo(int startTime) {
        this(-1, startTime);
    }

    public AppStartInfo(int id, int startTime) {
        this(id, 0, startTime);
    }

    public AppStartInfo(int id, long recordTime, int startTime) {
        mId = id;
        this.recordTime = recordTime;
        mStartTime = startTime;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson()
                .put(KET_START_TIME, mStartTime);
        return jsonObject;
    }

    public int getStartTime() {
        return mStartTime;
    }

    @Override
    public void parserJsonStr(String json) throws JSONException {
        parserJson(new JSONObject(json));
    }

    @Override
    public void parserJson(JSONObject jsonObject) throws JSONException {
        this.mStartTime = jsonObject.getInt(KET_START_TIME);
    }

    @Override
    public String toString() {
        String value;
        try {
            value = toJson().toString();
        } catch (Exception e) {
            value = super.toString();
        }
        return value;
    }
}
