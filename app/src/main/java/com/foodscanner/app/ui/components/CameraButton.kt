package com.foodscanner.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource // For using a custom drawable
import com.foodscanner.app.ui.components.CameraButton

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
        // Option 1: Use the built-in Camera icon from Material Icons
        Icon(Icons.Filled.Camera, contentDescription = "Camera")

        // Option 2: Use a custom drawable resource if available
        // Uncomment the following lines if you want to use a custom drawable
        // Icon(
        //     painter = painterResource(id = R.drawable.camera_icon),
        //     contentDescription = "Camera"
        // )
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenWithFixedButtonPreview() {
    ScreenWithFixedButton()
}
