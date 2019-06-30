package com.ideacode.apm.library.core.tasks;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.tasks
 * @ClassName: ITask
 * @Description:  任务接口
 * @Author: randysu
 * @CreateDate: 2019-05-17 15:13
 * @UpdateUser:
 * @UpdateDate: 2019-05-17 15:13
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface ITask<T> {

    String getTaskName();

    void start();

    boolean isCanWork();

    void setCanWork(boolean isCanWork);

    void save(T t);

    void stop();

    void clean();

    void upload();
}
