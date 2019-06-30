package com.ideacode.apm.library.core.job.appstart;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.cloud.entity.AppStartUploadEntity;
import com.ideacode.apm.library.cloud.http.service.ideacode.IdeacodeApmLoader;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.storage.AppStartInfoDataRepository;
import com.ideacode.apm.library.core.storage.db.entity.AppStartInfoEntity;
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
 * @Package: com.ideacode.apm.library.core.job.appstart
 * @ClassName: AppStartTask
 * @Description: APP启动任务监听
 * @Author: randysu
 * @CreateDate: 2019-05-17 16:42
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 16:42
 * @UpdateRemark:
 * @Version: 1.0
 */
public class AppStartTask extends BaseTask<AppStartInfo> {

    private IdeacodeApmLoader ideacodeApmLoader;

    @Override
    public String getTaskName() {
        return ApmTask.TASK_APP_START;
    }

    @Override
    public void save(AppStartInfo appStartInfo) {
        super.save(appStartInfo);

        AppStartInfoDataRepository.getInstance().insertAppStartInfo(appStartInfo);
    }

    @Override
    public void clean() {
        super.clean();

        AppStartInfoDataRepository.getInstance().cleanAppStartInfo();
    }

    @Override
    public void upload() {
        super.upload();

        if (ideacodeApmLoader == null) {
            ideacodeApmLoader = new IdeacodeApmLoader();
        }

        List<AppStartInfoEntity> appStartInfoEntities = AppStartInfoDataRepository.getInstance().getNotUploadAppStartInfo();
        List<AppStartUploadEntity> uploadEntities = new ArrayList<>();
        for (AppStartInfoEntity entity : appStartInfoEntities) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "upload  " + entity.getStartTime());
            }

            AppStartUploadEntity uploadEntity = new AppStartUploadEntity();
            uploadEntity.setStart_time(entity.getStartTime());
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

            for (AppStartInfoEntity entity : appStartInfoEntities) {
                AppStartInfoDataRepository.getInstance().updateAppStartInfo(entity.getId());
            }
        }
    }
}
