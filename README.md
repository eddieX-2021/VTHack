VT-Hack24
https://chatgpt.com/share/66e4fa40-62ec-8012-aaf1-21eb5798d14a

Figma UI/UX for food apps:
https://www.figma.com/design/6cwfy8uiStAsY8NrRCQow1/%F0%9F%8D%94-Food-Ordering-App-UI-Kit-(practice)?node-id=14-892&node-type=canvas&t=BnahdIFM1xQgGR5W-0

Tech stack: Backend: Firebase sync with spoonacular api for nutrition information gathering, Kotlin and google ml kit for computer vision, front-end and android development using Android studio

# Food Scanner App

## Description
The **Food Scanner App** allows users to scan barcodes of food products using their device’s camera, retrieves food data (name, calories, ingredients) from the OpenFoodFacts API, and saves scanned items to Firebase. Users can view a history of their scanned items and get recipe suggestions based on their scanned ingredients.

## Features
- **Real-time barcode scanning** using CameraX and ML Kit.
- **Fetch food data** (name, calories, ingredients) from the **OpenFoodFacts API**.
- **User Authentication** with Firebase (optional).
- **Store scanned items** in Firebase for each user.
- **View scanned food history**.
- **Get recipe suggestions** based on scanned food items (optional, via Spoonacular API).

## Project Architecture
1. **Android App (Frontend)**: 
   - **CameraX** for camera input.
   - **ML Kit** for barcode detection.
   - **UI** built with XML layouts.
   - **User Authentication** using Firebase Authentication.

2. **Firebase Backend (Storage)**:
   - **Firebase Realtime Database** for storing user-specific food data.
   - **Firebase Authentication** for user management.

3. **External API (Data Source)**:
   - **OpenFoodFacts** API for retrieving food data based on scanned barcodes.
   - **Spoonacular API** (optional) for recipe suggestions.

## Project Flow
1. **Camera Input**: The camera captures the barcode of food items using CameraX.
2. **Barcode Detection**: ML Kit processes the camera frame to detect the barcode.
3. **API Call**: The detected barcode is used to query the OpenFoodFacts API for food data.
4. **Display Food Data**: The food data (name, calories, ingredients) is displayed in the UI.
5. **Save to Firebase** (Optional): The user can save the scanned items to Firebase.
6. **Recipe Suggestions** (Optional): Based on the user's scanned food items, recipes can be suggested using the Spoonacular API.

## Project Structure
- **MainActivity.kt**: 
   - Handles camera preview and barcode scanning.
   - Calls OpenFoodFacts API to retrieve food data based on the scanned barcode.
   - Displays food data and saves it to Firebase.

- **FirebaseHelper.kt**: 
   - Manages Firebase interactions such as saving and retrieving scanned food items.

- **FoodRecognition.kt**: 
   - Contains logic for barcode scanning using ML Kit.

- **OpenFoodFactsApi.kt**: 
   - Retrofit interface for making API calls to OpenFoodFacts to retrieve food data.

- **activity_main.xml**: 
   - Defines the layout for the main activity, including camera preview and food data display.

## Dependencies
Ensure the following dependencies are added to your `build.gradle` (app-level):

```gradle
dependencies {
    // ML Kit Barcode Scanning API
    implementation 'com.google.mlkit:barcode-scanning:17.0.3'
    
    // CameraX for camera functionality
    implementation 'androidx.camera:camera-core:1.0.2'
    implementation 'androidx.camera:camera-camera2:1.0.2'
    implementation 'androidx.camera:camera-lifecycle:1.0.2'
    implementation 'androidx.camera:camera-view:1.0.0-alpha27'

    // Retrofit for making API calls
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    // Firebase Authentication and Database
    implementation 'com.google.firebase:firebase-auth:22.0.0'
    implementation 'com.google.firebase:firebase-database:20.0.5'
}
'''
#Setup Instructions

    Clone the repository.
    Set up Firebase:
        Enable Firebase Authentication (for user accounts).
        Set up Firebase Realtime Database for storing scanned food items.
        Download and include the google-services.json file in your project.
    Add the necessary dependencies to your build.gradle.
    Set up the OpenFoodFacts API for fetching food data.
    (Optional) Set up the Spoonacular API for recipe suggestions.
    Run the app and start scanning barcodes!

#API Integration

    OpenFoodFacts API: The app sends the barcode to the OpenFoodFacts API to fetch food-related data.
    Spoonacular API (optional): Used to suggest recipes based on scanned food items.

#Project Workflow
+-----------------+        +--------------------+       +----------------+
|                 |        |                    |       |                |
|  Camera Input   +------->|  ML Kit (Barcode)   +------>|  Barcode       |
|  (CameraX)      |        |  Detection          |       |  Detected      |
|                 |        |                    |       |                |
+-----------------+        +--------------------+       +----------------+
                                                                 |
                                                                 |
                                                          +------+------+
                                                          |             |
                                                    +-----v-----+ +-----v------+
                                                    | Fetch from| |Display in   |
                                                    | API (e.g.,| |UI (Name,    |
                                                    | OpenFood- | |Calories,    |
                                                    | Facts)    | |Ingredients) |
                                                    +-----------+ +-------------+
                                                           |
                                                           |
                                                    +------v-------+
                                                    | Firebase      |
                                                    | Save Food     |
                                                    | Data for User |
                                                    +---------------+


    Add more detailed nutritional tracking (e.g., aggregate daily calorie intake).
    Implement push notifications to remind users about expiring food items.
    Introduce social sharing features (e.g., sharing scanned items with friends).
