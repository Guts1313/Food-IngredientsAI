package com.example.test2.data

data class Ingredient(
    val id: Int? = null, // Make id optional or nullable
    val name: String,
    val amount: Double,
    val unit: String
)