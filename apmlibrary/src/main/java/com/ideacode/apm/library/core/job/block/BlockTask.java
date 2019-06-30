package com.ideacode.apm.library.core.job.block;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.cloud.entity.BlockUploadEntity;
import com.ideacode.apm.library.cloud.http.service.ideacode.IdeacodeApmLoader;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.storage.BlockInfoDataRepository;
import com.ideacode.apm.library.core.storage.db.entity.BlockInfoEntity;
import com.ideacode.apm.library.core.tasks.BaseTask;
import com.ideacode.apm.library.core.tasks.ITask;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.AsyncThreadTask;
import com.ideacode.apm.library.utils.CommonUtils;
import com.ideacode.apm.library.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.block
 * @ClassName: BlockTask
 * @Description:    卡顿task
 * @Author: randysu
 * @CreateDate: 2019-05-24 13:53
 * @UpdateUser:
 * @UpdateDate: 2019-05-24 13:53
 * @UpdateRemark:
 * @Version: 1.0
 *
 * StackTrace简述
 * 1 StackTrace用栈的形式保存了方法的调用信息.
 * 2 怎么获取这些调用信息呢?
 *   可用Thread.currentThread().getStackTrace()方法
 *   得到当前线程的StackTrace信息.
 *   该方法返回的是一个StackTraceElement数组.
 * 3 该StackTraceElement数组就是StackTrace中的内容.
 * 4 遍历该StackTraceElement数组.就可以看到方法间的调用流程.
 *   比如线程中methodA调用了methodB那么methodA先入栈methodB再入栈.
 * 5 在StackTraceElement数组下标为2的元素中保存了当前方法的所属文件名,当前方法所属
 *   的类名,以及该方法的名字.除此以外还可以获取方法调用的行数.
 * 6 在StackTraceElement数组下标为3的元素中保存了当前方法的调用者的信息和它调用
 *   时的代码行数.
 *
 *
 *
 * 卡顿监控实现逻辑：
 * 1、通过拦截MainLooper的Printer，消息分发前会打印 ">>>>> Dispatching" 关键字，消息分发后会打印 "<<<<< Finished" 关键字
 * 2、mHandler在消息开始分发时向HandlerThread发送runnable消息，延时4500毫秒发送
 * 3、MainLooper在分发消息过程时间如果小于4500毫秒，则不会响应HandlerThread的runnable，因为mHandler已经将该runnable消息remove了，
 *   否则消息分发过程大于4500毫秒，视为卡顿，记录卡顿信息
 */
public class BlockTask extends BaseTask<BlockInfo> {

    private static final String SUB_TAG = BlockTask.class.getCanonicalName();

    private IdeacodeApmLoader ideacodeApmLoader;

    private HandlerThread mBlockThread = new HandlerThread("blockThread");
    private Handler mHandler;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isCanWork()) {
                return;
            }

            String stackInfo = CommonUtils.getStack();

            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "thread stack info:" + stackInfo);
            }
            saveBlockInfo(stackInfo);
        }
    };

    @Override
    public String getTaskName() {
        return ApmTask.TASK_BLOCK;
    }

    @Override
    public void start() {
        super.start();

        if (!mBlockThread.isAlive()) {
            mBlockThread.start();
            mHandler = new Handler(mBlockThread.getLooper());
            ApmLogX.i(APM_TAG, SUB_TAG, "blockThread Looper:" + mBlockThread.getLooper() + "  mainLooper:" + Looper.getMainLooper());

            // 监听主线程的Looper分发消息入口
            Looper.getMainLooper().setMessageLogging(new Printer() {

                // 这里通过设置新的Printer来hook Looper的消息遍历操作，监听分发消息开始和结束关键字
                private static final String START = ">>>>> Dispatching";
                private static final String END = "<<<<< Finished";

                @Override
                public void println(String x) {
                    if (x.startsWith(START)) {
                        mHandler.postDelayed(runnable, TaskConfig.DEFAULE_BLOCK_TIME);
                    }
                    if (x.startsWith(END)) {
                        mHandler.removeCallbacks(runnable);
                    }
                }
            });
        }
    }

    @Override
    public void save(BlockInfo blockInfo) {
        super.save(blockInfo);

        BlockInfoDataRepository.getInstance().insertBlockInfo(blockInfo);
    }

    private void saveBlockInfo(String stack) {
        AsyncThreadTask.execute(() -> {
            BlockInfo info = new BlockInfo();
            info.currentProcessName = ProcessUtils.getCurrentProcessName();
            info.blockStack = stack;
            info.blockTime = TaskConfig.DEFAULE_BLOCK_TIME;
            ITask task = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_BLOCK);
            if (task != null) {
                task.save(info);
            } else {
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "BlockInfo task == null");
                }
            }
        });
    }

    @Override
    public void clean() {
        super.clean();

        BlockInfoDataRepository.getInstance().cleanBlockInfo();
    }

    @Override
    public void upload() {
        super.upload();

        if (ideacodeApmLoader == null) {
            ideacodeApmLoader = new IdeacodeApmLoader();
        }

        List<BlockInfoEntity> blockInfoEntities = BlockInfoDataRepository.getInstance().getNotUploadBlockInfo();
        List<BlockUploadEntity> uploadEntities = new ArrayList<>();
        for (BlockInfoEntity entity : blockInfoEntities) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "upload blockTime:" + entity.getBlockTime());
            }

            BlockUploadEntity uploadEntity = new BlockUploadEntity();
            uploadEntity.setBlock_stack(entity.getBlockStack());
            uploadEntity.setBlock_time(entity.getBlockTime());
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

            for (BlockInfoEntity entity : blockInfoEntities) {
                BlockInfoDataRepository.getInstance().updateBlockInfo(entity.getId());
            }
        }
    }
}
