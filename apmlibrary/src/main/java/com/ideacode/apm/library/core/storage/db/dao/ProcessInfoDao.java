package com.ideacode.apm.library.core.storage.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ideacode.apm.library.core.storage.db.entity.ProcessInfoEntity;

import java.util.List;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.dao
 * @ClassName: ProcessInfoDao
 * @Description:    Process数据保存
 * @Author: randysu
 * @CreateDate: 2019-05-23 15:21
 * @UpdateUser:
 * @UpdateDate: 2019-05-23 15:21
 * @UpdateRemark:
 * @Version: 1.0
 */

@Dao
public interface ProcessInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProcessInfoData(ProcessInfoEntity entity);

    @Query("select * from process_info where process_name = :currentProcessName")
    ProcessInfoEntity queryProcessInfoByCurrentProcessName(String currentProcessName);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int updateProcessInfo(ProcessInfoEntity entity);

    @Query("DELETE FROM process_info WHERE is_upload = 1 AND id IN (SELECT id FROM process_info ORDER BY create_time ASC LIMIT :limitNum)")
    void cleanProcessInfo(int limitNum);

    @Query("SELECT * FROM process_info WHERE is_upload = 0")
    List<ProcessInfoEntity> getNotUploadProcessData();

    @Query("UPDATE process_info SET is_upload = 1 WHERE id = :id")
    void updateProcessData(int id);

}
