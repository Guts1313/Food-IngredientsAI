package com.example.test2.domain

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ClarifaiService {
    @POST("/v2/models/food-item-recognition/outputs")
    fun analyzeImage(
        @Header("Authorization") authorization: String, // Bearer token for API Key
        @Body image: RequestBody // Send image bytes
    ): Call<ClarifaiResponse> // Specify the response type
}
