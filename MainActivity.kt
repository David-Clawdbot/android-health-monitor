package com.healthmonitor

import android.Manifest
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var temperatureView: TextView
    private lateinit var activityView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        temperatureView = findViewById(R.id.textView_temperature)
        activityView = findViewById(R.id.textView_activity)
        
        // 模拟数据（实际应从传感器获取）
        val temperature = 25.5f
        val activity = "步行中"
        val healthScore = 85
        
        updateUI(temperature, activity, healthScore)
    }
    
    private fun updateUI(temperature: Float, activity: String, score: Int) {
        temperatureView.text = String.format("当前温度: %.1f°C", temperature)
        activityView.text = String.format("运动状态: %s", activity)
        
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