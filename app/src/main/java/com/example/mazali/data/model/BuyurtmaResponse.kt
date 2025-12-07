package com.example.mazali.data.model

data class BuyurtmaResponse(
    val id: Int,
    val author: String,
    val status: String,
    val created_at: String,
    val lat: String,
    val long: String,
    val items: List<ProductItem>
)