package com.foodscanner.app

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.foodscanner.app.ui.theme.Test1Theme // Updated to reflect new package name
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.Labeling
import com.google.mlkit.vision.label.Labeler
import java.util.concurrent.Executors
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    private lateinit var labeler: Labeler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Initialize ML Kit Image Labeler
        labeler = Labeling.getClient()

        // Request camera permissions
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Log.e("MainActivity", "Camera permission denied")
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        setContent {
            Test1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build()
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), YourImageAnalyzer())

            cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(this))
    }

    private inner class YourImageAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                labeler.process(image)
                    .addOnSuccessListener { labels ->
                        for (label in labels) {
                            val text = label.text
                            val confidence = label.confidence
                            Log.d("MainActivity", "Label: $text, Confidence: $confidence")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("MainActivity", "Image labeling failed", e)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Test1Theme {
        Greeting("Android")
    }
}
