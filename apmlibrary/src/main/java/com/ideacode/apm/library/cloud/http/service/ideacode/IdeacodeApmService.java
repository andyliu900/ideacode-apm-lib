package com.ideacode.apm.library.cloud.http.service.ideacode;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.cloud.CloudConfig;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.http.service.ideacode
 * @ClassName: IdeacodeApmService
 * @Description: 云米服务
 * @Author: randysu
 * @CreateDate: 2019/3/4 2:12 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/4 2:12 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IdeacodeApmService {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(CloudConfig.IDEACODE_APM_DATA)
    Observable<JsonObject> apmData(@Query("signdata") String signdata, @Query("_t") String _t, @Body List params);

}
