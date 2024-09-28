package com.example.test2.domain

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ClarifaiService {
    @POST("/v2/users/clarifai/apps/main/models/food-item-recognition/versions/1d5fd481e0cf4826aa72ec3ff049e044/outputs")
    suspend fun analyzeImage(
        @Header("Authorization") authorization: String,
        @Body requestBody: RequestBody
    ): ClarifaiResponse
}
