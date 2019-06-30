package com.ideacode.apm.library.core.storage.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.ideacode.apm.library.ApmExecutor;
import com.ideacode.apm.library.Env;
import com.ideacode.apm.library.core.Manager;
import com.ideacode.apm.library.core.storage.db.converter.BooleanConverter;
import com.ideacode.apm.library.core.storage.db.converter.DateConverter;
import com.ideacode.apm.library.core.storage.db.dao.ActivityInfoDao;
import com.ideacode.apm.library.core.storage.db.dao.AppStartInfoDao;
import com.ideacode.apm.library.core.storage.db.dao.BlockInfoDao;
import com.ideacode.apm.library.core.storage.db.dao.FpsInfoDao;
import com.ideacode.apm.library.core.storage.db.dao.MemoryInfoDao;
import com.ideacode.apm.library.core.storage.db.dao.NetInfoDao;
import com.ideacode.apm.library.core.storage.db.dao.ProcessInfoDao;
import com.ideacode.apm.library.core.storage.db.entity.ActivityInfoEntity;
import com.ideacode.apm.library.core.storage.db.entity.AppStartInfoEntity;
import com.ideacode.apm.library.core.storage.db.entity.BlockInfoEntity;
import com.ideacode.apm.library.core.storage.db.entity.FpsInfoEntity;
import com.ideacode.apm.library.core.storage.db.entity.MemoryInfoEntity;
import com.ideacode.apm.library.core.storage.db.entity.NetInfoEntity;
import com.ideacode.apm.library.core.storage.db.entity.ProcessInfoEntity;
import com.ideacode.apm.library.utils.ApmLogX;

import static com.ideacode.apm.library.Env.APM_TAG;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage.db
 * @ClassName: ApmDataBase
 * @Description:    APM监控信息数据库
 * @Author: randysu
 * @CreateDate: 2019-05-22 10:32
 * @UpdateUser:
 * @UpdateDate: 2019-05-22 10:32
 * @UpdateRemark:
 * @Version: 1.0
 *
 * 升级时要指定字段是否null，default值
 */

@Database(entities = {
        ActivityInfoEntity.class,
        AppStartInfoEntity.class,
        ProcessInfoEntity.class,
        MemoryInfoEntity.class,
        BlockInfoEntity.class,
        FpsInfoEntity.class,
        NetInfoEntity.class},
        version = 1)
@TypeConverters({DateConverter.class, BooleanConverter.class})
public abstract class ApmDataBase extends RoomDatabase {

    private static final String SUB_TAG = ApmDataBase.class.getCanonicalName();

    private static ApmDataBase instance;

    private boolean mIsDatabaseCreated = true;

    public static ApmDataBase getInstance(Context context, ApmExecutor executor) {
        if (instance == null) {
            synchronized (ApmDataBase.class) {
                if (instance == null) {
                    instance = buildDataBase(context, executor);
                }
            }
        }

        return instance;
    }

    private static ApmDataBase buildDataBase(Context context, ApmExecutor executor) {
        return Room.databaseBuilder(context, ApmDataBase.class, Env.APM_DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executor.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                // 返回的是同一个对象
                                ApmDataBase dataBase = ApmDataBase.getInstance(context, executor);
                                dataBase.setDatabaseCreated();

                                if (Manager.isDebugLog()) {
                                    SupportSQLiteOpenHelper openHelper = dataBase.getOpenHelper();
                                    ApmLogX.i(APM_TAG, SUB_TAG, "DB created name:" + openHelper.getDatabaseName()
                                            + "  version:" + openHelper.getReadableDatabase().getVersion());
                                }
                            }
                        });
                    }
                })
//                .addMigrations(MIGRATION_1_2)
                .build();
    }

//    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE memory_info ADD COLUMN current_used_mem INTEGER NOT NULL DEFAULT 0");
//            database.execSQL("ALTER TABLE memory_info ADD COLUMN total_mem INTEGER NOT NULL DEFAULT 0");
//        }
//    };

    private void setDatabaseCreated() {
        mIsDatabaseCreated = true;
    }

    public abstract ActivityInfoDao activityInfoDao();

    public abstract AppStartInfoDao appStartInfoDao();

    public abstract ProcessInfoDao processInfoDao();

    public abstract MemoryInfoDao memoryInfoDao();

    public abstract BlockInfoDao blockInfoDao();

    public abstract FpsInfoDao fpsInfoDao();

    public abstract NetInfoDao netInfoDao();

}
