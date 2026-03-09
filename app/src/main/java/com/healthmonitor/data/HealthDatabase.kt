package com.healthmonitor.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.healthmonitor.data.dao.HealthRecordDao
import com.healthmonitor.data.entities.HealthRecord

/**
 * 健康监测数据库 - Room Database实现
 */
@Database(
    entities = [HealthRecord::class],
    version = 1,
    exportSchema = false
)
abstract class HealthDatabase : RoomDatabase() {
    
    abstract fun healthRecordDao(): HealthRecordDao
    
    companion object {
        private var INSTANCE: HealthDatabase? = null
        private const val DATABASE_NAME = "health_monitor.db"
        
        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
