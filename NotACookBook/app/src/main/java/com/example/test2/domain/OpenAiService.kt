package com.example.test2.domain

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class OpenAIService {

    private val client = OkHttpClient()

    fun sendRequestToOpenAI() {
        val jsonBody = """
        {
            "model": "text-davinci-003",
            "prompt": "Analyze this image to find ingredients: [image description]",
            "max_tokens": 100
        }
        """.trimIndent()

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody)

        val request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .post(requestBody)
            .addHeader("Authorization", "Bearer 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") // Add your OpenAI API key here
            .build()

        // Enqueue the call within this function
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                Log.e("OpenAI", "Request failed: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("OpenAI", "Response: $responseBody")
                // Handle the response here
            }
        })
    }
}
