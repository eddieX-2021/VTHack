package com.foodscanner.app.foodrecognition

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.foodscanner.app.R
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var barcodeScanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        previewView = findViewById(R.id.preview_view)

        // Initialize the Barcode Scanner
        barcodeScanner = BarcodeScanning.getClient()

        // Initialize the camera executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Start the CameraX preview
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, { imageProxy ->
                        processCameraFrame(imageProxy)  // Process camera frame using ML Kit
                    })
                }

            // Bind the lifecycle of cameras to the lifecycle owner
            cameraProvider.bindToLifecycle(this, cameraProvider.cameraSelector, imageAnalysis)

        }, ContextCompat.getMainExecutor(this))
    }

    // Process each frame to detect barcodes
    private fun processCameraFrame(imageProxy: ImageProxy) {
        // Convert ImageProxy to InputImage for ML Kit processing
        val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        // Process the image with ML Kit's Barcode Scanner
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                processBarcodes(barcodes)
            }
            .addOnFailureListener { e ->
                Log.e("MLKit", "Barcode detection failed", e)
            }
            .addOnCompleteListener {
                imageProxy.close()  // Close the image proxy to avoid memory leaks
            }
    }

    // Process detected barcodes and log them
    private fun processBarcodes(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            val rawValue = barcode.rawValue
            rawValue?.let {
                Log.d("MLKit", "Scanned Barcode: $it")
                // You can now correlate this barcode with food data using an online API
                correlateBarcodeWithFood(it)
            }
        }
    }

    // Function to correlate the scanned barcode with food data (you will call an API here)
    private fun correlateBarcodeWithFood(barcode: String) {
        // Placeholder: Make API call to get food data (e.g., OpenFoodFacts or Spoonacular)
        Log.d("API", "Correlating barcode $barcode with online food data")
    }
    private fun saveFoodToFirebase(userId: String, foodItem: Product) {
        val database = FirebaseDatabase.getInstance().reference
        val food = FoodItem(
            name = foodItem.product_name ?: "Unknown",
            calories = foodItem.nutriments?.energy_kcal_100g?.toInt() ?: 0,
            ingredients = foodItem.ingredients_text ?: "N/A"
        )
        database.child("Users").child(userId).child("foodItems").push().setValue(food)
            .addOnSuccessListener {
                Log.d("Firebase", "Food saved successfully")
            }
            .addOnFailureListener { error ->
                Log.e("Firebase", "Failed to save food", error)
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

private fun correlateBarcodeWithFood(barcode: String) {
    // Get the API instance
    val apiClient = ApiClient.getOpenFoodFactsApi()

    // Make an API call to OpenFoodFacts
    apiClient.getFoodItem(barcode).enqueue(object : retrofit2.Callback<FoodItemResponse> {
        override fun onResponse(call: Call<FoodItemResponse>, response: retrofit2.Response<FoodItemResponse>) {
            if (response.isSuccessful) {
                val foodItem = response.body()?.product
                if (foodItem != null) {
                    // Log or display the food details
                    Log.d("Food Data", "Food Name: ${foodItem.product_name}")
                    Log.d("Food Data", "Calories: ${foodItem.nutriments?.energy_kcal_100g}")
                    Log.d("Food Data", "Ingredients: ${foodItem.ingredients_text}")
                    // Here you can update the UI with the fetched food data
                } else if (foodItem == null) {
                    Toast.makeText(this@CameraActivity, "Food not found", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("Food Data", "No food data found for barcode: $barcode")
                }
            } else {
                Log.e("API Error", "Error response code: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<FoodItemResponse>, t: Throwable) {
            Toast.makeText(this@CameraActivity, "Failed to fetch food data", Toast.LENGTH_SHORT).show()
        }


    })
}
