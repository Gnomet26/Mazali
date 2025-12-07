package com.example.mazali.data.model

data class BuyurtmaRequest(
    val lat: Double,
    val long: Double,
    val products: List<ProductRequest>
)