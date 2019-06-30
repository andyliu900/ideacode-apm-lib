package com.ideacode.apm.library.core.storage;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.job.memory.MemoryInfo;
import com.ideacode.apm.library.core.storage.db.ApmDataBase;
import com.ideacode.apm.library.core.storage.db.entity.MemoryInfoEntity;
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
 * @ClassName: MemoryInfoDataRepository
 * @Description:    MemoryInfo数据库操作类
 * @Author: randysu
 * @CreateDate: 2019-05-23 18:28
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 18:28
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MemoryInfoDataRepository {

    private static final String SUB_TAG = MemoryInfoDataRepository.class.getCanonicalName();

    private ApmDataBase dataBase;

    private static MemoryInfoDataRepository instance;

    public static MemoryInfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (MemoryInfoDataRepository.class) {
                if (instance == null) {
                    instance = new MemoryInfoDataRepository();
                }
            }
        }

        return instance;
    }

    private MemoryInfoDataRepository() {
        dataBase = ApmDataBase.getInstance(Manager.getContext(), Manager.getApmExecutor());
    }

    public void insertProcessInfo(MemoryInfo memoryInfo) {
        if (memoryInfo == null) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "memoryInfo is null");
            }

            return;
        }

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "MemoryInfoDataRepository insertMemoryInfoData processName:" + memoryInfo.currentProcessName);
        }

        MemoryInfoEntity entity = new MemoryInfoEntity();
        entity.setCurrentUsedMem(memoryInfo.currentUsedMem);
        entity.setTotalMem(memoryInfo.totalMem);
        entity.setDalvikPss(memoryInfo.dalvikPss);
        entity.setNativePss(memoryInfo.nativePss);
        entity.setOtherPss(memoryInfo.otherPss);
        entity.setTotalPss(memoryInfo.totalPss);
        entity.setExtend(memoryInfo.toString());
        entity.setUploaded(false);

        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.memoryInfoDao().insertMemoryInfoData(entity));
    }

    public void cleanMemoryInfo() {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.memoryInfoDao().cleanMemoryInfoData(TaskConfig.CLEAN_DATA_NUM));
    }

    public List<MemoryInfoEntity> getNotUploadMemoryInfo() {
        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "memoryInfo  getNotUploadMemoryInfo");
        }
        try {
            ApmExecutor executor = Manager.getApmExecutor();
            Future<List<MemoryInfoEntity>> future = executor.diskIO().submit(new MemoryInfoDataRepository.GetNotUploadMemoryInfoCallable());
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetNotUploadMemoryInfoCallable implements Callable<List<MemoryInfoEntity>> {

        @Override
        public List<MemoryInfoEntity> call() throws Exception {
            List<MemoryInfoEntity> memoryInfoEntities = dataBase.memoryInfoDao().getNotUploadMemoryData();
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "memoryInfo  memoryInfoEntities size:" + memoryInfoEntities.size());
            }
            return memoryInfoEntities;
        }
    }

    public void updateMemoryInfo(int id) {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.memoryInfoDao().updateMemoryData(id));
    }

}
