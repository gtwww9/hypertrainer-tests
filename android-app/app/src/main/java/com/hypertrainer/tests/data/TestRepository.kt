package com.hypertrainer.tests.data

import com.google.gson.GsonBuilder
import com.hypertrainer.tests.model.IndexFile
import com.hypertrainer.tests.model.TestContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TestRepository {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val gson = GsonBuilder().setLenient().create()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/gtwww9/hypertrainer-tests/main/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    private val api = retrofit.create(TestApi::class.java)
    
    suspend fun getIndex(): Result<IndexFile> = withContext(Dispatchers.IO) {
        try {
            val response = api.getIndex()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTestContent(filename: String): Result<TestContent> = withContext(Dispatchers.IO) {
        try {
            val url = "https://raw.githubusercontent.com/gtwww9/hypertrainer-tests/main/$filename"
            val response = api.getTestContent(url)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun checkForUpdates(currentVersion: Int): Result<Int> = withContext(Dispatchers.IO) {
        try {
            // В будущем можно добавить файл version.json в репозиторий
            Result.success(currentVersion)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
