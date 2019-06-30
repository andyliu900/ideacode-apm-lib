package com.ideacode.apm.library.core.job.block;

import com.ideacode.apm.library.core.BaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.block
 * @ClassName: BlockInfo
 * @Description:    卡顿信息
 * @Author: randysu
 * @CreateDate: 2019-05-24 13:44
 * @UpdateUser:
 * @UpdateDate: 2019-05-24 13:44
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BlockInfo extends BaseInfo {

    public String currentProcessName;
    public String blockStack;
    public int blockTime;

    public static final String KEY_CURRENT_PROCESS_NAME = "current_process_name";
    public static final String KEY_BLOCK_STACK = "block_stack";
    public static final String KEY_BLOCK_TIME = "block_time";

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson()
                .put(KEY_CURRENT_PROCESS_NAME, currentProcessName)
                .put(KEY_BLOCK_STACK, blockStack)
                .put(KEY_BLOCK_TIME, blockTime);
        return jsonObject;
    }

    @Override
    public void parserJsonStr(String json) throws JSONException {
        parserJson(new JSONObject(json));
    }

    @Override
    public void parserJson(JSONObject jsonObject) throws JSONException {
        currentProcessName = jsonObject.getString(KEY_CURRENT_PROCESS_NAME);
        blockStack = jsonObject.getString(KEY_BLOCK_STACK);
        blockTime = jsonObject.getInt(KEY_BLOCK_TIME);
    }

}
