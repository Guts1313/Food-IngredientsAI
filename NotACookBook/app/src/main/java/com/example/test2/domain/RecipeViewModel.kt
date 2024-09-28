package com.example.test2.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test2.data.Recipe
import com.example.test2.data.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        fetchRecipes("13698defe8e74203ba2e4dce02c95f2d", "pasta", 10)  // Pass your API key here
    }

    private fun fetchRecipes(apiKey: String, query: String, number: Int) {
        viewModelScope.launch {
            try {
                val fetchedRecipes = recipeRepository.fetchRecipes(apiKey, query, number)
                _recipes.value = fetchedRecipes
            } catch (e: Exception) {
                // Log and handle errors
                Log.e("RecipeViewModel", "Error fetching recipes", e)
            }
        }
    }
    // Add getRecipeById function
    fun getRecipeById(recipeId: Int): Recipe? {
        return _recipes.value?.find { it.id == recipeId }
    }
    fun fetchRecipeDetails(apiKey: String, recipeId: Int) {
        viewModelScope.launch {
            val recipeDetails = recipeRepository.fetchRecipeDetails(apiKey, recipeId)
            _selectedRecipe.postValue(recipeDetails)
        }
    }


}
