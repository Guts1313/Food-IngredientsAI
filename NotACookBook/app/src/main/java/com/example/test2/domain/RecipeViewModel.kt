package com.example.test2.domain

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test2.data.Recipe
import com.example.test2.data.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _selectedRecipe = MutableLiveData<RecipeDetailsResponse>()
    val selectedRecipe: LiveData<RecipeDetailsResponse> get() = _selectedRecipe

    private val _ingredients = MutableLiveData<List<String>>()
    val ingredients: LiveData<List<String>> = _ingredients

    private val clarifaiService: ClarifaiService // For analyzing images

    private val recipeApiKey = "bc931648c46c46ee922d83058dab5a43"
    // Initialization block for setting up Clarifai Retrofit service
    init {
        clarifaiService = setupClarifaiService()
        fetchRecipes(recipeApiKey, "all", 50)
    }

    // Function to set up Retrofit and OkHttpClient for Clarifai API
    private fun setupClarifaiService(): ClarifaiService {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.clarifai.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ClarifaiService::class.java)
    }

    // Function to fetch recipes using the repository
     fun fetchRecipes(apiKey: String, query: String, number: Int) {
        viewModelScope.launch {
            try {
                val fetchedRecipes = recipeRepository.fetchRecipes(apiKey, query, number)
                _recipes.value = fetchedRecipes
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching recipes", e)
            }
        }
    }

    // Function to fetch recipe details using the repository
    fun fetchRecipeDetails(apiKey: String, recipeId: Int) {
        viewModelScope.launch {
            try {
                val recipeDetails = recipeRepository.fetchRecipeDetails(apiKey, recipeId)
                _selectedRecipe.postValue(recipeDetails)
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching recipe details", e)
            }
        }
    }

    // Function to get a specific recipe by ID
    fun getRecipeById(recipeId: Int): Recipe? {
        return _recipes.value?.find { it.id == recipeId }
    }

    // Function to analyze image and update ingredients using Clarifai API
    fun analyzeImageAndUpdateIngredients(imageBytes: ByteArray, apiKey: String) {
        // Convert image to Base64
        val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP) // Remove any newlines

        val requestBody = """
            {
                "inputs": [
                    {
                        "data": {
                            "image": {
                                "base64": "$base64Image"
                            }
                        }
                    }
                ]
            }
        """.trimIndent().toRequestBody("application/json".toMediaTypeOrNull())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Call Clarifai API
                val response = clarifaiService.analyzeImage("Key $apiKey", requestBody)

                if (response.status?.code == 10000) {
                    // Extract ingredients from response
                    Log.d("Clarifai", "Successful image processing: ${response.status?.code}")
                    val ingredients = response.outputs?.get(0)?.data?.concepts?.mapNotNull { it.name } ?: emptyList()
                    _ingredients.postValue(ingredients) // Update ingredients LiveData
                } else {
                    Log.e("ClarifaiError", "Failed with status code: ${response.status?.code}")
                    _ingredients.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.e("ClarifaiError", "Error analyzing image: ${e.message}")
                _ingredients.postValue(emptyList()) // Handle failure
            }
        }
    }
}


