package com.ideacode.apm.library.core.storage;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.job.fps.FpsInfo;
import com.ideacode.apm.library.core.storage.db.ApmDataBase;
import com.ideacode.apm.library.core.storage.db.entity.FpsInfoEntity;
import com.ideacode.apm.library.utils.ApmLogX;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db
 * @ClassName: FpsInfoDataRepository
 * @Description:    Fps数据库操作类
 * @Author: randysu
 * @CreateDate: 2019-05-28 10:00
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 10:00
 * @UpdateRemark:
 * @Version: 1.0
 */
public class FpsInfoDataRepository {

    private static final String SUB_TAG = FpsInfoDataRepository.class.getCanonicalName();

    private ApmDataBase dataBase;

    private static FpsInfoDataRepository instance;

    public static FpsInfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (FpsInfoDataRepository.class) {
                if (instance == null) {
                    instance = new FpsInfoDataRepository();
                }
            }
        }

        return instance;
    }

    private FpsInfoDataRepository() {
        dataBase = ApmDataBase.getInstance(Manager.getContext(), Manager.getApmExecutor());
    }

    public void insertFpsInfo(FpsInfo info) {
        if (null == info) {
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "fpsInfo  is  null");
            }
            return;
        }

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "FpsInfoDataRepository insertFpsInfo fps:" + info.fps);
        }

        FpsInfoEntity entity = new FpsInfoEntity();
        entity.setActivityName(info.activityName);
        entity.setFps(info.fps);
        entity.setStack(info.stack);
        entity.setExtend(info.toString());
        entity.setUploaded(false);

        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.fpsInfoDao().insertFpsData(entity));
    }

    public void cleanFpsInfo() {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.fpsInfoDao().cleanFpsData(TaskConfig.CLEAN_DATA_NUM));
    }

    public List<FpsInfoEntity> getNotUploadFpsInfo() {
        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "fpsInfo  getNotUploadFpsInfo");
        }
        try {
            ApmExecutor executor = Manager.getApmExecutor();
            Future<List<FpsInfoEntity>> future = executor.diskIO().submit(new FpsInfoDataRepository.GetNotUploadFpsInfoCallable());
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetNotUploadFpsInfoCallable implements Callable<List<FpsInfoEntity>> {

        @Override
        public List<FpsInfoEntity> call() throws Exception {
            List<FpsInfoEntity> fpsInfoEntities = dataBase.fpsInfoDao().getNotUploadFpsData();
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "fpsInfo  fpsInfoEntities size:" + fpsInfoEntities.size());
            }
            return fpsInfoEntities;
        }
    }

    public void updateFpsInfo(int id) {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.fpsInfoDao().updateFpsData(id));
    }
    
}
