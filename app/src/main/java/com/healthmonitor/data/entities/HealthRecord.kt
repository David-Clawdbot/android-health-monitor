package com.healthmonitor.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

/**
 * 健康记录实体 - Room数据库表定义
 */
@Entity(tableName = "health_records")
@Indices(Index(value = ["timestamp"]))
data class HealthRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "temperature")
    val temperature: Float,
    
    @ColumnInfo(name = "activity_type")
    val activityType: String,  // walking, running, sitting, sleeping
    
    @ColumnInfo(name = "confidence")
    val confidence: Float,
    
    @ColumnInfo(name = "heart_rate")
    val heartRate: Int? = null,
    
    @ColumnInfo(name = "health_score")
    val healthScore: Int,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
    
    constructor(
        timestamp: Long,
        temperature: Float,
        activityType: String,
        confidence: Float,
        healthScore: Int
    ) {
        this.id = 0
        this.timestamp = timestamp
        this.temperature = temperature
        this.activityType = activityType
        this.confidence = confidence
        this.healthScore = healthScore
        this.createdAt = System.currentTimeMillis()
    }
    
    override fun toString(): String {
        return "HealthRecord(id=$id, temp=$temperature, activity=$activityType, score=$healthScore)"
    }
}
