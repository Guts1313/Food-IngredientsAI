package com.example.test2.data

import android.util.Log
import com.example.test2.domain.RecipeDetailsResponse
import com.example.test2.domain.RecipeService
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val apiClient: RecipeService) {

    suspend fun fetchRecipes(apiKey: String, query: String, number: Int): List<Recipe> {
        try {
            val response = apiClient.getRecipes(apiKey, number, query)
            Log.d("RecipeRepository", "API call success: ${response.results.size} recipes found")
            return response.results
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error fetching recipes", e)
            return emptyList()
        }
    }

    suspend fun fetchRecipeDetails(apiKey: String, recipeId: Int): RecipeDetailsResponse {
        return apiClient.getRecipeDetails(recipeId, apiKey)
    }

}


//class RecipeRepository @Inject constructor(private val apiClient: RecipeService) {