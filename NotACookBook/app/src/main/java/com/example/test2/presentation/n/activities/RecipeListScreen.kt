package com.example.test2.presentation.n.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test2.R
import com.example.test2.data.Recipe
import com.example.test2.domain.RecipeViewModel
import java.io.ByteArrayOutputStream

@Composable
fun RecipeListScreen(viewModel: RecipeViewModel = hiltViewModel(), onRecipeClick: (Recipe) -> Unit) {
    val recipes by viewModel.recipes.observeAsState(emptyList()) // Observe recipes from ViewModel

    // Use LazyVerticalGrid to display recipes in a grid format
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        // Use the items function to iterate over the recipes list
        items(recipes) { recipe ->
            // Call the RecipeCard composable and handle click event
            RecipeCard(recipe = recipe, onRecipeClick = {
                onRecipeClick(recipe) // Trigger navigation to recipe detail when clicked
            })
        }
    }
}


@Composable
fun RecipeDetailScreen(
    viewModel: RecipeViewModel,
    recipeId: Int,
    apiKey: String,
    cameraLauncher: ActivityResultLauncher<Intent>
) {
    // Fetch the recipe details
    LaunchedEffect(recipeId) {
        viewModel.fetchRecipeDetails(apiKey, recipeId)
    }

    val recipeDetails by viewModel.selectedRecipe.observeAsState()
    val ingredients by viewModel.ingredients.observeAsState(emptyList())
    val context = LocalContext.current

    // Separate state to control the expand/collapse behavior for Ingredients and Analyzed Ingredients
    var isIngredientsExpanded by remember { mutableStateOf(false) }
    var isAnalyzedIngredientsExpanded by remember { mutableStateOf(false) } // Separate state for Analyzed Ingredients

    recipeDetails?.let { recipe ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Recipe image
                Image(
                    painter = rememberImagePainter(data = recipe.image),
                    contentDescription = null,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Recipe title
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ingredients section header (collapsible)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isIngredientsExpanded = !isIngredientsExpanded }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ingredients:",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Icon(
                        imageVector = if (isIngredientsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isIngredientsExpanded) "Collapse" else "Expand"
                    )
                }

                // Collapsible ingredients list
                if (isIngredientsExpanded) {
                    LazyColumn {
                        items(recipe.extendedIngredients) { ingredient ->
                            Text(
                                text = "${ingredient.amount} ${ingredient.unit} of ${ingredient.name}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button to trigger the camera and capture the image
                Button(onClick = {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent) // Open the camera
                }) {
                    Text(text = "Capture Image")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // New "Test Analysis" button to use predefined image
                Button(onClick = {
                    val imageBytes = captureTestImage(context) // Use the predefined image
                    if (imageBytes != null) {
                        val apiKey = "e8da20d8077445c0b5755c1eebcdf938"
                        viewModel.analyzeImageAndUpdateIngredients(imageBytes, apiKey) // Trigger image analysis
                    } else {
                        Toast.makeText(context, "Failed to load test image", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text(text = "Test Analysis")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Analyzed Ingredients section header (collapsible)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isAnalyzedIngredientsExpanded = !isAnalyzedIngredientsExpanded } // Use separate state
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Analyzed Ingredients:",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Icon(
                        imageVector = if (isAnalyzedIngredientsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isAnalyzedIngredientsExpanded) "Collapse" else "Expand"
                    )
                }

                // Collapsible analyzed ingredients list
                if (isAnalyzedIngredientsExpanded) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        items(ingredients) { ingredient ->
                            Text(
                                text = ingredient,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    } ?: Text(text = "Loading...", style = MaterialTheme.typography.headlineLarge)
}















fun captureTestImage(context: Context): ByteArray? {
    // Get the drawable resource (replace `R.drawable.test_image` with your image resource)
    val drawable = ContextCompat.getDrawable(context, R.drawable.hamburger)

    // Convert the drawable to a Bitmap
    val bitmap = when (drawable) {
        is BitmapDrawable -> drawable.bitmap
        else -> {
            // Create a bitmap from other drawable types if it's not a BitmapDrawable
            val width = drawable?.intrinsicWidth ?: 1
            val height = drawable?.intrinsicHeight ?: 1
            val config = Bitmap.Config.ARGB_8888
            val bitmap = Bitmap.createBitmap(width, height, config)
            val canvas = Canvas(bitmap)
            drawable?.setBounds(0, 0, canvas.width, canvas.height)
            drawable?.draw(canvas)
            bitmap
        }
    }

    // Convert the bitmap to a byte array
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}



@Composable
fun RecipeCard(recipe: Recipe, onRecipeClick: (Recipe) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onRecipeClick(recipe) }, // Call the onRecipeClick lambda on click
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            // Recipe image
            Image(
                painter = rememberImagePainter(data = recipe.image),
                contentDescription = null,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            // Recipe title
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

