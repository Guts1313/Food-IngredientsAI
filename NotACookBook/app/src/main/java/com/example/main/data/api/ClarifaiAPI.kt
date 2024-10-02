package com.example.main.data.api

import com.example.main.data.dto.ClarifaiResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ClarifaiAPI {
    @POST("/v2/users/clarifai/apps/main/models/food-item-recognition/versions/1d5fd481e0cf4826aa72ec3ff049e044/outputs")
    suspend fun analyzeImage(
        @Header("Authorization") authorization: String,
        @Body requestBody: RequestBody
    ): ClarifaiResponse
}
