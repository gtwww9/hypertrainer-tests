package com.hypertrainer.tests.data

import android.content.Context
import android.content.SharedPreferences
import com.hypertrainer.tests.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("hypertrainer_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val KEY_MISTAKES = "mistakes_"
        private const val KEY_LAST_UPDATE_CHECK = "last_update_check"
        private const val KEY_APP_VERSION = "app_version"
    }
    
    fun getMistakes(testId: String): List<Question> {
        val json = prefs.getString(KEY_MISTAKES + testId, null) ?: return emptyList()
        val type = object : TypeToken<List<Question>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun saveMistakes(testId: String, mistakes: List<Question>) {
        val json = gson.toJson(mistakes)
        prefs.edit().putString(KEY_MISTAKES + testId, json).apply()
    }
    
    fun addMistake(testId: String, question: Question) {
        val currentMistakes = getMistakes(testId).toMutableList()
        if (!currentMistakes.any { it.question == question.question }) {
            currentMistakes.add(question)
            saveMistakes(testId, currentMistakes)
        }
    }
    
    fun removeMistake(testId: String, question: Question) {
        val currentMistakes = getMistakes(testId).toMutableList()
        currentMistakes.removeAll { it.question == question.question }
        saveMistakes(testId, currentMistakes)
    }
    
    fun clearMistakes(testId: String) {
        prefs.edit().remove(KEY_MISTAKES + testId).apply()
    }
    
    fun getLastUpdateCheck(): Long {
        return prefs.getLong(KEY_LAST_UPDATE_CHECK, 0L)
    }
    
    fun setLastUpdateCheck(timestamp: Long) {
        prefs.edit().putLong(KEY_LAST_UPDATE_CHECK, timestamp).apply()
    }
    
    fun getAppVersion(): Int {
        return prefs.getInt(KEY_APP_VERSION, 1)
    }
    
    fun setAppVersion(version: Int) {
        prefs.edit().putInt(KEY_APP_VERSION, version).apply()
    }
}
