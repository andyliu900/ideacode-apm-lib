package com.ideacode.apm.library.core.storage.db.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ideacode.apm.library.core.storage.db.entity.AppStartInfoEntity;

import java.util.List;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.dao
 * @ClassName: AppStartInfoDao
 * @Description:    AppStart数据保存
 * @Author: randysu
 * @CreateDate: 2019-05-22 16:22
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 16:22
 * @UpdateRemark:
 * @Version: 1.0
 */

@Dao
public interface AppStartInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppStartData(AppStartInfoEntity entity);

    @Query("DELETE FROM appstart_info WHERE is_upload = 1 AND id IN (SELECT id FROM appstart_info ORDER BY create_time ASC LIMIT :limitNum)")
    void cleanAppStartData(int limitNum);

    @Query("SELECT * FROM appstart_info WHERE is_upload = 0")
    List<AppStartInfoEntity> getNotUploadAppStartData();

    @Query("UPDATE appstart_info SET is_upload = 1 WHERE id = :id")
    void updateAppStartData(int id);

}
