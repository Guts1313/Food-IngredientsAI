package com.example.test2.data

import android.util.Log
import com.example.test2.domain.ClarifaiResponse
import com.example.test2.domain.ClarifaiService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ClarifaiRepository @Inject constructor() {

    private val clarifaiService: ClarifaiService
    private val apiKey = "4711c62afe194d6490fa8390b9da2681" // API key stored here

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.clarifai.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        clarifaiService = retrofit.create(ClarifaiService::class.java)
    }

    fun analyzeImage(imageBytes: ByteArray, onResult: (List<String>) -> Unit) {
        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)

        clarifaiService.analyzeImage("Key $apiKey", requestBody)
            .enqueue(object : Callback<ClarifaiResponse> {
                override fun onResponse(call: Call<ClarifaiResponse>, response: Response<ClarifaiResponse>) {
                    if (response.isSuccessful) {
                        val ingredients = response.body()?.outputs?.firstOrNull()?.data?.map { it.name } ?: emptyList()
                        onResult(ingredients)
                    } else {
                        Log.e("ClarifaiError", "Failed with error code: ${response.code()}")
                        onResult(emptyList())
                    }
                }

                override fun onFailure(call: Call<ClarifaiResponse>, t: Throwable) {
                    Log.e("ClarifaiError", "Failed to analyze image: ${t.message}")
                    onResult(emptyList())
                }
            })
    }
}




