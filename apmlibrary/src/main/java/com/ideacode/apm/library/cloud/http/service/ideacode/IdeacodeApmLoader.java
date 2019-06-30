package com.ideacode.apm.library.cloud.http.service.ideacode;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.Env;
import com.ideacode.apm.library.cloud.CloudConfig;
import com.ideacode.apm.library.cloud.http.ObjectLoader;
import com.ideacode.apm.library.cloud.http.RetrofitServiceManager;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.SystemUtils;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.http.service.ideacode
 * @ClassName: IdeacodeApmLoader
 * @Description: 云米Loader
 * @Author: randysu
 * @CreateDate: 2019/3/20 3:24 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/20 3:24 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class IdeacodeApmLoader extends ObjectLoader {

    private static final String SUB_TAG = IdeacodeApmLoader.class.getName();

    private IdeacodeApmService ideacodeApmService;

    public IdeacodeApmLoader() {
        ideacodeApmService = RetrofitServiceManager.getInstance().create(IdeacodeApmService.class);
    }

    public Observable<JsonObject> apmData(List params) {
        Date nowDate = new Date();
        StringBuilder sourceSB = new StringBuilder();
        String _t = String.valueOf(nowDate.getTime());
        String key = CloudConfig.API_SIGN_KEY;
        String mac = SystemUtils.getMacAddress(Manager.getContext());
        sourceSB.append(_t).append(key).append(mac);

        String signdata = SystemUtils.md5(sourceSB.toString());
        if (Manager.isDebugLog()) {
            ApmLogX.d(Env.APM_TAG, SUB_TAG, "signdata:" + signdata);
        }

        return observe(ideacodeApmService.apmData(signdata, _t, params));
    }

}
