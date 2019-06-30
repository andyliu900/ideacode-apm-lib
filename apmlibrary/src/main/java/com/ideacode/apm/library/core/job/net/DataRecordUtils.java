package com.ideacode.apm.library.core.job.net;

import android.text.TextUtils;

import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.storage.NetInfoDataRepository;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.AsyncThreadTask;
import com.ideacode.apm.library.utils.SystemUtils;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.net
 * @ClassName: DataRecordUtils
 * @Description:    保存网络请求信息
 * @Author: randysu
 * @CreateDate: 2019-05-28 13:55
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 13:55
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DataRecordUtils {

    private static final String SUB_TAG = DataRecordUtils.class.getCanonicalName();

    /**
     * 保存网络信息
     * @param okHttpData
     */
    public static void recordNetinfo(OkHttpData okHttpData) {
        if (okHttpData == null || TextUtils.isEmpty(okHttpData.url)) {
            return;
        }

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "okhttpData url:" + okHttpData.url);
        }

        NetInfo info = new NetInfo();
        info.url = okHttpData.url;
        info.headers = okHttpData.headers;
        info.method = okHttpData.method;
        info.sentBytes = okHttpData.requestSize;
        info.startTime = okHttpData.startTime;
        info.costTime = okHttpData.costTime;
        info.isWifi = SystemUtils.isWifiConnected();
        info.statusCode = okHttpData.responseCode;
        info.errorMessage = okHttpData.errorMessage;

        AsyncThreadTask.execute(() -> NetInfoDataRepository.getInstance().insertNetInfo(info));

    }

}
