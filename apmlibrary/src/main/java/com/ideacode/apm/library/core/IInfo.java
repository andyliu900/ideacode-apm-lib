package com.ideacode.apm.library.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core
 * @ClassName: IInfo
 * @Description: 通用基本数据接口
 * @Author: randysu
 * @CreateDate: 2019-05-17 17:54
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 17:54
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IInfo extends Serializable {

    int getId();

    JSONObject toJson() throws JSONException;

    void parserJsonStr(String json) throws JSONException;

    void parserJson(JSONObject jsonObject) throws JSONException;

}
