package com.example.test2.presentation.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.test2.domain.RecipeViewModel
import com.example.test2.presentation.n.activities.MainScreen
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create the ViewModel (viewModel delegate provides it automatically)
        val recipeViewModel: RecipeViewModel by viewModels()

        // Initialize Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Log an event to check if Firebase is working
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "test_id")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "test_name")
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "test_content")
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val imageBytes = captureImageFromUri(it)
                if (imageBytes != null) {
                    recipeViewModel.analyzeImageAndUpdateIngredients(imageBytes, "e8da20d8077445c0b5755c1eebcdf938")
                } else {
                    Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Register the camera permission launcher
        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }


        // Register the camera intent launcher to get the captured image
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as Bitmap?
                    // Convert the captured Bitmap to ByteArray
                    if (imageBitmap != null) {
                        val stream = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val imageBytes = stream.toByteArray()

                        // Pass the ByteArray to the ViewModel for analysis
                        recipeViewModel.analyzeImageAndUpdateIngredients(
                            imageBytes,
                            "e8da20d8077445c0b5755c1eebcdf938"
                        )
                    } else {
                        Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        setContent {
            MainScreen(
                viewModel = recipeViewModel,
                cameraLauncher = cameraLauncher, // Pass the camera launcher here
                filePickerLauncher = filePickerLauncher // Pass file picker to the screen

            )
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    private fun captureImageFromUri(uri: Uri): ByteArray? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            inputStream?.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}


//@Composable
//fun chipSection(chips: List<String>) {
//    var selectedIndex by remember { mutableStateOf(0) }
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3),
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(15.dp))
//    ) {
//        items(chips.size) { index ->
//            Box(
//                contentAlignment = Alignment.Center,  // Center the content inside the Box
//                modifier = Modifier
//                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
//                    .clickable { selectedIndex = index }
//                    .clip(RoundedCornerShape(15.dp))
//                    .height(25.dp)
//                    .background(if (selectedIndex == index) Color.Blue else Color.DarkGray)
//            ) {
//                Text(
//                    text = chips[index],
//                    textAlign = TextAlign.Center,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    color = Color.White,
//                    modifier = Modifier.fillMaxWidth()  // Ensure the text fills the width of the Box
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun ImageCard(
//    painter: Painter, contentDescription: String, title: String, modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(25.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 35.dp)
//    ) {
//        // Make the image fill the entire card
//        Image(
//            painter = painter,
//            contentDescription = contentDescription,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.height(200.dp)  // Fill the entire card with the image
//        )
//
//        // Add semi-transparent background behind the text for better visibility
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(Color.Transparent, Color.Blue), startY = 300f
//                    )
//                )
//        ) {
//
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Brush.verticalGradient(listOf(Color.Red, Color.Blue))),
//                fontSize = TextUnit(14F, TextUnitType(14)),
//                text = contentDescription,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//
//
//        }





