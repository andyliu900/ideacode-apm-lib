package com.ideacode.apm.library.core.storage;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.job.block.BlockInfo;
import com.ideacode.apm.library.core.storage.db.ApmDataBase;
import com.ideacode.apm.library.core.storage.db.entity.BlockInfoEntity;
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
 * @ClassName: BlockInfoDataRepository
 * @Description:    BlockInfo数据库操作类
 * @Author: randysu
 * @CreateDate: 2019-05-24 16:30
 * @UpdateUser:
 * @UpdateDate: 2019-05-24 16:30
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BlockInfoDataRepository {

    private static final String SUB_TAG = BlockInfoDataRepository.class.getCanonicalName();

    private ApmDataBase dataBase;

    private static BlockInfoDataRepository instance;

    public static BlockInfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (BlockInfoDataRepository.class) {
                if (instance == null) {
                    instance = new BlockInfoDataRepository();
                }
            }
        }

        return instance;
    }

    private BlockInfoDataRepository() {
        dataBase = ApmDataBase.getInstance(Manager.getContext(), Manager.getApmExecutor());
    }

    public void insertBlockInfo(BlockInfo blockInfo) {
        if (blockInfo == null) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "blockInfo is null");
            }
            return;
        }

        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "BlockInfoDataRepository insertBlockInfo processName:" + blockInfo.currentProcessName);
        }

        BlockInfoEntity entity = new BlockInfoEntity();
        entity.setBlockStack(blockInfo.blockStack);
        entity.setBlockTime(blockInfo.blockTime);
        entity.setExtend(blockInfo.toString());
        entity.setUploaded(false);

        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.blockInfoDao().insertBlockData(entity));
    }

    public void cleanBlockInfo() {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.blockInfoDao().cleanBlockData(TaskConfig.CLEAN_DATA_NUM));
    }

    public List<BlockInfoEntity> getNotUploadBlockInfo() {
        if (Manager.isDebugLog()) {
            ApmLogX.e(APM_TAG, SUB_TAG, "blockInfo  getNotUploadBlockInfo");
        }
        try {
            ApmExecutor executor = Manager.getApmExecutor();
            Future<List<BlockInfoEntity>> future = executor.diskIO().submit(new BlockInfoDataRepository.GetNotUploadBlockInfoCallable());
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetNotUploadBlockInfoCallable implements Callable<List<BlockInfoEntity>> {

        @Override
        public List<BlockInfoEntity> call() throws Exception {
            List<BlockInfoEntity> blockInfoEntities = dataBase.blockInfoDao().getNotUploadBlockData();
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, "blockInfo  blockInfoEntities size:" + blockInfoEntities.size());
            }
            return blockInfoEntities;
        }
    }

    public void updateBlockInfo(int id) {
        ApmExecutor executor = Manager.getApmExecutor();
        executor.diskIO().execute(() -> dataBase.blockInfoDao().updateBlockData(id));
    }

}
