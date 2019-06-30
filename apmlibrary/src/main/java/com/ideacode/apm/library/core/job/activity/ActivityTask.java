package com.ideacode.apm.library.core.job.activity;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.cloud.entity.ActivityUploadEntity;
import com.ideacode.apm.library.cloud.http.service.ideacode.IdeacodeApmLoader;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.storage.ActivityInfoDataRepository;
import com.ideacode.apm.library.core.storage.db.entity.ActivityInfoEntity;
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
 * @Package: com.ideacode.apm.library.core.job.activity
 * @ClassName: ActivityTask
 * @Description:  Activity监听任务
 * @Author: randysu
 * @CreateDate: 2019-05-17 16:46
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 16:46
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ActivityTask extends BaseTask<ActivityInfo> {

    private static final String SUB_TAG = ActivityTask.class.getName();

    private IdeacodeApmLoader ideacodeApmLoader;

    @Override
    public String getTaskName() {
        return ApmTask.TASK_ACTIVITY;
    }

    @Override
    public void start() {
        super.start();
        if (Manager.getInstance().getConfig().isEnabled(ApmTask.FLAG_COLLECT_ACTIVITY_INSTRUMENTATION)
            && !InstrumentationHooker.isHookSucceed()) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "canWoek hook: hook失败");
            }
            mIsCanWork = false;
        }

        ideacodeApmLoader = new IdeacodeApmLoader();
    }

    @Override
    public void save(ActivityInfo activityInfo) {
        super.save(activityInfo);

        ActivityInfoDataRepository.getInstance().insertActivityInfo(activityInfo);
    }

    @Override
    public void clean() {
        super.clean();

        ActivityInfoDataRepository.getInstance().cleanActivityInfo();
    }

    @Override
    public void upload() {
        super.upload();

        if (ideacodeApmLoader == null) {
            ideacodeApmLoader = new IdeacodeApmLoader();
        }

        List<ActivityInfoEntity> activityInfoEntities = ActivityInfoDataRepository.getInstance().getNotUploadActivityInfo();
        List<ActivityUploadEntity> uploadEntities = new ArrayList<>();
        for (ActivityInfoEntity entity : activityInfoEntities) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "upload " + entity.getActivityName() + "  " + entity.getLifeCycleStr());
            }

            ActivityUploadEntity uploadEntity = new ActivityUploadEntity();
            uploadEntity.setActivity_name(entity.getActivityName());
            uploadEntity.setStart_type(entity.getStartType());
            uploadEntity.setTime(entity.getTime());
            uploadEntity.setLife_cycle(entity.getLifeCycle());
            uploadEntity.setLife_cycle_str(entity.getLifeCycleStr());
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

            for (ActivityInfoEntity entity : activityInfoEntities) {
                ActivityInfoDataRepository.getInstance().updateActivityInfo(entity.getId());
            }
        }

    }
}
