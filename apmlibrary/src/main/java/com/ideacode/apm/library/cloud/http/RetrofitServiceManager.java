package com.ideacode.apm.library.cloud.http;

import android.text.TextUtils;

import com.ideacode.apm.library.cloud.CloudConfig;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.job.net.IdeacodeApmNetInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.http
 * @ClassName: RetrofitServiceManager
 * @Description: Retrofit + okhttp + rxjava
 * @Author: randysu
 * @CreateDate: 2019/3/2 4:57 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 4:57 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class RetrofitServiceManager {

    private Retrofit mRetrofit;

    private RetrofitServiceManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CloudConfig.DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(CloudConfig.DEFAULT_READ_WRITE_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(CloudConfig.DEFAULT_READ_WRITE_TIME_OUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        builder.addInterceptor(new IdeacodeApmNetInterceptor());

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        if (!TextUtils.isEmpty(Manager.getBaseUrl())) {
            retrofitBuilder.baseUrl(Manager.getBaseUrl());
        }

        mRetrofit = retrofitBuilder.build();
    }

    private static RetrofitServiceManager mInstance;

    public static RetrofitServiceManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitServiceManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitServiceManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

}
