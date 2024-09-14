package com.example.test1.ui.screens

import androidx.camera.core.Camera
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera // Change to a valid icon if needed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.test1.ui.components.CameraButton

@Composable
fun ScreenWithFixedButton() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Add your scrollable content here
            Spacer(modifier = Modifier.height(1000.dp)) // Placeholder for long content
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            CameraButton(onClick = { /* Handle click */ })
        }
    }
}

@Composable
fun CameraButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.Camera, contentDescription = "Camera") // Use a valid icon
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenWithFixedButtonPreview() {
    ScreenWithFixedButton()
}
