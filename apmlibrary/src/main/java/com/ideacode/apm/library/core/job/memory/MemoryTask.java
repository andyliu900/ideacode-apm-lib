package com.ideacode.apm.library.core.job.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.text.format.Formatter;
import android.util.Log;

import com.google.gson.JsonObject;
import com.ideacode.apm.library.api.ApmTask;
import com.ideacode.apm.library.cloud.entity.MemoryUploadEntity;
import com.ideacode.apm.library.cloud.http.service.ideacode.IdeacodeApmLoader;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.TaskConfig;
import com.ideacode.apm.library.core.storage.MemoryInfoDataRepository;
import com.ideacode.apm.library.core.storage.db.entity.MemoryInfoEntity;
import com.ideacode.apm.library.core.tasks.BaseTask;
import com.ideacode.apm.library.utils.ApmLogX;
import com.ideacode.apm.library.utils.AsyncThreadTask;
import com.ideacode.apm.library.utils.PreperenceUtils;
import com.ideacode.apm.library.utils.ProcessUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.memory
 * @ClassName: MemoryTask
 * @Description:    内存收集处理
 * @Author: randysu
 * @CreateDate: 2019-05-23 17:12
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 17:12
 * @UpdateRemark:
 * @Version: 1.0
 *
 *
 * 通过  Debug.MemoryInfo  获得内存信息，这个操作是耗时操作，要在子线程内处理
 * 通过统计所有入口的pss分数，记录内存占用信息
 * https://blog.csdn.net/lihenair/article/details/64476823    android pss 内存分数解释
 */
public class MemoryTask extends BaseTask<MemoryInfo> {

    private static final String SUB_TAG = MemoryTask.class.getCanonicalName();

    private IdeacodeApmLoader ideacodeApmLoader;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isCanWork() || !checkTime()) {
                return;
            }

            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "save memoryinfo");
            }

            MemoryInfo info = getMemoryInfo();
            save(info);
            updateLastTime();

            AsyncThreadTask.executeDelayed(runnable, TaskConfig.MEMORY_TRACE_INTERVAL);
        }
    };

    @Override
    public void start() {
        super.start();

        AsyncThreadTask.executeDelayed(runnable,  TaskConfig.MEMORY_TRACE_INTERVAL + (int)(Math.round(Math.random() * 1000)));
    }

    @Override
    public void save(MemoryInfo memoryInfo) {
        super.save(memoryInfo);

        MemoryInfoDataRepository.getInstance().insertProcessInfo(memoryInfo);
    }

    @Override
    public String getTaskName() {
        return ApmTask.TASK_MEM;
    }

    /**
     * 获取当前内存信息，耗时操作，再子线程操作
     * @return
     */
    private MemoryInfo getMemoryInfo() {

        int pid = android.os.Process.myPid();

        Context context = Manager.getContext();
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[]{pid});

        long currentUsedMem = memoryInfoArray[0].getTotalPrivateDirty();
        long totalMem = getTotalMem();
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG, "当前应用使用内存:" + Formatter.formatFileSize(context, currentUsedMem * 1024) + "的内存" +
                    "  系统总内存:" + Formatter.formatFileSize(context, totalMem * 1024));
        }

        Debug.MemoryInfo info = new Debug.MemoryInfo();
        Debug.getMemoryInfo(info);
        if (Manager.isDebugLog()) {
            ApmLogX.d(APM_TAG, SUB_TAG,
                    "currentProcess:" + ProcessUtils.getCurrentProcessName()
                            + "  dalvikPss:" + info.dalvikPss
                            + "  nativePss:" + info.nativePss
                            + "  otherPass:" + info.otherPss
                            + "  totalPss:" + info.getTotalPss());
        }
        return new MemoryInfo(ProcessUtils.getCurrentProcessName(), currentUsedMem, totalMem, info.dalvikPss, info.nativePss, info.otherPss, info.getTotalPss());
    }

    private boolean checkTime() {
        long diff = System.currentTimeMillis() - PreperenceUtils.getLong(Manager.getContext(), PreperenceUtils.SP_KEY_LAST_MEMORY_TIME, 0L);
        boolean flag = diff > TaskConfig.MEMORY_TRACE_INTERVAL;
        return flag;
    }

    private void updateLastTime() {
        PreperenceUtils.setLong(Manager.getContext(), PreperenceUtils.SP_KEY_LAST_MEMORY_TIME, System.currentTimeMillis());
    }

    @Override
    public void clean() {
        super.clean();

        MemoryInfoDataRepository.getInstance().cleanMemoryInfo();
    }

    private long getTotalMem() {
        String meminfoFile = "/proc/meminfo";
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(meminfoFile);
            BufferedReader bufferedReader = new BufferedReader(localFileReader, 8192);
            String content = bufferedReader.readLine(); // 只读第一行，获得系统总内存信息
            String[] arrayOfString = content.split("\\s+");
            for (String text : arrayOfString) {
                Log.i(SUB_TAG, "value:" + text);
            }
            initial_memory = Long.parseLong(arrayOfString[1]);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return initial_memory;
    }

    @Override
    public void upload() {
        super.upload();

        if (ideacodeApmLoader == null) {
            ideacodeApmLoader = new IdeacodeApmLoader();
        }

        List<MemoryInfoEntity> memoryInfoEntities = MemoryInfoDataRepository.getInstance().getNotUploadMemoryInfo();
        List<MemoryUploadEntity> uploadEntities = new ArrayList<>();
        for (MemoryInfoEntity entity : memoryInfoEntities) {
            if (Manager.isDebugLog()) {
                ApmLogX.d(APM_TAG, SUB_TAG, "currentUserMem:" + entity.getCurrentUsedMem());
            }

            MemoryUploadEntity uploadEntity = new MemoryUploadEntity();
            uploadEntity.setCurrent_used_mem(entity.getCurrentUsedMem());
            uploadEntity.setTotal_mem(entity.getTotalMem());
            uploadEntity.setDalvik_pss(entity.getDalvikPss());
            uploadEntity.setNative_pss(entity.getNativePss());
            uploadEntity.setOther_pss(entity.getOtherPss());
            uploadEntity.setTotal_pss(entity.getTotalPss());
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

            for (MemoryInfoEntity entity : memoryInfoEntities) {
                MemoryInfoDataRepository.getInstance().updateMemoryInfo(entity.getId());
            }
        }
    }
}
