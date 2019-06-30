package com.ideacode.apm.library.core.storage;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.job.activity.ActivityInfo;
import com.ideacode.apm.library.core.storage.db.ApmDataBase;
import com.ideacode.apm.library.core.storage.db.entity.ActivityInfoEntity;
import com.ideacode.apm.library.utils.ApmLogX;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage
 * @ClassName: ActivityInfoDataRepository
 * @Description:    Activity数据库操作类
 * @Author: randysu
 * @CreateDate: 2019-05-22 13:39
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 13:39
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ActivityInfoDataRepository {

    private static final String SUB_TAG = ActivityInfoDataRepository.class.getCanonicalName();

    private ApmDataBase dataBase;

    private static ActivityInfoDataRepository instance;

    public static ActivityInfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (ActivityInfoDataRepository.class) {
                if (instance == null) {
                    instance = new ActivityInfoDataRepository();
                }
            }
        }

        return instance;
    }

    private ActivityInfoDataRepository() {
        dataBase = ApmDataBase.getInstance(Manager.getContext(), Manager.getApmExecutor());
    }

    public void insertActivityInfo(ActivityInfo info) {
        if (null == info) {
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "activityInfo  is  null");
            }
            return;
        }

        ActivityInfoEntity entity = new ActivityInfoEntity();
        entity.setActivityName(info.activityName);
        entity.setStartType(info.startType);
        entity.setTime(info.time);
        entity.setLifeCycle(info.lifeCycle);
        entity.setLifeCycleStr(ActivityInfo.getLifeCycleString(info.lifeCycle));
        entity.setExtend(info.toString());
        entity.setUploaded(false);

        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.activityInfoDao().insertActivityData(entity));
    }

    public void cleanActivityInfo() {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.activityInfoDao().cleanActivityData(TaskConfig.CLEAN_DATA_NUM));
    }

    public List<ActivityInfoEntity> getNotUploadActivityInfo() {
        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "activityInfo  getNotUploadActivityInfo");
        }
        try {
            ApmExecutor executor = Manager.getApmExecutor();
            Future<List<ActivityInfoEntity>> future = executor.diskIO().submit(new GetNotUploadActiviyInfoCallable());
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetNotUploadActiviyInfoCallable implements Callable<List<ActivityInfoEntity>> {

        @Override
        public List<ActivityInfoEntity> call() throws Exception {
            List<ActivityInfoEntity> activityInfoEntities = dataBase.activityInfoDao().getNotUploadActivityData();
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "activityInfo  activityInfoEntities size:" + activityInfoEntities.size());
            }
            return activityInfoEntities;
        }
    }

    public void updateActivityInfo(int id) {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.activityInfoDao().updateActivityData(id));
    }

}
