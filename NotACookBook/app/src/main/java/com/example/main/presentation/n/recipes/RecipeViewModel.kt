package com.example.main.presentation.n.recipes

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.main.domain.model.Ingredient
import com.example.main.domain.model.Recipe
import com.example.main.domain.repository.RecipeRepository
import com.example.main.data.api.ClarifaiAPI
import com.example.main.data.dto.RecipeDetailsResponse
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    val firebaseFirestore: FirebaseFirestore // Add Firebase Firestore for saving the recipe
) : ViewModel() {

    private val _savedRecipes = MutableLiveData<List<RecipeDetailsResponse>>() // Holds the saved recipes
    val savedRecipes: LiveData<List<RecipeDetailsResponse>> get() = _savedRecipes

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _selectedRecipe = MutableLiveData<RecipeDetailsResponse>()
    val selectedRecipe: LiveData<RecipeDetailsResponse> get() = _selectedRecipe

    private val _ingredients = MutableLiveData<List<String>>()
    val ingredients: LiveData<List<String>> = _ingredients

    private val clarifaiService: ClarifaiAPI // For analyzing images

    private val recipeApiKey = "bc931648c46c46ee922d83058dab5a43"

    // Initialization block for setting up Clarifai Retrofit service
    init {
        clarifaiService = setupClarifaiService()
        fetchRecipes(recipeApiKey, "all", 50)
    }
    fun fetchSavedRecipes() {
        firebaseFirestore.collection("myRecipes")
            .get()
            .addOnSuccessListener { result ->
                val recipes = result.map { doc ->
                    RecipeDetailsResponse(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        title = doc.getString("title") ?: "",
                        image = doc.getString("image") ?: "",
                        extendedIngredients = (doc.get("ingredients") as? List<HashMap<String, Any>>)?.map { ingredient ->
                            Ingredient(
                                id = (ingredient["id"] as? Long)?.toInt() ?: 0,
                                name = ingredient["name"] as? String ?: "",
                                amount = (ingredient["amount"] as? Double) ?: 0.0,
                                unit = ingredient["unit"] as? String ?: ""
                            )
                        } ?: emptyList()
                    )
                }
                _savedRecipes.value = recipes
            }

            .addOnFailureListener { e ->
                Log.e("Firebase", "Error fetching saved recipes", e)
            }
    }
    fun setSelectedRecipe(recipe: RecipeDetailsResponse) {
        _selectedRecipe.value = recipe
    }

    // Function to set up Retrofit and OkHttpClient for Clarifai API
    private fun setupClarifaiService(): ClarifaiAPI {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.clarifai.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ClarifaiAPI::class.java)
    }

    // Function to fetch recipes using the repository
    fun fetchRecipes(apiKey: String, query: String, number: Int) {
        viewModelScope.launch {
            try {
                val fetchedRecipes = recipeRepository.fetchRecipes(apiKey, query, number)
                _recipes.value = fetchedRecipes
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching recipes", e)
            }
        }
    }

    // Function to fetch recipe details using the repository
    fun fetchRecipeDetails(apiKey: String, recipeId: Int) {
        viewModelScope.launch {
            try {
                val recipeDetails = recipeRepository.fetchRecipeDetails(apiKey, recipeId)
                _selectedRecipe.postValue(recipeDetails)
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching recipe details", e)
            }
        }
    }

    // Function to get a specific recipe by ID
    fun getRecipeById(recipeId: Int): Recipe? {
        return _recipes.value?.find { it.id == recipeId }
    }

    // Function to analyze image and update ingredients using Clarifai API
    fun analyzeImageAndUpdateIngredients(imageBytes: ByteArray, apiKey: String) {
        val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

        val requestBody = """
        {
            "inputs": [
                {
                    "data": {
                        "image": {
                            "base64": "$base64Image"
                        }
                    }
                }
            ]
        }
    """.trimIndent().toRequestBody("application/json".toMediaTypeOrNull())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = clarifaiService.analyzeImage("Key $apiKey", requestBody)

                if (response.status?.code == 10000) {
                    Log.d("ClarifaiSuccess", "Successful analysis")

                    val analyzedIngredients =
                        response.outputs?.get(0)?.data?.concepts?.mapNotNull { it.name }
                            ?: emptyList()

                    // Merge original and analyzed ingredients
                    _selectedRecipe.value?.let { recipe ->
                        val allIngredients = recipe.extendedIngredients.map { it.name } + analyzedIngredients
                        _ingredients.postValue(analyzedIngredients)

                        // Save original + analyzed ingredients to Firebase
                        val updatedRecipe = recipe.copy(
                            extendedIngredients = recipe.extendedIngredients + analyzedIngredients.map {
                                Ingredient(0, it, 1.0, "")
                            }
                        )
                        saveRecipeToFirebase(updatedRecipe)
                    }
                }
            } catch (e: Exception) {
                Log.e("ClarifaiError", "Error analyzing image: ${e.message}")
            }
        }
    }

    fun resetAnalyzedIngredients() {
        _ingredients.postValue(emptyList()) // Clear the analyzed ingredients
    }

    fun saveRecipeToFirebase(recipe: RecipeDetailsResponse) {
        val recipeMap = hashMapOf(
            "title" to recipe.title,
            "image" to recipe.image,
            "ingredients" to recipe.extendedIngredients.map { ingredient ->
                hashMapOf(
                    "id" to ingredient.id,
                    "name" to ingredient.name,
                    "amount" to ingredient.amount,
                    "unit" to ingredient.unit
                )
            }
        )


        firebaseFirestore.collection("myRecipes")
            .document(recipe.title)
            .set(recipeMap)
            .addOnSuccessListener {
                Log.d("Firebase", "Recipe successfully saved!")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error saving recipe", e)
            }
    }


}


