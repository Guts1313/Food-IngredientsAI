package com.example.test2.presentation.n.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.test2.R
import com.example.test2.domain.RecipeViewModel
import com.example.test2.presentation.activities.GlassMorphismLoginPage
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScreen(
    viewModel: RecipeViewModel,
    cameraLauncher: ActivityResultLauncher<Intent> // Pass the camera launcher
) {
    val navController = rememberNavController()

    // State to hold the selected category
    var selectedCategory by remember { mutableStateOf("all") }

    NavHost(
        navController = navController,
        startDestination = "category_selection", // Start with the category selection screen
        modifier = Modifier.fillMaxSize()
    ) {
        // Define the Category Selection route
        composable("category_selection") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header text
                Text(
                    text = "Choose Recipe Type",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                // Four-block clickable section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CategoryBlock("Desserts", R.drawable.deserts, onClick = {
                        selectedCategory = "desserts"
                        viewModel.fetchRecipes("bc931648c46c46ee922d83058dab5a43", selectedCategory, 50)
                    })
                    CategoryBlock("Drinks", R.drawable.drink, onClick = {
                        selectedCategory = "drinks"
                        viewModel.fetchRecipes("bc931648c46c46ee922d83058dab5a43", selectedCategory, 50)
                    })
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CategoryBlock("Main Dishes", R.drawable.maindish, onClick = {
                        selectedCategory = "main course"
                        viewModel.fetchRecipes("bc931648c46c46ee922d83058dab5a43", selectedCategory, 50)
                    })
                    CategoryBlock("Side Dishes", R.drawable.side, onClick = {
                        selectedCategory = "side dish"
                        viewModel.fetchRecipes("bc931648c46c46ee922d83058dab5a43", selectedCategory, 50)
                    })
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Once the user selects a category, display the recipe list
                RecipeListScreen(viewModel = viewModel) { recipe ->
                    navController.navigate("recipe_detail/${recipe.id}")
                }
            }
        }

        // Define the RecipeDetailScreen route
        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
            if (recipeId != null) {
                RecipeDetailScreen(
                    viewModel = viewModel,
                    recipeId = recipeId,
                    apiKey = "bc931648c46c46ee922d83058dab5a43",
                    cameraLauncher = cameraLauncher
                )
            } else {
                Log.e("Navigation", "Invalid recipeId: $recipeId")
            }
        }
    }
}


@Composable
fun CategoryBlock(label: String, imageResId: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(150.dp)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = label,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}


