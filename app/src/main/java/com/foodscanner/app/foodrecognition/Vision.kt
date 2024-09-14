package com.foodscanner.app.foodrecognition

class Vision com.foodscanner.app.foodrecognition

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class FoodRecognition(context: Context) {

    private val imageLabeler: ImageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    // Function to process the camera frame (bitmap) and get food labels
    fun analyzeImage(bitmap: Bitmap, onSuccess: (List<ImageLabel>) -> Unit, onFailure: (Exception) -> Unit) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        imageLabeler.process(inputImage)
            .addOnSuccessListener { labels ->
                // Pass the labels to the onSuccess callback
                onSuccess(labels)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                onFailure(exception)
            }
    }

    // Optionally: Method to correlate recognized food with items in your Firebase database
    fun correlateFoodWithDatabase(labels: List<ImageLabel>, databaseItems: List<String>): List<String> {
        val matchedItems = mutableListOf<String>()

        for (label in labels) {
            if (databaseItems.contains(label.text.toLowerCase())) {
                matchedItems.add(label.text)
            }
        }
        return matchedItems
    }
}
{
}