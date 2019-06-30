package com.ideacode.apm.library.core.storage.db.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ideacode.apm.library.core.storage.db.entity.ActivityInfoEntity;

import java.util.List;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.dao
 * @ClassName: ActivityInfoDao
 * @Description:    Activity数据保存
 * @Author: randysu
 * @CreateDate: 2019-05-22 13:56
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 13:56
 * @UpdateRemark:
 * @Version: 1.0
 */

@Dao
public interface ActivityInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertActivityData(ActivityInfoEntity entity);

    @Query("DELETE FROM activity_info WHERE is_upload = 1 AND id IN (SELECT id FROM activity_info ORDER BY create_time ASC LIMIT :limitNum)")
    void cleanActivityData(int limitNum);

    @Query("SELECT * FROM activity_info WHERE is_upload = 0")
    List<ActivityInfoEntity> getNotUploadActivityData();

    @Query("UPDATE activity_info SET is_upload = 1 WHERE id = :id")
    void updateActivityData(int id);

}
