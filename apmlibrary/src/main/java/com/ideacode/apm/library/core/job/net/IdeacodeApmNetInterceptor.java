package com.ideacode.apm.library.core.job.net;

import android.text.TextUtils;

import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.tasks.TaskManager;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.TimeUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.net
 * @ClassName: IdeacodeApmNetInterceptor
 * @Description:    OkHttp网络拦截器
 * @Author: randysu
 * @CreateDate: 2019-05-28 11:34
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 11:34
 * @UpdateRemark:
 * @Version: 1.0
 *
 * OkHttp网络拦截器，在这里记录访问网址、网络耗时、请求数据大小、返回数据大小、返回码信息
 *
 */
public class IdeacodeApmNetInterceptor implements Interceptor {

    private static final String SUB_TAG = IdeacodeApmNetInterceptor.class.getCanonicalName();

    private OkHttpData okHttpData;

    public IdeacodeApmNetInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        boolean netTaskCanWork = TaskManager.getInstance().taskIsCanWork(ApmTask.TASK_NET);
        if (!netTaskCanWork) {
            return chain.proceed(chain.request());
        }

        long startNs = System.currentTimeMillis();

        okHttpData = new OkHttpData();
        okHttpData.startTime = startNs;

        if (Manager.isDebugLog()) {
            ApmLogX.i(APM_TAG, SUB_TAG, "okhttp request startTime:" + TimeUtils.getFormatTime(okHttpData.startTime, TimeUtils.DATETIMEMILLIS_SPLIT));
        }

        Request request = chain.request();

        recordRequestData(request);

        Response response;

        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            if (Manager.isDebugLog()) {
                e.printStackTrace();
                ApmLogX.e(APM_TAG, SUB_TAG, "http faild:" + e.getMessage());
            }
            okHttpData.errorMessage = e.getMessage();
            DataRecordUtils.recordNetinfo(okHttpData);
            throw e;
        }

        okHttpData.costTime = System.currentTimeMillis() - startNs;

        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "request costTime:" + TimeUtils.millsToTime(okHttpData.costTime));
        }

        recordResponseData(response);

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "okhttp chain.proceed end");
        }

        if (!request.url().toString().startsWith(Manager.getBaseUrl())) {
            DataRecordUtils.recordNetinfo(okHttpData);
        }

        return response;
    }

    private void recordRequestData(Request request) {
        if (null == request ||
                null == request.url() ||
                TextUtils.isEmpty(request.url().toString())) {
            return;
        }

        okHttpData.url = request.url().toString();
        okHttpData.headers = request.headers().toString();
        okHttpData.method = request.method();
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "okhttp request url:" + okHttpData.url);
            ApmLogX.d(APM_TAG, SUB_TAG, "okhttp request headers:\n" + okHttpData.headers);
            ApmLogX.d(APM_TAG, SUB_TAG, "okhttp request method:" + okHttpData.method);
        }

        RequestBody requestBody = request.body();
        if (requestBody == null) {
            okHttpData.requestSize = request.url().toString().getBytes().length;
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "okhttp request upload datasize:" + okHttpData.requestSize + " byte");
            }
            return;
        }

        long contentLength = 0;
        try {
            contentLength = requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (contentLength > 0) {
            okHttpData.requestSize = contentLength;
        } else {
            okHttpData.requestSize = request.url().toString().getBytes().length;
        }
    }

    private void recordResponseData(Response response) {
        if (response == null) {
            return;
        }

        okHttpData.responseCode = response.code();

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "okhttp response code:" + okHttpData.responseCode);
        }

        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return;
        }

        long contentLength = responseBody.contentLength();

        if (contentLength > 0) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "直接通过responseBody取到contentLength:" + contentLength + " byte");
            }
        } else {
            BufferedSource source = responseBody.source();
            if (source != null) {
                try {
                    source.request(Long.MAX_VALUE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Buffer buffer = source.buffer();
                contentLength = buffer.size();

                if (Manager.isDebugLog()) {
                    ApmLogX.e(APM_TAG, SUB_TAG, "通过responseBody.source()取到contentLength:" + contentLength + " byte");
                }
            }
        }

        okHttpData.responseSize = contentLength;

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "okhttp response download size:" + okHttpData.responseSize + " byte" );
        }
    }
}
