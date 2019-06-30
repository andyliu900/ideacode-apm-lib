package com.ideacode.apm.library.core.job.processinfo;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.cloud.entity.ProcessUploadEntity;
import com.ideacode.apm.library.cloud.http.service.ideacode.IdeacodeApmLoader;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.storage.ProcessInfoDataRepository;
import com.ideacode.apm.library.core.storage.db.entity.ProcessInfoEntity;
import com.ideacode.apm.library.core.tasks.BaseTask;
import com.ideacode.apm.library.core.tasks.ITask;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.AsyncThreadTask;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.processinfo
 * @ClassName: ProcessInfoTask
 * @Description:   进程信息任务
 * @Author: randysu
 * @CreateDate: 2019-05-23 14:24
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 14:24
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ProcessInfoTask extends BaseTask<ProcessInfo> {

    private static final String SUB_TAG = ProcessInfoTask.class.getCanonicalName();

    private IdeacodeApmLoader ideacodeApmLoader;

    @Override
    public void start() {
        super.start();
        getProcessInfo();
    }

    @Override
    public void save(ProcessInfo processInfo) {
        super.save(processInfo);

        if (null == processInfo) {
            return;
        }

        ProcessInfoDataRepository.getInstance().queryProcessInfoByCurrentProcessName(processInfo.processName, entity -> {
            if (null != entity) {
                entity.setStartCount(entity.getStartCount() + 1);
                ProcessInfoDataRepository.getInstance().updateProcessInfo(entity);
            } else {
                ProcessInfoEntity newProcessInfoEntity = new ProcessInfoEntity();
                newProcessInfoEntity.setStartCount(processInfo.startCount);
                newProcessInfoEntity.setExtend(processInfo.toString());

                ProcessInfoDataRepository.getInstance().insertProcessInfo(newProcessInfoEntity);
            }
        });
    }

    @Override
    public String getTaskName() {
        return ApmTask.TASK_PROCESS_INFO;
    }

    private void getProcessInfo() {
        AsyncThreadTask.executeDelayed(() -> {
            if (!isCanWork()) {
                return;
            }

            ProcessInfo info = new ProcessInfo();
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "ProcessInfo process_name:" + info.processName);
            }
            ITask task = Manager.getInstance().getTaskManager().getTask(ApmTask.TASK_PROCESS_INFO);
            if (task != null) {
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "ProcessInfo task save");
                }
                task.save(info);
            } else {
                if (Manager.isDebugLog()) {
                    ApmLogX.d(APM_TAG, SUB_TAG, "ProcessInfo task == null");
                }
            }
        }, 2000 + (int) (Math.round(Math.random() * 1000)));
    }

    @Override
    public void clean() {
        super.clean();

        ProcessInfoDataRepository.getInstance().cleanProcessInfo();
    }

    @Override
    public void upload() {
        super.upload();

        if (ideacodeApmLoader == null) {
            ideacodeApmLoader = new IdeacodeApmLoader();
        }

        List<ProcessInfoEntity> netInfoEntities = ProcessInfoDataRepository.getInstance().getNotUploadProcessInfo();
        List<ProcessUploadEntity> uploadEntities = new ArrayList<>();
        for (ProcessInfoEntity entity : netInfoEntities) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "upload processName:" + entity.getProcessName() + "  startCount:" + entity.getStartCount());
            }

            ProcessUploadEntity uploadEntity = new ProcessUploadEntity();
            uploadEntity.setStart_count(entity.getStartCount());
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

            for (ProcessInfoEntity entity : netInfoEntities) {
                ProcessInfoDataRepository.getInstance().updateProcessInfo(entity.getId());
            }
        }
    }
}
