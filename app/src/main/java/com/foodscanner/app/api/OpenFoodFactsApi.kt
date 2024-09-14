package com.foodscanner.app.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Define the OpenFoodFacts API service
interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    fun getFoodItem(@Path("barcode") barcode: String): Call<FoodItemResponse>

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/"
    }
}
