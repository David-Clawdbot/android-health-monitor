package com.healthmonitor.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.healthmonitor.data.HealthDatabase
import com.healthmonitor.data.dao.HealthRecordDao
import com.healthmonitor.data.entities.HealthRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * 健康数据仓库 - Repository模式
 */
class HealthRepository(
    private val database: HealthDatabase,
    private val healthRecordDao: HealthRecordDao
) {
    
    /**
     * 获取当前温度
     */
    fun getCurrentTemperature(): LiveData<Float> {
        // 实际实现中应该从Service获取
        val temperature = MutableLiveData<Float>()
        temperature.postValue(25.0f)
        return temperature
    }
    
    /**
     * 获取温度历史
     */
    fun getTemperatureHistory(limit: Int = 100): LiveData<List<Float>> {
        val history = MutableLiveData<List<Float>>()
        // 实际实现中应该从数据库查询
        
        return history
    }
    
    /**
     * 插入健康记录
     */
    suspend fun insertRecord(record: HealthRecord): Long {
        return healthRecordDao.insert(record)
    }
    
    /**
     * 获取最近记录
     */
    fun getRecentRecords(limit: Int = 100): LiveData<List<HealthRecord>> {
        return healthRecordDao.getRecentRecords(limit).toLiveData()
    }
    
    /**
     * 获取今天的记录流
     */
    fun getTodayRecordsFlow(): Flow<List<HealthRecord>> {
        val dayStart = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
        return healthRecordDao.getRecordsTodayFlow(dayStart)
    }
    
    /**
     * 获取平均健康评分
     */
    suspend fun getAverageHealthScore(): Float? {
        return healthRecordDao.getAverageHealthScore(dayStart = System.currentTimeMillis() - (24 * 60 * 60 * 1000))
    }
}
