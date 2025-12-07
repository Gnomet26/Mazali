package com.example.mazali.data

data class FoodItem(
    val id: Int,
    val name: String,
    val price: String,
    val image: String,
    val category: String,
    val comment: String,
    var quantity: Int = 1
)


