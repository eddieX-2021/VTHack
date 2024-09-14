package com.foodscanner.app.foodrecognition

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.foodscanner.app.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class cameraactivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        previewView = findViewById(R.id.preview_view)
        cameraExecutor = Executors.newSingleThreadExecutor()

        startCamera()  // Start camera and set up image analysis
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, { image ->
                        processCameraFrame(image)
                    })
                }

            cameraProvider.bindToLifecycle(
                this, cameraProvider.cameraSelector, imageAnalysis
            )

        }, ContextCompat.getMainExecutor(this))
    }

    private fun processCameraFrame(imageProxy: ImageProxy) {
        val bitmap = imageProxy.toBitmap()  // Convert imageProxy to Bitmap
        // Process the image using your ML Kit logic (e.g., image recognition)
    }
}
