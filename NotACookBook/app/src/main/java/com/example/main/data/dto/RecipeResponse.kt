package com.example.main.data.dto

import com.example.main.domain.model.Recipe

data class RecipeResponse(
    val results: List<Recipe>
)