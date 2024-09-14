package com.foodscanner.app.model

data class FoodItemResponse(
    val product: Product?
)

data class Product(
    val product_name: String?,
    val nutriments: Nutriments?,
    val ingredients_text: String?
)

data class Nutriments(
    val energy_kcal_100g: Double?, // Calories per 100g
    val fat_100g: Double?,
    val sugars_100g: Double?
)
