package com.foodscanner.app.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var retrofit: Retrofit? = null

    fun getOpenFoodFactsApi(): OpenFoodFactsApi {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(OpenFoodFactsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(OpenFoodFactsApi::class.java)
    }
}
