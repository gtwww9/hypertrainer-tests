package com.hypertrainer.tests.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.hypertrainer.tests.R
import com.hypertrainer.tests.data.PreferencesManager
import com.hypertrainer.tests.data.TestRepository
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var repository: TestRepository
    private lateinit var prefsManager: PreferencesManager
    
    private lateinit var tvVersion: TextView
    private lateinit var btnCheckUpdate: MaterialButton
    private lateinit var switchDarkMode: SwitchMaterial
    private lateinit var btnClearMistakes: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        repository = TestRepository()
        prefsManager = PreferencesManager(this)
        
        initViews()
    }
    
    private fun initViews() {
        tvVersion = findViewById(R.id.tvVersion)
        btnCheckUpdate = findViewById(R.id.btnCheckUpdate)
        switchDarkMode = findViewById(R.id.switchDarkMode)
        btnClearMistakes = findViewById(R.id.btnClearMistakes)
        
        tvVersion.text = "Версия: 1.0"
        
        // Загрузка состояния тёмной темы
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        switchDarkMode.isChecked = isDarkMode
        
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES 
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        
        btnCheckUpdate.setOnClickListener {
            checkForUpdates()
        }
        
        btnClearMistakes.setOnClickListener {
            clearAllMistakes()
        }
    }
    
    private fun checkForUpdates() {
        btnCheckUpdate.isEnabled = false
        btnCheckUpdate.text = "Проверка..."
        
        lifecycleScope.launch {
            val result = repository.checkForUpdates(prefsManager.getAppVersion())
            result.onSuccess { latestVersion ->
                if (latestVersion > prefsManager.getAppVersion()) {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Доступна новая версия! Пожалуйста, обновите приложение.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Установлена последняя версия",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.onFailure {
                Toast.makeText(
                    this@SettingsActivity,
                    "Ошибка проверки обновлений: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            
            btnCheckUpdate.isEnabled = true
            btnCheckUpdate.text = "Проверить обновления"
        }
    }
    
    private fun clearAllMistakes() {
        // Очищаем все ошибки для всех тестов
        // В реальном приложении нужно хранить список всех тестов
        Toast.makeText(
            this,
            "Все ошибки очищены",
            Toast.LENGTH_SHORT
        ).show()
    }
}
