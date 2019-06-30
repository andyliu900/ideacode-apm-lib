package com.ideacode.apm.library.core.storage;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage
 * @ClassName: DBManager
 * @Description:    数据库控制管理类
 * @Author: randysu
 * @CreateDate: 2019-06-04 16:19
 * @UpdateUser:
 * @UpdateDate: 2019-06-04 16:19
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DBManager {

    private static final String SUB_TAG = DBManager.class.getCanonicalName();

    private static DBManager instance;

    private DBManager() {

    }

    public DBManager getInstance() {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager();
                }
            }
        }

        return instance;
    }



}
