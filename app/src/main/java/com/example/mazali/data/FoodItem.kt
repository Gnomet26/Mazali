package com.example.mazali.data

data class FoodItem(
    val name: String,
    val price: String,
    val imageResId: Int,
    val category: String,
    val description: String,
    var quantity: Int = 1
)
