package com.ideacode.apm.library.core.job.processinfo;

import com.ideacode.apm.library.core.BaseInfo;
import com.ideacode.apm.library.utils.ProcessUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.processinfo
 * @ClassName: ProcessInfo
 * @Description:    进程信息
 * @Author: randysu
 * @CreateDate: 2019-05-23 13:50
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 13:50
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ProcessInfo extends BaseInfo {

    private static final String SUB_TAG = ProcessInfo.class.getCanonicalName();

    public String processName; // 进程名称
    public int startCount; // 启动次数

    /**
     * process DB数据私有字段
     */
    public static final String KEY_PROCESSNAME = "process_name"; // 进程名称
    public static final String KEY_STARTCOUNT = "start_count"; // 启动次数

    public ProcessInfo() {
        processName = ProcessUtils.getCurrentProcessName();
        startCount = 1;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson()
                .put(KEY_PROCESSNAME, processName)
                .put(KEY_STARTCOUNT, startCount);
        return jsonObject;
    }

    @Override
    public void parserJsonStr(String json) throws JSONException {
        parserJson(new JSONObject(json));
    }

    @Override
    public void parserJson(JSONObject jsonObject) throws JSONException {
        processName = jsonObject.getString(KEY_PROCESSNAME);
        startCount = jsonObject.getInt(KEY_STARTCOUNT);
    }

}
