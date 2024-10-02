package com.example.main.presentation.n.recipes

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.main.domain.model.Recipe
import com.example.test2.R
import java.io.ByteArrayOutputStream

@Composable
fun RecipeListScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit
) {
    val recipes by viewModel.recipes.observeAsState(emptyList()) // Observe recipes from ViewModel
    val newApiKey = "e8da20d8077445c0b5755c1eebcdf938"
    // Use LazyVerticalGrid to display recipes in a grid format
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Set black background
            .border(width = 3.dp, shape = RoundedCornerShape(15.dp), brush = Brush.linearGradient(
                listOf(Color.Red,Color.DarkGray,Color.Red,Color.DarkGray)
            ))
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
    cameraLauncher: ActivityResultLauncher<Intent>,
    filePickerLauncher: ActivityResultLauncher<String>
) {
    // Fetch the recipe details
    LaunchedEffect(recipeId) {
        viewModel.resetAnalyzedIngredients() // Reset the analyzed ingredients
        viewModel.fetchRecipeDetails(apiKey, recipeId) // Fetch new recipe details
    }

    val recipeDetails by viewModel.selectedRecipe.observeAsState()
    val analyzedIngredients by viewModel.ingredients.observeAsState(emptyList())
    val context = LocalContext.current

    // State to control the expand/collapse behavior for Ingredients and Analyzed Ingredients
    var isIngredientsExpanded by remember { mutableStateOf(false) }
    var isAnalyzedIngredientsExpanded by remember { mutableStateOf(false) }

    // Pagination states for Ingredients and Analyzed Ingredients
    var displayedIngredientsCount by remember { mutableStateOf(5) } // Show 5 items initially
    var displayedAnalyzedIngredientsCount by remember { mutableStateOf(5) } // Show 5 items initially

    recipeDetails?.let { recipe ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
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
                        .clip(shape = RoundedCornerShape(25.dp))
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Recipe title
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ingredients section header (collapsible) (Original Ingredients Only)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF913831).copy(0.2f),
                                    Color(0xFF121212).copy(0.2f),
                                    Color(0xFF913831).copy(0.2f),
                                    Color(0xFF121212).copy(0.2f),
                                    Color(0xFF913831).copy(0.2f),
                                )
                            )
                        )
                        .clickable { isIngredientsExpanded = !isIngredientsExpanded }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ingredients:",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                    Icon(
                        imageVector = if (isIngredientsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isIngredientsExpanded) "Collapse" else "Expand",
                        tint = Color.Yellow
                    )
                }

                // Collapsible ingredients list (Show Original Ingredients in Table)
                if (isIngredientsExpanded) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xFF913831).copy(0.2f),
                                        Color(0xFF121212).copy(0.2f),
                                        Color(0xFF913831).copy(0.2f),
                                        Color(0xFF121212).copy(0.2f),
                                        Color(0xFF913831).copy(0.2f),
                                    )
                                )
                            )
                            .padding(8.dp)
                    ) {
                        items(recipe.extendedIngredients.take(displayedIngredientsCount)) { ingredient ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = ingredient.name.capitalize(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = "${ingredient.amount} ${ingredient.unit}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White,
                                    textAlign = TextAlign.End
                                )
                            }
                        }

                        // Show "Load More" button if more ingredients are available
                        if (recipe.extendedIngredients.size > displayedIngredientsCount) {
                            item {
                                Button(
                                    onClick = {
                                        displayedIngredientsCount += 5 // Show 5 more items each time
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Load More Ingredients")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Analyzed Ingredients section header (collapsible)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF913831).copy(0.2f),
                                    Color(0xFF121212).copy(0.2f),
                                    Color(0xFF913831).copy(0.2f),
                                    Color(0xFF121212).copy(0.2f),
                                    Color(0xFF913831).copy(0.2f),
                                )
                            )
                        )
                        .clickable {
                            isAnalyzedIngredientsExpanded = !isAnalyzedIngredientsExpanded
                        }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Analyzed Ingredients:",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                    Icon(
                        imageVector = if (isAnalyzedIngredientsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isAnalyzedIngredientsExpanded) "Collapse" else "Expand",
                        tint = Color.Yellow,
                        modifier = Modifier.padding(top = 9.dp)
                    )
                }

                // Collapsible analyzed ingredients list (with pagination)
                if (isAnalyzedIngredientsExpanded) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xFF850000).copy(0.2f),
                                        Color(0xFF121212).copy(0.2f),
                                        Color(0xFF850000).copy(0.2f),
                                        Color(0xFF121212).copy(0.2f),
                                        Color(0xFF850000).copy(0.2f),
                                    )
                                )
                            )
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        items(analyzedIngredients.take(displayedAnalyzedIngredientsCount)) { ingredient ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = ingredient.capitalize(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            }
                        }

                        // Show "Load More" button if more analyzed ingredients are available
                        if (analyzedIngredients.size > displayedAnalyzedIngredientsCount) {
                            item {
                                Button(
                                    onClick = {
                                        displayedAnalyzedIngredientsCount += 5 // Show 5 more items each time
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Load More Analyzed Ingredients")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add buttons at the bottom

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.Bottom,
//                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // SmartDetect Button for camera
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                cameraLauncher.launch(cameraIntent) // Open the camera
                            },
                            modifier = Modifier
                                .width(120.dp).height(60.dp)
                                .clip(shape = RoundedCornerShape(15.dp))
                            , border = BorderStroke(1.5.dp, brush = Brush.linearGradient(listOf(
                                Color.Red,Color.White,Color.Red
                            ))),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Camera Icon",
                                    modifier = Modifier.padding(top = 8.dp, end = 1.dp).size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Add Detected Button to pick image from file picker

                        Button(
                            onClick = {
                                filePickerLauncher.launch("image/*") // Open the file picker to select an image
                            },
                            modifier = Modifier
                                .width(120.dp).height(60.dp)
                                .clip(shape = RoundedCornerShape(15.dp))
                                , border = BorderStroke(1.5.dp, brush = Brush.linearGradient(listOf(
                                    Color.Red,Color.White,Color.Red
                                ))),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "Camera Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
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
            .background(Color(0xFF121212)) // Set black background
            .padding(4.dp)
            .clip(shape = RoundedCornerShape(2.dp))
            .fillMaxWidth()
            .clickable { onRecipeClick(recipe) }, // Call the onRecipeClick lambda on click
        shape = RoundedCornerShape(2.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(2.dp))
                .background(Color(0xFF121212)) // Set black background

        ) {
            // Recipe image
            Image(
                painter = rememberImagePainter(data = recipe.image),
                contentDescription = null,
                modifier = Modifier
                    .height(160.dp)
                    .clip(shape = RoundedCornerShape(15.dp))

                    .width(185.dp)
                    .aspectRatio(1f)
                    .background(Color(0xFF121212)) // Set black background
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            // Recipe title
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp),
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

