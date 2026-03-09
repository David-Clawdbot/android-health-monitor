package com.healthmonitor.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

/**
 * 温度监测后台服务
 * 持续监测温度传感器并更新UI
 */
class TemperatureService : Service(), SensorEventListener {
    
    companion object {
        private const val TAG = "TemperatureService"
        private const val CHANNEL_ID = "temperature_channel"
        private const val NOTIFICATION_ID = 1
    }
    
    private lateinit var sensorManager: SensorManager
    private lateinit var temperatureSensor: Sensor?
    private lateinit var notificationManager: NotificationManager
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var temperatureViewUpdater: Handler.Callback
    
    // 绑定
    private val binder = LocalBinder()
    private var currentTemperature: Float = 25.0f
    private val temperatureHistory = mutableListOf<Float>()
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Temperature Service Started")
        
        // 初始化通知
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        
        // 初始化传感器
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        
        // 注册传感器监听
        temperatureSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        
        // 启动为前台服务
        startForegroundService()
        
        // 设置温度更新回调
        temperatureViewUpdater = Handler.Callback { message ->
            if (message.what == UPDATE_TEMPERATURE) {
                val temp = message.obj as Float
                updateTemperatureUI(temp)
            }
        }
        
        // 模拟温度数据（实际应从传感器读取）
        startTemperatureSimulation()
    }
    
    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "健康监测服务",
                NotificationManager.IMPORTANCE_LOW,
                NotificationCompat.PRIORITY_DEFAULT,
                "温度监测运行中"
            )
            
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("健康监测")
            .setContentText("正在监测温度和运动状态")
            .setSmallIcon(android.R.drawable.ic_launcher_foreground)
            .setOngoing(true)
        
        startForeground(NOTIFICATION_ID, notification.build())
    }
    
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            val temperature = event.values[0]
            currentTemperature = temperature
            
            // 添加到历史记录
            temperatureHistory.add(temperature)
            
            // 限制历史记录数量
            if (temperatureHistory.size > 1000) {
                temperatureHistory.removeAt(0)
            }
            
            // 发送温度更新
            val message = handler.obtainMessage(UPDATE_TEMPERATURE, temperature)
            temperatureViewUpdater.sendMessage(message)
            
            // 记录日志
            Log.d(TAG, "Temperature: %.1f°C", temperature)
            
            // 高温或低温告警
            if (temperature > 38.0f) {
                showHighTemperatureAlert(temperature)
            } else if (temperature < 18.0f) {
                showLowTemperatureAlert(temperature)
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // 精度变化时调用
    }
    
    override fun onBind(intent: Intent): IBinder {
        return binder
    }
    
    private fun updateTemperatureUI(temperature: Float) {
        // 这里应该通过EventBus或广播通知UI
        // 简化实现：发送广播
        val intent = Intent("com.healthmonitor.UPDATE_TEMPERATURE")
        intent.putExtra("temperature", temperature)
        sendBroadcast(intent)
    }
    
    private fun showHighTemperatureAlert(temperature: Float) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("⚠️ 高温告警")
            .setContentText(String.format("当前温度：%.1f°C，请注意防暑", temperature))
            .setSmallIcon(android.R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        
        val uniqueId = (System.currentTimeMillis() / 1000).toInt()
        notificationManager.notify(uniqueId, notification.build())
    }
    
    private fun showLowTemperatureAlert(temperature: Float) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("❄️ 低温告警")
            .setContentText(String.format("当前温度：%.1f°C，注意保暖", temperature))
            .setSmallIcon(android.R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        
        val uniqueId = (System.currentTimeMillis() / 1000).toInt()
        notificationManager.notify(uniqueId, notification.build())
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Temperature Service Destroyed")
        
        // 注销传感器
        temperatureSensor?.let {
            sensorManager.unregisterListener(this, it)
        }
        
        // 停止温度模拟
        stopTemperatureSimulation()
    }
    
    companion object {
        private const val UPDATE_TEMPERATURE = 1001
    }
    
    // 内部类 - 本地绑定
    inner class LocalBinder : Binder() {
        fun getService(): TemperatureService = this@TemperatureService
    }
    
    // 模拟温度数据（用于测试，实际应该从传感器读取）
    private val simulationRunnable = object : Runnable {
        override fun run() {
            // 模拟温度变化
            val variation = (Math.random() - 0.5).toFloat()
            currentTemperature = 25.0f + variation
            
            // 保持温度在合理范围内
            if (currentTemperature < 15.0f) currentTemperature = 15.0f
            if (currentTemperature > 40.0f) currentTemperature = 40.0f
            
            // 发送更新
            val message = handler.obtainMessage(UPDATE_TEMPERATURE, currentTemperature)
            temperatureViewUpdater.sendMessage(message)
            
            // 继续模拟
            handler.postDelayed(this, 5000)
        }
    }
    
    private fun startTemperatureSimulation() {
        handler.postDelayed(simulationRunnable, 1000)
    }
    
    private fun stopTemperatureSimulation() {
        handler.removeCallbacks(simulationRunnable)
    }
}
