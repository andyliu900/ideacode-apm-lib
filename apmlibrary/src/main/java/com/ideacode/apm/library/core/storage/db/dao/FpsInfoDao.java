package com.ideacode.apm.library.core.storage.db.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ideacode.apm.library.core.storage.db.entity.FpsInfoEntity;

import java.util.List;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.dao
 * @ClassName: FpsInfoDao
 * @Description:    刷新帧率数据保存
 * @Author: randysu
 * @CreateDate: 2019-05-28 09:59
 * @UpdateUser:
 * @UpdateDate: 2019-05-28 09:59
 * @UpdateRemark:
 * @Version: 1.0
 */

@Dao
public interface FpsInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFpsData(FpsInfoEntity entity);

    @Query("DELETE FROM fps_info WHERE is_upload = 1 AND id IN (SELECT id FROM fps_info ORDER BY create_time ASC LIMIT :limitNum)")
    void cleanFpsData(int limitNum);

    @Query("SELECT * FROM fps_info WHERE is_upload = 0")
    List<FpsInfoEntity> getNotUploadFpsData();

    @Query("UPDATE fps_info SET is_upload = 1 WHERE id = :id")
    void updateFpsData(int id);

}
