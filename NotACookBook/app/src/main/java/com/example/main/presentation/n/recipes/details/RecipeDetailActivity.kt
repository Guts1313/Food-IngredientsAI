package com.example.main.presentation.n.recipes.details

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.main.presentation.n.recipes.RecipeViewModel
import java.io.File
import java.io.IOException

//class RecipeDetailActivity : ComponentActivity() {
//
//    private val viewModel: RecipeViewModel by viewModels()
//
//    private var capturedImageUri: Uri? = null
//    companion object {
//        private const val CAMERA_PERMISSION_CODE = 101
//    }
//    // Launcher for the camera capture
//    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            capturedImageUri?.let { uri ->
//                val imageBytes = uriToByteArray(this, uri)
//                if (imageBytes != null) {
//                    viewModel.analyzeImageAndUpdateIngredients(imageBytes, "e8da20d8077445c0b5755c1eebcdf938")
//                }
//            }
//        }
//    }
//
//    fun openCamera() {
//        Log.d("RecipeDetailActivity", "openCamera() called") // Add log to ensure function is called
//
//        if (checkCameraAndStoragePermissions()) {
//            val photoFile: File = createImageFile()
//            val uri = FileProvider.getUriForFile(
//                this,
//                "${applicationContext.packageName}.provider",
//                photoFile
//            )
//            Log.d("RecipeDetailActivity", "Launching camera intent") // Log before launching camera
//            capturedImageUri = uri
//            takePictureLauncher.launch(uri)
//        } else {
//            Log.d("RecipeDetailActivity", "Permissions not granted") // Log if permissions are missing
//        }
//    }
//
//
//    // Check camera and storage permissions
//    private fun checkCameraAndStoragePermissions(): Boolean {
//        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
//                storagePermission == PackageManager.PERMISSION_GRANTED
//    }
//
//    // Request permissions
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_PERMISSION_CODE)
//    }
//
//    // Create image file for storing the captured photo
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
//        return File.createTempFile(
//            "JPEG_${System.currentTimeMillis()}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        )
//    }
//
//    // Convert URI to byte array
//    private fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
//        return context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }
//    }
//}







