package com.hypertrainer.tests.data

import com.hypertrainer.tests.model.IndexFile
import com.hypertrainer.tests.model.TestContent
import retrofit2.http.GET
import retrofit2.http.Url

interface TestApi {
    @GET("index.json")
    suspend fun getIndex(): IndexFile
    
    @GET
    suspend fun getTestContent(@Url url: String): TestContent
}
