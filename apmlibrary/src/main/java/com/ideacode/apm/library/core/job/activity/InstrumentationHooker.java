package com.ideacode.apm.library.core.job.activity;

import android.app.Instrumentation;
import android.util.Log;

import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.utils.ApmLogX;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.NoSuchFileException;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.job.activity
 * @ClassName: InstrumentationHooker
 * @Description:   InstrumentationHooker hook
 * @Author: randysu
 * @CreateDate: 2019-05-17 16:50
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 16:50
 * @UpdateRemark:
 * @Version: 1.0
 */
public class InstrumentationHooker {

    private static final String SUB_TAG = InstrumentationHooker.class.getName();

    private static boolean isHookSucceed = false; // 是否已经Hook成功

    public static void doHook() {
        try {
            hookInstrumentation();
            isHookSucceed = true;
        } catch (Exception e) {
            if (Manager.isDebugLog()) {
                ApmLogX.e(APM_TAG, SUB_TAG, Log.getStackTraceString(e.fillInStackTrace()));
            }
        }
    }

    public static boolean isHookSucceed() {
        return isHookSucceed;
    }

    /**
     * 插桩实现
     *
     * @throws ClassNotFoundException
     * @throws NoSuchMethodError
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchFileException
     */
    private static void hookInstrumentation() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, NoSuchFieldException {
        Class<?> c = Class.forName("android.app.ActivityThread");
        Method currentActivityThread = c.getDeclaredMethod("currentActivityThread");
        boolean acc = currentActivityThread.isAccessible();
        if (!acc) {
            currentActivityThread.setAccessible(true);
        }
        Object o = currentActivityThread.invoke(null);
        if (!acc) {
            currentActivityThread.setAccessible(acc);
        }
        Field f = c.getDeclaredField("mInstrumentation");
        acc = f.isAccessible();
        if (!acc) {
            f.setAccessible(true);
        }

        Instrumentation currentInstrumentation = (Instrumentation)f.get(o);
        Instrumentation ins = new ApmInstrumentation(currentInstrumentation);
        f.set(o, ins);
        if (!acc) {
            f.setAccessible(acc);
        }
    }

}
