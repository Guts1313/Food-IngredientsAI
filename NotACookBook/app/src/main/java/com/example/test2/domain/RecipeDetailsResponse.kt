package com.example.test2.domain

import com.example.test2.data.Ingredient

data class RecipeDetailsResponse(
    val id: Int,
    val title: String,
    val image: String,
    val extendedIngredients: List<Ingredient> // Adjust based on the API's response structure
)