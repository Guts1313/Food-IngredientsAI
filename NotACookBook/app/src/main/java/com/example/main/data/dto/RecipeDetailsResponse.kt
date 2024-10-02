package com.example.main.data.dto

import com.example.main.domain.model.Ingredient

data class RecipeDetailsResponse(
    val id: Int,
    val title: String,
    val image: String,
    val extendedIngredients: List<Ingredient> // Adjust based on the API's response structure
)