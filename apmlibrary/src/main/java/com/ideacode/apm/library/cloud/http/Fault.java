package com.ideacode.apm.library.cloud.http;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.cloud.http
 * @ClassName: Fault
 * @Description: 异常处理类，将异常包装成一个 Fault ,抛给上层统一处理
 * @Author: randysu
 * @CreateDate: 2019/3/2 5:26 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 5:26 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Fault extends RuntimeException {

    private int errorCode;

    public Fault(int errorCode,String message){
        super(message);
        errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
