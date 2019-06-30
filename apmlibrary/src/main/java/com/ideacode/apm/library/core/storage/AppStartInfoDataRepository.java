package com.ideacode.apm.library.core.storage;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.job.appstart.AppStartInfo;
import com.ideacode.apm.library.core.storage.db.ApmDataBase;
import com.ideacode.apm.library.core.storage.db.entity.AppStartInfoEntity;
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
 * @ClassName: AppStartInfoDataRepository
 * @Description:    AppStart数据库操作类
 * @Author: randysu
 * @CreateDate: 2019-05-22 16:14
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 16:14
 * @UpdateRemark:
 * @Version: 1.0
 */
public class AppStartInfoDataRepository {

    private static final String SUB_TAG = AppStartInfoDataRepository.class.getCanonicalName();

    private ApmDataBase dataBase;

    private static AppStartInfoDataRepository instance;

    public static AppStartInfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (AppStartInfoDataRepository.class) {
                if (instance == null) {
                    instance = new AppStartInfoDataRepository();
                }
            }
        }

        return instance;
    }

    private AppStartInfoDataRepository() {
        dataBase = ApmDataBase.getInstance(Manager.getContext(), Manager.getApmExecutor());
    }

    public void insertAppStartInfo(AppStartInfo info) {
        if (null == info) {
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "AppStartInfo  is  null");
            }
            return;
        }

        AppStartInfoEntity entity = new AppStartInfoEntity();
        entity.setStartTime(info.getStartTime());
        entity.setExtend(info.toString());
        entity.setUploaded(false);

        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.appStartInfoDao().insertAppStartData(entity));
    }

    public void cleanAppStartInfo() {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.appStartInfoDao().cleanAppStartData(TaskConfig.CLEAN_DATA_NUM));
    }

    public List<AppStartInfoEntity> getNotUploadAppStartInfo() {
        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "appStartInfo  getNotUploadAppStartInfo");
        }
        try {
            ApmExecutor executor = Manager.getApmExecutor();
            Future<List<AppStartInfoEntity>> future = executor.diskIO().submit(new AppStartInfoDataRepository.GetNotUploadAppStartInfoCallable());
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetNotUploadAppStartInfoCallable implements Callable<List<AppStartInfoEntity>> {

        @Override
        public List<AppStartInfoEntity> call() throws Exception {
            List<AppStartInfoEntity> appStartInfoEntities = dataBase.appStartInfoDao().getNotUploadAppStartData();
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "appStartInfo  appStartInfoEntities size:" + appStartInfoEntities.size());
            }
            return appStartInfoEntities;
        }
    }

    public void updateAppStartInfo(int id) {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.appStartInfoDao().updateAppStartData(id));
    }

}
