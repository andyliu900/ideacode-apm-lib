package com.ideacode.apm.library.core.storage.db.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ideacode.apm.library.core.storage.db.entity.BlockInfoEntity;

import java.util.List;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db.dao
 * @ClassName: BlockInfoDao
 * @Description:    Block数据保存
 * @Author: randysu
 * @CreateDate: 2019-05-24 16:27
 * @UpdateUser:
 * @UpdateDate: 2019-05-24 16:27
 * @UpdateRemark:
 * @Version: 1.0
 */

@Dao
public interface BlockInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBlockData(BlockInfoEntity entity);

    @Query("DELETE FROM block_info WHERE is_upload = 1 AND id IN (SELECT id FROM block_info ORDER BY create_time ASC LIMIT :limitNum)")
    void cleanBlockData(int limitNum);

    @Query("SELECT * FROM block_info WHERE is_upload = 0")
    List<BlockInfoEntity> getNotUploadBlockData();

    @Query("UPDATE block_info SET is_upload = 1 WHERE id = :id")
    void updateBlockData(int id);

}
