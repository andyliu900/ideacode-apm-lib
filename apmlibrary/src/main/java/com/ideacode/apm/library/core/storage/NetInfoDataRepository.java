package com.ideacode.apm.library.core.storage;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.job.net.NetInfo;
import com.ideacode.apm.library.core.storage.db.ApmDataBase;
import com.ideacode.apm.library.core.storage.db.entity.NetInfoEntity;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.TimeUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage
 * @ClassName: NetInfoDataRepository
 * @Description:    NetInfo数据库操作类
 * @Author: randysu
 * @CreateDate: 2019-05-23 18:28
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 18:28
 * @UpdateRemark:
 * @Version: 1.0
 */
public class NetInfoDataRepository {

    private static final String SUB_TAG = NetInfoDataRepository.class.getCanonicalName();

    private ApmDataBase dataBase;

    private static NetInfoDataRepository instance;

    public static NetInfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (NetInfoDataRepository.class) {
                if (instance == null) {
                    instance = new NetInfoDataRepository();
                }
            }
        }

        return instance;
    }

    private NetInfoDataRepository() {
        dataBase = ApmDataBase.getInstance(Manager.getContext(), Manager.getApmExecutor());
    }

    public void insertNetInfo(NetInfo info) {
        if (info == null) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "NetInfo is null");
            }
            return;
        }

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "NetInfoDataRepository insertNetInfo url:" + info.url);
        }

        NetInfoEntity entity = new NetInfoEntity();
        entity.setUrl(info.url);
        entity.setHeaders(info.headers);
        entity.setMethod(info.method);
        entity.setSentBytes(info.sentBytes);
        entity.setReceivedBytes(info.receivedBytes);
        entity.setStartTime(TimeUtils.long2Date(info.startTime));
        entity.setCostTime(info.costTime);
        entity.setWifi(info.isWifi);
        entity.setStatusCode(info.statusCode);
        entity.setErrorMessage(info.errorMessage);
        entity.setExtend(info.toString());
        entity.setUploaded(false);

        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.netInfoDao().insertNetInfoData(entity));
    }

    public void cleanNetInfo() {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.netInfoDao().cleanNetInfoData(TaskConfig.CLEAN_DATA_NUM));
    }

    public List<NetInfoEntity> getNotUploadNetInfo() {
        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "netInfo  getNotUploadNetInfo");
        }
        try {
            ApmExecutor executor = Manager.getApmExecutor();
            Future<List<NetInfoEntity>> future = executor.diskIO().submit(new NetInfoDataRepository.GetNotUploadNetInfoCallable());
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetNotUploadNetInfoCallable implements Callable<List<NetInfoEntity>> {

        @Override
        public List<NetInfoEntity> call() throws Exception {
            List<NetInfoEntity> netInfoEntities = dataBase.netInfoDao().getNotUploadNetData();
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "netInfo  netInfoEntities size:" + netInfoEntities.size());
            }
            return netInfoEntities;
        }
    }

    public void updateNetInfo(int id) {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.netInfoDao().updateNetData(id));
    }

}
