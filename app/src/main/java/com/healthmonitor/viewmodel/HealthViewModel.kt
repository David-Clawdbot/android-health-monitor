package com.healthmonitor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitor.data.HealthDatabase
import com.healthmonitor.data.dao.HealthRecordDao
import com.healthmonitor.data.entities.HealthRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 健康监测ViewModel - MVVM架构
 */
class HealthViewModel(application: android.app.Application) : ViewModel() {
    
    // 温度数据
    private val _currentTemperature = MutableLiveData<Float>()
    val currentTemperature: LiveData<Float> = _currentTemperature
    
    // 温度历史
    private val _temperatureHistory = MutableLiveData<List<Float>>()
    val temperatureHistory: LiveData<List<Float>> = _temperatureHistory
    
    // 运动状态
    private val _currentActivity = MutableLiveData<String>()
    val currentActivity: LiveData<String> = _currentActivity
    
    // 健康评分
    private val _healthScore = MutableLiveData<Int>()
    val healthScore: LiveData<Int> = _healthScore
    
    // 最近记录
    private val _recentRecords = MutableLiveData<List<HealthRecord>>()
    val recentRecords: LiveData<List<HealthRecord>> = _recentRecords
    
    // 数据库
    private lateinit var database: HealthDatabase
    private lateinit var healthRecordDao: HealthRecordDao
    
    init {
        database = HealthDatabase.getDatabase(application)
        healthRecordDao = database.healthRecordDao()
        
        // 加载最近数据
        loadRecentRecords()
    }
    
    /**
     * 更新当前温度
     */
    fun updateTemperature(temperature: Float) {
        _currentTemperature.postValue(temperature)
        
        // 添加到历史记录
        val history = _temperatureHistory.value ?: mutableListOf()
        history.add(temperature)
        
        // 限制历史记录数量
        if (history.size > 1000) {
            history.removeAt(0)
        }
        
        _temperatureHistory.postValue(history)
        
        // 保存到数据库
        viewModelScope.launch(Dispatchers.IO) {
            val record = HealthRecord(
                timestamp = System.currentTimeMillis(),
                temperature = temperature,
                activityType = "temperature",
                confidence = 1.0f,
                healthScore = calculateHealthScore()
            )
            healthRecordDao.insert(record)
            
            // 清理旧记录（30天前）
            val oldTimestamp = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000)
            healthRecordDao.deleteOldRecords(oldTimestamp)
        }
    }
    
    /**
     * 更新运动状态
     */
    fun updateActivity(activity: String, confidence: Float) {
        _currentActivity.postValue(activity)
        
        // 保存到数据库
        viewModelScope.launch(Dispatchers.IO) {
            val record = HealthRecord(
                timestamp = System.currentTimeMillis(),
                temperature = currentTemperature.value ?: 25.0f,
                activityType = activity,
                confidence = confidence,
                healthScore = _healthScore.value ?: 85
            )
            healthRecordDao.insert(record)
        }
    }
    
    /**
     * 计算健康评分
     */
    fun calculateHealthScore(): Int {
        val temp = currentTemperature.value ?: 25.0f
        val activity = currentActivity.value ?: "unknown"
        
        var score = 85 // 基础分
        
        // 温度因素
        if (temp in 18.0f..24.0f) {
            score += 10
        } else if (temp < 18.0f || temp > 28.0f) {
            score -= 5
        }
        
        // 运动因素
        when (activity) {
            "walking", "running" -> score += 10
            "sitting" -> score -= 5
            "sleeping" -> score += 5
            "standing" -> score += 5
            "lying" -> score += 0
        }
        
        // 限制评分范围
        score = score.coerceIn(50, 100)
        
        _healthScore.postValue(score)
        return score
    }
    
    /**
     * 加载最近记录
     */
    private fun loadRecentRecords() {
        viewModelScope.launch(Dispatchers.IO) {
            val records = healthRecordDao.getRecentRecords(100)
            _recentRecords.postValue(records)
            
            // 设置最新数据
            if (records.isNotEmpty()) {
                _currentTemperature.postValue(records[0].temperature)
                _currentActivity.postValue(records[0].activityType)
                _healthScore.postValue(records[0].healthScore)
            }
        }
    }
    
    /**
     * 获取今天的所有记录
     */
    fun getTodayRecords(): List<HealthRecord> {
        val dayStart = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
        return runBlocking {
            healthRecordDao.getRecordsAfter(dayStart)
        }
    }
    
    /**
     * 按活动类型获取记录
     */
    fun getRecordsByActivity(activityType: String): List<HealthRecord> {
        return runBlocking {
            healthRecordDao.getByActivityType(activityType)
        }
    }
}
