package com.example.shop_client


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    fun getProducts(): List<Product>

    object ApiClient {

        private const val BASE_URL = "http://localhost:8080"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val productService: ProductApi by lazy {
            retrofit.create(productService::class.java)
        }

    }
}