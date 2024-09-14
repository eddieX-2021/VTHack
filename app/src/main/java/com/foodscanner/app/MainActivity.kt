package com.foodscanner.app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.foodscanner.app.api.ApiClient
import com.foodscanner.app.model.FoodItemResponse
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    // CameraX components
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var barcodeScanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitycamera)

        // Initialize the PreviewView and the BarcodeScanner
        previewView = findViewById(R.id.preview_view)
        barcodeScanner = BarcodeScanning.getClient()

        // Initialize the CameraX executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Start the CameraX camera preview
        startCamera()
    }

    // Start the CameraX preview and image analysis
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Bind CameraX lifecycle to this activity
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Create a CameraSelector for the back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Create the Preview use case
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Create the ImageAnalysis use case
            val imageAnalysis = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(cameraExecutor, { imageProxy ->
                    processCameraFrame(imageProxy)  // Process each frame with ML Kit
                })
            }

            try {
                // Unbind previous use cases and bind new ones
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (exc: Exception) {
                Log.e("MainActivity", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // Process each frame and detect barcodes using ML Kit
    @OptIn(ExperimentalGetImage::class)
    private fun processCameraFrame(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Pass the image to ML Kit's barcode scanner
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    processBarcodes(barcodes)
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivity", "Barcode scanning failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()  // Close the image proxy to avoid memory leaks
                }
        }
    }

    // Handle detected barcodes
    private fun processBarcodes(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            val rawValue = barcode.rawValue
            rawValue?.let {
                Log.d("MainActivity", "Detected barcode: $it")
                correlateBarcodeWithFood(it)  // Call API to fetch food data
            }
        }
    }

    // Make an API call to correlate the barcode with food data (using OpenFoodFacts)
    private fun correlateBarcodeWithFood(barcode: String) {
        val apiClient = ApiClient.getOpenFoodFactsApi()

        // Make API call to OpenFoodFacts
        apiClient.getFoodItem(barcode).enqueue(object : Callback<FoodItemResponse> {
            override fun onResponse(call: Call<FoodItemResponse>, response: Response<FoodItemResponse>) {
                if (response.isSuccessful) {
                    val foodItem = response.body()?.product
                    if (foodItem != null) {
                        // Log or display the food details
                        Log.d("MainActivity", "Food Name: ${foodItem.product_name}")
                        Log.d("MainActivity", "Calories: ${foodItem.nutriments?.energy_kcal_100g}")
                        Log.d("MainActivity", "Ingredients: ${foodItem.ingredients_text}")
                        // Update UI or display the fetched food data
                    } else {
                        Toast.makeText(this@MainActivity, "Food not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "API error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FoodItemResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch food data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()  // Shutdown the camera executor when activity is destroyed
    }
}
