package com.example.main.domain.repository

import android.util.Log
import com.example.main.data.api.RecipeAPI
import com.example.main.data.dto.RecipeDetailsResponse
import com.example.main.domain.model.Recipe
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val apiClient: RecipeAPI) {

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