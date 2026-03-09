package com.healthmonitor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthmonitor.data.entities.HealthRecord
import kotlinx.coroutines.flow.Flow

/**
 * 健康记录数据访问对象 - Room DAO接口
 */
@Dao
interface HealthRecordDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HealthRecord): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<HealthRecord>)
    
    @Query("SELECT * FROM health_records ORDER BY timestamp DESC LIMIT 100")
    suspend fun getRecentRecords(limit: Int = 100): List<HealthRecord>
    
    @Query("SELECT * FROM health_records WHERE timestamp > :startTime")
    suspend fun getRecordsAfter(startTime: Long): List<HealthRecord>
    
    @Query("SELECT * FROM health_records WHERE activity_type = :activityType ORDER BY timestamp DESC LIMIT 50")
    suspend fun getByActivityType(activityType: String): List<HealthRecord>
    
    @Query("SELECT AVG(health_score) FROM health_records WHERE timestamp > :dayStart")
    suspend fun getAverageHealthScore(dayStart: Long): Float?
    
    @Query("SELECT * FROM health_records WHERE timestamp > :oldTimestamp")
    suspend fun deleteOldRecords(oldTimestamp: Long): Int
    
    @Query("SELECT * FROM health_records WHERE timestamp BETWEEN :startTime AND :endTime")
    suspend fun getRecordsByTimeRange(startTime: Long, endTime: Long): List<HealthRecord>
    
    @Query("SELECT COUNT(*) FROM health_records")
    suspend fun getTotalCount(): Int
    
    @Query("SELECT * FROM health_records WHERE timestamp > :dayStart")
    fun getRecordsTodayFlow(dayStart: Long): Flow<HealthRecord>
}
