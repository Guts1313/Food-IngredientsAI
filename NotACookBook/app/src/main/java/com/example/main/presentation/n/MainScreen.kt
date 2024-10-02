package com.example.main.presentation.n

import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.main.domain.AuthManager
import com.example.main.presentation.n.recipes.RecipeViewModel
import com.example.main.presentation.n.login.GlassMorphismLoginPage
import com.example.main.presentation.n.login.GlassMorphismSignUpPage
import com.example.main.presentation.n.login.SignUpCard
import com.example.main.presentation.n.login.signUpUser
import com.example.main.presentation.n.recipes.MyRecipesScreen
import com.example.main.presentation.n.recipes.RecipeDetailScreen
import com.example.main.presentation.n.recipes.RecipeListScreen
import com.example.main.presentation.n.recipes.SavedRecipeDetailScreen
import com.example.test2.R

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScreen(
    viewModel: RecipeViewModel,
    cameraLauncher: ActivityResultLauncher<Intent>,
    filePickerLauncher: ActivityResultLauncher<String>,
    authManager: AuthManager = AuthManager() // Add AuthManager as a default parameter
) {
    val navController = rememberNavController()
    val apiKey = "658fb12f9f3b43aa889e0ae00041fb61"
    var isLoggedIn by remember { mutableStateOf(authManager.isLoggedIn()) }

    // Monitor the auth state using an auth state listener
    LaunchedEffect(Unit) {
        authManager.addAuthStateListener { loggedIn ->
            isLoggedIn = loggedIn
        }
    }
    fun handleLogout() {
        isLoggedIn = false // Update the logged-in state after logout
    }
    Scaffold(
        bottomBar = {
            if (isLoggedIn) {
                BottomNavigationBar(
                    navController = navController,
                    authManager = authManager,
                    isLoggedIn = isLoggedIn,
                    onLogout = ::handleLogout // Pass the logout handler
                )
            }
        }

    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "category_selection" else "login", // Check if logged in immediately
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply the padding values here
        ) {

            // Define the Login route
            composable("login") {
                GlassMorphismLoginPage(
                    navController = navController,
                    authManager = authManager, // Pass authManager here
                    onLoginSuccess = {
                        // Update login state after successful login
                        isLoggedIn = true
                        navController.navigate("category_selection") {
                            popUpTo("login") { inclusive = true } // Clear login from backstack
                        }
                    }
                )
            }


            // Define the Category Selection route
            composable("category_selection") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header text
                    Text(
                        text = "Choose Recipe Type",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    // Four-block clickable section
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(Color(0xFF121212)), // Set black background
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CategoryBlock("Desserts", R.drawable.deserts, onClick = {
                            viewModel.fetchRecipes(apiKey, "desserts", 50)
                        })
                        CategoryBlock("Drinks", R.drawable.drink, onClick = {
                            viewModel.fetchRecipes(apiKey, "drinks", 50)
                        })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(Color(0xFF121212)), // Set black background
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CategoryBlock("Main Dishes", R.drawable.maindish, onClick = {
                            viewModel.fetchRecipes(apiKey, "main course", 50)
                        })
                        CategoryBlock("Side Dishes", R.drawable.side, onClick = {
                            viewModel.fetchRecipes(apiKey, "side dish", 50)
                        })
                    }

                    Spacer(modifier = Modifier.height(44.dp))
                    Text(text = "Recipe list", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 14.dp))
//                    Button(
//                        onClick = { navController.navigate("my_recipes") }, // Navigate to MyRecipesScreen
//                        modifier = Modifier
//                            .fillMaxWidth(0.5f)
//                            .padding(vertical = 16.dp)
//                            .clip(shape = RoundedCornerShape(25.dp))
//                            .background(Color(0xFF121312)),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.Transparent,
//                            contentColor = Color.White
//                        ),
//                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
//                    ) {
//                        Icon(
//                            contentDescription = "My Recipes",
//                            imageVector = Icons.Default.Bookmark,
//                            tint = Color.Red
//                        )
//                        Text(text = "My Recipes", style = MaterialTheme.typography.bodyLarge)
//                    }

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
                        cameraLauncher = cameraLauncher,
                        filePickerLauncher = filePickerLauncher // Pass the file picker launcher
                    )
                }
            }

            // Define the Sign-Up route
            composable("signup") {
                GlassMorphismSignUpPage(
                    navController = navController,
                    onSignUp = { name, email, password, age ->
                        signUpUser(name, email, password, age)
                    }
                )
            }

            composable("recipe_detail_saved") {
                SavedRecipeDetailScreen(
                    viewModel = viewModel, // Use the selected recipe from the ViewModel
                    cameraLauncher = cameraLauncher,
                    filePickerLauncher = filePickerLauncher
                )
            }

            // Define the MyRecipesScreen route
            composable("my_recipes") {
                MyRecipesScreen(viewModel = viewModel, navController = navController)
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
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun BottomNavigationBar(
    navController: NavController,
    authManager: AuthManager,
    isLoggedIn: Boolean,
    onLogout: () -> Unit
) {
    val items = listOf(
        BottomNavItem("", Icons.Default.Home, "category_selection",Color.White),
        BottomNavItem("", Icons.Default.Bookmark, "my_recipes",Color.Red),
        BottomNavItem("", Icons.Default.ExitToApp, "logout",Color.White) // Add Logout item with Exit icon
    )

    if (isLoggedIn) {
        BottomNavigation(
            backgroundColor = Color.Black,
            contentColor = Color.White
        ) {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = item.color
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (item.route == "logout") {
                            // Handle logout
                            authManager.signOut()
                            onLogout() // Update state after logout
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        } else {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    selectedContentColor = Color.Red,
                    unselectedContentColor = Color.Gray
                )
            }
        }
    }
}


data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val color:Color
)
