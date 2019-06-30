package com.ideacode.apm.library.core.job.fps;

import android.view.Choreographer;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.cloud.entity.FpsUploadEntity;
import com.ideacode.apm.library.cloud.http.service.ideacode.IdeacodeApmLoader;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.job.activity.ActivityCore;
import com.ideacode.apm.library.core.storage.FpsInfoDataRepository;
import com.ideacode.apm.library.core.storage.db.entity.FpsInfoEntity;
import com.ideacode.apm.library.core.tasks.BaseTask;
import com.ideacode.apm.library.core.tasks.ITask;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.AsyncThreadTask;
import com.ideacode.apm.library.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.fps
 * @ClassName: FpsTask
 * @Description:    帧率收集task
 * @Author: randysu
 * @CreateDate: 2019-05-22 17:02
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 17:02
 * @UpdateRemark:
 * @Version: 1.0
 *
 *
 * doFrame方法会在收到系统刷新信号后回调，在doFrame中记录刷新帧数，线程runnable每隔1秒计算一次帧率
 * 公式：帧数 ÷ （记录开始时间 - 记录结束时间）= 帧率
 *
 */
public class FpsTask extends BaseTask<FpsInfo> implements Choreographer.FrameCallback {

    private static final String SUB_TAG = FpsTask.class.getCanonicalName();

    private IdeacodeApmLoader ideacodeApmLoader;

    private long mLastFrameTimeNanos = 0; // 最后一次时间
    private long mFrameTimeNanos = 0; // 本次的当前时间
    private int mCurrentCount = 0; // 当前采集的条数
    private int mFpsCount = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isCanWork()) {
                mCurrentCount = 0;
                return;
            }

            calculateFPS();

            mCurrentCount++;
            AsyncThreadTask.executeDelayed(runnable, TaskConfig.FPS_INTERVAL);
        }
    };

    @Override
    public void start() {

        super.start();
        AsyncThreadTask.executeDelayed(runnable, (int)(Math.round(Math.random() * TaskConfig.TASK_DELAY_RANDOM_INTERVAL)));
        Choreographer.getInstance().postFrameCallback(this);
    }

    @Override
    public void save(FpsInfo fpsInfo) {
        super.save(fpsInfo);

        FpsInfoDataRepository.getInstance().insertFpsInfo(fpsInfo);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        mFpsCount++;
        mFrameTimeNanos = frameTimeNanos;
        if (isCanWork()) {
            Choreographer.getInstance().postFrameCallback(this);
        } else {
            mCurrentCount = 0;
        }
    }

    @Override
    public String getTaskName() {
        return ApmTask.TASK_FPS;
    }

    private void calculateFPS() {
        if (mLastFrameTimeNanos == 0) {
            mLastFrameTimeNanos = mFrameTimeNanos;
            return;
        }

        float costTime = (float)(mFrameTimeNanos - mLastFrameTimeNanos) / 1000000.0F; // 转换成单位秒
        if (mFpsCount <= 0 && costTime <= 0.0F) {
            return;
        }

        // 每一帧花费的时间就是帧率，每秒画了多少帧就是帧率
        int fpsResult = (int)(mFpsCount * 1000 / costTime);
        if (fpsResult < 0) {
            return;
        }

//        if (Manager.isDebugLog()) {
//            ApmLogX.e(APM_TAG, SUB_TAG, "fpsResult:" + fpsResult);
//        }

        if (fpsResult <= TaskConfig.DEFAULT_FPS_MIN_COUNT) { // 如果刷新帧率小于30，则视为掉帧，保存帧率信息
            FpsInfo info = new FpsInfo();
            info.activityName = ActivityCore.currentActivityName;
            info.fps = fpsResult;
            info.stack = CommonUtils.getStack();

            ITask task = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_FPS);
            if (task != null) {
                task.save(info);
            } else {
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "FpsInfo task == null");
                }
            }
        }

        mLastFrameTimeNanos = mFrameTimeNanos;
        mFpsCount = 0;
    }

    @Override
    public void clean() {
        super.clean();

        FpsInfoDataRepository.getInstance().cleanFpsInfo();
    }

    @Override
    public void upload() {
        super.upload();

        if (ideacodeApmLoader == null) {
            ideacodeApmLoader = new IdeacodeApmLoader();
        }

        List<FpsInfoEntity> fpsInfoEntities = FpsInfoDataRepository.getInstance().getNotUploadFpsInfo();
        List<FpsUploadEntity> uploadEntities = new ArrayList<>();
        for (FpsInfoEntity entity : fpsInfoEntities) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "upload  fps:" + entity.getFps());
            }

            FpsUploadEntity uploadEntity = new FpsUploadEntity();
            uploadEntity.setActivity_name(entity.getActivityName());
            uploadEntity.setFps(entity.getFps());
            uploadEntity.setStack(entity.getStack());
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

            for (FpsInfoEntity entity : fpsInfoEntities) {
                FpsInfoDataRepository.getInstance().updateFpsInfo(entity.getId());
            }
        }
    }
}
