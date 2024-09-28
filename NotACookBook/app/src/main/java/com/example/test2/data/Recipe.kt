package com.example.test2.data

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val ingredients: List<String>? = null // Ensure this is a list of ingredients
)
