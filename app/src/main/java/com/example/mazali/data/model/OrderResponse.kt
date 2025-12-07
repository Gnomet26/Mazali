package com.example.mazali.data.model

data class OrderResponse(
    val id: Int,
    val status: String,
    val created_at: String,
    val lat: String,
    val long: String,
    val items: List<ItemResponse> // shu kerak
)