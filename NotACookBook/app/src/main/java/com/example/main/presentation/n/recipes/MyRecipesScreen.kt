package com.example.main.presentation.n.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.main.domain.model.Recipe
import com.example.main.data.dto.RecipeDetailsResponse

@Composable
fun MyRecipesScreen(
    viewModel: RecipeViewModel,
    navController: NavController // Add NavController to handle navigation
) {
    // Observe the saved recipes from the ViewModel
    val myRecipes by viewModel.savedRecipes.observeAsState(emptyList()) // This should be a List<RecipeDetailsResponse>

    // Fetch the saved recipes from Firebase when the screen is opened
    LaunchedEffect(Unit) {
        viewModel.fetchSavedRecipes() // Trigger the ViewModel to load the recipes from Firebase
    }
    fun convertToRecipe(recipeDetailsResponse: RecipeDetailsResponse): Recipe {
        return Recipe(
            id = recipeDetailsResponse.id,
            title = recipeDetailsResponse.title,
            image = recipeDetailsResponse.image,
            ingredients = recipeDetailsResponse.extendedIngredients.map { ingredient ->
                ingredient.name // Extract the name of each ingredient
            }
        )
    }
    // Display the saved recipes in a LazyColumn
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Use a grid format for the saved recipes
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Set black background
            .padding(16.dp)
    ) {

        items(myRecipes.size) { index ->
            val recipeDetailsResponse = myRecipes[index]

            // Call the RecipeCard composable and handle click event
            RecipeCard(recipe = convertToRecipe(recipeDetailsResponse), onRecipeClick = {
                // Navigate to the detail screen passing the entire RecipeDetailsResponse
                navController.navigate("recipe_detail_saved") {
                    // Pass the recipe details via a route parameter, or ideally, using a SharedViewModel or navigation arguments
                    viewModel.setSelectedRecipe(recipeDetailsResponse)
                }
            })
        }
    }
}


