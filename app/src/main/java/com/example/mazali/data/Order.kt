package com.example.mazali.data

data class Order(
    val id: String,
    val date: String,
    val totalPrice: Int,
    val status: String,
    val address: String,
    val paymentMethod: String,
    val products: List<FoodItem>,
    var isExpanded: Boolean = false // expand/collapse uchun
)
