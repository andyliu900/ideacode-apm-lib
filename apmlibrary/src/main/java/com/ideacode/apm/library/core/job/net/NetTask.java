package com.ideacode.apm.library.core.job.net;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.cloud.entity.NetUploadEntity;
import com.ideacode.apm.library.cloud.http.service.ideacode.IdeacodeApmLoader;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.storage.NetInfoDataRepository;
import com.ideacode.apm.library.core.storage.db.entity.NetInfoEntity;
import com.ideacode.apm.library.core.tasks.BaseTask;
import com.ideacode.apm.library.utils.ApmLogX;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.net
 * @ClassName: NetTask
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-05-28 14:19
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 14:19
 * @UpdateRemark:
 * @Version: 1.0
 */
public class NetTask extends BaseTask {

    private static final String SUB_TAG = NetTask.class.getCanonicalName();

    private IdeacodeApmLoader ideacodeApmLoader;
    
    @Override
    public String getTaskName() {
        return ApmTask.TASK_NET;
    }

    @Override
    public void clean() {
        super.clean();

        NetInfoDataRepository.getInstance().cleanNetInfo();
    }

    @Override
    public void upload() {
        super.upload();

        if (ideacodeApmLoader == null) {
            ideacodeApmLoader = new IdeacodeApmLoader();
        }

        List<NetInfoEntity> netInfoEntities = NetInfoDataRepository.getInstance().getNotUploadNetInfo();
        List<NetUploadEntity> uploadEntities = new ArrayList<>();
        for (NetInfoEntity entity : netInfoEntities) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "upload url:" + entity.getUrl());
            }

            NetUploadEntity uploadEntity = new NetUploadEntity();
            uploadEntity.setUrl(entity.getUrl());
            uploadEntity.setHeaders(entity.getHeaders());
            uploadEntity.setMethod(entity.getMethod());
            uploadEntity.setSent_bytes(entity.getSentBytes());
            uploadEntity.setReceived_bytes(entity.getReceivedBytes());
            uploadEntity.setStart_time(entity.getStartTime().getTime());
            uploadEntity.setCost_time(entity.getCostTime());
            uploadEntity.setIs_wifi(entity.isWifi() ? 1 : 0);
            uploadEntity.setStatus_code(entity.getStatusCode());
            uploadEntity.setError_message(entity.getErrorMessage());
            uploadEntity.setBaseData(entity);

            uploadEntities.add(uploadEntity);
        }

        if (uploadEntities.size() > 0) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "uploadEntities:" + uploadEntities.toString());
            }
            ideacodeApmLoader.apmData(uploadEntities).subscribe(new Action1<JsonObject>() {
                @Override
                public void call(JsonObject jsonObject) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "jsonObject:" + jsonObject);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "error message:" + throwable.getMessage());
                }
            });

            for (NetInfoEntity entity : netInfoEntities) {
                NetInfoDataRepository.getInstance().updateNetInfo(entity.getId());
            }
        }
    }
}
