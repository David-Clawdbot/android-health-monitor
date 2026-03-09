package com.healthmonitor

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*

/**
 * 主Activity - 健康监测App的主入口
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var temperatureView: TextView
    private lateinit var activityView: TextView
    private lateinit var healthScoreView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 初始化视图
        temperatureView = findViewById(R.id.textView_temperature)
        activityView = findViewById(R.id.textView_activity)
        healthScoreView = findViewById(R.id.textView_health_score)
        
        // 模拟数据（实际应从传感器或服务获取）
        val temperature = 25.5f
        val activity = "步行中"
        val healthScore = 85
        
        // 更新UI
        updateUI(temperature, activity, healthScore)
        
        Log.d("MainActivity", "App started successfully")
    }
    
    private fun updateUI(temperature: Float, activity: String, score: Int) {
        // 使用协程异步更新
        GlobalScope.launch(Dispatchers.Main) {
            temperatureView.text = String.format("当前温度: %.1f°C", temperature)
            activityView.text = String.format("运动状态: %s", activity)
            healthScoreView.text = String.format("健康评分: %d分", score)
            
            // 根据温度调整颜色
            when {
                temperature > 28 -> {
                    temperatureView.setTextColor(android.graphics.Color.RED)
                }
                temperature < 20 -> {
                    temperatureView.setTextColor(android.graphics.Color.BLUE)
                }
                else -> {
                    temperatureView.setTextColor(android.graphics.Color.BLACK)
                }
            }
        }
    }
}
