package com.ideacode.apm.library.core.storage.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ideacode.apm.library.core.storage.db.entity.MemoryInfoEntity;

import java.util.List;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.dao
 * @ClassName: MemoryInfoDao
 * @Description:    Memory数据保存
 * @Author: randysu
 * @CreateDate: 2019-05-23 18:23
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 18:23
 * @UpdateRemark:
 * @Version: 1.0
 */

@Dao
public interface MemoryInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMemoryInfoData(MemoryInfoEntity entity);

    @Query("DELETE FROM memory_info WHERE is_upload = 1 AND id IN (SELECT id FROM memory_info ORDER BY create_time ASC LIMIT :limitNum)")
    void cleanMemoryInfoData(int limitNum);

    @Query("SELECT * FROM memory_info WHERE is_upload = 0")
    List<MemoryInfoEntity> getNotUploadMemoryData();

    @Query("UPDATE memory_info SET is_upload = 1 WHERE id = :id")
    void updateMemoryData(int id);

}
