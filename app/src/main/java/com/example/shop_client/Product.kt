package com.example.shop_client


data class Product(
    val id: Int? = null,
    val name: String,
    val qty: Int,
    val price: Double,
    val type: String
)
