package com.ideacode.apm.library.core.storage;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.storage.db.ApmDataBase;
import com.ideacode.apm.library.core.storage.db.entity.ProcessInfoEntity;
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
 * @ClassName: ProcessInfoDataRepository
 * @Description:    ProcessInfo数据库操作类
 * @Author: randysu
 * @CreateDate: 2019-05-23 15:37
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 15:37
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ProcessInfoDataRepository {

    private static final String SUB_TAG = ProcessInfoDataRepository.class.getCanonicalName();

    private ApmDataBase dataBase;

    private static ProcessInfoDataRepository instance;

    public static ProcessInfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (ProcessInfoDataRepository.class) {
                if (instance == null) {
                    instance = new ProcessInfoDataRepository();
                }
            }
        }

        return instance;
    }

    private ProcessInfoDataRepository() {
        dataBase = ApmDataBase.getInstance(Manager.getContext(), Manager.getApmExecutor());
    }

    public void queryProcessInfoByCurrentProcessName(String processName, ProcessInfoCallback<ProcessInfoEntity> callback) {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "ProcessInfoDataRepository queryProcessInfoByCurrentProcessName processName:" + processName);
        }
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> {
            ProcessInfoEntity entity = dataBase.processInfoDao().queryProcessInfoByCurrentProcessName(processName);

            if (null != callback) {
                callback.onResult(entity);
            }

        });
    }

    public void insertProcessInfo(ProcessInfoEntity entity) {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "ProcessInfoDataRepository insertProcessInfo processName:" + entity.getProcessName() + "  startCount:" + entity.getStartCount());
        }
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.processInfoDao().insertProcessInfoData(entity));
    }

    public void updateProcessInfo(ProcessInfoEntity entity) {
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "ProcessInfoDataRepository updateProcessInfo processName:" + entity.getProcessName() + "  startCount:" + entity.getStartCount());
        }
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.processInfoDao().updateProcessInfo(entity));
    }

    public void cleanProcessInfo() {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.processInfoDao().cleanProcessInfo(TaskConfig.CLEAN_DATA_NUM));
    }

    public interface ProcessInfoCallback<T> {
        void onResult(T t);
    }

    public List<ProcessInfoEntity> getNotUploadProcessInfo() {
        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "processInfo  getNotUploadProcessInfo");
        }
        try {
            ApmExecutor executor = Manager.getApmExecutor();
            Future<List<ProcessInfoEntity>> future = executor.diskIO().submit(new ProcessInfoDataRepository.GetNotUploadProcessInfoCallable());
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetNotUploadProcessInfoCallable implements Callable<List<ProcessInfoEntity>> {

        @Override
        public List<ProcessInfoEntity> call() throws Exception {
            List<ProcessInfoEntity> processInfoEntities = dataBase.processInfoDao().getNotUploadProcessData();
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "processInfo  processInfoEntities size:" + processInfoEntities.size());
            }
            return processInfoEntities;
        }
    }

    public void updateProcessInfo(int id) {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.processInfoDao().updateProcessData(id));
    }

}
