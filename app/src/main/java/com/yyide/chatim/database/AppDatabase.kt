package com.yyide.chatim.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 *
 * @author liu tao
 * @date 2021/10/14 14:31
 * @description 描述
 */
@Database(
    entities = [ScheduleBean::class, LabelList::class, ParticipantList::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "schedule.db" //数据库名称
                ).allowMainThreadQueries()
                    .build()
            }
            return instance as AppDatabase
        }
    }
}