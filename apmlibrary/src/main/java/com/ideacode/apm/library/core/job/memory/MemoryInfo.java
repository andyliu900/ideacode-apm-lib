package com.ideacode.apm.library.core.job.memory;

import com.ideacode.apm.library.core.BaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.memory
 * @ClassName: MemoryInfo
 * @Description:    内存占用，单位kb
 * @Author: randysu
 * @CreateDate: 2019-05-23 16:32
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 16:32
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MemoryInfo extends BaseInfo {

    public String currentProcessName;
    public long currentUsedMem;
    public long totalMem;
    public int dalvikPss;
    public int nativePss;
    public int otherPss;
    public int totalPss;

    public static final String KEY_CURRENT_PROCESS_NAME = "current_process_name";
    public static final String KEY_CURRENT_USED_MEM = "current_used_mem";
    public static final String KEY_TOTAL_MEM = "total_mem";
    public static final String KEY_DALVIKX_PSS = "dalvik_pss";
    public static final String KEY_NATIVE_PSS = "native_pss";
    public static final String KEY_OTHER_PSS = "other_pss";
    public static final String KEY_TOTAL_PSS = "total_pss";

    public MemoryInfo() {

    }

    public MemoryInfo(String currentProcessName, long currentUsedMem, long totalMem, int dalvikPss, int nativePss, int otherPss, int totalPss) {
        this.currentUsedMem = currentUsedMem;
        this.totalMem = totalMem;
        this.currentProcessName = currentProcessName;
        this.dalvikPss = dalvikPss;
        this.nativePss = nativePss;
        this.otherPss = otherPss;
        this.totalPss = totalPss;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson()
                .put(KEY_CURRENT_USED_MEM, currentUsedMem)
                .put(KEY_TOTAL_MEM, totalMem)
                .put(KEY_CURRENT_PROCESS_NAME, currentProcessName)
                .put(KEY_DALVIKX_PSS, dalvikPss)
                .put(KEY_NATIVE_PSS, nativePss)
                .put(KEY_OTHER_PSS, otherPss)
                .put(KEY_TOTAL_PSS, totalPss);
        return jsonObject;
    }

    @Override
    public void parserJsonStr(String json) throws JSONException {
        parserJson(new JSONObject(json));
    }

    @Override
    public void parserJson(JSONObject jsonObject) throws JSONException {
        currentProcessName = jsonObject.getString(KEY_CURRENT_PROCESS_NAME);
        currentUsedMem = jsonObject.getLong(KEY_CURRENT_USED_MEM);
        totalMem = jsonObject.getLong(KEY_TOTAL_MEM);
        dalvikPss = jsonObject.getInt(KEY_DALVIKX_PSS);
        nativePss = jsonObject.getInt(KEY_NATIVE_PSS);
        otherPss = jsonObject.getInt(KEY_OTHER_PSS);
        totalPss = jsonObject.getInt(KEY_TOTAL_PSS);
    }

    @Override
    public String toString() {
        String value;
        try {
            value = toJson().toString();
        } catch (Exception e) {
            e.printStackTrace();
            value = super.toString();
        }
        return value;
    }
}
