package com.ideacode.apm.library.core.job.fps;

import com.ideacode.apm.library.core.BaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.fps
 * @ClassName: FpsInfo
 * @Description:    刷新帧率信息
 * @Author: randysu
 * @CreateDate: 2019-05-27 17:07
 * @UpdateUser:
 * @UpdateDate: 2019-05-27 17:07
 * @UpdateRemark:
 * @Version: 1.0
 */
public class FpsInfo extends BaseInfo {

    public String activityName;
    public int fps;
    public String stack;

    public static final String KEY_ACTIVITYNAME = "activity_name";
    public static final String KEY_FPS = "fps";
    public static final String KEY_STACK = "stack";

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson()
                .put(KEY_ACTIVITYNAME, activityName)
                .put(KEY_FPS, fps)
                .put(KEY_STACK, stack);
        return jsonObject;
    }

    @Override
    public void parserJsonStr(String json) throws JSONException {
        parserJson(new JSONObject(json));
    }

    @Override
    public void parserJson(JSONObject jsonObject) throws JSONException {
        activityName = jsonObject.getString(KEY_ACTIVITYNAME);
        fps = jsonObject.getInt(KEY_FPS);
        stack = jsonObject.getString(KEY_STACK);
    }
}
