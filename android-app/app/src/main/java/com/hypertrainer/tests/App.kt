package com.hypertrainer.tests

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.hypertrainer.tests.service.UpdateService

class App : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Инициализация канала уведомлений для обновлений
        UpdateService.createNotificationChannel(this)
        
        // Применение сохранённой темы
        val prefs = getSharedPreferences("hypertrainer_prefs", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES 
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
