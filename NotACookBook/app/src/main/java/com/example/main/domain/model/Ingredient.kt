package com.example.main.domain.model

data class Ingredient(
    val id: Int? = null, // Make id optional or nullable
    val name: String,
    val amount: Double,
    val unit: String
)