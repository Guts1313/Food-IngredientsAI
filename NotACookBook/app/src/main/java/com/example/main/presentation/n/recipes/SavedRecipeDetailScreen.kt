package com.example.main.presentation.n.recipes

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun SavedRecipeDetailScreen(
    viewModel: RecipeViewModel,
    cameraLauncher: ActivityResultLauncher<Intent>,
    filePickerLauncher: ActivityResultLauncher<String>
) {
    val recipeDetails by viewModel.selectedRecipe.observeAsState()
    var isIngredientsExpanded by remember { mutableStateOf(false) }

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

                // Ingredients section header (collapsible)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color.DarkGray,
                                    Color(0xFF121212).copy(0.2f),
                                    Color.DarkGray.copy(0.5f),
                                    Color.Transparent
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

                // Collapsible ingredients list (Original + Analyzed Ingredients)
                if (isIngredientsExpanded) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color.DarkGray,
                                        Color(0xFF121212).copy(0.6f)
                                    )
                                )
                            )
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        // Show both original and analyzed ingredients
                        val allIngredients = recipe.extendedIngredients.map { it.name }
                        items(allIngredients) { ingredient ->
                            Text(
                                text = ingredient,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}



